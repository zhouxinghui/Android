package com.yzy.ebag.student.moudle.account

import android.content.Intent
import android.os.Handler
import com.yzy.ebag.student.MainActivity
import com.yzy.ebag.student.R
import ebag.core.base.App
import ebag.core.base.BaseActivity
import ebag.core.util.StringUtils
import ebag.mobile.base.Constants

/**
 * Created by YZY on 2018/5/14.
 */
class WelcomeActivity: BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_welcome
    }

    override fun initViews() {
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