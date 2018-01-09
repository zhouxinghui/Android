package com.yzy.ebag.student.activity.account

import android.content.Intent
import com.yzy.ebag.student.activity.main.MainActivity
import com.yzy.ebag.student.R
import ebag.hd.ui.activity.account.BLoginActivity

class LoginActivity : BLoginActivity() {
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
