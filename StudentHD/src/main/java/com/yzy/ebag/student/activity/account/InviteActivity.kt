package com.yzy.ebag.student.activity.account

import android.content.Context
import android.content.Intent
import cn.jpush.android.api.JPushInterface
import com.yzy.ebag.student.activity.main.MainActivity
import ebag.core.base.App
import ebag.core.util.SerializableUtils
import ebag.hd.base.Constants
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

    override fun changeCount() {
        App.deleteToken()
        SerializableUtils.deleteSerializable(ebag.hd.base.Constants.STUDENT_USER_ENTITY)
//                删除别名， 停止推送
        JPushInterface.deleteAlias(this, 0)
        JPushInterface.stopPush(this)
        startActivity(
                Intent(this, LoginActivity::class.java)
                        .putExtra(Constants.KEY_TO_MAIN,true)
        )
    }
}