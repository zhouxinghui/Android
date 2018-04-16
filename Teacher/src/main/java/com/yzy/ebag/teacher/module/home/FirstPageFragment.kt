package com.yzy.ebag.teacher.module.home

import android.os.Bundle
import android.view.View
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseFragment

/**
 * Created by YZY on 2018/4/16.
 */
class FirstPageFragment: BaseFragment() {
    companion object {
        fun newInstance(): FirstPageFragment{
            val fragment = FirstPageFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun getLayoutRes(): Int {
        return R.layout.fragment_first_page
    }

    override fun getBundle(bundle: Bundle?) {

    }

    override fun initViews(rootView: View) {

    }
}