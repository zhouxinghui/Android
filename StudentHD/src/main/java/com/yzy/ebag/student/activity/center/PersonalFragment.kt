package com.yzy.ebag.student.activity.center

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.yzy.ebag.student.R
import ebag.core.base.BaseFragment

/**
 * Created by unicho on 2018/1/13.
 */
class PersonalFragment: BaseFragment() {
    companion object {
        fun newInstance() : Fragment {
            val fragment = PersonalFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun getLayoutRes(): Int {
        return R.layout.fragment_center_personal
    }

    override fun getBundle(bundle: Bundle) {
    }

    override fun initViews(rootView: View) {
    }
}