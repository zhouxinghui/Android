package com.yzy.ebag.teacher.ui.activity

import android.content.Intent
import android.util.Log
import android.view.View
import com.umeng.socialize.utils.Log.toast
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
import ebag.hd.base.Constants
import ebag.hd.bean.response.UserEntity
import ebag.hd.http.EBagApi
import ebag.hd.ui.activity.account.BLoginActivity
import kotlinx.android.synthetic.main.activity_binding.*
import kotlinx.android.synthetic.main.common_title_bar.*

class BindingActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_binding;
    }

    override fun initViews() {
        var BX = intent.getStringExtra("BX")
        var name=   intent.getStringExtra("name")
        var iconurl=  intent.getStringExtra("iconurl")
        var gender=  intent.getStringExtra("gender")
        var shareMedia=   intent.getStringExtra("shareMedia")
        var accessToken=  intent.getStringExtra("accessToken")
        var uid = intent.getStringExtra("uid")

        var account = et_user.text.toString()
        var pwd = et_user.text.toString()

        Log.d("haha","$BX,$name,$iconurl,$gender,$shareMedia,$accessToken,$uid")
        if (BX == "b") {
            btn_binding.setOnClickListener {
                if (
//                isLoginInfoCorrect(account, pwd)
                    true
                ) {
                    EBagApi.login("1000857", "ysb123456", 1, BLoginActivity.TEACHER_ROLE, accessToken,uid,object : RequestCallBack<UserEntity>() {
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
                            finish()

                        }

                        override fun onError(exception: Throwable) {
                            LoadingDialogUtil.closeLoadingDialog()
                            exception.handleThrowable(this@BindingActivity)

                        }
                    })
                }
            }

        }
        if (BX == "x") {
            et_user.visibility = View.GONE
            btn_binding.setOnClickListener {
                EBagApi.register(name,null,BLoginActivity.TEACHER_ROLE,"2","ysb123456",accessToken,uid,1,object : RequestCallBack<UserEntity>() {
                override fun onSuccess(entity: UserEntity?) {

                    if (entity != null) {
                        LoadingDialogUtil.closeLoadingDialog()
                        App.modifyToken(entity.token)
                        entity.roleCode = BLoginActivity.TEACHER_ROLE
                        SerializableUtils.deleteSerializable(
                                if (BLoginActivity.TEACHER_ROLE == BLoginActivity.STUDENT_ROLE) {
                                    Constants.STUDENT_USER_ENTITY
                                } else {
                                    Constants.TEACHER_USER_ENTITY
                                }
                        )
                        SerializableUtils.setSerializable(
                                if (BLoginActivity.TEACHER_ROLE == BLoginActivity.STUDENT_ROLE) {
                                    Constants.STUDENT_USER_ENTITY
                                } else {
                                    Constants.TEACHER_USER_ENTITY
                                }, entity)
                        startActivity(Intent(this@BindingActivity,MainActivity::class.java))
                        finish()
                    } else {
                        LoadingDialogUtil.closeLoadingDialog()
                        MsgException("1", "无数据返回").handleThrowable(this@BindingActivity)
                    }

                }

                override fun onError(exception: Throwable) {
                    LoadingDialogUtil.closeLoadingDialog()
                    exception.handleThrowable(this@BindingActivity)

                }
            })
            }
        }
        back_text.setOnClickListener { finish() }
    }

    /**判断账号和密码格式是否输入错误*/
    private fun isLoginInfoCorrect(account: String, pwd: String): Boolean =
            if (account.isEmpty() || pwd.isEmpty()) {
                toast(this, "请输入账号密码！")
                false
            } else if (account.length < 7) {
                toast(this, "请输入正确的账号！")
                false
            } else if (account.length == 11 && !StringUtils.isMobileNo(account)) {
                toast(this, "手机格式输入错误！")
                false
            } else if (!StringUtils.isPassword(pwd)) {
                toast(this, "请输入6~20位字母数字混合密码")
                false
            } else {
                true
            }
    fun judge(thirdParty:String):Int {
        if ("QQ".equals(thirdParty, true)) {
            return 4
        } else if ("sina".equals(thirdParty, true)) {
            return 6
        } else {
            return 5
        }
    }
}
