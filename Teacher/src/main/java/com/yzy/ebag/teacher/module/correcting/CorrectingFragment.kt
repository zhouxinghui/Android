package com.yzy.ebag.teacher.module.correcting

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
import ebag.mobile.base.Constants
import java.util.*

/**
 * Created by YZY on 2018/4/26.
 */
class CorrectingFragment: BaseListFragment<List<CorrectingBean>, CorrectingBean.SubjectVosBean.HomeWorkInfoVosBean>() {
    companion object {
        fun newInstance(type: String, classId: String, subCode: String): CorrectingFragment{
            val fragment = CorrectingFragment()
            val bundle = Bundle()
            bundle.putString("type", type)
            bundle.putString("classId", classId)
            bundle.putString("subCode", subCode)
            fragment.arguments = bundle
            return fragment
        }

        fun newInstance(): CorrectingFragment{
            val fragment = CorrectingFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun getBundle(bundle: Bundle?) {
        type = bundle?.getString("type") ?: "1"
        classId = bundle?.getString("classId") ?: ""
        subCode = bundle?.getString("subCode") ?: ""
    }
    private var type = ""
    private var classId = ""
    private var subCode = ""
    private var className = ""
    override fun loadConfig() {

    }

    fun setData(list: List<CorrectingBean.SubjectVosBean.HomeWorkInfoVosBean>?){
        withFirstPageData(list)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (!isViewCreated)
            return
        if (isVisibleToUser && mContext is CorrectingActivity){
            val classId = (mContext as CorrectingActivity).classId
            val subCode = (mContext as CorrectingActivity).subCode
            if (this.classId != classId || this.subCode != subCode){
                this.classId = classId
                this.subCode = subCode
                onRetryClick()
            }
        }
    }

    fun update(classId: String, subCode: String){
        if (this.classId != classId || this.subCode != subCode){
            this.classId = classId
            this.subCode = subCode
            onRetryClick()
        }
    }

    override fun getPageSize(): Int = 10

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<CorrectingBean>>) {
        TeacherApi.searchPublish(type, requestCallBack, classId, subCode, page, getPageSize())
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<CorrectingBean>?): List<CorrectingBean.SubjectVosBean.HomeWorkInfoVosBean>? {
        if (parent == null || parent.isEmpty() || parent[0].subjectVos == null || parent[0].subjectVos.isEmpty())
            return ArrayList()
        className = parent[0].className
        return parent[0].subjectVos[0].homeWorkInfoVos
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        adapter as MyAdapter
        if (mContext == null)
            mContext = activity
        CorrectingDescActivity.jump(mContext, adapter.data[position].id, type)
    }

    override fun getAdapter(): BaseQuickAdapter<CorrectingBean.SubjectVosBean.HomeWorkInfoVosBean, BaseViewHolder> = MyAdapter()

    override fun getLayoutManager(adapter: BaseQuickAdapter<CorrectingBean.SubjectVosBean.HomeWorkInfoVosBean, BaseViewHolder>): RecyclerView.LayoutManager? = null

    inner class MyAdapter: BaseQuickAdapter<CorrectingBean.SubjectVosBean.HomeWorkInfoVosBean, BaseViewHolder>(R.layout.fragment_correct_sub_item){
        override fun convert(helper: BaseViewHolder, item: CorrectingBean.SubjectVosBean.HomeWorkInfoVosBean) {
            val classNameTv = helper.getView<TextView>(R.id.classNameTv)
            val contentTv = helper.getView<TextView>(R.id.tvContent)
            val completeTv = helper.getView<TextView>(R.id.completeNum)
            val timeTv = helper.getView<TextView>(R.id.tvTime)
            val statusTv = helper.getView<TextView>(R.id.tvStatus)
            helper.setText(R.id.createTime, "布置时间：${DateUtil.getFormatDateTime(Date(item.createDate), "yyyy-MM-dd HH:mm")}")
            classNameTv.text = "$className${item.groupName?:""}"
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