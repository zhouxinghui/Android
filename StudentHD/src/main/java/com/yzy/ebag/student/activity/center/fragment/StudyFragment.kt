package com.yzy.ebag.student.activity.center.fragment

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.SubjectBean
import com.yzy.ebag.student.dialog.ListPopupWindow
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.util.SPUtils
import ebag.core.util.StringUtils
import ebag.hd.activity.ReportTestActivity
import ebag.hd.base.Constants
import ebag.hd.homework.DoHomeworkActivity

/**
 * @author caoyu
 * @date 2018/1/17
 * @description
 */
class StudyFragment : BaseListFragment<List<SubjectBean>, SubjectBean.HomeWorkInfoBean>() {

    companion object {
        fun newInstance(): StudyFragment {
            return StudyFragment()
        }
    }

    override fun getBundle(bundle: Bundle?) {
    }

    private var subCode = ""
    lateinit var spinner: View
    private lateinit var tvSelect: TextView
    override fun loadConfig() {
        val params = refreshLayout.layoutParams as RelativeLayout.LayoutParams
        params.topMargin = resources.getDimensionPixelSize(R.dimen.x48)
        refreshLayout.layoutParams = params
        stateView.layoutParams = params

//        rootView.setPadding(0, resources.getDimensionPixelSize(R.dimen.x48), 0, 0)
        spinner = LayoutInflater.from(mContext).inflate(R.layout.item_study_task_spinner, null)
        val rParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        rParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
        rootView.addView(spinner, rParams)
        tvSelect = spinner.findViewById(R.id.tvSelect)

        spinner.visibility = View.GONE
        spinner.setOnClickListener {
            popZhWnd.show()
        }

//        val ll = ArrayList<SubjectBean>()
//        var dd = SubjectBean()
//        dd.subCode = "yw"
//        dd.subject = "语文"
//        ll.add(dd)
//        dd = SubjectBean()
//        dd.subCode = "yy"
//        dd.subject = "英语"
//        ll.add(dd)
//        dd = SubjectBean()
//        dd.subCode = "sx"
//        dd.subject = "数学"
//        ll.add(dd)
//        spinner.visibility = View.VISIBLE
//        tvSelect.text = ll[0].subject
//        popZhWnd.setData(ll)
//
//        val list = ArrayList<SubjectBean.HomeWorkInfoBean>()
//        var data = SubjectBean.HomeWorkInfoBean()
//        data.content = "课后作业"
//        data.remark = "回家好好做题。"
//        data.endTime = ""
//        data.state = "0"
//        data.questionCount = 10
//        data.questionComplete = 9
//        list.add(data)
//        data = SubjectBean.HomeWorkInfoBean()
//        data.content = "课后作业"
//        data.remark = "回家好好做题。"
//        data.endTime = ""
//        data.state = "0"
//        data.questionCount = 10
//        data.questionComplete = 9
//        list.add(data)
//        data = SubjectBean.HomeWorkInfoBean()
//        data.content = "课后作业"
//        data.remark = "回家好好做题。"
//        data.endTime = ""
//        data.state = "0"
//        data.questionCount = 10
//        data.questionComplete = 9
//        list.add(data)
//        data = SubjectBean.HomeWorkInfoBean()
//        data.content = "课后作业"
//        data.remark = "回家好好做题。"
//        data.endTime = ""
//        data.state = "0"
//        data.questionCount = 10
//        data.questionComplete = 9
//        list.add(data)
//        data = SubjectBean.HomeWorkInfoBean()
//        data.content = "课后作业"
//        data.remark = "回家好好做题。"
//        data.endTime = ""
//        data.state = "0"
//        data.questionCount = 10
//        data.questionComplete = 9
//        list.add(data)
//
//        withFirstPageData(list)
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<SubjectBean>>) {
        StudentApi.subjectWorkList(com.yzy.ebag.student.base.Constants.PARENT_TYPE, SPUtils.get(activity, Constants.CLASS_ID, "") as String, subCode, page, getPageSize(), requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<SubjectBean>?): List<SubjectBean.HomeWorkInfoBean>? {
        if (subCode.isEmpty() && parent != null && parent.isNotEmpty()) {
            val list = parent.filter { !StringUtils.isEmpty(it.subject) }
            if (list.isNotEmpty()) {
                spinner.visibility = View.VISIBLE
                tvSelect.text = list[0].subject
                subCode = list[0].subCode
                popZhWnd.setData(list)
            } else {
                spinner.visibility = View.GONE
            }
        }
        return if (parent != null && parent.isNotEmpty()) {
            parent[0].homeWorkInfoVos
        } else {
            null
        }

    }

    override fun getAdapter(): BaseQuickAdapter<SubjectBean.HomeWorkInfoBean, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<SubjectBean.HomeWorkInfoBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        adapter as Adapter
        if (adapter.getItem(position)?.state == Constants.CORRECT_UNFINISH) {
            DoHomeworkActivity.jump(
                    mContext,
                    adapter.getItem(position)?.id ?: "",
                    com.yzy.ebag.student.base.Constants.PARENT_TYPE,
                    com.yzy.ebag.student.base.Constants.PARENT_TYPE
            )
        } else {
            ReportTestActivity.jump(mContext, adapter.getItem(position)?.id
                    ?: "", com.yzy.ebag.student.base.Constants.PARENT_TYPE)
            //ReportClassActivity.jump(mContext, adapter.getItem(position)?.id ?: "", com.yzy.ebag.student.base.Constants.PARENT_TYPE)
        }
    }

    class Adapter : BaseQuickAdapter<SubjectBean.HomeWorkInfoBean, BaseViewHolder>(R.layout.item_fragment_task_study) {

        override fun convert(helper: BaseViewHolder, item: SubjectBean.HomeWorkInfoBean?) {
//            val spannableString = SpannableString("完成： ${item?.questionComplete}/${item?.questionCount}")
//            spannableString.setSpan(ForegroundColorSpan(resources.getColor(R.color.color_homework_selected)), 4, 4 + "${entity.doneCount}".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            setter.setText(R.id.tvCount, spannableString)
            helper.setText(R.id.tvCount, Html.fromHtml("完成： <font color='#FF7800'>${item?.questionComplete}</font>/${item?.questionCount}"))
                    .setText(R.id.tvContent, "内容： ${item?.content}")
                    .setText(R.id.tvTime, "截止时间： ${item?.endTime ?: "无"}")
                    .setText(R.id.tvStatus,
                            when (item?.state) {
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

    private val popZhWnd by lazy {
        object : ListPopupWindow<SubjectBean>(mContext, spinner, resources.getDimensionPixelSize(R.dimen.x268)) {

            private val selected = Color.parseColor("#499DFF")
            private val noDone = Color.parseColor("#FF666D")
            private val normal = Color.parseColor("#373b45")
            override fun popupVisible(visible: Boolean) {
                spinner.isSelected = visible
            }

            override fun itemClick(item: SubjectBean?) {
                if (subCode != item?.subCode) {
                    subCode = item?.subCode ?: ""
                    tvSelect.text = item?.subject
                    onRetryClick()
                }
            }

            override fun fillData(helper: BaseViewHolder, item: SubjectBean?) {
                helper.setText(R.id.text, item?.subject)
                helper.setTextColor(
                        R.id.text,
                        when {
                            subCode == item?.subCode -> selected
                            item?.homeWorkComplete != "0" -> noDone
                            else -> normal
                        }
                )
            }
        }
    }
}