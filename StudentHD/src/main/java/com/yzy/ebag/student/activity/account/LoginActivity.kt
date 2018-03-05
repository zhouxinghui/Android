package com.yzy.ebag.student.activity.account

import android.content.Intent
import android.view.View
import com.yzy.ebag.student.R
import com.yzy.ebag.student.activity.main.MainActivity
import ebag.hd.ui.activity.account.BLoginActivity

class LoginActivity : BLoginActivity() {
    override fun threeParty(view: View,uid : String?, accessToken: String?, name: String?, iconurl: String?, gender: String?,share_media:String?) {
    }

    override fun forgetClick(view: View) {
        startActivity(Intent(this, ForgetActivity::class.java))
    }

    override fun initViews() {
        super.initViews()
        loginEdit.setText("1000723")
        pwdEdit.setText("ysb123456")
    }

    override fun getJumpIntent(): Intent {
        return Intent(this, MainActivity::class.java)
    }

    override fun getRoleCode(): String {
        return BLoginActivity.STUDENT_ROLE
    }

    override fun getLogoResId(): Int {
        return R.drawable.student_logo
    }

}
