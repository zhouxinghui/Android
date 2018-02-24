package com.yzy.ebag.teacher.widget

import android.content.Context
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseDialog
import ebag.core.util.StringUtils
import ebag.core.util.T
import kotlinx.android.synthetic.main.dialog_smart_push.*

/**
 * Created by YZY on 2018/2/24.
 */
class SmartPushDialog(context: Context): BaseDialog(context) {
    override fun getLayoutRes(): Int {
        return R.layout.dialog_smart_push
    }
    override fun setWidth(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.x600)
    }
    override fun setHeight(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.y400)
    }

    var onConfirmClickListener: ((count: Int) -> Unit)? = null
    init {
        confirmBtn.setOnClickListener {
            val count = countEdit.text.toString()
            if (StringUtils.isEmpty(count)){
                T.show(context, "请输入题目数量")
                return@setOnClickListener
            }
            if (count.toInt() < 10){
                T.show(context, "题目数量范围为：10-100")
                return@setOnClickListener
            }
            if (count.toInt() > 100){
                T.show(context, "题目数量范围为：10-100")
                return@setOnClickListener
            }
            onConfirmClickListener?.invoke(count.toInt())
        }
    }
}