package com.yzy.ebag.parents.ui.fragment

import android.os.Bundle
import android.view.View
import com.yzy.ebag.parents.R
import ebag.core.base.BaseFragment
import kotlinx.android.synthetic.main.item_homeworkcontent.*

class HomeworkFragment : BaseFragment() {

    private lateinit var status: String
    private lateinit var content: String
    private lateinit var date: String

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
        status = bundle!!.getString("status")
        content = bundle.getString("content")
        date = bundle.getString("endTime")
    }

    override fun initViews(rootView: View) {
        homework_status.visibility = View.VISIBLE
        homework_date.visibility = View.VISIBLE

        when(status){
            "0" -> homework_status.text = "未完成"
            "1" -> homework_status.text = "未批改"
            "2" -> homework_status.text = "已批改"
            "3" -> homework_status.text = "老师评语完成"
            "4" -> homework_status.text = "家长签名和评语完成"
        }

        homework_content.text = content

        homework_date.text = date.split(" ")[0]
    }


}