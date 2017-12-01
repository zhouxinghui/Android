package ebag.core.util

import android.util.Log

/**
 * Created by unicho on 2017/11/1.
 */
object L {
    val IS_DEBUG = true
    private var TAG = "--Log--eBag:  "

    fun init(tag: String){
        TAG = tag
    }

    private fun logPrint(level: Int, tag: String, message: Any?, tr: Throwable?) {
        if (IS_DEBUG) {
            val str = message?.toString() ?: "   null"
            var i = 0
            while (i < str.length) {
                when(i+4000){
                    in 0 until str.length -> Log.println(level, tag, str.substring(i, i + 4000))
                    else -> Log.println(level, tag, str.substring(i, str.length) + "\n" + Log.getStackTraceString(tr))
                }
                i += 4000
            }
        }
    }

    fun i(message: Any) = logPrint(Log.INFO, TAG, message, null)

    fun e(message: Any) = logPrint(Log.ERROR, TAG, message, null)

    fun w(message: Any) = logPrint(Log.WARN, TAG, message, null)

    fun d(message: Any) = logPrint(Log.DEBUG, TAG, message, null)

    fun v(message: Any) = logPrint(Log.VERBOSE, TAG, message, null)

    fun i(TAG: String, message: Any) = logPrint(Log.INFO, TAG, message, null)

    fun e(TAG: String, message: Any) { logPrint(Log.ERROR, TAG, message, null)}

    fun w(TAG: String, message: Any) = logPrint(Log.WARN, TAG, message, null)

    fun d(TAG: String, message: Any) = logPrint(Log.DEBUG, TAG, message, null)

    fun v(TAG: String, message: Any) = logPrint(Log.VERBOSE, TAG, message, null)

    fun i(message: Any, tr: Throwable) = logPrint(Log.INFO, TAG, message, tr)

    fun e(message: Any, tr: Throwable) = logPrint(Log.ERROR, TAG, message, tr)

    fun w(message: Any, tr: Throwable) = logPrint(Log.WARN, TAG, message, tr)

    fun d(message: Any, tr: Throwable) = logPrint(Log.DEBUG, TAG, message, tr)

    fun v(message: Any, tr: Throwable) = logPrint(Log.VERBOSE, TAG, message, tr)

    fun i(TAG: String, message: Any, tr: Throwable) = logPrint(Log.INFO, TAG, message, tr)

    fun e(TAG: String, message: Any, tr: Throwable) = logPrint(Log.ERROR, TAG, message, tr)

    fun w(TAG: String, message: Any, tr: Throwable) = logPrint(Log.WARN, TAG, message, tr)

    fun d(TAG: String, message: Any, tr: Throwable) = logPrint(Log.DEBUG, TAG, message, tr)

    fun v(TAG: String, message: Any, tr: Throwable) = logPrint(Log.VERBOSE, TAG, message, tr)
}