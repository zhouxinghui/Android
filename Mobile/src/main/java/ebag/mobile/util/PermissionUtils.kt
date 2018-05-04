package ebag.mobile.util

import android.content.Context
import android.support.v7.app.AlertDialog
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission

/**
 * Created by YZY on 2018/5/4.
 */
fun Context.requestPermission(vararg group: String?){
    AndPermission.with(this)
            .permission(group)
            .onGranted {

            }
            .rationale { context, permissions, executor ->
                val list = Permission.transformText(this, permissions)
                val sb = StringBuilder()
                for (s in list) {
                    sb.append("$s、")
                }
                sb.deleteCharAt(sb.length - 1)
                AlertDialog.Builder(this)
                        .setTitle("温馨提示")
                        .setMessage("为确保程序正常运行，请打开必要权限：$sb")
                        .setCancelable(false)
                        .setPositiveButton("确定", {dialog, _ ->
                            executor.execute()
                            dialog.dismiss()
                        }).show()
            }.onDenied {
                if (AndPermission.hasAlwaysDeniedPermission(this, it)) {
                    // 这里使用一个Dialog展示没有这些权限应用程序无法继续运行，询问用户是否去设置中授权。
                    val list = Permission.transformText(this, it)
                    val sb = StringBuilder()
                    for (s in list) {
                        sb.append("$s、")
                    }
                    sb.deleteCharAt(sb.length - 1)
                    AlertDialog.Builder(this)
                            .setTitle("温馨提示")
                            .setMessage("为确保程序正常运行，请打开必要权限：$sb")
                            .setCancelable(false)
                            .setPositiveButton("确定", {dialog, _ ->
                                val settingService = AndPermission.permissionSetting(this)
                                // 如果用户同意去设置：
                                settingService.execute()
                                dialog.dismiss()

                                it as ArrayList<String>
                                val array = arrayOfNulls<String>(it.size)
                                for (i in it.indices){
                                    array[i] = it[i]
                                }
                                requestPermission(*array)
                            }).show()
                    return@onDenied
                }
                it as ArrayList<String>
                val array = arrayOfNulls<String>(it.size)
                for (i in it.indices){
                    array[i] = it[i]
                }
                requestPermission(*array)
                    // 如果用户不同意去设置：
                    //settingService.cancel()
            }.start()
}
object PermissionDialogUtil{
    private var dialog : AlertDialog? = null
    private var isShowing = false
    fun showDialog(context: Context, sb: StringBuilder){
        if (dialog == null){
            dialog = AlertDialog.Builder(context)
                    .setTitle("温馨提示")
                    .setMessage("为确保程序正常运行，请打开必要权限：$sb")
                    .setCancelable(false)
                    .setPositiveButton("确定", {dialog, _ ->
                        val settingService = AndPermission.permissionSetting(context)
                        // 如果用户同意去设置：
                        settingService.execute()
                        dialog.dismiss()
                        isShowing = false
                    }).create()
        }
        if (!isShowing){
            dialog?.show()
            isShowing = true
        }
    }
}