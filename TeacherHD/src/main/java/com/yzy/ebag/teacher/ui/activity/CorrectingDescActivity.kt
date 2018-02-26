package com.yzy.ebag.teacher.ui.activity

import android.content.Context
import android.content.Intent
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseActivity

class CorrectingDescActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_correcting_desc
    }
    companion object {
        fun jump(context: Context){
            context.startActivity(Intent(context, CorrectingDescActivity::class.java))
        }
    }
    override fun initViews() {

    }

}
