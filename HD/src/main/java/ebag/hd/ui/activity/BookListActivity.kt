package ebag.hd.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.base.BaseListActivity
import ebag.core.http.file.DownLoadObserver
import ebag.core.http.file.DownloadInfo
import ebag.core.http.file.DownloadManager
import ebag.core.http.network.RequestCallBack
import ebag.core.util.*
import ebag.hd.R
import ebag.hd.activity.ReaderActivity
import ebag.hd.bean.BookBean
import ebag.hd.http.EBagApi
import java.io.File
import java.io.IOException
import java.util.zip.ZipException


/**
 * Created by caoyu on 2018/1/8.
 */
class BookListActivity: BaseListActivity<List<BookBean>, BookBean>() {
    companion object {
        fun jump(context: Context){
            context.startActivity(Intent(context, BookListActivity::class.java))
        }
    }
    override fun loadConfig(intent: Intent) {
        setPageTitle("学习课本")
        loadMoreEnabled(false)
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<BookBean>>) {
        EBagApi.myBookList(requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<BookBean>?): List<BookBean>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<BookBean, BaseViewHolder> {
        return BookListAdapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<BookBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return GridLayoutManager(this,3)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        adapter as BookListAdapter
        val bookBean = adapter.getItem(position)!!
        val url = bookBean.downloadUrl
        val downloadPath = FileUtil.getBookPath() + "${bookBean.bookId}/"
        val imagePath = downloadPath + url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."))
        val file = downloadPath + url.substring(url.lastIndexOf("/") + 1)

        if (FileUtil.isFileExists(imagePath)) {
            ReaderActivity.jump(this, imagePath)
            return
        }
        FileUtil.createDir(downloadPath)
        DownloadManager.getInstance().download(
                url,
                downloadPath,
                object : DownLoadObserver(){
            override fun onNext(downloadInfo: DownloadInfo) {
                super.onNext(downloadInfo)
                LoadingDialogUtil.showLoading(this@BookListActivity, "正在下载...${downloadInfo.progress * 100 / downloadInfo.total}%")
            }

            override fun onComplete() {
                try {
                    ZipUtils.upZipFile(File(file), downloadPath)
                } catch (e: ZipException) {
                    T.show(this@BookListActivity, "文件解压失败")
                    e.printStackTrace()
                } catch (e: IOException) {
                    T.show(this@BookListActivity, "文件解压失败")
                    e.printStackTrace()
                }

                LoadingDialogUtil.closeLoadingDialog()
                FileUtil.deleteFile(file)
                T.show(this@BookListActivity, "下载完成")
                ReaderActivity.jump(this@BookListActivity, imagePath)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                LoadingDialogUtil.closeLoadingDialog()
                T.show(this@BookListActivity, "下载失败，请稍后重试")
                DownloadManager.getInstance().cancel(url)
            }
        })
    }

    class BookListAdapter: BaseQuickAdapter<BookBean, BaseViewHolder>(R.layout.item_activity_book_list){
        override fun convert(helper: BaseViewHolder, item: BookBean) {
            helper.getView<ImageView>(R.id.ivBook).loadImage(item.pageImageUrl)
            helper.setText(R.id.tvEdition,item.bookVersionName)
                    .setText(R.id.tvTime,"[添加时间:${System.currentTimeMillis()}]")
                    .setText(R.id.tvSemester,item.semester)
                    .setText(R.id.tvSubject,item.bookName)
                    .setText(R.id.tvClass,item.gradeName)
        }
    }
}