package com.yzy.ebag.teacher.activity.account

import android.content.Intent
import android.view.View
import cn.jpush.android.api.JPushInterface
import com.yzy.ebag.teacher.activity.home.MainActivity
import com.yzy.ebag.teacher.R
import ebag.core.util.L
import ebag.hd.bean.response.UserEntity
import ebag.hd.ui.activity.account.BLoginActivity

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
        loginEdit.setText("10000651")
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

    override fun onLoginSuccess(userEntity: UserEntity) {
        L.e("Teacher", "[user] uid:${userEntity.uid}")
//        JPushInterface.deleteAlias(this, 0)
//        设置别名 开启推送
        if(JPushInterface.isPushStopped(this))
            JPushInterface.resumePush(this)
        JPushInterface.setAlias(this, 0, userEntity.uid)
        super.onLoginSuccess(userEntity)

    }
}
