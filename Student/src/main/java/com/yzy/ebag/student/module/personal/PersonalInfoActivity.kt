package com.yzy.ebag.student.module.personal

import android.app.Activity
import android.content.Intent
import android.os.Message
import android.view.View
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.tools.PictureFileUtils
import com.yzy.ebag.student.R
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.*
import ebag.mobile.base.Constants
import ebag.mobile.bean.UserEntity
import ebag.mobile.bean.UserInfoBean
import ebag.mobile.http.EBagApi
import ebag.mobile.module.personal.ModifyInfoDialog
import ebag.mobile.widget.ListBottomShowDialog
import kotlinx.android.synthetic.main.activity_personal_center.*
import java.io.File

/**
 * Created by YZY on 2018/5/14.
 */
class PersonalInfoActivity: BaseActivity(), View.OnClickListener {
    override fun getLayoutId(): Int {
        return R.layout.activity_personal_center
    }
    private var userEntity: UserEntity? = null
    private var uploadHeadUrl = ""
    /**
     * 0: 修改头像 1：修改姓名 2：修改性别 3：修改家庭住址
     */
    private var modifyType = 0
    private var key = "headUrl"
    private var modifyStr = ""
    private val modifyDialog by lazy {
        val dialog = ModifyInfoDialog(this)
        dialog.onConfirmClickListener = {
            modifyStr = it
            EBagApi.modifyPersonalInfo(key, it, modifyRequest)
            dialog.dismiss()
        }
        dialog
    }
    private val sexDialog by lazy {
        val sexList = ArrayList<SexBean>()
        val bean1 = SexBean()
        bean1.sex = "1"
        bean1.sexStr = "男"
        sexList.add(bean1)
        val bean2 = SexBean()
        bean2.sex = "2"
        bean2.sexStr = "女"
        sexList.add(bean2)
        val dialog = object : ListBottomShowDialog<SexBean>(this, sexList) {
            override fun setText(data: SexBean?): String {
                return data!!.sexStr
            }
        }
        dialog.setOnDialogItemClickListener { _, data, position ->
            modifyStr = data.sexStr
            EBagApi.modifyPersonalInfo(key, data.sex, modifyRequest)
            dialog.dismiss()
        }
        dialog
    }
    private val modifyRequest by lazy {
        object : RequestCallBack<String>() {
            override fun onStart() {
                LoadingDialogUtil.showLoading(this@PersonalInfoActivity, "正在上传...")
            }

            override fun onSuccess(entity: String?) {
                LoadingDialogUtil.closeLoadingDialog()
                when (modifyType) {
                    0 -> {
                        ivAvatar.loadHead(uploadHeadUrl)
                        userEntity?.headUrl = uploadHeadUrl
                    }
                    1 -> {
                        tvName.text = modifyStr
                        userEntity?.name = modifyStr
                    }
                    2 -> {
                        tvGender.text = modifyStr
                        userEntity?.sex = if (modifyStr == "男") "1" else "2"
                    }
                    3 -> {
                        tvAddress.text = modifyStr
                        userEntity?.address = modifyStr
                    }
                }
                SerializableUtils.setSerializable(Constants.STUDENT_USER_ENTITY, userEntity)
            }

            override fun onError(exception: Throwable) {
                LoadingDialogUtil.closeLoadingDialog()
                exception.handleThrowable(this@PersonalInfoActivity)
            }
        }
    }
    private var currentTime = 0L
    override fun initViews() {
        ivAvatar.setOnClickListener(this)
        tvName.setOnClickListener(this)
        tvGender.setOnClickListener(this)
        tvAddress.setOnClickListener(this)
        currentTime = System.currentTimeMillis()
        showContent()
    }

    private fun showContent() {
        userEntity = SerializableUtils.getSerializable<UserEntity>(Constants.STUDENT_USER_ENTITY)
        EBagApi.queryUserInfo(object : RequestCallBack<UserInfoBean>() {
            override fun onStart() {
                super.onStart()
                LoadingDialogUtil.showLoading(this@PersonalInfoActivity, "正在加载..")
            }

            override fun onSuccess(entity: UserInfoBean?) {
                LoadingDialogUtil.closeLoadingDialog()
                userEntity?.name = entity?.name
                userEntity?.uid = entity?.uid
                userEntity?.ysbCode = entity?.ysbCode
                userEntity?.headUrl = entity?.headUrl
                userEntity?.sex = entity?.sex
                userEntity?.address = entity?.address
                userEntity?.schoolName = entity?.schoolName
                userEntity?.className = entity?.className

                tvName.text = entity?.name
                tvId.text = entity?.ysbCode
                tvContact.text = entity?.phone ?: ""
                ivAvatar.loadHead(entity?.headUrl)
                tvGender.text = when (entity?.sex) {
                    "1" -> "男  "
                    "2" -> "女  "
                    else -> ""
                }

                tvAddress.text = entity?.address
                tvSchool.text = entity?.schoolName
                tvClass.text = entity?.className
                uploadHeadUrl = "${ebag.core.util.Constants.OSS_BASE_URL}/personal/headUrl/${userEntity?.uid}$currentTime"

            }

            override fun onError(exception: Throwable) {
                LoadingDialogUtil.closeLoadingDialog()
            }

        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivAvatar -> {
                modifyType = 0
                key = "headUrl"
                startSelectPicture(1, true, true, false)
            }
            R.id.tvName -> {
                modifyType = 1
                key = "name"
                modifyDialog.show("请输入姓名")
            }
            R.id.tvGender -> {
                modifyType = 2
                key = "sex"
                sexDialog.show()
            }
            R.id.tvAddress -> {
                modifyType = 3
                key = "address"
                modifyDialog.show("请输入地址")
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
//                    headImage.loadHead(filePath)
                    LoadingDialogUtil.showLoading(this, "正在上传...")
                    OSSUploadUtils.getInstance().UploadPhotoToOSS(this, File(filePath), "personal/headUrl", "${userEntity?.uid}$currentTime", myHandler)
                }
            }
        }
    }

    private val myHandler by lazy { MyHandler(this) }

    class MyHandler(activity: PersonalInfoActivity) : HandlerUtil<PersonalInfoActivity>(activity) {
        override fun handleMessage(activity: PersonalInfoActivity, msg: Message) {
            when (msg.what) {
                ebag.core.util.Constants.UPLOAD_SUCCESS -> {
                    EBagApi.modifyPersonalInfo(activity.key, activity.uploadHeadUrl, activity.modifyRequest)
                    PictureFileUtils.deleteCacheDirFile(activity)
                }
                ebag.core.util.Constants.UPLOAD_FAIL -> {
                    LoadingDialogUtil.closeLoadingDialog()
                    T.show(activity, "上传图片失败，请稍后重试")
                }
            }
        }
    }

    inner class SexBean {
        var sex = "1"
        var sexStr = "男"
    }
}