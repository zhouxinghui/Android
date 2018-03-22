package com.yzy.ebag.teacher.activity.correcting

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.CorrectingBean
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack
import ebag.hd.base.Constants

/**
 * Created by YZY on 2018/1/13.
 */
class CorrectingSubFragment: BaseListFragment<List<CorrectingBean.SubjectVosBean.HomeWorkInfoVosBean>, CorrectingBean.SubjectVosBean.HomeWorkInfoVosBean>() {

    companion object {
        fun newInstance(list: ArrayList<CorrectingBean.SubjectVosBean.HomeWorkInfoVosBean>, type: String): CorrectingSubFragment {
            val fragment = CorrectingSubFragment()
            val bundle = Bundle()
            bundle.putSerializable("list", list)
            bundle.putSerializable("type", type)
            fragment.arguments = bundle
            return fragment
        }
    }
    private lateinit var list: ArrayList<CorrectingBean.SubjectVosBean.HomeWorkInfoVosBean>
    private var type = ""
    override fun getBundle(bundle: Bundle?) {
        list = bundle?.getSerializable("list") as ArrayList<CorrectingBean.SubjectVosBean.HomeWorkInfoVosBean>
        type = bundle.getString("type")
    }

    override fun loadConfig() {
        withFirstPageData(list)
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<CorrectingBean.SubjectVosBean.HomeWorkInfoVosBean>>) {
        requestCallBack.onSuccess(ArrayList())
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<CorrectingBean.SubjectVosBean.HomeWorkInfoVosBean>?): List<CorrectingBean.SubjectVosBean.HomeWorkInfoVosBean>? {
        return null
    }

    override fun getAdapter(): BaseQuickAdapter<CorrectingBean.SubjectVosBean.HomeWorkInfoVosBean, BaseViewHolder> {
        return MyAdapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<CorrectingBean.SubjectVosBean.HomeWorkInfoVosBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        adapter as MyAdapter
        CorrectingDescActivity.jump(mContext, adapter.data[position].id, type)
    }

    inner class MyAdapter: BaseQuickAdapter<CorrectingBean.SubjectVosBean.HomeWorkInfoVosBean, BaseViewHolder>(R.layout.fragment_correct_sub_item){
        override fun convert(helper: BaseViewHolder, item: CorrectingBean.SubjectVosBean.HomeWorkInfoVosBean) {
            val classNameTv = helper.getView<TextView>(R.id.classNameTv)
            val contentTv = helper.getView<TextView>(R.id.tvContent)
            val completeTv = helper.getView<TextView>(R.id.completeNum)
            val timeTv = helper.getView<TextView>(R.id.tvTime)
            val statusTv = helper.getView<TextView>(R.id.tvStatus)
            classNameTv.text = item.className
            contentTv.text = item.content
            completeTv.text = "完成： ${item.homeWorkCompleteCount}/${item.studentCount}"
            if (type ==  Constants.KSSJ_TYPE)
                timeTv.text = "考试时间： ${item.endTime}"
            else
                timeTv.text = "截止时间： ${item.endTime}"
            if (item.state == "0")
                statusTv.text = "待检查"
            else
                statusTv.text = "已检查"
            statusTv.isSelected = item.state != "0"
        }

    }
}