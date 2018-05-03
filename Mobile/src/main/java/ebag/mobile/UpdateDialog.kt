package ebag.mobile

import android.content.Context
import ebag.core.base.BaseDialog
import ebag.core.http.file.DownLoadObserver
import ebag.core.http.file.DownloadInfo
import ebag.core.http.file.DownloadManager
import ebag.core.util.FileUtil
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.T
import kotlinx.android.synthetic.main.dialog_update.*

/**
 * Created by YZY on 2018/3/7.
 */
class UpdateDialog(
        private val mContext: Context,
        roleName: String,
        versionName: String,
        content: String,
        url: String,
        isUpdate: String) : BaseDialog(mContext) {
    override fun getLayoutRes(): Int {
        return R.layout.dialog_update
    }
    init {
        tvUpdateVersion.text = versionName
        tvUpdateContent.text = content
        if (isUpdate == "Y"){ //强制更新
            setCancelable(false)
        }else{
            btnUpdateClose.setOnClickListener {
                dismiss()
            }
        }
        btnUpdate.setOnClickListener {
            dismiss()
            downloadApk(url, roleName)
        }
    }

    private fun downloadApk(url: String, roleName: String){
        val fileName = "$roleName.apk"
        val filePath = "${FileUtil.getApkFilePath()}$fileName"
        if (FileUtil.isFileExists(filePath)){
            FileUtil.deleteFile(filePath)
        }
        DownloadManager.getInstance().download(url, FileUtil.getApkFilePath(), fileName, object : DownLoadObserver(){
            override fun onNext(downloadInfo: DownloadInfo) {
                super.onNext(downloadInfo)
                LoadingDialogUtil.showLoading(mContext, "正在下载...${downloadInfo.progress * 100 / downloadInfo.total}%")
            }

            override fun onComplete() {
                LoadingDialogUtil.closeLoadingDialog()
                mContext.installApk(filePath)
            }

            override fun onError(e: Throwable) {
                LoadingDialogUtil.closeLoadingDialog()
                T.show(mContext, "下载失败，请稍后重试")
                DownloadManager.getInstance().cancel(url)
            }
        })
    }
}