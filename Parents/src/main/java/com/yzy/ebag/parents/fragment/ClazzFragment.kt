package com.yzy.ebag.parents.fragment

import android.os.Bundle
import android.view.View
import com.yzy.ebag.parents.R
import ebag.core.base.BaseFragment

class ClazzFragment : BaseFragment() {

    companion object {
        fun newInstance(): ClazzFragment {
            val fragment = ClazzFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutRes(): Int = R.layout.fragment_class

    override fun getBundle(bundle: Bundle?) {

    }

    override fun initViews(rootView: View) {

    }

}