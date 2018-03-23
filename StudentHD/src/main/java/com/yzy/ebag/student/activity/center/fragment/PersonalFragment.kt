package com.yzy.ebag.student.activity.center.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.view.View
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.tools.PictureFileUtils
import com.yzy.ebag.student.R
import com.yzy.ebag.student.activity.account.LoginActivity
import ebag.core.base.BaseFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.util.*
import ebag.hd.base.Constants
import ebag.hd.bean.response.UserEntity
import ebag.hd.http.EBagApi
import ebag.hd.widget.ListBottomShowDialog
import ebag.hd.widget.ModifyInfoDialog
import ebag.hd.widget.startSelectPicture
import kotlinx.android.synthetic.main.fragment_center_personal.*
import java.io.File

/**
 * Created by caoyu on 2018/1/13.
 */
class PersonalFragment: BaseFragment(), View.OnClickListener {
    companion object {
        fun newInstance() : PersonalFragment {
            val fragment = PersonalFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun getLayoutRes(): Int {
        return R.layout.fragment_center_personal
    }

    override fun getBundle(bundle: Bundle?) {
    }
    var onHeadOrNameChange : ((headUrl: String?, name: String?) -> Unit)? = null
    private var userEntity: UserEntity? = null
    private var uploadHeadUrl = ""
    /**
     * 0: 修改头像 1：修改姓名 2：修改性别 3：修改家庭住址
     */
    private var modifyType = 0
    private var key = "headUrl"
    private var modifyStr = ""
    private val modifyDialog by lazy {
        val dialog = ModifyInfoDialog(mContext)
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
        val dialog = object : ListBottomShowDialog<SexBean>(mContext, sexList){
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
        object : RequestCallBack<String>(){
            override fun onStart() {
                LoadingDialogUtil.showLoading(mContext,"正在上传...")
            }

            override fun onSuccess(entity: String?) {
                LoadingDialogUtil.closeLoadingDialog()
                when(modifyType){
                    0 ->{
                        ivAvatar.loadHead(uploadHeadUrl, true, System.currentTimeMillis().toString())
                        userEntity?.headUrl = uploadHeadUrl
                        onHeadOrNameChange?.invoke(uploadHeadUrl, null)
                    }
                    1 ->{
                        tvName.text = modifyStr
                        userEntity?.name = modifyStr
                        onHeadOrNameChange?.invoke(null, modifyStr)
                    }
                    2 ->{
                        tvGender.text = modifyStr
                        userEntity?.sex = if (modifyStr == "男") "1" else "2"
                    }
                    3 ->{
                        tvAddress.text = modifyStr
                        userEntity?.address = modifyStr
                    }
                }
                SerializableUtils.setSerializable(Constants.STUDENT_USER_ENTITY, userEntity)
            }

            override fun onError(exception: Throwable) {
                LoadingDialogUtil.closeLoadingDialog()
                T.show(mContext, "请求失败，请稍后重试")
            }
        }
    }

    override fun initViews(rootView: View) {
//        showContent()
        ivAvatar.setOnClickListener(this)
        tvName.setOnClickListener(this)
        tvGender.setOnClickListener(this)
        tvAddress.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        showContent()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden){
            showContent()
        }
    }

    private fun showContent(){
        userEntity = SerializableUtils.getSerializable<UserEntity>(Constants.STUDENT_USER_ENTITY)
        uploadHeadUrl = "${ebag.core.util.Constants.OSS_BASE_URL}/personal/headUrl/${userEntity?.uid}"
        if(userEntity != null){
            tvName.text = userEntity?.name
            tvId.text = userEntity?.ysbCode
            ivAvatar.loadHead(userEntity?.headUrl, true, System.currentTimeMillis().toString())
            tvGender.text = when(userEntity?.sex){
                                "1" -> "男  "
                                "2" -> "女  "
                                else -> ""
                            }
//            tvContact.text = userEntity.
            tvAddress.text = userEntity?.address
            tvSchool.text = userEntity?.schoolName
//            tvClass.text = userEntity.
        }else{
            startActivity(
                    Intent(mContext, LoginActivity::class.java)
                            .putExtra(Constants.KEY_TO_MAIN,true)
            )
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivAvatar ->{
                modifyType = 0
                key = "headUrl"
                startSelectPicture(true, true, 1, false)
            }
            R.id.tvName ->{
                modifyType = 1
                key = "name"
                modifyDialog.show("请输入姓名")
            }
            R.id.tvGender ->{
                modifyType = 2
                key = "sex"
                sexDialog.show()
            }
            R.id.tvAddress ->{
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
                    LoadingDialogUtil.showLoading(mContext, "正在上传...")
                    OSSUploadUtils.getInstance().UploadPhotoToOSS(mContext, File(filePath), "personal/headUrl", "${userEntity?.uid}", myHandler)
                }
            }
        }
    }
    private val myHandler by lazy { MyHandler(this) }
    class MyHandler(fragment: PersonalFragment): HandlerUtil<PersonalFragment>(fragment){
        override fun handleMessage(fragment: PersonalFragment, msg: Message) {
            when(msg.what){
                ebag.core.util.Constants.UPLOAD_SUCCESS ->{
                    EBagApi.modifyPersonalInfo(fragment.key, fragment.uploadHeadUrl, fragment.modifyRequest)
                    PictureFileUtils.deleteCacheDirFile(fragment.mContext)
                }
                ebag.core.util.Constants.UPLOAD_FAIL ->{
                    LoadingDialogUtil.closeLoadingDialog()
                    T.show(fragment.mContext, "上传图片失败，请稍后重试")
                }
            }
        }
    }

    inner class SexBean {
        var sex = "1"
        var sexStr = "男"
    }
}