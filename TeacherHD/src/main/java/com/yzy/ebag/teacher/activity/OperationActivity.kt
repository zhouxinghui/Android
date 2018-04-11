package com.yzy.ebag.teacher.activity

import android.content.Context
import android.content.Intent
import android.view.View
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseActivity
import kotlinx.android.synthetic.main.activity_operation.*

/**
 * Created by YZY on 2018/4/11.
 */
class OperationActivity: BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_operation
    }

    companion object {
        fun jump(context: Context){
            context.startActivity(Intent(context, OperationActivity::class.java))
        }
    }
    override fun initViews() {
        tv1.setOnClickListener {
            img1.visibility = if (img1.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
        tv2.setOnClickListener {
            img2.visibility = if (img2.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
        tv3.setOnClickListener {
            img3.visibility = if (img3.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }
}