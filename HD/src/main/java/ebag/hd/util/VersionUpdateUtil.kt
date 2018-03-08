package ebag.hd.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import com.umeng.socialize.utils.DeviceConfig.context
import ebag.core.http.network.RequestCallBack
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.T
import ebag.hd.bean.VersionUpdateBean
import ebag.hd.dialog.UpdateDialog
import ebag.hd.http.EBagApi
import java.io.File




/**
 * Created by YZY on 2018/3/7.
 */
fun Context.checkUpdate(roleName: String, isShowToast: Boolean = true){
    val currentVersionCode = getVersionCode().toInt()
    val updateRequest = object : RequestCallBack<VersionUpdateBean>(){
        override fun onStart() {
            if (isShowToast)
                LoadingDialogUtil.showLoading(this@checkUpdate, "检查更新")
        }

        override fun onSuccess(entity: VersionUpdateBean?) {
            LoadingDialogUtil.closeLoadingDialog()
            if (entity == null){
                if (isShowToast)
                    T.show(this@checkUpdate, "当前已是最新版本")
                return
            }
            val versionCode = entity.versionNumber.toInt()
            if (versionCode > currentVersionCode){//有版本更新
                val dialog = UpdateDialog(this@checkUpdate, roleName, entity.versionName, entity.mark, entity.url, entity.isUpdate)
                dialog.show()
            }else{//无版本更新
                if (isShowToast)
                    T.show(this@checkUpdate, "当前已是最新版本")
            }
        }

        override fun onError(exception: Throwable) {
            LoadingDialogUtil.closeLoadingDialog()
            if (isShowToast)
                T.show(this@checkUpdate, "当前已是最新版本")
        }
    }
    EBagApi.checkUpdate(roleName, currentVersionCode.toString(), updateRequest)
}

/**
 * 安装apk
 */
fun Context.installApk(apkPath: String){
    val intent = Intent(Intent.ACTION_VIEW)
    // 由于没有在Activity环境下启动Activity,设置下面的标签
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    //版本在7.0以上是不能直接通过uri访问的
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
        val file = File(apkPath)
        //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
        val apkUri = FileProvider.getUriForFile(context, getProviderName(), file)
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
    } else {
        intent.setDataAndType(Uri.fromFile(File(apkPath)), "application/vnd.android.package-archive")
    }

    startActivity(intent)
}

fun Context.getVersionCode(): String{
    val packageManager = this.packageManager
    val packageInfo: PackageInfo
    var versionCode = ""
    try {
        packageInfo = packageManager.getPackageInfo(this.packageName, 0)
        versionCode = packageInfo.versionCode.toString() + ""
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    return versionCode
}

fun Context.getProviderName(): String{
    return "$packageName.fileprovider"
}