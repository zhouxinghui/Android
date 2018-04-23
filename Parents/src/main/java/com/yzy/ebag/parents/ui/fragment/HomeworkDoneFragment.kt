package com.yzy.ebag.parents.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.bean.HomeworkAbstractBean
import ebag.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_homework_done.*
import java.text.DecimalFormat

@SuppressLint("ValidFragment")
class HomeworkDoneFragment(private val data: HomeworkAbstractBean,private val endTime:String,private val subject:String) : BaseFragment() {

    override fun getLayoutRes(): Int = R.layout.fragment_homework_done

    override fun getBundle(bundle: Bundle?) {

    }

    override fun initViews(rootView: View) {

        homework_sum.text = setSpan(DecimalFormat("0").format(data.totalScore) + "分")
        homework_top.text = setSpan(DecimalFormat("0").format(data.maxScore) + "分")
        homework_error.text = setSpan(DecimalFormat("0").format(data.errorNum) + "道")
        val time = endTime.split(" ")[0].split("-")
        homework_error_content.text = "${time[1]}月${time[2]}日${subject}作业共错${DecimalFormat("0").format(data.errorNum)}道题"
        if (data.teacherComment.isNullOrEmpty()){
            homework_teacher_layout.visibility = View.GONE
        }else{
            homework_teacher_layout.visibility = View.VISIBLE
            homework_teacher_comment.text = data.teacherComment

        }

        if (data.parentComment.isNullOrEmpty()){
            homework_parents_edit.visibility = View.VISIBLE
            homework_parents_comment.visibility = View.GONE
        }else{
            homework_parents_edit.visibility = View.GONE
            homework_parents_comment.visibility = View.VISIBLE
            homework_parents_comment.text = data.parentComment
        }
    }

    private fun setSpan(text: String): SpannableStringBuilder {
        val ssb = SpannableStringBuilder()
        ssb.append(text)
        ssb.setSpan(AbsoluteSizeSpan(35), text.length - 1, text.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        if (text.contains("道"))
            ssb.setSpan(ForegroundColorSpan(Color.parseColor("#ff7500")), 0, text.length - 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        return ssb
    }

}