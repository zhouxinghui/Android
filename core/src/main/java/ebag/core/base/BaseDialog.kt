package ebag.core.base

import android.app.Dialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.view.inputmethod.InputMethodManager

/**
 * 自定义dialog
 * Created by YZY on 2018/1/24.
 */
abstract class BaseDialog(context: Context): Dialog(context) {
    init {
        val contentView = LayoutInflater.from(context).inflate(this.getLayoutRes(), null)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setContentView(contentView)
        window.setBackgroundDrawable(ColorDrawable(0))
        val params = window.attributes
        params.width = this.setWidth()
        params.height = this.setHeight()
        params.gravity = this.getGravity()
        window.attributes = params
    }
    abstract fun getLayoutRes(): Int

    open fun setWidth(): Int{
        return WindowManager.LayoutParams.WRAP_CONTENT
    }
    open fun setHeight(): Int{
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    open fun getGravity(): Int = Gravity.NO_GRAVITY

    /**
     * 点击空白位置 隐藏软键盘
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (null != this.currentFocus) {
            val mInputMethodManager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(this.currentFocus.windowToken, 0)
        }
        return super.onTouchEvent(event)
    }
}