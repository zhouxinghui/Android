package com.yzy.ebag.student.moudle.account

import android.content.Intent
import ebag.mobile.base.Constants
import ebag.mobile.module.account.BForgetActivity

/**
 * Created by YZY on 2017/12/20.
 */
class ForgetActivity : BForgetActivity(){

    override fun resetSuccess() {
        startActivity(
                Intent(this, LoginActivity::class.java)
                        .putExtra(Constants.KEY_TO_MAIN, true)
        )
    }

}