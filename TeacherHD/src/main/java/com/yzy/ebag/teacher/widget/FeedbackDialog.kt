package com.yzy.ebag.teacher.widget

import android.content.Context
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseDialog

/**
 * Created by YZY on 2018/2/7.
 */
class FeedbackDialog(context: Context): BaseDialog(context) {
    override fun getLayoutRes(): Int {
        return R.layout.dialog_wrong_question_feedback
    }

    override fun setWidth(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.x600)
    }
    override fun setHeight(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.y400)
    }
}