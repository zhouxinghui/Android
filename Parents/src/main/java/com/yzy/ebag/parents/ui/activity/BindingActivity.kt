package com.yzy.ebag.parents.ui.activity

import android.content.Intent
import ebag.mobile.module.account.BaseBindingActivity

/**
 * Created by YZY on 2018/5/7.
 */
class BindingActivity: BaseBindingActivity() {
    override fun jumpToMain() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}