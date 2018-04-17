package com.yzy.ebag.parents.activity

import android.content.Intent
import android.os.Handler
import android.view.KeyEvent
import com.yzy.ebag.parents.R
import ebag.core.base.App
import ebag.core.base.BaseActivity
import ebag.core.util.StringUtils
import ebag.mobile.bean.Constants

class WelcomeActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_welcome

    override fun initViews() {
        val token: String = App.TOKEN
        Handler().postDelayed({
            startActivity(
                    if (!StringUtils.isEmpty(token)) {
                        Intent(this, MainActivity::class.java)
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