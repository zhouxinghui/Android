package ebag.core.base

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.R
import ebag.core.http.network.RequestCallBack
import kotlinx.android.synthetic.main.base_list_tab_view.*
import java.util.*

/**
 * Created by unicho on 2018/1/13.
 */
abstract class BaseListTabFragment<Parent, E>: BaseFragment(),
        BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener{

    private var mAdapter: BaseQuickAdapter<E, BaseViewHolder>? = null
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

    override fun getLayoutRes(): Int {
        return R.layout.base_list_tab_view
    }

    override fun initViews(rootView: View) {

        // 设置 recyclerView 的 Adapter
        mAdapter = getLeftAdapter()
        // 设置 RecyclerView 的 LayoutManager
        recyclerView.layoutManager = getLayoutManager(mAdapter!!) ?: LinearLayoutManager(mContext)
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
    }

    protected fun setLeftWidth(pxSize: Int){
        val params = recyclerView.layoutParams
        params.width = pxSize
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
            setViewPager()
        }
    }

    open fun setViewPager(){
        viewPager.adapter = SectionsPagerAdapter(childFragmentManager, arrayOfNulls(getViewPagerSize(mAdapter!!)))
        viewPager.setCurrentItem(0,false)
    }

    override fun onResume() {
        super.onResume()
        // userVisibleHint 判断当前fragment是否显示
        // needFirstLoad 是否需要第一次的网络加载
        if (userVisibleHint && needFirstLoad) {
            // 加载各种数据
            requestData(requestCallBack)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //判断这个是否进行了初始化
        if(requestDelegate.isInitialized())
            requestCallBack.cancelRequest()
    }

    protected fun setCurrentItem(index: Int){
        viewPager.setCurrentItem(index,false)
    }

    /**
     * 列表页的点击事件
     * @param view
     * @param position
     * @param t
     */
    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {}

    inner class SectionsPagerAdapter(fm: FragmentManager, private val fragments: Array<Fragment?>) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            if (fragments[position] == null) {
                fragments[position] = getFragment(position,mAdapter!!)
            }
            return fragments[position]!!
        }

        override fun getCount(): Int {
            return mAdapter?.itemCount ?: 0
        }
    }
}