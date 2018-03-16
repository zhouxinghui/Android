package ebag.hd.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Message
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.tools.PictureFileUtils
import ebag.core.base.BaseActivity
import ebag.core.base.PhotoPreviewActivity
import ebag.core.util.*
import ebag.hd.R
import ebag.hd.widget.startSelectPicture
import kotlinx.android.synthetic.main.activity_publish_content.*
import java.io.File


/**
 * Created by YZY on 2018/1/17.
 */
abstract class BPublishContentActivity: BaseActivity() {
    private val imgList = ArrayList<String>()
    private val imgAdapter by lazy { MyAdapter(imgList) }
    private var userId = "1"
    private var uploadPosition = 0
    private var sb = StringBuilder()
    override fun getLayoutId(): Int {
        return R.layout.activity_publish_content
    }
    private var currentPosition = 0
    private val deleteDialog by lazy {
        AlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage("是否删除所选图片？")
                .setNegativeButton("取消", null)
                .setPositiveButton("删除", {dialog, which ->
                    imgAdapter.remove(currentPosition)
                }).create()
    }
    override fun initViews() {
        titleBar.setRightText(resources.getString(R.string.commit), {
            if(StringUtils.isEmpty(contentEdit.text.toString())){
                T.show(this, "请填写内容")
                return@setRightText
            }
            LoadingDialogUtil.showLoading(this, "正在上传...")
            if (sb.isNotEmpty())// 阿里云的上传成功了， 但是commit 提交的时候失败了
                commit(contentEdit.text.toString())
            else {
                val fileName = System.currentTimeMillis().toString()
                val url = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/$userId/$fileName"
                OSSUploadUtils.getInstance().UploadPhotoToOSS(this, File(imgAdapter.getItem(0)), "personal/$userId", fileName, myHandler)
                sb.append("$url,")
            }
        })
        imgList.add("")
        recyclerView.layoutManager = GridLayoutManager(this, 8)
        recyclerView.adapter = imgAdapter
        numberOfWord.text = getString(R.string.number_of_word, 0)
        contentEdit.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                numberOfWord.text = getString(R.string.number_of_word, s?.length)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        imgAdapter.setOnItemClickListener { adapter, _, position ->
            adapter as MyAdapter
            when {
                adapter.data.size == 9 -> T.show(this, "图片选择上限为：8张")
                position == adapter.data.size - 1 -> startSelectPicture(9 - imgAdapter.itemCount)
                else -> {
                    val list = adapter.data.filter { !StringUtils.isEmpty(it) }
                    PhotoPreviewActivity.jump(this, list , position)
                }
            }
        }

        imgAdapter.setOnItemLongClickListener { adapter, view, position ->
            if (position < imgAdapter.data.size - 1) {
                currentPosition = position
                deleteDialog.show()
                true
            }else{
                false
            }
        }

        userId = getUid()
    }

    abstract fun commit(content: String, urls: String = "")
    abstract fun getUid(): String

    inner class MyAdapter(list: ArrayList<String>): BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_imageview, list){
        override fun convert(helper: BaseViewHolder, item: String) {
            val position = helper.adapterPosition
            val imageView = helper.getView<ImageView>(R.id.imageView)
            if (position == data.size - 1){
                imageView.setImageResource(R.drawable.add_pic)
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
                    selectList.forEach { imgAdapter.addData(imgAdapter.itemCount - 1, it.path)}
                }
            }
        }
    }

    private val myHandler by lazy { MyHandler(this) }
    class MyHandler(activity: BPublishContentActivity): HandlerUtil<BPublishContentActivity>(activity){
        override fun handleMessage(activity: BPublishContentActivity, msg: Message) {
            when(msg.what){
                Constants.UPLOAD_SUCCESS ->{
                    activity.uploadPosition ++
                    if (activity.uploadPosition < activity.imgAdapter.itemCount - 1) {
                        val fileName = System.currentTimeMillis().toString()
                        val url = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/${activity.userId}/$fileName"
                        OSSUploadUtils.getInstance().UploadPhotoToOSS(
                                activity,
                                File(activity.imgAdapter.getItem(activity.uploadPosition)),
                                "personal/${activity.userId}",
                                fileName,
                                activity.myHandler)
                        activity.sb.append("$url,")
                    }else{
                        activity.commit(
                                activity.contentEdit.text.toString(),
                                activity.sb.substring(0, activity.sb.lastIndexOf(",")))
                        PictureFileUtils.deleteCacheDirFile(activity)
                    }
                }
                Constants.UPLOAD_FAIL ->{
                    activity.sb = StringBuilder()
                    LoadingDialogUtil.closeLoadingDialog()
                    T.show(activity, "上传图片失败，请稍后重试")
                }
            }
        }
    }
}