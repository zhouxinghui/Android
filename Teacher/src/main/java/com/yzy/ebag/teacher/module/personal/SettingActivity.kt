package com.yzy.ebag.teacher.module.personal

import android.content.Intent
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.module.account.LoginActivity
import ebag.core.base.App
import ebag.core.base.BaseActivity
import ebag.core.util.AppManager
import ebag.mobile.base.Constants
import kotlinx.android.synthetic.main.activity_setting.*

/**
 * Created by YZY on 2018/4/26.
 */
class SettingActivity: BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_setting

    override fun initViews() {
        btn.setOnClickListener {
            App.deleteToken()
            startActivity(Intent(this, LoginActivity::class.java).putExtra(Constants.KEY_TO_MAIN, true))
            AppManager.finishAllActivity()
        }
    }
}