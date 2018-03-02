package com.yzy.ebag.teacher.ui.activity

import android.content.Intent
import android.util.Log
import android.view.View
import com.umeng.socialize.utils.Log.toast
import com.yzy.ebag.teacher.MainActivity
import com.yzy.ebag.teacher.R
import ebag.core.base.App
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.SerializableUtils
import ebag.core.util.StringUtils
import ebag.hd.bean.response.UserEntity
import ebag.hd.http.EBagApi
import ebag.hd.ui.activity.account.BLoginActivity
import kotlinx.android.synthetic.main.activity_binding.*

class BindingActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_binding;
    }

    override fun initViews() {
        var name = intent.getStringExtra("name")
        var uid = intent.getStringExtra("uid")
        var thirdPartyToken = intent.getStringExtra("access_token")
        var account = et_user.text.toString()
        var pwd = et_user.text.toString()
        if (name == "b") {
            btn_binding.setOnClickListener {
                Log.d("aa","$thirdPartyToken,$uid,$name,")
                if (
//                isLoginInfoCorrect(account, pwd)
                    true
                ) {
                    EBagApi.login("1000857", "ysb123456", 1, BLoginActivity.TEACHER_ROLE, thirdPartyToken, uid, object : RequestCallBack<UserEntity>() {
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
                            Log.d("wy", "错误")
                        }
                    })
                }
            }

        }
        if (name == "x") {
            et_user.visibility = View.GONE
        }
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

}
