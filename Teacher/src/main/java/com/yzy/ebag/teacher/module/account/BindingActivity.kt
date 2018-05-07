package com.yzy.ebag.teacher.module.account

import android.content.Intent
import android.view.View
import com.umeng.socialize.bean.SHARE_MEDIA
import com.yzy.ebag.teacher.MainActivity
import com.yzy.ebag.teacher.R
import ebag.core.base.App
import ebag.core.base.BaseActivity
import ebag.core.http.network.MsgException
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.SerializableUtils
import ebag.core.util.StringUtils
import ebag.mobile.base.Constants
import ebag.mobile.bean.UserEntity
import ebag.mobile.http.EBagApi
import ebag.mobile.module.account.BLoginActivity
import kotlinx.android.synthetic.main.activity_binding.*

class BindingActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_binding
    }

    override fun initViews() {
        var BX = intent.getStringExtra("BX")
        var name = intent.getStringExtra("name")
        var iconurl = intent.getStringExtra("iconurl")
        var gender = intent.getStringExtra("gender")
        var shareMedia = intent.getStringExtra("shareMedia")
        var accessToken = intent.getStringExtra("accessToken")
        var uid = intent.getStringExtra("uid")

        titleBar.setTitle(intent.getStringExtra("titleText") ?: "")

        judgeImage(shareMedia)

        if (BX == "b") {
            btn_binding.setOnClickListener {
                var type: String
                if (StringUtils.isMobileNo(et_user.text.toString())) {
                    type = BLoginActivity.PHONE_TYPE
                } else {
                    type = BLoginActivity.EBAG_TYPE
                }

                EBagApi.login(et_user.text.toString(), et_pwd.text.toString(), type, judge(shareMedia), BLoginActivity.TEACHER_ROLE, accessToken, uid, object : RequestCallBack<UserEntity>() {
                    override fun onSuccess(entity: UserEntity?) {
                        LoadingDialogUtil.closeLoadingDialog()
                        if (entity != null) {
                            App.modifyToken(entity.token)
                        }
                        if (entity != null) {
                            entity.roleCode = "teacher"
                        }
                        SerializableUtils.deleteSerializable("teacher")
                        SerializableUtils.setSerializable("teacher", entity)
//                        setResult(Constants.CODE_LOGIN_RESULT)
                        startActivity(Intent(this@BindingActivity, MainActivity::class.java))
                        BLoginActivity.mActivity!!.finish()
                        LoginSelectActivity.mActivity!!.finish()
                        finish()
                    }

                    override fun onError(exception: Throwable) {
                        myException(exception, false)
                    }
                })
            }

        }
        if (BX == "x") {
            et_user.visibility = View.GONE
            btn_binding.setOnClickListener {
                var type: String
                if (StringUtils.isMobileNo(et_user.text.toString())) {
                    type = BLoginActivity.PHONE_TYPE
                } else {
                    type = BLoginActivity.EBAG_TYPE
                }
                EBagApi.register(name, iconurl, if (gender == "男") {
                    "1"
                } else {
                    "2"
                }, null, null, BLoginActivity.TEACHER_ROLE, et_pwd.text.toString(), accessToken, uid, type, judge(shareMedia), object : RequestCallBack<UserEntity>() {
                    override fun onSuccess(entity: UserEntity?) {

                        if (entity != null) {
                            LoadingDialogUtil.closeLoadingDialog()
                            App.modifyToken(entity.token)
                            entity.roleCode = BLoginActivity.TEACHER_ROLE
                            SerializableUtils.deleteSerializable(Constants.TEACHER_USER_ENTITY)
                            SerializableUtils.setSerializable(Constants.TEACHER_USER_ENTITY, entity)
                            startActivity(Intent(this@BindingActivity, MainActivity::class.java))
                            finish()
                        } else {
                            LoadingDialogUtil.closeLoadingDialog()
                            MsgException("1", "无数据返回").handleThrowable(this@BindingActivity)
                        }
                        BLoginActivity.mActivity!!.finish()
                        LoginSelectActivity.mActivity!!.finish()
                        finish()
                    }

                    override fun onError(exception: Throwable) {
                        myException(exception, true)
                    }

                })
            }
        }
    }

    private fun myException(exception: Throwable, boolean: Boolean) {
        LoadingDialogUtil.closeLoadingDialog()
        exception.handleThrowable(this@BindingActivity)
    }

    fun judge(thirdParty: String): String {
        if ("QQ".equals(thirdParty, true)) {
            return "4"
        } else if ("sina".equals(thirdParty, true)) {
            return "6"
        } else {
            return "5"
        }
    }

    fun judgeImage(threeparty: String): SHARE_MEDIA {
        if ("QQ".equals(threeparty, true)) {
            iv_thirdly.setImageResource(R.drawable.icon_third_party_login_qq)
            return SHARE_MEDIA.QQ
        } else if ("sina".equals(threeparty, true)) {
            iv_thirdly.setImageResource(R.drawable.icon_third_party_login_micro_blog)
            return SHARE_MEDIA.SINA
        } else {
            iv_thirdly.setImageResource(R.drawable.icon_third_party_login_weinxin)
            return SHARE_MEDIA.WEIXIN
        }
    }
}
