package com.yzy.ebag.teacher.ui.activity

import android.content.Intent
import android.os.Handler
import com.yzy.ebag.teacher.MainActivity
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.ui.activity.account.LoginActivity
import ebag.core.base.App
import ebag.core.base.BaseActivity
import ebag.core.util.StringUtils
import ebag.hd.base.Constants

class WelcomeActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_welcome
    }

    override fun initViews() {
        val token: String = App.TOKEN
        Handler().postDelayed({
            intent = if (!StringUtils.isEmpty(token)){
                Intent(this, MainActivity::class.java)
                intent.putExtra(Constants.KEY_TO_MAIN,true)
            }else{
                Intent(this, LoginActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 2000)
    }
}
