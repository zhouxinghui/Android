package com.yzy.ebag.student.activity

import android.content.Intent
import android.os.Handler
import com.yzy.ebag.student.MainActivity
import com.yzy.ebag.student.R
import com.yzy.ebag.student.activity.account.LoginActivity
import ebag.core.base.App
import ebag.core.base.BaseActivity
import ebag.core.util.StringUtils

class WelcomeActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_welcome
    }

    override fun initViews() {
        val token: String = App.TOKEN
        Handler().postDelayed({
            intent = if (!StringUtils.isEmpty(token)){
                Intent(this, MainActivity::class.java)
            }else{
                Intent(this, LoginActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 2000)
    }
}
