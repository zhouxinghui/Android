package com.yzy.ebag.teacher.activity.prepare

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
import com.yzy.ebag.teacher.widget.DialogSelectClasses
import com.yzy.ebag.teacher.widget.LoadPrepareFileDialog
import ebag.core.base.BaseListFragment
import ebag.core.base.PhotoPreviewActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.hd.activity.OfficeActivity
import ebag.hd.bean.BaseClassesBean
import ebag.hd.widget.VideoPlayDialog
import ebag.hd.widget.VoicePlayDialog

/**
 * Created by YZY on 2018/3/2.
 */
class PrepareFragment: BaseListFragment<List<PrepareFileBean>, PrepareFileBean>(), BaseQuickAdapter.OnItemLongClickListener{
    companion object {
        fun newInstance(list: List<PrepareFileBean>?, type: String, gradeCode: String?, subCode: String?): PrepareFragment {
            val fragment = PrepareFragment()
            val bundle = Bundle()
            bundle.putSerializable("list", list as ArrayList)
            bundle.putString("type", type)
            bundle.putString("gradeCode", gradeCode)
            bundle.putString("subCode", subCode)
            fragment.arguments = bundle
            return fragment
        }
    }

    /**删除课件*/
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
    /**推送课件到学生自习*/
    private val pushRequest by lazy {
        object : RequestCallBack<String>() {
            override fun onStart() {
                selectClassesDialog.dismiss()
                LoadingDialogUtil.showLoading(mContext, "正在推送...")
            }

            override fun onSuccess(entity: String?) {
                LoadingDialogUtil.closeLoadingDialog()
                T.show(mContext, "推送成功")
            }

            override fun onError(exception: Throwable) {
                LoadingDialogUtil.closeLoadingDialog()
                exception.handleThrowable(mContext)
            }
        }
    }
    /**保存文件*/
    private val saveFileRequest by lazy {
        object : RequestCallBack<String>(){
            override fun onStart() {
                LoadingDialogUtil.showLoading(mContext)
            }

            override fun onSuccess(entity: String?) {
                LoadingDialogUtil.closeLoadingDialog()
                T.show(mContext, "保存成功")
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
    private var mGradeCode: String? = ""
    private var mSubCode: String? = ""
    private var unitId: String? = ""
    private var fileId = ""
    private lateinit var fileBean: PrepareFileBean
    private var type = ""
    /**删除文件提示*/
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
    /**推送备课时选择班级*/
    private val selectClassesDialog by lazy {
        val dialog = DialogSelectClasses(mContext)
        dialog.onConfirmClick = {
            pushFile(it)
        }
        dialog
    }
    /**长按选项*/
    private val selectDialog by lazy {
        val items = if (type == "1")
            arrayOf("推送至学生自习", "删除文件")
        else
            arrayOf("保存到个人备课")
        val dialog = AlertDialog.Builder(mContext)
                .setItems(items, {dialog, which ->
                    if (which == 0){
                        if(type != "1") {
                            if (StringUtils.isEmpty(gradeCode))
                                loadPrepareFileDialog.show(mGradeCode, mSubCode)
                            else
                                loadPrepareFileDialog.show(gradeCode, subCode)
                        }else {
                            selectClassesDialog.show()
                        }
                    }else if(which == 1){
                        deleteFileDialog.show()
                    }
                    dialog.dismiss()
                }).create()
        dialog
    }
    /**保存到个人备课时版本和单元选择*/
    private val loadPrepareFileDialog by lazy {
        val dialog = LoadPrepareFileDialog(mContext)
        dialog.onConfirmClick = {
            val flag = StringUtils.isEmpty(gradeCode)
            TeacherApi.savePrepareFile(fileBean, if(flag) mGradeCode else gradeCode, if (flag) mSubCode else subCode, it, saveFileRequest)
        }
        dialog
    }
    private val videoPlayerDialog by lazy {
        VideoPlayDialog(mContext)
    }
    private val voicePlayerDialog by lazy {
        VoicePlayDialog(mContext)
    }
    override fun getBundle(bundle: Bundle?) {
        try {
            list = bundle?.getSerializable("list") as ArrayList<PrepareFileBean>
            type = bundle.getString("type")
            mGradeCode = bundle.getString("gradeCode")
            mSubCode = bundle.getString("subCode")
        }catch (e: Exception){

        }
    }

    private fun deleteFile(){
        TeacherApi.deletePrepareFile(fileId, deleteRequest)
    }
    private fun pushFile(classes: List<BaseClassesBean?>){
        TeacherApi.pushPrepareFile(fileId, classes, pushRequest)
    }

    override fun loadConfig() {
//        withFirstPageData(list, true)
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
        return GridLayoutManager(mContext, 4)
    }
    /*
    "pdf", "doc", "docx","xls","xlsx","ppt","pptx","jpg","png","bpm","bmp","avi","mp4","mpg","mp3","swf","wmv"
    */
    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        adapter as MyAdapter
        val bean = adapter.data[position]
        val fileType = bean.fileType ?: bean.fileName.substring(bean.fileName.lastIndexOf(".") + 1, bean.fileName.length)
        when(fileType){
            "doc","docx","xls","xlsx","ppt","pptx","txt","pdf" ->{
//                DisplayOfficeFileActivity.jump(mContext, bean.fileUrl)
                OfficeActivity.jump(mContext, bean.fileUrl, bean.fileName)
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
                PhotoPreviewActivity.jump(mContext, imgList , 0)
            }
            "mp4","rmvb","avi"->{
                videoPlayerDialog.show(bean.fileUrl, bean.fileName)
            }
            "mp3", "amr", "wav" ->{
                voicePlayerDialog.show(bean.fileUrl, bean.fileName)
            }
            else ->{
                T.show(mContext, "不支持的文件类型，请在PC端尝试打开此文件")
            }
        }
    }

    override fun onItemLongClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int): Boolean {
        adapter as MyAdapter
        fileBean = adapter.data[position]
        fileId = fileBean.id
        selectDialog.show()
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
        voicePlayerDialog.stop()
        super.onDestroy()
    }
}