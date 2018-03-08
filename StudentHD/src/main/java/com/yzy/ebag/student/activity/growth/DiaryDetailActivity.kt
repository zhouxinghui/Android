package com.yzy.ebag.student.activity.growth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.Diary
import ebag.core.base.BaseActivity
import ebag.core.base.PhotoPreviewActivity
import ebag.core.util.*
import ebag.hd.widget.startSelectPicture
import kotlinx.android.synthetic.main.activity_diary_detail.*
import java.io.File


/**
 * @author caoyu
 * @date 2018/2/2
 * @description
 */
class DiaryDetailActivity: BaseActivity() {

    companion object {
        fun jump(context: Context, gradeId: String, diary: Diary?){
            context.startActivity(
                    Intent(context, DiaryDetailActivity::class.java)
                            .putExtra("gradeId", gradeId)
                            .putExtra("diary",diary)
            )
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_diary_detail
    }


    private val maxWordLength = 200

    private val imgList = ArrayList<String>()
    private val imgAdapter by lazy { Adapter() }
    private var userId = "1"
    private var uploadPosition = 0
    private val sb = StringBuilder()
    private var diary: Diary? = null
    private lateinit var gradeId: String
    override fun initViews() {
        gradeId = intent.getStringExtra("gradeId") ?: ""
        diary = intent.getSerializableExtra("diary") as Diary?

        recyclerView.layoutManager = GridLayoutManager(this, 8)
        recyclerView.adapter = imgAdapter

        if(diary == null){//添加日记

            titleEdit.isEnabled = true
            contentEdit.isEnabled = true

            contentEdit.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxWordLength))
            contentEdit.addTextChangedListener(object : TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                    numberOfWord.text = "${s?.length}/$maxWordLength"
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            numberOfWord.text = "0/$maxWordLength"

            titleBar.setRightText("完成"){
                if(StringUtils.isEmpty(titleEdit.text.toString())){
                    T.show(this, "请输入标题")
                    return@setRightText
                }
                if(StringUtils.isEmpty(contentEdit.text.toString())){
                    T.show(this, "请填写内容")
                    return@setRightText
                }

                LoadingDialogUtil.showLoading(this, "正在上传...")
                if (sb.isNotEmpty())
                    commit(titleEdit.text.toString(), contentEdit.text.toString())
                else {
                    val fileName = System.currentTimeMillis().toString()
                    val url = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/diary/$userId/$fileName"
                    OSSUploadUtils.getInstance().UploadPhotoToOSS(this, File(imgAdapter.getItem(0)), "personal/$userId", fileName, myHandler)
                    sb.append("$url,")
                }
            }

            imgAdapter.setOnItemClickListener { adapter, _, position ->
                adapter as Adapter
                when {
                    adapter.data.size == 9 -> T.show(this, "图片选择上限为：8张")
                    position == adapter.data.size - 1 -> startSelectPicture(9 - imgAdapter.itemCount)
                    else -> {
                        val list = adapter.data.filter { !StringUtils.isEmpty(it) }
                        PhotoPreviewActivity.jump(this, list , position)
                    }
                }
            }
            imgList.add("")
            imgAdapter.setNewData(imgList)

        }else{// 查看日记
            titleEdit.isEnabled = false
            contentEdit.isEnabled = false

            titleEdit.setText(diary?.title)
            contentEdit.setText(diary?.content)
            imgAdapter.setNewData(diary?.photos)
        }
    }

    private fun commit(title: String, content: String, urls: String = ""){

    }

    inner class Adapter: BaseQuickAdapter<String, BaseViewHolder>(ebag.hd.R.layout.imageview){
        override fun convert(helper: BaseViewHolder, item: String) {
            val position = helper.adapterPosition
            val imageView = helper.getView<ImageView>(ebag.hd.R.id.imageView)

            // 添加日记 并且是最后一张图片
            if (diary == null && position == data.size - 1){
                imageView.setImageResource(ebag.hd.R.drawable.add_pic)
            }else {
                imageView.loadImage(item)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    // 图片选择结果回调
                    val selectList = PictureSelector.obtainMultipleResult(data)
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    selectList.forEach { imgAdapter.addData(imgAdapter.itemCount - 1, it.path) }
                }
            }
        }
    }

    private val myHandler by lazy { MyHandler(this) }
    class MyHandler(activity: DiaryDetailActivity): HandlerUtil<DiaryDetailActivity>(activity){
        override fun handleMessage(activity: DiaryDetailActivity, msg: Message) {
            when(msg.what){
                Constants.UPLOAD_SUCCESS ->{
                    activity.uploadPosition ++
                    if (activity.uploadPosition < activity.imgAdapter.itemCount - 1) {
                        val fileName = System.currentTimeMillis().toString()
                        val url = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/diary/${activity.userId}/$fileName"
                        OSSUploadUtils.getInstance().UploadPhotoToOSS(
                                activity,
                                File(activity.imgAdapter.getItem(activity.uploadPosition)),
                                "personal/${activity.userId}",
                                fileName,
                                activity.myHandler)
                        activity.sb.append("$url,")
                    }else{
                        activity.commit(
                                activity.titleEdit.text.toString(),
                                activity.contentEdit.text.toString(),
                                activity.sb.substring(0, activity.sb.lastIndexOf(","))
                        )
                    }
                }
                Constants.UPLOAD_FAIL ->{
                    LoadingDialogUtil.closeLoadingDialog()
                    T.show(activity, "上传图片失败，请稍后重试")
                }
            }
        }
    }
}