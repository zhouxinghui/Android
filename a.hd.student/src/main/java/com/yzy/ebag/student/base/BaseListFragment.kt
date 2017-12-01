package com.yzy.ebag.student.base

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.yzy.ebag.student.R
import ebag.core.base.BaseFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.widget.empty.StateView
import ebag.core.xRecyclerView.XRecyclerView
import ebag.core.xRecyclerView.adapter.OnItemChildClickListener
import ebag.core.xRecyclerView.adapter.OnRVItemClickListener
import ebag.core.xRecyclerView.adapter.RecyclerAdapter
import kotlinx.android.synthetic.main.base_listview.*
import java.util.*

/**
 * Created by unicho on 2017/11/30.
 */
abstract class BaseListFragment<T> : BaseFragment(), XRecyclerView.OnLoadMoreListener
        , XRecyclerView.OnRefreshListener,StateView.OnRetryClickListener,OnRVItemClickListener,OnItemChildClickListener {

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
    private var mAdapter: RecyclerAdapter<T>? = null
    protected var mCurrentPage = 1
    private var isInit: Boolean = false
    protected var isFirstLoadSuccess: Boolean = false

    /**
     *  网络请求
     * @param page
     * @param requestCallBack
     */
    protected abstract fun requestData(page: Int, requestCallBack: RequestCallBack<List<T>>)

    /**
     * 设置 recyclerView 的适配器 Adapter
     */
    protected abstract fun getAdapter(): RecyclerAdapter<T>

    /**
     * 设置 recyclerView 的 LayoutManager
     */
    protected abstract fun getLayoutManager(): RecyclerView.LayoutManager?

    /**
     * 列表页的点击事件
     * @param view
     * @param position
     * @param t
     */
    protected abstract fun onItemClick(view: View, position: Int, t: T)

    /**
     * 列表页的点击事件
     * @param view
     * @param position
     * @param t
     */
    protected abstract fun onItemChildClick(view: View, position: Int, t: T)

    /** 每页默认加载的数量 */
    protected fun getPageSize(): Int = DEFAULT_PAGE_SIZE

    /** 是否有加载更多操作 */
    protected fun canLoadMore(): Boolean = true

    /** 是否有下拉刷新操作 */
    protected fun canRefresh(): Boolean = true

    /** 当前网络请求所处的状态 */
    private var loadingStatus: Int = FIRST

    override fun getRootViewId(): Int {
        return R.layout.base_listview
    }

    /** 初始化操作 */
    override fun initViews(rootView: View) {
        // 第一次网络请求失败时点击重新加载
        stateView.setOnRetryClickListener(this)

        // 是否可以刷新
        if(canRefresh())
            recyclerView.setOnRefreshListener(this)

        // 是否可以加载更多
        if(canLoadMore())
            recyclerView.setOnLoadMoreListener(this)
        else
            recyclerView.noMoreLoadView()

        // 设置 RecyclerView 的 LayoutManager
        recyclerView.layoutManager = getLayoutManager() ?: LinearLayoutManager(mContext)
        // 设置 recyclerView 的 Adapter
        mAdapter = getAdapter()
        recyclerView.adapter = mAdapter

        //设置 点击监听事件
        mAdapter?.setOnRVItemClickListener(this)
        mAdapter?.setOnItemChildClickListener(this)

        isInit = true
    }

    private val requestCallBack: RequestCallBack<List<T>> = object: RequestCallBack<List<T>>(){

        override fun onStart() {
            when (loadingStatus) {
                FIRST -> stateView.showLoading()
                REFRESH -> {
                }
                LOAD_MORE -> {
                }
            }
        }

        override fun onSuccess(entity: List<T>) {
            var result: List<T>? = entity
            if (result == null) {
                //添加判断，防止异常
                result = ArrayList()
            }
            when (loadingStatus) {
                /** 刚进入页面，第一次请求成功 */
                FIRST -> {
                    //第一次请求成功
                    isFirstLoadSuccess = true

                    if (result.isEmpty()) {
                        //返回数据为空时，展示无数据
                        stateView.showEmpty()
                    } else {
                        //返回数据不为空时，等待层消失，展示数据
                        stateView.showContent()

                        mAdapter?.datas = result
                        //没有更多了
                        if(result.size < getPageSize()){
                            recyclerView.noMore()
                        }else{//还可以加载更多
                            recyclerView.loadMoreComplete()
                        }
                    }
                }
                /** 刷新的时候数据请求成功 */
                REFRESH -> {
                    //刷新成功
                    recyclerView.refreshComplete()

                    if (result.isEmpty()) {
                        //返回数据为空时，展示无数据
                        stateView.showEmpty()
                    } else {//刷新状态时此时已经是  stateView.showContent() 的状态 所以不用再show一次
                        //展示数据
                        mAdapter?.datas = result
                        //没有更多了
                        if(result.size < getPageSize()){
                            recyclerView.noMore()
                        }else{//还可以加载更多
                            recyclerView.loadMoreComplete()
                        }
                    }
                }
                /** 加载更多成功 */
                LOAD_MORE -> {
                    mAdapter?.addMoreDatas(result)
                    //没有更多了
                    if(result.size < getPageSize()){
                        recyclerView.noMore()
                    }else{//还可以加载更多
                        recyclerView.loadMoreComplete()
                    }
                }
            }
        }

        override fun onError(exception: Throwable) {
            when (loadingStatus) {
                //进入页面第一次加载出现的异常
                FIRST -> stateView.showError()
                //刷新的时候出现的异常
                REFRESH -> recyclerView.refreshError()
                //加载更多的时候出现的异常
                LOAD_MORE -> {
                    mCurrentPage--
                    recyclerView.loadMoreError()
                }
            }
        }

    }


    override fun onResume() {
        super.onResume()
        // 判断当前fragment是否显示
        if (userVisibleHint && !isFirstLoadSuccess) {
            showData()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        // 每次切换fragment时调用的方法
        if (isVisibleToUser && !isFirstLoadSuccess) {
            showData()
        }
    }

    /**
     * 第一次加载数据
     */
    private fun showData() {
        if (!isInit) {
            return
        }
        loadingStatus = FIRST
        mCurrentPage = 1
        // 加载各种数据
        requestData(mCurrentPage, requestCallBack)
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
    override fun onLoadMore() {
        loadingStatus = LOAD_MORE
        mCurrentPage++
        requestData(mCurrentPage, requestCallBack)
    }

    override fun onRVItemClick(recyclerView: ViewGroup, view: View, position: Int) {
        onItemClick(view, position - (recyclerView as XRecyclerView).headerSize, mAdapter?.getItem(position)!!)
    }

    override fun onItemChildClick(recyclerView: ViewGroup, view: View, position: Int) {
        onItemChildClick(view, position - (recyclerView as XRecyclerView).headerSize, mAdapter?.getItem(position)!!)
    }
}