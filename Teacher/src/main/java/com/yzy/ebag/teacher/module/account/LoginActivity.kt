package com.yzy.ebag.teacher.module.account

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.yanzhenjie.permission.Permission
import com.yzy.ebag.teacher.MainActivity
import com.yzy.ebag.teacher.R
import ebag.core.util.SPUtils
import ebag.mobile.base.Constants
import ebag.mobile.module.account.BLoginActivity
import ebag.mobile.util.requestPermission


/**
 * Created by YZY on 2018/4/16.
 */
class LoginActivity: BLoginActivity() {
    override fun getLogoResId(): Int {
        return R.drawable.teacher_logo
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissions = arrayOf(
                Permission.CAMERA,
                Permission.WRITE_EXTERNAL_STORAGE,
                Permission.READ_EXTERNAL_STORAGE,
                Permission.RECORD_AUDIO)
        requestPermission(*permissions)
    }

    override fun getRoleCode(): String {
        return BLoginActivity.TEACHER_ROLE
    }

    override fun getJumpIntent(): Intent {
        return Intent(this, MainActivity::class.java)
    }

    override fun forgetClick(view: View) {
        startActivity(Intent(this, ForgetActivity::class.java))
    }

    override fun threeParty(view: View, uid: String?, accessToken: String?, name: String?, iconurl: String?, gender: String?, share_media: String?) {
    }

    override fun initViews() {
        super.initViews()
        loginEdit.setText(SPUtils.get(this, Constants.USER_ACCOUNT, "") as String)
    }
}