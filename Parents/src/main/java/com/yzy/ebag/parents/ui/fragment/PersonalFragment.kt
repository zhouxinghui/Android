package com.yzy.ebag.parents.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.ui.activity.LoginActivity
import ebag.core.base.App
import ebag.core.base.BaseFragment
import ebag.core.util.AppManager
import ebag.core.util.SPUtils
import ebag.core.util.SerializableUtils
import ebag.mobile.base.Constants
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
            SerializableUtils.deleteSerializable(Constants.STUDENT_USER_ENTITY)
            SPUtils.remove(activity,com.yzy.ebag.parents.common.Constants.CURRENT_CHILDREN_YSBCODE)
            startActivity(Intent(mContext, LoginActivity::class.java).putExtra(Constants.KEY_TO_MAIN, true))
            AppManager.finishAllActivity()
        }
    }

}