package com.yzy.ebag.teacher.ui.fragment.custom

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseFragment
import ebag.core.util.StringUtils
import ebag.core.util.T
import kotlinx.android.synthetic.main.fragment_application.*

/**
 * Created by YZY on 2018/2/8.
 */
class ApplicationFragment : BaseFragment(), ICustomQuestionView {
    companion object {
        fun newInstance(): Fragment{
            val fragment = ApplicationFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun getLayoutRes(): Int {
        return R.layout.fragment_complete
    }

    override fun getBundle(bundle: Bundle?) {

    }

    override fun initViews(rootView: View) {

    }
    override fun getTitle(): String? {
        val str = titleEdit.text.toString()
        if (StringUtils.isEmpty(str)){
            T.show(mContext, "请输入题目标题")
            return null
        }
        return str
    }

    override fun getContent(): String? {
        val str = contentEdit.text.toString()
        if (StringUtils.isEmpty(str)){
            T.show(mContext, "请输入题目内容")
            return null
        }
        return str
    }

    override fun getAnswer(): String? {
        val str = answerEdit.text.toString()
        if (StringUtils.isEmpty(str)){
            T.show(mContext, "请输入题目正确答案")
            return null
        }
        return str
    }

    override fun getAnalyse(): String? {
        val str = analyseEdit.text.toString()
        if (StringUtils.isEmpty(str)){
            T.show(mContext, "请输入题目分析")
            return null
        }
        return str
    }

    override fun upload(onConfirmClickListener: OnConfirmClickListener) {
        onConfirmClickListener.onConfirmClick(getTitle(), getContent(), getAnswer(), getAnalyse())
    }
}