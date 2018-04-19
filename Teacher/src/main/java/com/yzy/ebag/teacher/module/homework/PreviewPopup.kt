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

    override fun setHeight(): Int = contentView.resources.getDimensionPixelSize(R.dimen.y150)

    override fun setWidth(): Int = contentView.resources.getDimensionPixelSize(R.dimen.x80)


    private val assignGroupTv = contentView.findViewById<TextView>(R.id.assignGroupTv)
    private val assignClassTv = contentView.findViewById<TextView>(R.id.assignClass)
    var onAssignClick: ((type: Int) -> Unit)? = null
    init {
        assignGroupTv.setOnClickListener {
            onAssignClick?.invoke(1)
            dismiss()
        }
        assignClassTv.setOnClickListener {
            onAssignClick?.invoke(2)
            dismiss()
        }
    }
}