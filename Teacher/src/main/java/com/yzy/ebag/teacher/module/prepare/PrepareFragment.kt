package com.yzy.ebag.teacher.module.prepare

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.PrepareFileBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.base.BaseListFragment
import ebag.core.base.PhotoPreviewActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.T

/**
 * Created by YZY on 2018/3/2.
 */
class PrepareFragment: BaseListFragment<List<PrepareFileBean>, PrepareFileBean>(), BaseQuickAdapter.OnItemLongClickListener{
    companion object {
        fun newInstance(): PrepareFragment {
            val fragment = PrepareFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
    private val deleteRequest by lazy {
        object : RequestCallBack<String>() {
            override fun onStart() {
                deleteFileDialog.dismiss()
                LoadingDialogUtil.showLoading(mContext, "正在删除...")
            }

            override fun onSuccess(entity: String?) {
                LoadingDialogUtil.closeLoadingDialog()
                T.show(mContext, "删除成功")
                onRetryClick()
            }

            override fun onError(exception: Throwable) {
                LoadingDialogUtil.closeLoadingDialog()
                exception.handleThrowable(mContext)
            }
        }
    }
    private lateinit var list: List<PrepareFileBean>
    private var gradeCode: String? = ""
    private var subCode: String? = ""
    private var unitId: String? = ""
    private var fileId = ""
    private var type = ""
    private val deleteFileDialog by lazy {
        val dialog = AlertDialog.Builder(mContext)
                .setMessage("删除此文件？")
                .setPositiveButton("删除", { dialog, which ->
                    deleteFile()
                })
                .setNegativeButton("取消", { dialog, which ->
                    dialog.dismiss()
                }).create()
        dialog
    }
    /*private val videoPlayerDialog by lazy {
        VideoPlayDialog(mContext)
    }
    private val voicePlayerDialog by lazy {
        VoicePlayDialog(mContext)
    }*/
    override fun getBundle(bundle: Bundle?) {
    }

    private fun deleteFile(){
        TeacherApi.deletePrepareFile(fileId, deleteRequest)
    }

    override fun loadConfig() {
    }

    fun notifyRequest(type: String, gradeCode: String?, subCode: String?, unitId: String?){
        this.type = type
        this.gradeCode = gradeCode
        this.subCode = subCode
        this.unitId = unitId
        onRetryClick()
    }

    override fun getPageSize(): Int {
        return 16
    }
    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<PrepareFileBean>>) {
        TeacherApi.prepareList(type, page, getPageSize(), requestCallBack, gradeCode, subCode, unitId)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<PrepareFileBean>?): List<PrepareFileBean>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<PrepareFileBean, BaseViewHolder> {
        val adapter = MyAdapter()
        adapter.onItemLongClickListener = this
        return adapter
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<PrepareFileBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return GridLayoutManager(mContext, 3)
    }
    /*
    "pdf", "doc", "docx","xls","xlsx","ppt","pptx","jpg","png","bpm","bmp","avi","mp4","mpg","mp3","swf","wmv"
    */
    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        adapter as MyAdapter
        val bean = adapter.data[position]
        val fileType = bean.fileType ?: bean.fileName.substring(bean.fileName.lastIndexOf(".") + 1, bean.fileName.length)
        when(fileType){
            "doc","docx","xls","xlsx","ppt","pptx" ->{
//                DisplayOfficeFileActivity.jump(mContext, bean.fileUrl)
            }
            "txt" ->{
//                DisplayTxtFileActivity.jump(mContext, bean.fileUrl)
            }
            "pdf" ->{
//                DisplayPdfFileActivity.jump(mContext, bean.fileUrl)
            }
            "jpg","png","bmp","jpeg","gif" ->{
                val imgList = ArrayList<String>()
                imgList.add(bean.fileUrl)
                PhotoPreviewActivity.jump(mContext, imgList , 0)
            }
            "mp4","rmvb","avi"->{
//                videoPlayerDialog.show(bean.fileUrl, bean.fileName)
            }
            "mp3", "amr", "wav" ->{
//                voicePlayerDialog.show(bean.fileUrl, bean.fileName)
            }
            else ->{
                T.show(mContext, "不支持的文件类型，请在PC端尝试打开此文件")
            }
        }
    }

    override fun onItemLongClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int): Boolean {
        adapter as MyAdapter
        fileId = adapter.data[position].id
        deleteFileDialog.show()
        return true
    }

    inner class MyAdapter: BaseQuickAdapter<PrepareFileBean, BaseViewHolder>(R.layout.item_prepare){
        override fun convert(helper: BaseViewHolder, item: PrepareFileBean?) {
            val imageView = helper.getView<ImageView>(R.id.fileImg)
            val fileName = item?.fileName
            setFileIcon(imageView, item?.fileType ?: fileName?.substring(fileName.lastIndexOf(".") + 1, fileName.length))
            helper.setText(R.id.fileName, item?.fileName)
        }
    }

    /*
      "pdf", "doc", "docx","xls","xlsx","ppt","pptx","jpg","png","bpm","bmp","avi","mp4","mpg","mp3","swf","wmv"
     */

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
//        voicePlayerDialog.stop()
        super.onDestroy()
    }
}