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
        val list = ArrayList<BookBean>()
        list.add(BookBean("人教版", "2010-10-24", "上学期", "语文", "三年级"))
        list.add(BookBean("人教版", "2010-10-24", "上学期", "英语", "三年级"))
        list.add(BookBean("人教版", "2010-10-24", "上学期", "数学", "三年级"))
        list.add(BookBean("人教版", "2010-10-24", "上学期", "生物", "三年级"))
        list.add(BookBean("人教版", "2010-10-24", "上学期", "化学", "三年级"))
        list.add(BookBean("人教版", "2010-10-24", "上学期", "历史", "三年级"))
        list.add(BookBean("人教版", "2010-10-24", "上学期", "社会", "三年级"))
        withFirstPageData(list)
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<BookBean>>) {
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
        //TODO replace url
        val url = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/CAI/grade1/phase1/ddyfz.zip"
        val imagePath = FileUtil.getBookPath() + "123456/" + url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."))
        val file = FileUtil.getBookPath() + url.substring(url.lastIndexOf("/"))

        if (FileUtil.isFileExists(file)) {
            ReaderActivity.jump(this, imagePath)
            return
        }
        DownloadManager.getInstance().download(
                url,
                FileUtil.getBookPath(),
                object : DownLoadObserver(){
            override fun onNext(downloadInfo: DownloadInfo) {
                super.onNext(downloadInfo)
                LoadingDialogUtil.showLoading(this@BookListActivity, "正在下载...${downloadInfo.progress * 100 / downloadInfo.total}%")
            }

            override fun onComplete() {
                //TODO replace fileName
                val fileName = FileUtil.getBookPath() + url.substring(url.lastIndexOf("/"))
                try {
                    ZipUtils.upZipFile(File(fileName), FileUtil.getBookPath() + "123456")
                } catch (e: ZipException) {
                    T.show(this@BookListActivity, "文件解压失败")
                    e.printStackTrace()
                } catch (e: IOException) {
                    T.show(this@BookListActivity, "文件解压失败")
                    e.printStackTrace()
                }

                LoadingDialogUtil.closeLoadingDialog()
                T.show(this@BookListActivity, "下载完成")
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
            helper.getView<ImageView>(R.id.ivBook).loadImage(item.image)
            helper.setText(R.id.tvEdition,item.edition)
                    .setText(R.id.tvTime,"[添加时间:${item.time}]")
                    .setText(R.id.tvSemester,item.item)
                    .setText(R.id.tvSubject,item.subject)
                    .setText(R.id.tvClass,item.classX)
        }
    }
}