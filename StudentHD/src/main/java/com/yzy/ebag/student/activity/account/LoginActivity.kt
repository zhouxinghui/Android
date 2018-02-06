package com.yzy.ebag.student.activity.account

import android.content.Intent
import android.view.View
import com.yzy.ebag.student.R
import com.yzy.ebag.student.activity.main.MainActivity
import ebag.hd.ui.activity.account.BLoginActivity

class LoginActivity : BLoginActivity() {
    override fun forgetClick(view: View) {
        startActivity(Intent(this, ForgetActivity::class.java))
    }

    override fun initViews() {
        super.initViews()
        loginEdit.setText("1000725")
        pwdEdit.setText("ysb123456")
    }
    override fun getJumpIntent(): Intent {
        return Intent(this, MainActivity::class.java)
    }

    override fun getRoleCode(): String {
        return "student"
    }

    override fun getLogoResId(): Int {
        return R.drawable.student_logo
    }

}
