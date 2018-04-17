package com.yzy.ebag.parents.fragment

import android.os.Bundle
import android.view.View
import com.yzy.ebag.parents.R
import ebag.core.base.BaseFragment

class PersonalFragment : BaseFragment() {

    companion object {
        fun newInstance(): PersonalFragment {
            val fragment = PersonalFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun getLayoutRes(): Int = R.layout.fragment_personal

    override fun getBundle(bundle: Bundle?) {

    }

    override fun initViews(rootView: View) {
    }

}