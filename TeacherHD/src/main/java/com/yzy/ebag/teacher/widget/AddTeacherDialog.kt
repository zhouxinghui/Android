package com.yzy.ebag.teacher.widget

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import com.yzy.ebag.teacher.R
import ebag.core.util.DensityUtil
import ebag.core.util.T
import kotlinx.android.synthetic.main.dialog_textbook_version.*

/**
 * Created by YZY on 2018/1/11.
 */
class AddTeacherDialog(context: Context): Dialog(context) {

    init {
        val contentView = LayoutInflater.from(context).inflate(R.layout.dialog_add_teacher, null)
        setContentView(contentView)
        window.setBackgroundDrawable(BitmapDrawable())
        val params = window.attributes
        params.width = DensityUtil.dip2px(context, context.resources.getDimensionPixelSize(R.dimen.x350).toFloat())
        params.height = DensityUtil.dip2px(context, context.resources.getDimensionPixelSize(R.dimen.y350).toFloat())
        window.attributes = params

        confirmBtn.setOnClickListener {
            T.show(context, "确定")
        }
    }
}