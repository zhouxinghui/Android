package com.yzy.ebag.teacher.module.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.module.account.LoginActivity
import ebag.core.base.App
import ebag.core.base.BaseFragment
import ebag.core.util.AppManager
import ebag.core.util.loadImage
import ebag.mobile.bean.Constants
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * Created by YZY on 2018/4/16.
 */
class MineFragment: BaseFragment() {
    companion object {
        fun newInstance(): MineFragment{
            val fragment = MineFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun getLayoutRes(): Int {
        return R.layout.fragment_mine
    }

    override fun getBundle(bundle: Bundle?) {

    }

    override fun initViews(rootView: View) {
        btn.setOnClickListener {
            App.deleteToken()
            startActivity(Intent(mContext, LoginActivity::class.java).putExtra(Constants.KEY_TO_MAIN, true))
            AppManager.finishAllActivity()
        }

        imageView.loadImage("https://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/OralLanguage/00ce0945b6db492e84b0d17162a9e836Unit1%20read%20and%20fill%20in%20the%20table%20PartB.mp4")
    }
}