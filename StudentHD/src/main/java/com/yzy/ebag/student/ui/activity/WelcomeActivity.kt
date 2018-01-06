package com.yzy.ebag.student.ui.activity

import android.content.Intent
import android.os.Handler
import com.yzy.ebag.student.MainActivity
import com.yzy.ebag.student.R
import com.yzy.ebag.student.ui.activity.account.LoginActivity
import ebag.core.base.BaseActivity
import ebag.core.util.SerializableUtils
import ebag.hd.base.Constants
import ebag.hd.bean.response.UserEntity

class WelcomeActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_welcome
    }

    override fun initViews() {
        val userEntity: UserEntity? = SerializableUtils.getSerializable<UserEntity>(Constants.STUDENT_USER_ENTITY)
        Handler().postDelayed({
            intent = if (userEntity != null){
                Intent(this, MainActivity::class.java)
            }else{
                Intent(this, LoginActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 2000)
    }
}
