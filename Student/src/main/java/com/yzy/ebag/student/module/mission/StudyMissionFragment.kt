package com.yzy.ebag.student.module.mission

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.SubjectBean
import com.yzy.ebag.student.http.StudentApi
import com.yzy.ebag.student.module.homework.DoHomeworkActivity
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.util.SPUtils
import ebag.core.util.StringUtils
import ebag.mobile.base.Constants
import ebag.mobile.module.homework.WorkReportActivity

/**
 * Created by YZY on 2018/5/15.
 */
class StudyMissionFragment: BaseListFragment<List<SubjectBean>, SubjectBean.HomeWorkInfoBean>() {
    companion object {
        fun newInstance(): StudyMissionFragment {
            return StudyMissionFragment()
        }
    }

    var onSubjectChange: ((list: List<SubjectBean>) -> Unit)? = null
    private var subCode = ""

    fun setSubcode(subCode: String){
        this.subCode = subCode
        onRetryClick()
    }

    override fun getBundle(bundle: Bundle?) {
    }

    override fun loadConfig() {
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<SubjectBean>>) {
        StudentApi.subjectWorkList(Constants.PARENT_TYPE,
                SPUtils.get(mContext, Constants.CLASS_ID,"") as String,
                subCode,
                page,
                getPageSize(),
                requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<SubjectBean>?): List<SubjectBean.HomeWorkInfoBean>? {
        if(subCode.isEmpty() && parent != null && parent.isNotEmpty()){
            val list = parent.filter { !StringUtils.isEmpty(it.subject) }
            if(list.isNotEmpty()){
                subCode = list[0].subCode
                onSubjectChange?.invoke(list)
            }
        }
        return if(parent != null && parent.isNotEmpty()){
            parent[0].homeWorkInfoVos
        }else{
            null
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        adapter as Adapter
        if(adapter.getItem(position)?.state == Constants.CORRECT_UNFINISH){
            DoHomeworkActivity.jump(
                    mContext,
                    adapter.getItem(position)?.id ?: "",
                    Constants.PARENT_TYPE
            )
        }else{
            WorkReportActivity.jump(mContext, adapter.getItem(position)?.id ?: "", Constants.PARENT_TYPE)
        }
    }

    override fun getAdapter(): BaseQuickAdapter<SubjectBean.HomeWorkInfoBean, BaseViewHolder> = Adapter()

    override fun getLayoutManager(adapter: BaseQuickAdapter<SubjectBean.HomeWorkInfoBean, BaseViewHolder>): RecyclerView.LayoutManager? = null

    private inner class Adapter: BaseQuickAdapter<SubjectBean.HomeWorkInfoBean,BaseViewHolder>(R.layout.item_fragment_task_study){
        override fun convert(helper: BaseViewHolder, item: SubjectBean.HomeWorkInfoBean?) {
            helper.setText(R.id.tvCount, Html.fromHtml("完成： <font color='#FF7800'>${item?.questionComplete}</font>/${item?.questionCount}"))
                    .setText(R.id.tvContent,"内容： ${item?.content}")
                    .setText(R.id.tvTime,"截止时间： ${item?.endTime ?: "无"}")
                    .setText(R.id.tvStatus,
                            when(item?.state){
                                Constants.CORRECT_UNFINISH -> "未完成"
                                Constants.CORRECT_UNCORRECT -> "未批改"
                                Constants.CORRECT_CORRECTED -> "已批改"
                                Constants.CORRECT_TEACHER_REMARKED -> "老师评语完成"
                                Constants.CORRECT_PARENT_REMARKED -> "家长签名和评语完成"
                                else -> "未知状态"
                            }
                    )
            helper.getView<View>(R.id.tvStatus).isSelected = item?.questionComplete == item?.questionCount
        }
    }
}