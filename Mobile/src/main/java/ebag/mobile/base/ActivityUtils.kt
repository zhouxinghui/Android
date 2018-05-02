package ebag.mobile.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import java.util.*

/**
 * Created by fansan on 2018/3/21.
 */
class ActivityUtils {

    companion object {
        private val activityStacks: Stack<Activity> = Stack()

        fun addActivity(activity: Activity) {
            activityStacks.add(activity)
        }

        fun finishActivity() {
            activityStacks.lastElement()
        }

        fun finishActivity(activity: Activity) {

            activityStacks.remove(activity)
            activity.finish()
        }

        fun finishActivity(clz: Class<*>) {
            activityStacks.forEach {
                if (it.javaClass == clz)
                    finishActivity(it)
            }
        }

        fun skipActivityAndFinishAll(context: Context, goal: Class<*>) {
            val intent = Intent(context, goal)
            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
            activityStacks.forEach {
                it.finish()
            }
        }
    }
}