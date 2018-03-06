package com.yzy.ebag.teacher.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Message
import android.view.View
import android.widget.TextView
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.tools.PictureFileUtils
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.util.*
import ebag.hd.base.Constants
import ebag.hd.bean.response.UserEntity
import ebag.hd.widget.startSelectPicture
import kotlinx.android.synthetic.main.activity_personal_info.*
import java.io.File

class PersonalInfoActivity : BaseActivity(), View.OnClickListener{
    override fun getLayoutId(): Int {
        return R.layout.activity_personal_info
    }
    private var userEntity: UserEntity? = null
    private var uploadHeadUrl = ""
    /**
     * 0: 修改头像 1：修改姓名 2：修改性别 3：修改家庭住址
     */
    private var modifyType = 0
    private val modifyRequest by lazy {
        object : RequestCallBack<String>(){
            override fun onStart() {

            }

            override fun onSuccess(entity: String?) {
                LoadingDialogUtil.closeLoadingDialog()
                when(modifyType){
                    0 ->{
                        headImage.loadHead(uploadHeadUrl)
                        userEntity?.headUrl = uploadHeadUrl
                    }
                    1 ->{

                    }
                    2 ->{

                    }
                    3 ->{

                    }
                }
                SerializableUtils.setSerializable(Constants.TEACHER_USER_ENTITY, userEntity)
            }

            override fun onError(exception: Throwable) {
                LoadingDialogUtil.closeLoadingDialog()
                T.show(this@PersonalInfoActivity, "请求失败，请稍后重试")
            }
        }
    }
    override fun initViews() {
        headImageBtn.setOnClickListener(this)
        nameBtn.setOnClickListener(this)
        bagBtn.setOnClickListener(this)
        sexBtn.setOnClickListener(this)
        contactBtn.setOnClickListener(this)
        addressBtn.setOnClickListener(this)
        schoolBtn.setOnClickListener(this)

        userEntity = SerializableUtils.getSerializable<UserEntity>(Constants.TEACHER_USER_ENTITY)
        if (userEntity != null){
            headImage.loadHead(userEntity?.headUrl)
            setTv(name, userEntity?.name)
            setTv(bag, userEntity?.ysbCode)
            setTv(sex, userEntity?.sex)
//            setTv(contactInformation, userEntity?.) //联系方式
            setTv(familyAddress, userEntity?.address)
            setTv(schoolName, userEntity?.schoolName)
        }
    }

    private fun setTv(textView: TextView, string: String?){
        if (!StringUtils.isEmpty(string)){
            textView.text = string
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.headImageBtn ->{
                startSelectPicture(1, true, true, false)
            }
            R.id.nameBtn ->{
                T.show(this, "姓名")
            }
            R.id.bagBtn ->{
                T.show(this, "书包号")
            }
            R.id.sexBtn ->{
                T.show(this, "性别")
            }
            R.id.contactBtn ->{
                T.show(this, "联系方式")
            }
            R.id.addressBtn ->{
                T.show(this, "家庭住址")
            }
            R.id.schoolBtn ->{
                T.show(this, "所在学校")
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
                    val filePath = selectList[0].path
                    uploadHeadUrl = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/headUrl/${userEntity?.uid}"
                    LoadingDialogUtil.showLoading(this, "正在上传...")
                    OSSUploadUtils.getInstance().UploadPhotoToOSS(this, File(filePath), "personal/headUrl", userEntity?.uid, myHandler)
                }
            }
        }
    }
    private val myHandler by lazy { PersonalInfoActivity.MyHandler(this) }
    class MyHandler(activity: PersonalInfoActivity): HandlerUtil<PersonalInfoActivity>(activity){
        override fun handleMessage(activity: PersonalInfoActivity, msg: Message) {
            when(msg.what){
                ebag.core.util.Constants.UPLOAD_SUCCESS ->{
                    TeacherApi.modifyPersonalInfo("headUrl", activity.uploadHeadUrl, activity.modifyRequest)
                    PictureFileUtils.deleteCacheDirFile(activity)
                }
                ebag.core.util.Constants.UPLOAD_FAIL ->{
                    LoadingDialogUtil.closeLoadingDialog()
                    T.show(activity, "上传图片失败，请稍后重试")
                }
            }
        }
    }
}
