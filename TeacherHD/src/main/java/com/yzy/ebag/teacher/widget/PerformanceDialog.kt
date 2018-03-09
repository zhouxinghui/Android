package com.yzy.ebag.teacher.widget

import android.content.Context
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseDialog

/**
 * Created by YZY on 2018/3/8.
 */
class PerformanceDialog(context: Context): BaseDialog(context) {
    override fun getLayoutRes(): Int {
        return R.layout.dialog_performance
    }

    init {

    }
}