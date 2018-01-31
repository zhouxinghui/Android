package com.yzy.ebag.student.activity

import android.content.Intent
import android.os.Handler
import com.yzy.ebag.student.R
import com.yzy.ebag.student.TestActivity2
import ebag.core.base.App
import ebag.core.base.BaseActivity
import ebag.core.util.L

class WelcomeActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_welcome
    }

    override fun initViews() {
        val token: String = App.TOKEN
        L.e("token", token)
        Handler().postDelayed({

            startActivity(Intent(this@WelcomeActivity, TestActivity2::class.java))
//            startActivity(
//                    if (!StringUtils.isEmpty(token)){
//                        Intent(this@WelcomeActivity, MainActivity::class.java)
//                    }else{
//                        Intent(this, LoginActivity::class.java).putExtra(Constants.KEY_TO_MAIN,true)
//                    }
//            )
            finish()
        }, 2000)
    }
}
