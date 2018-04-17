package ebag.core.base

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.PopupWindow

/**
 * Created by YZY on 2018/4/17.
 */
abstract class BasePopupWindow(context: Context): PopupWindow(context) {
    init {
        contentView = LayoutInflater.from(context).inflate(this.getLayoutRes(), null)
        width = this.setWidth()
        height = this.setHeight()
        isFocusable = true
        isOutsideTouchable = true
        this.setBackgroundDrawable(ColorDrawable())
    }

    abstract fun getLayoutRes(): Int

    open fun setWidth(): Int{
        return WindowManager.LayoutParams.WRAP_CONTENT
    }
    open fun setHeight(): Int{
        return WindowManager.LayoutParams.WRAP_CONTENT
    }
}