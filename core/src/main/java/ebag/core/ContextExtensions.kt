package ebag.core

import android.content.Context
import android.widget.Toast

/**
 * Created by caoyu on 2017/11/1.
 */
class ContextExtensions {
    fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}