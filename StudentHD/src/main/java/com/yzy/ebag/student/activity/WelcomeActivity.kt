package com.yzy.ebag.student.activity

import android.content.Intent
import android.os.Handler
import com.yzy.ebag.student.R
import com.yzy.ebag.student.activity.account.LoginActivity
import com.yzy.ebag.student.activity.main.MainActivity
import ebag.core.base.App
import ebag.core.base.BaseActivity
import ebag.core.util.L
import ebag.core.util.StringUtils
import ebag.hd.base.Constants

class WelcomeActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_welcome
    }

    override fun initViews() {
        val token: String = App.TOKEN
        L.e("token", token)
        Handler().postDelayed({
            intent = if (!StringUtils.isEmpty(token)){
                Intent(this, MainActivity::class.java)
            }else{
                Intent(this, LoginActivity::class.java).putExtra(Constants.KEY_TO_MAIN,true)
            }
            startActivity(intent)
            finish()
        }, 2000)
    }
}
