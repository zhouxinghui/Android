package ebag.hd.activity

import android.content.Context
import android.content.Intent
import ebag.core.base.BaseActivity
import ebag.core.http.file.DownLoadObserver
import ebag.core.http.file.DownloadInfo
import ebag.core.http.file.DownloadManager
import ebag.core.util.FileUtil
import ebag.core.util.T
import ebag.hd.R
import kotlinx.android.synthetic.main.activity_display_txt_file.*
import java.io.File
import java.nio.charset.Charset


/**
 * Created by YZY on 2018/4/3.
 */
class DisplayTxtFileActivity: BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_display_txt_file
    }
    companion object {
        fun jump(context: Context, fileUrl: String) {
            context.startActivity(
                    Intent(context, DisplayTxtFileActivity::class.java)
                            .putExtra("fileUrl", fileUrl)
            )
        }
    }

    override fun initViews() {
        val fileUrl = intent.getStringExtra("fileUrl") ?: ""
        if (fileUrl.isEmpty()){
            stateView.showEmpty()
            return
        }
        val filePath = "${FileUtil.getPrepareFilePath()}${fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.length)}"
        val isFileExit = FileUtil.isFileExists(filePath)
        if (isFileExit){
            loadFile(filePath)
        }else{
            DownloadManager.getInstance().download(fileUrl, FileUtil.getPrepareFilePath(), object : DownLoadObserver(){
                override fun onNext(downloadInfo: DownloadInfo) {
                    super.onNext(downloadInfo)
//                    LoadingDialogUtil.showLoading(this@DisplayTxtFileActivity, "正在下载...${downloadInfo.progress * 100 / downloadInfo.total}%")
                    stateView.showLoading()
                }

                override fun onComplete() {
                    stateView.showContent()
                    loadFile(filePath)
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    stateView.showError()
                    T.show(this@DisplayTxtFileActivity, "文件下载失败，请稍后重试")
                    DownloadManager.getInstance().cancel(fileUrl)
                }
            })
        }
    }

    private fun loadFile(filePath: String){
        val file = File(filePath)
        val sb = StringBuilder()
        file.forEachLine(Charset.forName("GB2312"), {
            sb.append("$it\n")
        })
        textView.text = sb.toString()
    }
}