package com.yzy.ebag.student.module.personal

import android.content.Context
import android.content.Intent
import com.yzy.ebag.student.R
import ebag.core.base.BaseActivity

/**
 * Created by YZY on 2018/5/14.
 */
class MyMissionActivity: BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_my_mission
    companion object {
        fun jump(context: Context, classId: String){
            context.startActivity(
                    Intent(context, MyMissionActivity::class.java)
                            .putExtra("classId", classId)
            )
        }
    }
    override fun initViews() {

    }
}