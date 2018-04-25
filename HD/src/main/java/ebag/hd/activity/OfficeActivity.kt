package ebag.hd.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.widget.FrameLayout
import com.tencent.smtt.sdk.TbsReaderView
import ebag.core.base.BaseActivity
import ebag.core.http.file.DownLoadObserver
import ebag.core.http.file.DownloadInfo
import ebag.core.http.file.DownloadManager
import ebag.core.util.FileUtil
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.T
import ebag.hd.R
import kotlinx.android.synthetic.main.activity_office.*

/**
 * Created by YZY on 2018/4/25.
 */
class OfficeActivity: BaseActivity(), TbsReaderView.ReaderCallback  {
    override fun getLayoutId(): Int = R.layout.activity_office
    companion object {
        fun jump(context: Context, fileUrl: String, fileName: String){
            context.startActivity(
                    Intent(context, OfficeActivity::class.java)
                            .putExtra("fileUrl", fileUrl)
                            .putExtra("fileName", fileName)
            )
        }
    }
    private var url = ""
    private lateinit var mTbsReaderView: TbsReaderView
    private var mFileName = ""
    override fun initViews() {
        titleBar.setTitle(intent.getStringExtra("fileName") ?: "")
        mTbsReaderView = TbsReaderView(this, this)
        rootView.addView(mTbsReaderView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))

        url = intent.getStringExtra("fileUrl") ?: ""
        mFileName = url.substring(url.lastIndexOf("/") + 1, url.length)
        val filePath = "${FileUtil.getPrepareFilePath()}$mFileName"
        val isFileExit = FileUtil.isFileExists(filePath)
        if (isFileExit){
//            OfficeActivity.jump(this@OfficeActivity, filePath)
            displayFile(filePath)
        }else {
            DownloadManager.getInstance().download(url, FileUtil.getPrepareFilePath(), object : DownLoadObserver() {
                override fun onNext(downloadInfo: DownloadInfo) {
                    super.onNext(downloadInfo)
                    LoadingDialogUtil.showLoading(this@OfficeActivity, "正在下载...${downloadInfo.progress * 100 / downloadInfo.total}%")
                }

                override fun onComplete() {
                    LoadingDialogUtil.closeLoadingDialog()
//                    OfficeActivity.jump(this@OfficeActivity, filePath)
                    displayFile(filePath)
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    LoadingDialogUtil.closeLoadingDialog()
                    T.show(this@OfficeActivity, "文件下载失败，请稍后重试")
                    DownloadManager.getInstance().cancel(url)
                }
            })
        }
    }

    private fun displayFile(filePath: String) {
        val bundle = Bundle()
        bundle.putString("filePath", filePath)
        bundle.putString("tempPath", Environment.getExternalStorageDirectory().path)
        val result = mTbsReaderView.preOpen(parseFormat(mFileName), false)
        if (result) {
            mTbsReaderView.openFile(bundle)
        }else{
            T.show(this, "不支持的文件类型")
            finish()
        }
    }

    private fun parseFormat(fileName: String): String {
        return fileName.substring(fileName.lastIndexOf(".") + 1)
    }

    override fun onDestroy() {
        DownloadManager.getInstance().cancel(url)
        mTbsReaderView.onStop()
        super.onDestroy()
    }

    override fun onCallBackAction(p0: Int?, p1: Any?, p2: Any?) {

    }
}