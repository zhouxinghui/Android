package com.yzy.ebag.teacher.ui.fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack

/**
 * Created by YZY on 2018/3/2.
 */
class PrepareFragment: BaseListFragment<List<String>, String>() {
    companion object {
        fun newInstance(): PrepareFragment{
            val fragment = PrepareFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun getBundle(bundle: Bundle?) {

    }

    override fun loadConfig() {
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<String>>) {
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<String>?): List<String>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<String, BaseViewHolder> {
        return MyAdapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<String, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    inner class MyAdapter: BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_prepare){
        override fun convert(helper: BaseViewHolder?, item: String?) {

        }
    }
}