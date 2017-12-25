package ebag.core.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.view.Gravity
import android.widget.Toast

@SuppressLint("StaticFieldLeak")
/**
 * Created by unicho on 2017/11/1.
 */
object T {
    var toast: Toast? = null
    fun show(context: Context, info: CharSequence, isShort: Boolean) {
        if(toast == null){
            toast = Toast.makeText(context, info, if (isShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG)
            toast?.show()
        }else{
            toast?.duration = if (isShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
            toast?.setText(info)
            toast?.show()
        }
    }

    fun show(context: Context, resId: Int, isShort: Boolean) {
        if(toast == null){
            toast = Toast.makeText(context, resId, if (isShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG)
            toast?.show()
        }else{
            toast?.duration = if (isShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
            toast?.setText(resId)
            toast?.show()
        }
    }

    fun show(context: Context, info: CharSequence) {
        show(context, info, true)
    }

    fun show(context: Context, info: Int) {
        show(context, info, true)
    }

    fun showLong(context: Context, message: CharSequence) {
        show(context, message, false)
    }

    fun showLong(context: Context, message: Int) {
        show(context, message, false)
    }

    fun threadShow(context: Context, handler: Handler, info: CharSequence) {
        handler.post { show(context, info) }
    }

    fun threadShow(context: Context, handler: Handler, info: Int) {
        handler.post { show(context, info) }
    }

    fun centerShow(context: Context, info: Int) {
        val mToast = Toast.makeText(context, info, Toast.LENGTH_SHORT)
        mToast.setGravity(Gravity.CENTER, 0, 0)
        mToast.show()
    }

    fun centerShow(context: Context, info: CharSequence) {
        val mToast = Toast.makeText(context, info, Toast.LENGTH_SHORT)
        mToast.setGravity(Gravity.CENTER, 0, 0)
        mToast.show()
    }

    fun centerShow(context: Context, handler: Handler, info: Int) {
        handler.post { centerShow(context, info) }
    }



    /**
     * @Description: 显示Toast消息,当在非UI线程中需要显示消息时调用此方法
     * @param activity
     * @param message
     */
    fun showToastMsgOnUiThread(activity: Activity, message: String) {
        activity.runOnUiThread { show(activity, message) }
    }
}