package com.yzy.ebag.teacher.ui.fragment.custom

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseFragment

/**
 * Created by YZY on 2018/2/8.
 */
class CompleteFragment: BaseFragment(), ICustomQuestionView {
    companion object {
        fun newInstance(): Fragment{
            val fragment = CompleteFragment()
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
    override fun getTitle(): String {
        return ""
    }

    override fun getContent(): String {
        return ""
    }

    override fun getAnswer(): String {
        return ""
    }

    override fun getAnalyse(): String {
        return ""
    }
    override fun upload(onConfirmClickListener: OnConfirmClickListener) {

    }
}