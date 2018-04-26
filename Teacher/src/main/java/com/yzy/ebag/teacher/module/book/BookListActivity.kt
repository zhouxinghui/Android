package com.yzy.ebag.teacher.module.book

import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.module.clazz.CreateClassActivity
import ebag.core.http.file.DownLoadObserver
import ebag.core.http.file.DownloadInfo
import ebag.core.http.file.DownloadManager
import ebag.core.http.network.MsgException
import ebag.core.http.network.RequestCallBack
import ebag.core.util.*
import ebag.mobile.base.BaseListActivity
import ebag.mobile.bean.BookBean
import ebag.mobile.http.EBagApi
import ebag.mobile.module.book.ReaderActivity
import java.io.File
import java.io.IOException
import java.util.zip.ZipException


/**
 * Created by caoyu on 2018/1/8.
 */
class BookListActivity: BaseListActivity<List<BookBean>, BookBean>() {
    companion object {
        fun jump(context: Context, classId: String? = null){
            context.startActivity(
                    Intent(context, BookListActivity::class.java)
                            .putExtra("classId", classId)
            )
        }
    }
    private var classId: String? = ""
    private val courseDialog by lazy {
        val dialog = AlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage("你当前所在班级没有添加所教课程，请在对应班级的班级空间中添加所教课程")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", null)
                .create()
        dialog
    }
    private val createClassDialog by lazy {
        val dialog = AlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage("你暂未加入班级，你可以联系对应班级班主任添加任课老师，也可自己创建班级")
                .setNegativeButton("取消", null)
                .setPositiveButton("创建班级", {dialog, which ->
                    CreateClassActivity.jump(this)
                    finish()
                })
                .create()
        dialog
    }
    private var url = ""
    override fun loadConfig(intent: Intent) {
        setPageTitle("学习课本")
        loadMoreEnabled(false)
        classId = intent.getStringExtra("classId")
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<BookBean>>) {
        if (StringUtils.isEmpty(classId))
            EBagApi.myBookList(requestCallBack)
        else
            EBagApi.studentBookList(classId, requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<BookBean>?): List<BookBean>? {
        return parent
    }

    override fun onSpecialError(exception: MsgException) {
        if (exception.code == "3001"){
            courseDialog.show()
        }else if (exception.code == "2001"){
            createClassDialog.show()
        }
    }

    override fun getAdapter(): BaseQuickAdapter<BookBean, BaseViewHolder> {
        return BookListAdapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<BookBean, BaseViewHolder>): RecyclerView.LayoutManager? = null

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        adapter as BookListAdapter
        val bookBean = adapter.getItem(position)!!
        url = bookBean.downloadUrl
        val downloadPath = FileUtil.getBookPath() + "${bookBean.bookId}/"
        val imagePath = downloadPath + url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."))
        val file = downloadPath + url.substring(url.lastIndexOf("/") + 1)

        if (FileUtil.isFileExists(imagePath)) {
            ReaderActivity.jump(this, imagePath, bookBean.bookId)
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
                ReaderActivity.jump(this@BookListActivity, imagePath, bookBean.bookId)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                if (FileUtil.isFileExists(file)) {
                    FileUtil.deleteFile(file)
                }
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
                    .setText(R.id.tvSemester,item.semester)
                    .setText(R.id.tvSubject,item.bookName)
                    .setText(R.id.tvClass,item.gradeName)
        }
    }

    override fun onDestroy() {
        DownloadManager.getInstance().cancel(url)
        super.onDestroy()
    }
}