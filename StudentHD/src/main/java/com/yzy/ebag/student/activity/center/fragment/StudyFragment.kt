package com.yzy.ebag.student.activity.center.fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.SubjectBean
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.util.DateUtil

/**
 * @author caoyu
 * @date 2018/1/17
 * @description
 */
class StudyFragment: BaseListFragment<String, SubjectBean.HomeWorkInfoBean>() {

    companion object {
        fun newInstance(classId: String): StudyFragment {
            val fragment = StudyFragment()
            val bundle =Bundle()
            bundle.putString("classId",classId)
            fragment.arguments = bundle
            return fragment
        }
    }

    lateinit var classId: String
    override fun getBundle(bundle: Bundle?) {
        classId = bundle?.getString("classId") ?: ""
    }

    override fun loadConfig() {
        val list = ArrayList<SubjectBean.HomeWorkInfoBean>()
        var data = SubjectBean.HomeWorkInfoBean()
        data.content = "课后作业"
        data.remark = "回家好好做题。"
        data.endTime = 1515661597000
        data.state = "0"
        data.questionCount = 10
        data.questionComplete = 9
        list.add(data)
        data = SubjectBean.HomeWorkInfoBean()
        data.content = "课后作业"
        data.remark = "回家好好做题。"
        data.endTime = 1515661597000
        data.state = "0"
        data.questionCount = 10
        data.questionComplete = 9
        list.add(data)
        data = SubjectBean.HomeWorkInfoBean()
        data.content = "课后作业"
        data.remark = "回家好好做题。"
        data.endTime = 1515661597000
        data.state = "0"
        data.questionCount = 10
        data.questionComplete = 9
        list.add(data)
        data = SubjectBean.HomeWorkInfoBean()
        data.content = "课后作业"
        data.remark = "回家好好做题。"
        data.endTime = 1515661597000
        data.state = "0"
        data.questionCount = 10
        data.questionComplete = 9
        list.add(data)
        data = SubjectBean.HomeWorkInfoBean()
        data.content = "课后作业"
        data.remark = "回家好好做题。"
        data.endTime = 1515661597000
        data.state = "0"
        data.questionCount = 10
        data.questionComplete = 9
        list.add(data)

        withFirstPageData(list)
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<String>) {
    }

    override fun parentToList(isFirstPage: Boolean, parent: String?): List<SubjectBean.HomeWorkInfoBean>? {
        return null
    }

    override fun getAdapter(): BaseQuickAdapter<SubjectBean.HomeWorkInfoBean, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<SubjectBean.HomeWorkInfoBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    class Adapter: BaseQuickAdapter<SubjectBean.HomeWorkInfoBean,BaseViewHolder>(R.layout.item_fragment_task_study){

        override fun convert(helper: BaseViewHolder, item: SubjectBean.HomeWorkInfoBean?) {
//            val spannableString = SpannableString("完成： ${item?.questionComplete}/${item?.questionCount}")
//            spannableString.setSpan(ForegroundColorSpan(resources.getColor(R.color.color_homework_selected)), 4, 4 + "${entity.doneCount}".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            setter.setText(R.id.tvCount, spannableString)
            val time = DateUtil.getDateTime(item?.endTime ?: 0)
            helper.setText(R.id.tvCount, Html.fromHtml("完成： <font color='#FF7800'>${item?.questionComplete}</font>/${item?.questionCount}"))
                    .setText(R.id.tvContent,"内容： ${item?.content}")
                    .setText(R.id.tvTime,"截止时间： $time")
                    .setText(R.id.tvStatus,if(item?.questionComplete == item?.questionCount) "已完成" else "未完成")
            helper.getView<View>(R.id.tvStatus).isSelected = item?.questionComplete == item?.questionCount
        }
    }
}