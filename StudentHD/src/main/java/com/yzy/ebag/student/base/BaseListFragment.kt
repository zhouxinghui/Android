package com.yzy.ebag.student.base

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import ebag.core.base.BaseFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.util.T
import ebag.core.widget.empty.StateView
import kotlinx.android.synthetic.main.base_list_view.*
import java.util.*

/**
 * Created by unicho on 2017/11/30.
 */
 abstract class BaseListFragment<E> : BaseFragment(),
        StateView.OnRetryClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener,
        BaseQuickAdapter.RequestLoadMoreListener{

    companion object {
        /** 刚进入页面时的状态 */
        val FIRST = 0
        /** 刷新时的状态 */
        val REFRESH = 1
        /** 加载更多时的状态 */
        val LOAD_MORE = 2
        /** 每页默认加载的数量 */
        val DEFAULT_PAGE_SIZE = 10
    }
    private var mAdapter: BaseQuickAdapter<E,BaseViewHolder>? = null
    protected var mCurrentPage = 1
    protected var isFirstLoadSuccess: Boolean = false

    /**可刷新*/
    private var canRefresh = true
    /**可加载更多*/
    private var canLoadMore = true
    /**没有网络请求*/
    private var noNetWork = false

    protected abstract fun loadConfig()

    /**
     *  网络请求
     * @param page
     * @param requestCallBack
     */
    protected abstract fun requestData(page: Int, requestCallBack: RequestCallBack<List<E>>)

    /**
     * 设置 recyclerView 的适配器 Adapter
     */
    protected abstract fun getAdapter(): BaseQuickAdapter<E,BaseViewHolder>

    /**
     * 设置 recyclerView 的 LayoutManager
     */
    protected abstract fun getLayoutManager(): RecyclerView.LayoutManager?

    /** 每页默认加载的数量 */
    open protected fun getPageSize(): Int = DEFAULT_PAGE_SIZE

    /** 是否有加载更多操作 */
    protected fun loadMoreEnabled(enable: Boolean){
        this.canLoadMore = enable
        mAdapter?.setEnableLoadMore(enable)
    }

    /** 是否有下拉刷新操作 */
    protected fun refreshEnabled(enable: Boolean){
        this.canRefresh = enable
        refreshLayout.isEnabled = enable
    }

    /** 不需要网络请求操作 */
    protected fun onlyView(noNetWork: Boolean){
        this.noNetWork = noNetWork
    }

    /** 当前网络请求所处的状态 */
    private var loadingStatus: Int = FIRST

    override fun getLayoutRes(): Int {
        return R.layout.base_list_view
    }

    /** 初始化操作 */
    override fun initViews(rootView: View) {
        loadConfig()
        // 设置 RecyclerView 的 LayoutManager
        recyclerView.layoutManager = getLayoutManager() ?: LinearLayoutManager(mContext)
        // 设置 recyclerView 的 Adapter
        mAdapter = getAdapter()
        mAdapter?.enableLoadMoreEndClick(true)
        mAdapter?.bindToRecyclerView(recyclerView)

        //设置 点击监听事件
        mAdapter?.onItemClickListener = this
        mAdapter?.onItemChildClickListener = this

        if(noNetWork){
            loadMoreEnabled(false)
            refreshEnabled(false)
            stateView.showContent()
        }else{
            // 第一次网络请求失败时点击重新加载
            stateView.setOnRetryClickListener(this)
            refreshLayout.setOnRefreshListener(this)
            mAdapter?.setOnLoadMoreListener(this,recyclerView)
            loadMoreEnabled(true)
            refreshEnabled(true)
        }
    }

    private val requestDelegate = lazy{ object: RequestCallBack<List<E>>(){
            override fun onStart() {
                when (loadingStatus) {
                    BaseListFragment.FIRST -> stateView.showLoading()
                    BaseListFragment.REFRESH -> {
                        refreshLayout.isRefreshing = true
                    }
                    BaseListFragment.LOAD_MORE -> {
                    }
                }
            }

            override fun onSuccess(entity: List<E>) {
                var result: List<E>? = entity
                if (result == null) {
                    //添加判断，防止异常
                    result = ArrayList()
                }
                when (loadingStatus) {
                /** 刚进入页面，第一次请求成功 */
                    BaseListFragment.FIRST -> {
                        //第一次请求成功
                        isFirstLoadSuccess = true

                        if (result.isEmpty()) {
                            //返回数据为空时，展示无数据
                            stateView.showEmpty()
                        } else {
                            //返回数据不为空时，等待层消失，展示数据
                            stateView.showContent()

                            mAdapter?.setNewData(result)

                            //没有更多了
                            if(result.size < getPageSize()){
                                mAdapter?.loadMoreEnd()
                            }else{//还可以加载更多
                                mAdapter?.loadMoreComplete()
                            }
                        }
                    }
                /** 刷新的时候数据请求成功 */
                    BaseListFragment.REFRESH -> {
                        //刷新成功
                        refreshLayout.isRefreshing = false

                        if (result.isEmpty()) {
                            //返回数据为空时，展示无数据
                            stateView.showEmpty()
                        } else {//刷新状态时此时已经是  stateView.showContent() 的状态 所以不用再show一次
                            //展示数据
                            mAdapter?.setNewData(result)
                            //没有更多了
                            if(result.size < getPageSize()){
                                mAdapter?.loadMoreEnd()
                            }else{//还可以加载更多
                                mAdapter?.loadMoreComplete()
                            }
                        }
                    }
                /** 加载更多成功 */
                    BaseListFragment.LOAD_MORE -> {
                        mAdapter?.addData(result)
                        //没有更多了
                        if(result.size < getPageSize()){
                            mAdapter?.loadMoreEnd()
                        }else{//还可以加载更多
                            mAdapter?.loadMoreComplete()
                        }
                    }
                }
            }

            override fun onError(exception: Throwable) {
                when (loadingStatus) {
                //进入页面第一次加载出现的异常
                    BaseListFragment.FIRST -> stateView.showError()
                //刷新的时候出现的异常
                    BaseListFragment.REFRESH -> {
                        refreshLayout.isRefreshing = false
                        T.show(mContext,"数据刷新失败，请重试")
                    }
                //加载更多的时候出现的异常
                    BaseListFragment.LOAD_MORE -> {
                        mCurrentPage--
                        mAdapter?.loadMoreFail()
                    }
                }
            }
        }
    }

    /**
     * 网络请求 需要的时候才被加载，运用了懒加载的策略
     */
    private val requestCallBack: RequestCallBack<List<E>> by requestDelegate


    override fun onResume() {
        super.onResume()
        // onlyView() 是否做网络请求
        // userVisibleHint 判断当前fragment是否显示
        // isFirstLoadSuccess 第一次网络请求是否成功，不成功的话 会继续加载
        if (!noNetWork && userVisibleHint && !isFirstLoadSuccess) {
            loadingStatus = FIRST
            mCurrentPage = 1
            // 加载各种数据
            requestData(mCurrentPage, requestCallBack)
        }
    }

//    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        super.setUserVisibleHint(isVisibleToUser)
//        // 每次切换fragment时调用的方法
//        if (isVisibleToUser && !isFirstLoadSuccess) {
//            showData()
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        //判断这个是否进行了初始化
        if(requestDelegate.isInitialized())
            requestCallBack.cancelRequest()
    }

    /**
     * 第一次加载失败，后点击重新加载
     */
    override fun onRetryClick() {
        loadingStatus = REFRESH
        mCurrentPage = 1
        requestData(mCurrentPage, requestCallBack)
    }

    /**
     * 下拉刷新
     */
    override fun onRefresh() {
        loadingStatus = REFRESH
        mCurrentPage = 1
        requestData(mCurrentPage, requestCallBack)
    }

    /**
     * 上拉加载
     */
    override fun onLoadMoreRequested() {
        loadingStatus = LOAD_MORE
        mCurrentPage++
        requestData(mCurrentPage, requestCallBack)
    }

    /**
     * 列表页的点击事件
     * @param view
     * @param position
     * @param t
     */
    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {}
    /**
     * 列表页的点击事件
     * @param view
     * @param position
     * @param t
     */
    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {}

}