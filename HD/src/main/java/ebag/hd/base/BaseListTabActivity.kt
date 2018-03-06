package ebag.hd.base

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.RelativeLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.base.BaseActivity
import ebag.core.http.network.MsgException
import ebag.core.http.network.RequestCallBack
import ebag.hd.R
import ebag.hd.widget.TitleBar
import kotlinx.android.synthetic.main.activity_base_list_tab.*
import java.util.*

/**
 * Created by caoyu on 2018/1/13.
 */
abstract class BaseListTabActivity<Parent, E>: BaseActivity(),
        BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener{

    protected var mAdapter: BaseQuickAdapter<E, BaseViewHolder>? = null
    protected var needFirstLoad = true
    protected abstract fun loadConfig()
    protected lateinit var titleBar: TitleBar

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
    protected abstract fun getLayoutManager(adapter: BaseQuickAdapter<E,BaseViewHolder>): RecyclerView.LayoutManager?

    protected abstract fun getFragment(pagerIndex: Int, adapter: BaseQuickAdapter<E, BaseViewHolder>): Fragment

    protected abstract fun getViewPagerSize(adapter: BaseQuickAdapter<E, BaseViewHolder>): Int

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
        return R.layout.activity_base_list_tab
    }

    fun addLeftHeaderView(view: View){
        leftLayout.addView(view,0)
    }

    override fun initViews() {
        titleBar = titleView
        // 设置 recyclerView 的 Adapter
        mAdapter = getLeftAdapter()
        // 设置 RecyclerView 的 LayoutManager
        recyclerView.layoutManager = getLayoutManager(mAdapter!!) ?: LinearLayoutManager(this)
        mAdapter?.bindToRecyclerView(recyclerView)
        //设置 点击监听事件
        mAdapter?.onItemClickListener = this
        mAdapter?.onItemChildClickListener = this

        // 第一次网络请求失败时点击重新加载
        stateView.setOnRetryClickListener{
            request()
        }
        stateView.showEmpty()
        loadConfig()
        if (needFirstLoad) {
            // 加载各种数据
            requestData(requestCallBack)
        }
    }

    protected fun addExtraView(view: View, index: Int = 0){
        if (index == 0){
            rootView.addView(view)
        }else{
            rootView.addView(view, index)
        }

    }

    protected fun request(){
        requestData(requestCallBack)
    }

    protected fun setTitleContent(message: String){
        titleView.setTitle(message)
    }

    protected fun setTitleContent(message: Int){
        titleView.setTitle(message)
    }

    protected fun setLeftWidth(pxSize: Int){
        val params = leftLayout.layoutParams
        params.width = pxSize
    }

    protected fun setMiddleDistance(pxSize: Int){
        val params = leftLayout.layoutParams as RelativeLayout.LayoutParams
        params.rightMargin = pxSize
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
                if(exception is MsgException){
                    stateView.showError(exception.message)
                }else{
                    stateView.showError()
                }
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
    open protected fun firstPageDataLoad(result: List<E>){
        if (result.isEmpty()) {
            //返回数据为空时，展示无数据
            stateView.showEmpty()
        } else {
            stateView.showContent()
            mAdapter?.setNewData(result)
            val size = getViewPagerSize(mAdapter!!)
            viewPager.adapter = SectionsPagerAdapter(supportFragmentManager, arrayOfNulls(size))
            viewPager.offscreenPageLimit = 1
            viewPager.setCurrentItem(0,false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //判断这个是否进行了初始化
        if(requestDelegate.isInitialized())
            requestCallBack.cancelRequest()
    }

    inner class SectionsPagerAdapter(fm: FragmentManager, private val fragments: Array<Fragment?>) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            if (fragments[position] == null) {
                fragments[position] = getFragment(position, mAdapter!!)
            }
            return fragments[position]!!
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return "$position"
        }
    }

    protected fun setCurrentItem(index: Int){
        viewPager.setCurrentItem(index,false)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int) {}
}