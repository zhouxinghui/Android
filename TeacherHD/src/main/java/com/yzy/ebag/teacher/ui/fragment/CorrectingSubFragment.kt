package com.yzy.ebag.teacher.ui.fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.widget.TextView
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
        fun newInstance(): CorrectingSubFragment{
            val fragment = CorrectingSubFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getBundle(bundle: Bundle?) {

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

    override fun parentToList(isFirstPage: Boolean, parent: String?): List<String>? {
        return null
    }

    override fun getAdapter(): BaseQuickAdapter<String, BaseViewHolder> {
        return MyAdapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<String, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    inner class MyAdapter: BaseQuickAdapter<String, BaseViewHolder>(R.layout.fragment_correct_sub_item){
        override fun convert(helper: BaseViewHolder, item: String?) {
            val classNameTv = helper.getView<TextView>(R.id.classNameTv)
            val contentTv = helper.getView<TextView>(R.id.tvContent)
            val completeTv = helper.getView<TextView>(R.id.completeNum)
            val timeTv = helper.getView<TextView>(R.id.tvTime)
            val statusTv = helper.getView<TextView>(R.id.tvStatus)
            classNameTv.text = "一年级一班"
            contentTv.text = "内容： 第一单元 看一看"
            completeTv.text = "完成： 28/28"
            timeTv.text = "截止时间： 2017-12-25 12:50"
            statusTv.text = "已检查"
        }

    }
}