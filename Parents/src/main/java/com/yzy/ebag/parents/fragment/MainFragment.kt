package com.yzy.ebag.parents.fragment

import android.os.Bundle
import android.view.View
import com.yzy.ebag.parents.R
import ebag.core.base.BaseFragment

class MainFragment : BaseFragment() {

    companion object {
        fun newInstance(): MainFragment {
            val fragment = MainFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutRes(): Int = R.layout.fragment_main

    override fun getBundle(bundle: Bundle?) {

    }

    override fun initViews(rootView: View) {

    }

}