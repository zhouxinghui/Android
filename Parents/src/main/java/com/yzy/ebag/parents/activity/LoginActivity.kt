package com.yzy.ebag.parents.activity

import android.content.Intent
import android.view.View
import com.yzy.ebag.parents.R
import ebag.core.util.SPUtils
import ebag.mobile.bean.Constants
import ebag.mobile.module.account.BLoginActivity

class LoginActivity:BLoginActivity(){

    override fun getLogoResId(): Int = R.drawable.ic_launcher

    override fun getRoleCode(): String = BLoginActivity.PARENT_ROLE

    override fun getJumpIntent(): Intent =  Intent(this, MainActivity::class.java)
    override fun forgetClick(view: View) {

    }

    override fun threeParty(view: View, uid: String?, accessToken: String?, name: String?, iconurl: String?, gender: String?, share_media: String?) {

    }

    override fun initViews() {
        super.initViews()
        loginEdit.setText(SPUtils.get(this, Constants.USER_ACCOUNT, "") as String)
    }

}