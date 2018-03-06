package com.yzy.ebag.teacher.ui.activity.account

import android.content.Intent
import android.view.View
import com.yzy.ebag.teacher.MainActivity
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.ui.activity.LoginSelectActivity
import ebag.hd.ui.activity.account.BLoginActivity
import kotlinx.android.synthetic.main.item_notice_history.view.*

class LoginActivity : BLoginActivity() {
    override fun threeParty(view: View,uid : String?, accessToken: String?, name: String?, iconurl: String?, gender: String?,share_media:String?) {
//        threeParty(view,uid,access_token,name,iconurl,gender)
        startActivity(Intent(this, LoginSelectActivity::class.java)
                .putExtra("uid", uid)
                .putExtra("accessToken", accessToken)
                .putExtra("name", name)
                .putExtra("iconurl",iconurl)
                .putExtra("gender", gender)
                .putExtra("share_media",share_media))
    }

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
        return BLoginActivity.TEACHER_ROLE
    }

    override fun getLogoResId(): Int {
        return R.drawable.teacher_logo
    }
}
