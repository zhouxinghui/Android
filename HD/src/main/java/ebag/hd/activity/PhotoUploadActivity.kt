package ebag.hd.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import ebag.core.base.BaseActivity
import ebag.core.util.*
import ebag.hd.R
import ebag.hd.widget.startSelectPicture
import kotlinx.android.synthetic.main.activity_photo_upload.*

/**
 * Created by unicho on 2018/3/3.
 */
class PhotoUploadActivity: BaseActivity() {

    companion object {
        fun jump(context: Context, classId: String, photoGroupId: String){
            context.startActivity(
                    Intent(context, PhotoUploadActivity::class.java)
                            .putExtra("classId", classId)
                            .putExtra("photoGroupId", photoGroupId)
            )
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_photo_upload
    }

    lateinit var photoGroupId: String
    lateinit var classId: String
    private val imgList = ArrayList<String>()
    private val urlList by lazy { ArrayList<String>() }
    private val imgAdapter by lazy { Adapter() }
    private var uploadPosition = 0
    private val photoUrls = StringBuilder()
    override fun initViews() {

        classId = intent.getStringExtra("classId") ?: ""
        photoGroupId = intent.getStringExtra("photoGroupId") ?: ""

        titleView.setRightText("确定"){
            if(StringUtils.isEmpty(editView.text.toString())){
                T.show(this, "请输入一些内容")
                return@setRightText
            }



        }

        switchView.setOnCheckedChangeListener { buttonView, isChecked ->

        }

        editView.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable) {
                tipView.text = "${s.length}/100"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = imgAdapter

        imgAdapter.setOnItemClickListener { adapter, _, position ->
            if (adapter.data.size == 10){
                T.show(this, "图片选择上限为：9张")
                return@setOnItemClickListener
            }
            if (position == adapter.data.size - 1){
                startSelectPicture( 10 - imgAdapter.itemCount)
            }
        }
        imgList.add("")
        imgAdapter.setNewData(imgList)
    }

    inner class Adapter: BaseQuickAdapter<String, BaseViewHolder>(ebag.hd.R.layout.imageview){
        override fun convert(helper: BaseViewHolder, item: String) {
            val position = helper.adapterPosition
            val imageView = helper.getView<ImageView>(ebag.hd.R.id.imageView)

            // 添加日记 并且是最后一张图片
            if (position == data.size - 1){
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
                    selectList.forEach { urlList.add(it.path) }
                    imgAdapter.addData(imgAdapter.itemCount - 1, urlList)
                    urlList.clear()
                }
            }
        }
    }

    private val myHandler by lazy { MyHandler(this) }
    class MyHandler(activity: PhotoUploadActivity): HandlerUtil<PhotoUploadActivity>(activity){
        override fun handleMessage(activity: PhotoUploadActivity, msg: Message) {
            when(msg.what){
                Constants.UPLOAD_SUCCESS ->{
                    activity.uploadPosition ++
                    if (activity.uploadPosition < activity.urlList.size) {
                        val fileName = System.currentTimeMillis().toString()
//                        val url = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/photo/${activity.userId}/$fileName"
//                        OSSUploadUtils.getInstance().UploadPhotoToOSS(
//                                activity,
//                                File(activity.urlList[activity.uploadPosition]),
//                                "personal/${activity.userId}",
//                                fileName,
//                                activity.myHandler)
//                        activity.photoUrls.append("$url,")
                    }else{
//                        activity.commit(
//                                activity.titleEdit.text.toString(),
//                                activity.contentEdit.text.toString(),
//                                activity.sb.substring(0, activity.sb.lastIndexOf(","))
//                        )
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