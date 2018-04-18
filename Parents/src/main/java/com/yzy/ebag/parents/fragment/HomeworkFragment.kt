package com.yzy.ebag.parents.fragment

import android.os.Bundle
import android.view.View
import com.yzy.ebag.parents.R
import ebag.core.base.BaseFragment
import kotlinx.android.synthetic.main.item_homeworkcontent.*

class HomeworkFragment : BaseFragment() {

    companion object {
        fun newInstance(): HomeworkFragment {
            val fragment = HomeworkFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutRes(): Int = R.layout.item_homeworkcontent

    override fun getBundle(bundle: Bundle?) {

    }

    override fun initViews(rootView: View) {

        homework_status.text = "完成"
        homework_content.text = "完成作业"
        homework_date.text = "4-18"
    }


}