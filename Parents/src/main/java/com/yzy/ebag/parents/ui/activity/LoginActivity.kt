package com.yzy.ebag.parents.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.yanzhenjie.permission.Permission
import com.yzy.ebag.parents.R
import ebag.core.util.SPUtils
import ebag.mobile.base.Constants
import ebag.mobile.module.account.BLoginActivity
import ebag.mobile.util.requestPermission

class LoginActivity : BLoginActivity() {

    override fun getLogoResId(): Int = R.drawable.ic_launcher

    override fun getRoleCode(): String = BLoginActivity.PARENT_ROLE

    override fun getJumpIntent(): Intent = if ((SPUtils.get(this, com.yzy.ebag.parents.common.Constants.CURRENT_CHILDREN_YSBCODE, "") as String).isEmpty()) Intent(this, ChooseChildrenActivity::class.java).putExtra("flag", true) else Intent(this, MainActivity::class.java)
    override fun forgetClick(view: View) {
        startActivity(Intent(this, ForgetActivity::class.java))
    }

    override fun threeParty(view: View, uid: String?, accessToken: String?, name: String?, iconurl: String?, gender: String?, share_media: String?) {

    }

    override fun initViews() {
        super.initViews()
        loginEdit.setText(SPUtils.get(this, Constants.USER_ACCOUNT, "") as String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissionArray = arrayOf(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE, Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION)
        requestPermission(*permissionArray)
    }

}