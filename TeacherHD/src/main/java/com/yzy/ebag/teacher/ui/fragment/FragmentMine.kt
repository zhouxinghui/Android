package com.yzy.ebag.teacher.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseFragment

/**
 * Created by YZY on 2017/12/21.
 */
class FragmentMine : BaseFragment() {
    companion object {
        fun newInstance() : Fragment {
            val fragment = FragmentMine()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun getLayoutRes(): Int {
        return R.layout.fragment_mine
    }

    override fun getBundle(bundle: Bundle) {

    }

    override fun initViews(rootView: View) {
    }


}