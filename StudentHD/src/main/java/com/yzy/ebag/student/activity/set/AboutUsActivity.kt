package com.yzy.ebag.student.activity.set

import android.content.Context
import android.content.Intent
import com.yzy.ebag.student.R
import ebag.hd.ui.activity.BAboutUsActivity

/**
 * @author caoyu
 * @date 2018/2/6
 * @description
 */
class AboutUsActivity : BAboutUsActivity() {

    companion object {
        fun jump(context: Context){
            context.startActivity(Intent(context, AboutUsActivity::class.java))
        }
    }

    override fun getLogo(): Int {
        return R.drawable.student_logo
    }
}