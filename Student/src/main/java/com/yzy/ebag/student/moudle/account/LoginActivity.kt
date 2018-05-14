package com.yzy.ebag.student.moudle.account

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import com.yanzhenjie.permission.Permission
import com.yzy.ebag.student.MainActivity
import com.yzy.ebag.student.R
import ebag.mobile.module.account.BLoginActivity
import ebag.mobile.util.requestPermission

/**
 * Created by YZY on 2018/5/14.
 * 登录 liyimin
 */
class LoginActivity: BLoginActivity() {
    override fun getLogoResId(): Int {
        return R.drawable.student_logo
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissions = arrayOf(
                Permission.CAMERA,
                Permission.WRITE_EXTERNAL_STORAGE,
                Permission.READ_EXTERNAL_STORAGE,
                Permission.ACCESS_COARSE_LOCATION,
                Permission.ACCESS_FINE_LOCATION,
                Permission.RECORD_AUDIO
                )
        requestPermission(*permissions)
    }

    override fun getRoleCode(): String {
        return BLoginActivity.STUDENT_ROLE
    }

    override fun getJumpIntent(): Intent {
        return Intent(this, MainActivity::class.java)
    }

    override fun forgetClick(view: View) {
        startActivity(Intent(this, ForgetActivity::class.java))
    }

    override fun onCodeSuccess(codeEntity: String?) {
        AlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage(codeEntity)
                .setPositiveButton("知道了", null)
                .show()
        super.onCodeSuccess(codeEntity)
    }

    override fun threeParty(view: View, uid: String?, accessToken: String?, name: String?, iconurl: String?, gender: String?, share_media: String?) {
    }
}