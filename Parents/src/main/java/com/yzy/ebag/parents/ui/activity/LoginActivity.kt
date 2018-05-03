package com.yzy.ebag.parents.ui.activity

import android.content.Intent
import android.view.View
import com.yzy.ebag.parents.R
import ebag.core.util.SPUtils
import ebag.mobile.base.Constants
import ebag.mobile.module.account.BLoginActivity

class LoginActivity:BLoginActivity(){

    override fun getLogoResId(): Int = R.drawable.ic_launcher

    override fun getRoleCode(): String = BLoginActivity.PARENT_ROLE

    override fun getJumpIntent(): Intent =  if ((SPUtils.get(this,com.yzy.ebag.parents.common.Constants.CURRENT_CHILDREN_YSBCODE,"") as String).isEmpty()) Intent(this,ChooseChildrenActivity::class.java).putExtra("flag",true) else Intent(this,MainActivity::class.java)
    override fun forgetClick(view: View) {
        startActivity(Intent(this,ForgetActivity::class.java))
    }

    override fun threeParty(view: View, uid: String?, accessToken: String?, name: String?, iconurl: String?, gender: String?, share_media: String?) {

    }

    override fun initViews() {
        super.initViews()
        loginEdit.setText(SPUtils.get(this, Constants.USER_ACCOUNT, "") as String)
    }

}