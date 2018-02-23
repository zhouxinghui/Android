package com.yzy.ebag.teacher.ui.activity.account

import android.content.Intent
import android.view.View
import com.yzy.ebag.teacher.MainActivity
import com.yzy.ebag.teacher.R
import ebag.hd.ui.activity.account.BLoginActivity

class LoginActivity : BLoginActivity() {
    override fun forgetClick(view: View) {
        startActivity(Intent(this, ForgetActivity::class.java))
    }

    override fun initViews() {
        super.initViews()
        loginEdit.setText("1000857")
        pwdEdit.setText("ysb123456")
    }
    override fun getJumpIntent(): Intent {
        return Intent(this, MainActivity::class.java)
    }

    override fun getRoleCode(): String {
        return "teacher"
    }

    override fun getLogoResId() : Int {
        return R.drawable.teacher_logo
    }
}
