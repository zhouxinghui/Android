package com.yzy.ebag.student.activity

import android.content.Intent
import android.os.Handler
import android.view.KeyEvent
import cn.jpush.android.api.JPushInterface
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

        JPushInterface.init(applicationContext)
        val token: String = App.TOKEN
        L.e("token", token)
        Handler().postDelayed({

            //            startActivity(Intent(this@WelcomeActivity, TestActivity2::class.java))
            startActivity(
                    if (!StringUtils.isEmpty(token)) {
                        Intent(this@WelcomeActivity, MainActivity::class.java)
                    } else {
                        Intent(this, LoginActivity::class.java).putExtra(Constants.KEY_TO_MAIN, true)
                    }
            )
            finish()
        }, 2000)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return keyCode == KeyEvent.KEYCODE_BACK
    }
}
