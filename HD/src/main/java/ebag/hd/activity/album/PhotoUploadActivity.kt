package ebag.hd.activity.album

import android.app.Activity
import android.content.Intent
import android.os.Message
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.tools.PictureFileUtils
import ebag.core.base.BaseActivity
import ebag.core.base.PhotoPreviewActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.*
import ebag.hd.R
import ebag.hd.bean.PhotoUploadBean
import ebag.hd.bean.response.UserEntity
import ebag.hd.http.EBagApi
import ebag.hd.widget.startSelectPicture
import kotlinx.android.synthetic.main.activity_photo_upload.*
import java.io.File

/**
 * Created by unicho on 2018/3/3.
 */
class PhotoUploadActivity: BaseActivity() {

    companion object {
        fun jump(context: Activity, classId: String, photoGroupId: String, groupType: String){
            context.startActivityForResult(
                    Intent(context, PhotoUploadActivity::class.java)
                            .putExtra("classId", classId)
                            .putExtra("photoGroupId", photoGroupId)
                            .putExtra("groupType", groupType)
                    ,ebag.hd.base.Constants.UPLOAD_REQUEST
            )
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_photo_upload
    }

    private val imgList = ArrayList<String>()
    private val imgAdapter by lazy { Adapter() }
    private var uploadPosition = 0
    private val photoUploadBean = PhotoUploadBean()
    private lateinit var userId: String
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
        if(intent.getStringExtra("groupType") == ebag.hd.base.Constants.CLASS_TYPE){
            photoUploadBean.isShare = "true"
            bottomView.visibility = View.INVISIBLE
            shareTip.visibility = View.INVISIBLE
            switchView.visibility = View.INVISIBLE
        }
        photoUploadBean.classId = intent.getStringExtra("classId") ?: ""
        photoUploadBean.photoGroupId = intent.getStringExtra("photoGroupId") ?: ""

        val userEntity = SerializableUtils.getSerializable<UserEntity>(ebag.hd.base.Constants.STUDENT_USER_ENTITY)
        userId = userEntity?.uid ?: "1"

        titleView.setRightText("确定"){
            if(StringUtils.isEmpty(editView.text.toString())){
                T.show(this, "请输入一些内容")
                return@setRightText
            }
            photoUploadBean.comment = editView.text.toString()
            if(imgAdapter.itemCount == 1){
                T.show(this, "请选择一些照片")
                return@setRightText
            }
            LoadingDialogUtil.showLoading(this, "正在上传...")

            if(photoUploadBean.photoUrls.isEmpty()){// 照片没上传到过阿里云
                val fileName = System.currentTimeMillis().toString()
                val url = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/photo/$userId/$fileName"
                OSSUploadUtils.getInstance().UploadPhotoToOSS(this, File(imgAdapter.getItem(0)), "photo/$userId", fileName, myHandler)
                photoUploadBean.addPhoto(url)
            }else{// 照片上传过阿里云， 但是上传到自己服务器时失败了
                upload()
            }
        }

        switchView.setOnCheckedChangeListener { buttonView, isChecked ->
            photoUploadBean.isShare = isChecked.toString()
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
            adapter as Adapter
            when {
                // 当照片有9 张时 点击加号
                adapter.data.size == 10 -> T.show(this, "图片选择上限为：9张")

                // 不到9张时  点击加号
                position == adapter.data.size - 1 -> startSelectPicture( 10 - imgAdapter.itemCount)

                // 预览照片
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
        imgList.add("")
        imgAdapter.setNewData(imgList)
    }

    private var uploadRequest: RequestCallBack<String>? = null

    private fun upload(){
        if(uploadRequest == null)
            uploadRequest = object :RequestCallBack<String>(){
                override fun onSuccess(entity: String?) {
                    LoadingDialogUtil.closeLoadingDialog()
                    T.show(this@PhotoUploadActivity, "图片上传成功")
                    setResult(ebag.hd.base.Constants.UPLOAD_RESULT)
                    finish()
                }

                override fun onError(exception: Throwable) {
                    LoadingDialogUtil.closeLoadingDialog()
                    exception.handleThrowable(this@PhotoUploadActivity)
                }
            }

        EBagApi.photosUpload(photoUploadBean, uploadRequest!!)
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
                    selectList.forEach { imgAdapter.addData(imgAdapter.itemCount - 1, it.path) }
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
                    if (activity.uploadPosition < activity.imgAdapter.itemCount - 1) {
                        val fileName = System.currentTimeMillis().toString()
                        OSSUploadUtils.getInstance().UploadPhotoToOSS(
                                activity,
                                File(activity.imgAdapter.getItem(activity.uploadPosition)),
                                "photo/${activity.userId}",
                                fileName,
                                activity.myHandler)
                        val url = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/photo/${activity.userId}/$fileName"
                        activity.photoUploadBean.addPhoto(url)
                    }else{
                        activity.upload()
                        PictureFileUtils.deleteCacheDirFile(activity)//上传完毕之后删除本地缓存
                    }
                }
                Constants.UPLOAD_FAIL ->{
                    activity.photoUploadBean.clear()
                    LoadingDialogUtil.closeLoadingDialog()
                    T.show(activity, "上传图片失败，请稍后重试")
                }
            }
        }
    }

}