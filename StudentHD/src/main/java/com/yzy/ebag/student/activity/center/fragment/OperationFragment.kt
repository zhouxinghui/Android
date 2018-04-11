package com.yzy.ebag.student.activity.center.fragment

import android.os.Bundle
import android.view.View
import com.yzy.ebag.student.R
import ebag.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_operation.*

/**
 * Created by YZY on 2018/4/11.
 */
class OperationFragment: BaseFragment(){
    companion object {
        fun newInstance(): OperationFragment{
            val fragment = OperationFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun getLayoutRes(): Int {
        return R.layout.fragment_operation
    }

    override fun getBundle(bundle: Bundle?) {

    }

    override fun initViews(rootView: View) {
        tv1.setOnClickListener {
            img1.visibility = if (img1.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
        tv2.setOnClickListener {
            img2.visibility = if (img2.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }

}