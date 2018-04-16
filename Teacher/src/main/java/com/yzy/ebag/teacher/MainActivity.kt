package com.yzy.ebag.teacher

import android.content.Intent
import com.yzy.ebag.teacher.module.account.LoginActivity
import ebag.core.base.App
import ebag.core.base.BaseActivity
import ebag.core.util.AppManager
import ebag.mobile.bean.Constants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initViews() {
        btn.setOnClickListener {
            App.deleteToken()
            startActivity(Intent(this, LoginActivity::class.java).putExtra(Constants.KEY_TO_MAIN, true))
            AppManager.finishAllActivity()
        }
    }
}
