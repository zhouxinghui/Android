package com.yzy.ebag.teacher.ui.fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack

/**
 * Created by YZY on 2018/1/13.
 */
class CorrectingSubFragment: BaseListFragment<String, String>() {
    companion object {
        fun newInstance(): CorrectingFragment{
            val fragment = CorrectingFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getBundle(bundle: Bundle) {

    }

    override fun loadConfig() {
        val list = ArrayList<String>()
        for (i in 0..20){
            list.add("")
        }
        withFirstPageData(list)
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<String>) {

    }

    override fun parentToList(parent: String?): List<String>? {
        return null
    }

    override fun getAdapter(): BaseQuickAdapter<String, BaseViewHolder> {
        return MyAdapter()
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager? {
        return null
    }

    inner class MyAdapter: BaseQuickAdapter<String, BaseViewHolder>(R.layout.fragment_correct_sub_item){
        override fun convert(helper: BaseViewHolder?, item: String?) {

        }

    }
}