package com.yzy.ebag.student.activity.tools

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.PhotoPreviewActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.util.T
import ebag.hd.R
import ebag.hd.activity.OfficeActivity
import ebag.hd.base.BaseListActivity
import ebag.hd.bean.PrepareFileBean
import ebag.hd.widget.VideoPlayDialog
import ebag.hd.widget.VoicePlayDialog

/**
 * Created by YZY on 2018/5/22.
 */
class PrepareDisplayActivity: BaseListActivity<List<PrepareFileBean>, PrepareFileBean>() {
    private val videoPlayerDialog by lazy {
        VideoPlayDialog(this)
    }
    private val voicePlayerDialog by lazy {
        VoicePlayDialog(this)
    }
    companion object {
        fun jump(context: Context, classId: String){
            context.startActivity(
                    Intent(context, PrepareDisplayActivity::class.java)
                            .putExtra("classId", classId)
            )
        }
    }
    private lateinit var classId: String
    override fun loadConfig(intent: Intent) {
        titleBar.setTitle("每日预习")
        loadMoreEnabled(false)
        classId = intent.getStringExtra("classId")
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<PrepareFileBean>>) {
        StudentApi.prepareList(classId, requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<PrepareFileBean>?): List<PrepareFileBean>? = parent

    override fun getAdapter(): BaseQuickAdapter<PrepareFileBean, BaseViewHolder> {
        return MyAdapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<PrepareFileBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return GridLayoutManager(this, 6)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        adapter as MyAdapter
        val bean = adapter.data[position]
        val fileType = bean.fileType ?: bean.fileName.substring(bean.fileName.lastIndexOf(".") + 1, bean.fileName.length)
        when(fileType){
            "doc","docx","xls","xlsx","ppt","pptx","txt","pdf" ->{
//                DisplayOfficeFileActivity.jump(mContext, bean.fileUrl)
                OfficeActivity.jump(this, bean.fileUrl, bean.fileName)
            }
        /*"txt" ->{
            DisplayTxtFileActivity.jump(mContext, bean.fileUrl)
        }
        "pdf" ->{
            DisplayPdfFileActivity.jump(mContext, bean.fileUrl)
        }*/
            "jpg","png","bmp","jpeg","gif" ->{
                val imgList = ArrayList<String>()
                imgList.add(bean.fileUrl)
                PhotoPreviewActivity.jump(this, imgList , 0)
            }
            "mp4","rmvb","avi"->{
                videoPlayerDialog.show(bean.fileUrl, bean.fileName)
            }
            "mp3", "amr", "wav" ->{
                voicePlayerDialog.show(bean.fileUrl, bean.fileName)
            }
            else ->{
                T.show(this, "不支持的文件类型，请在PC端尝试打开此文件")
            }
        }
    }

    inner class MyAdapter: BaseQuickAdapter<PrepareFileBean, BaseViewHolder>(R.layout.item_prepare){
        override fun convert(helper: BaseViewHolder, item: PrepareFileBean?) {
            val imageView = helper.getView<ImageView>(R.id.fileImg)
            val fileName = item?.fileName
            setFileIcon(imageView, item?.fileType ?: fileName?.substring(fileName.lastIndexOf(".") + 1, fileName.length))
            helper.setText(R.id.fileName, item?.fileName)
        }
    }

    private fun setFileIcon(imageView: ImageView, extension: String?){
        when(extension){
            "doc","docx" ->{
                imageView.setImageResource(R.drawable.prepare_icon_word)
            }
            "pdf" ->{
                imageView.setImageResource(R.drawable.prepare_icon_pdf)
            }
            "txt" ->{
                imageView.setImageResource(R.drawable.prepare_icon_txt)
            }
            "ppt", "pptx" ->{
                imageView.setImageResource(R.drawable.prepare_icon_ppt)
            }
            "xls", "xlsx" ->{
                imageView.setImageResource(R.drawable.prepare_icon_excel)
            }
            "mp3","amr", "wav" ->{
                imageView.setImageResource(R.drawable.prepare_icon_music)
            }
            "mp4","rmvb","avi" ->{
                imageView.setImageResource(R.drawable.prepare_icon_video)
            }
            "jpg","png","bmp","jpeg","gif" ->{
                imageView.setImageResource(R.drawable.prepare_icon_image)
            }
            else ->{
                imageView.setImageResource(R.drawable.prepare_icon_unknown)
            }
        }
    }

    override fun onDestroy() {
        voicePlayerDialog.stop()
        super.onDestroy()
    }
}