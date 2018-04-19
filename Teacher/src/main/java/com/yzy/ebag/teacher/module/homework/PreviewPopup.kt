package com.yzy.ebag.teacher.module.homework

import android.content.Context
import android.widget.TextView
import com.yzy.ebag.teacher.R
import ebag.core.base.BasePopupWindow

/**
 * Created by YZY on 2018/4/19.
 */
class PreviewPopup(private val mContext: Context): BasePopupWindow(mContext) {
    override fun getLayoutRes(): Int = R.layout.popup_preview

    private val assignGroupTv = contentView.findViewById<TextView>(R.id.assignGroupTv)
    private val assignClassTv = contentView.findViewById<TextView>(R.id.assignClass)
    var onAssignClick: ((type: Int) -> Unit)? = null
    init {
        assignGroupTv.setOnClickListener {
            onAssignClick?.invoke(1)
        }
        assignClassTv.setOnClickListener {
            onAssignClick?.invoke(2)
        }
    }
}