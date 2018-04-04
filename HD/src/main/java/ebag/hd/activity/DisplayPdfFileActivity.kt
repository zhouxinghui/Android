package ebag.hd.activity

import android.content.Context
import android.content.Intent
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import ebag.core.base.BaseActivity
import ebag.core.http.file.DownLoadObserver
import ebag.core.http.file.DownloadInfo
import ebag.core.http.file.DownloadManager
import ebag.core.util.FileUtil
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.T
import ebag.hd.R
import kotlinx.android.synthetic.main.activity_display_pdf_file.*
import java.io.File

/**
 * Created by YZY on 2018/4/3.
 */
class DisplayPdfFileActivity: BaseActivity(), OnPageChangeListener {
    override fun getLayoutId(): Int {
        return R.layout.activity_display_pdf_file
    }

    companion object {
        fun jump(context: Context, fileUrl: String) {
            context.startActivity(
                    Intent(context, DisplayPdfFileActivity::class.java)
                            .putExtra("fileUrl", fileUrl)
            )
        }
    }
    private var fileUrl = ""
    private lateinit var filePath: String
    override fun initViews() {
        stateView.setOnRetryClickListener {
            downLoadFile(filePath)
        }
        fileUrl = intent.getStringExtra("fileUrl") ?: ""
        if (fileUrl.isEmpty()){
            stateView.showEmpty()
            return
        }
        filePath = "${FileUtil.getPrepareFilePath()}${fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.length)}"
        val isFileExit = FileUtil.isFileExists(filePath)
        if (isFileExit){
            loadFile(filePath)
        }else{
            downLoadFile(filePath)
        }
    }

    override fun onPageChanged(page: Int, pageCount: Int) {

    }

    private fun downLoadFile(filePath: String){
        DownloadManager.getInstance().download(fileUrl, FileUtil.getPrepareFilePath(), object : DownLoadObserver(){
            override fun onNext(downloadInfo: DownloadInfo) {
                super.onNext(downloadInfo)
                LoadingDialogUtil.showLoading(this@DisplayPdfFileActivity, "正在下载...${downloadInfo.progress * 100 / downloadInfo.total}%")
            }

            override fun onComplete() {
                stateView.showContent()
                LoadingDialogUtil.closeLoadingDialog()
                loadFile(filePath)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                stateView.showError()
                LoadingDialogUtil.closeLoadingDialog()
                T.show(this@DisplayPdfFileActivity, "文件下载失败，请稍后重试")
                DownloadManager.getInstance().cancel(fileUrl)
            }
        })
    }

    private fun loadFile(filePath: String){
        pdfView.fromFile(File(filePath))  //pdf地址
                .defaultPage(1)//默认页面
                .enableDoubletap(true)
                .swipeHorizontal(false)//是不是横向查看
                .onPageChange(this)
                .enableSwipe(true)
                .load()
    }
}