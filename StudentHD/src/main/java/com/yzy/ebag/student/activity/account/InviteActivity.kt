package com.yzy.ebag.student.activity.account

import android.content.Context
import android.content.Intent
import com.yzy.ebag.student.activity.main.MainActivity
import ebag.hd.ui.activity.account.BInviteActivity

/**
 * Created by unicho on 2018/3/1.
 */
class InviteActivity: BInviteActivity() {

    companion object {
        fun jump(context: Context, type: Int){
            context.startActivity(
                    Intent(context, InviteActivity::class.java)
                            .putExtra("type", type)
            )
        }
    }

    override fun inviteSuccess() {
        startActivity(
                Intent(this, MainActivity::class.java)
        )
    }
}