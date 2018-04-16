package com.yzy.ebag.teacher.activity.correcting

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.CorrectingBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.util.DateUtil
import ebag.hd.base.Constants
import java.util.*

/**
 * Created by YZY on 2018/1/13.
 */
class CorrectingSubFragment: BaseListFragment<List<CorrectingBean>, CorrectingBean.SubjectVosBean.HomeWorkInfoVosBean>() {
    companion object {
        fun newInstance(type: String, classId: String, subCode: String): CorrectingSubFragment{
            val fragment = CorrectingSubFragment()
            val bundle = Bundle()
            bundle.putString("type", type)
            bundle.putString("classId", classId)
            bundle.putString("subCode", subCode)
            fragment.arguments = bundle
            return fragment
        }

        fun newInstance(): CorrectingSubFragment{
            val fragment = CorrectingSubFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
    private var type = ""
    private var classId = ""
    private var subCode = ""
    private var className = ""
    override fun getBundle(bundle: Bundle?) {
        type = bundle?.getString("type") ?: "1"
        classId = bundle?.getString("classId") ?: ""
        subCode = bundle?.getString("subCode") ?: ""
    }

    override fun loadConfig() {

    }

    override fun getPageSize(): Int {
        return 10
    }

    fun update(type: String, classId: String, subCode: String){
        this.type = type
        this.classId = classId
        this.subCode = subCode
        onRetryClick()
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<CorrectingBean>>) {
        TeacherApi.searchPublish(type, requestCallBack, classId, subCode, page, getPageSize())
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<CorrectingBean>?): List<CorrectingBean.SubjectVosBean.HomeWorkInfoVosBean>? {
        if (parent == null || parent.isEmpty() || parent[0].subjectVos == null || parent[0].subjectVos.isEmpty())
            return ArrayList()
        className = parent[0].className
        return parent[0].subjectVos[0].homeWorkInfoVos
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
            helper.setText(R.id.createTime, "布置时间：${DateUtil.getFormatDateTime(Date(item.createDate), "yyyy-MM-dd HH:mm")}")
            classNameTv.text = item.groupName?:className
            contentTv.text = item.content
            completeTv.text = "完成： ${item.homeWorkCompleteCount}/${item.studentCount}"
            if (type ==  Constants.KSSJ_TYPE)
                timeTv.text = "考试时间： ${item.endTime}"
            else
                timeTv.text = "截止时间： ${item.endTime}"
            if (item.state == Constants.CORRECT_CORRECTED)
                statusTv.text = "已检查"
            else
                statusTv.text = "待检查"
            statusTv.isSelected = item.state != Constants.CORRECT_CORRECTED
        }

    }
}