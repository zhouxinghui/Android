package com.yzy.ebag.teacher.activity

import android.content.Intent
import android.os.Handler
import cn.jpush.android.api.JPushInterface
import com.yzy.ebag.teacher.activity.home.MainActivity
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.activity.account.LoginActivity
import ebag.core.base.App
import ebag.core.base.BaseActivity
import ebag.core.util.StringUtils
import ebag.hd.base.Constants

class WelcomeActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_welcome
    }

    override fun initViews() {
        JPushInterface.init(applicationContext)
        val token: String = App.TOKEN
        Handler().postDelayed({
            startActivity(
                    if (!StringUtils.isEmpty(token)){
                        Intent(this, MainActivity::class.java)
                    }else{
                        Intent(this, LoginActivity::class.java).putExtra(Constants.KEY_TO_MAIN,true)
                    }
            )
            finish()
        }, 2000)
    }
}
