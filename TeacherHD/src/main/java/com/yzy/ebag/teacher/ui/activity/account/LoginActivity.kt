package com.yzy.ebag.teacher.ui.activity.account

import android.content.Intent
import com.yzy.ebag.teacher.MainActivity
import ebag.hd.R
import ebag.hd.ui.activity.account.BLoginActivity

class LoginActivity : BLoginActivity() {
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
