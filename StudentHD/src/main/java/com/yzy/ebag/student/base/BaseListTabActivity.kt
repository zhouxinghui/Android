package com.yzy.ebag.student.base

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import ebag.core.base.App.Companion.mContext
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import kotlinx.android.synthetic.main.activity_list_tab.*
import kotlinx.android.synthetic.main.base_list_tab_view.*
import java.util.*

/**
 * Created by unicho on 2018/1/13.
 */
abstract class BaseListTabActivity<Parent, E>: BaseActivity(),
        BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener{

    protected var mAdapter: BaseQuickAdapter<E, BaseViewHolder>? = null
    protected var needFirstLoad = true
    protected abstract fun loadConfig()

    /**
     *  网络请求
     * @param page
     * @param requestCallBack
     */
    protected abstract fun requestData(requestCallBack: RequestCallBack<Parent>)

    protected abstract fun parentToList(parent: Parent?): List<E>?

    /**
     * 设置 recyclerView 的适配器 Adapter
     */
    protected abstract fun getLeftAdapter(): BaseQuickAdapter<E,BaseViewHolder>

    /**
     * 设置 recyclerView 的 LayoutManager
     */
    protected abstract fun getLayoutManager(): RecyclerView.LayoutManager?

    protected abstract fun getFragment(index: Int, item: E?): Fragment

    protected fun withTabData(list: List<E>?){
        if(list != null && mAdapter != null){
            needFirstLoad = false
            firstPageDataLoad(list)
        }
    }

    protected fun enableNetWork(enable: Boolean){
        this.needFirstLoad = enable
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_list_tab
    }


    override fun initViews() {
        // 设置 RecyclerView 的 LayoutManager
        recyclerView.layoutManager = getLayoutManager() ?: LinearLayoutManager(mContext)
        // 设置 recyclerView 的 Adapter
        mAdapter = getLeftAdapter()
        mAdapter?.bindToRecyclerView(recyclerView)
        //设置 点击监听事件
        mAdapter?.onItemClickListener = this
        mAdapter?.onItemChildClickListener = this

        // 第一次网络请求失败时点击重新加载
        stateView.setOnRetryClickListener{
            requestData(requestCallBack)
        }
        stateView.showEmpty()
        loadConfig()
        if (needFirstLoad) {
            // 加载各种数据
            requestData(requestCallBack)
        }
    }

    protected fun setTitleContent(message: String){
        titleView.setTitle(message)
    }

    protected fun setTitleContent(message: Int){
        titleView.setTitle(message)
    }

    protected fun setLeftWidth(pxSize: Int){
        val params = tagView.layoutParams
        params.width = pxSize
    }

    protected fun hideTipTag(){
        tvTag.visibility = View.GONE
    }

    protected fun showTipTag(str: String){
        tvTag.visibility = View.VISIBLE
        tvTag.text = str
    }

    protected fun showTipTag(str: Int){
        tvTag.visibility = View.VISIBLE
        tvTag.setText(str)
    }

    protected fun getTipTag(): TextView{
        return tvTag
    }

    private val requestDelegate = lazy {
        object : RequestCallBack<Parent>() {
            override fun onStart() {
                stateView.showLoading()
            }

            override fun onSuccess(entity: Parent?) {
                var result: List<E>? = parentToList(entity)
                if (result == null) {
                    //添加判断，防止异常
                    result = ArrayList()
                }
                //第一次请求成功
                needFirstLoad = false
                //返回数据不为空时，等待层消失，展示数据
                stateView.showContent()
                firstPageDataLoad(result)
            }

            override fun onError(exception: Throwable) {
                stateView.showError()
            }
        }
    }

    /**
     * 网络请求 需要的时候才被加载，运用了懒加载的策略
     */
    private val requestCallBack: RequestCallBack<Parent> by requestDelegate

    /**
     * 加载第一屏数据 的View的改变
     */
    private fun firstPageDataLoad(result: List<E>){
        if (result.isEmpty()) {
            //返回数据为空时，展示无数据
            stateView.showEmpty()
        } else {
            stateView.showContent()
            mAdapter?.setNewData(result)
            viewPager.adapter = SectionsPagerAdapter(supportFragmentManager, arrayOfNulls(mAdapter?.itemCount ?: 0))
            viewPager.setCurrentItem(0,false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //判断这个是否进行了初始化
        if(requestDelegate.isInitialized())
            requestCallBack.cancelRequest()
    }

    inner class SectionsPagerAdapter(fm: FragmentManager, private val fragments: Array<Fragment?>) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            if (fragments[position] == null) {
                fragments[position] = getFragment(position,mAdapter?.getItem(position))
            }
            return fragments[position]!!
        }

        override fun getCount(): Int {
            return mAdapter?.itemCount ?: 0
        }
    }

    protected fun setCurrentItem(index: Int){
        viewPager.setCurrentItem(index,false)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int) {
        leftItemClick(adapter,view,position)
        setCurrentItem(position)
    }

    abstract fun leftItemClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int)

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int) {}
}