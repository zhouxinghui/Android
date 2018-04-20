package com.yzy.ebag.parents.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.ui.activity.LoginActivity
import ebag.core.base.App
import ebag.core.base.BaseFragment
import ebag.core.util.AppManager
import ebag.mobile.bean.Constants
import kotlinx.android.synthetic.main.fragment_personal.*

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

        temp.setOnClickListener {
            App.deleteToken()
            startActivity(Intent(mContext, LoginActivity::class.java).putExtra(Constants.KEY_TO_MAIN, true))
            AppManager.finishAllActivity()
        }
    }

}