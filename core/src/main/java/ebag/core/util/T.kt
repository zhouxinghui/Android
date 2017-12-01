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

    fun show(context: Context, info: String, isShort: Boolean) {
        Toast.makeText(context, info, if (isShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG).show()
    }

    fun show(context: Context, resId: Int, isShort: Boolean) {
        Toast.makeText(context, resId, if (isShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG).show()
    }

    fun show(context: Context, info: String) {
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show()
    }

    fun show(context: Context, info: Int) {
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show()
    }

    fun threadShow(context: Context, handler: Handler, info: String) {
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

    fun centerShow(context: Context, info: String) {
        val mToast = Toast.makeText(context, info, Toast.LENGTH_SHORT)
        mToast.setGravity(Gravity.CENTER, 0, 0)
        mToast.show()
    }

    fun centerShow(context: Context, handler: Handler, info: Int) {
        handler.post { centerShow(context, info) }
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    fun showShort(context: Context, message: CharSequence) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    fun showShort(context: Context?, message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    fun showLong(context: Context, message: CharSequence) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    fun showLong(context: Context, message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
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