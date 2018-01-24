package com.yzy.ebag.student.activity.tools.practise

import android.content.Context
import android.content.Intent
import com.yzy.ebag.student.R
import ebag.core.base.mvp.MVPActivity

/**
 * @author caoyu
 * @date 2018/1/23
 * @description
 */
class WriteActivity: MVPActivity() {

    companion object {
        fun jump(context: Context){
            context.startActivity(Intent(context,WriteActivity::class.java))
        }
    }

    override fun destroyPresenter() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_write
    }

    override fun initViews() {

    }
}