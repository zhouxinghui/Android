package ebag.core.util

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import java.lang.Exception
import java.util.*

/**
 * 当这个类名改为object后 表示里面的所有方法都是静态方法
 *
 * Created by caoyu on 2017/10/31.
 */
object AppManager {

    private val activityStack = Stack<Activity>()

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {
        L.e("","")
        activityStack.push(activity)
    }

    /**
     * 出栈
     * @param activity
     */
    fun removeActivity(activity: Activity) {
        activityStack.remove(activity)
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity {
        return activityStack.lastElement()
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishCurrentActivity() {
        activityStack.pop()?.finish()
    }

    /**
     * 结束指定的Activity
     */
    private fun finishActivity(activity: Activity?) {
        if (activity != null) {
            activityStack.remove(activity)
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<Activity>) {
        activityStack
                .filter { it.javaClass == cls }
                .forEach { finishActivity(it) }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        while (!activityStack.empty()) {
            activityStack.pop()?.finish()
        }
        activityStack.clear()
    }

    /**
     * 退出应用程序
     */
    fun AppExit(context: Context) {
        try {
            finishAllActivity()
            val manager = context
                    .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            manager.killBackgroundProcesses(context.packageName)
            System.exit(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}