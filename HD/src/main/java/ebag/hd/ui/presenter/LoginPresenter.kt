package ebag.hd.ui.presenter

import android.content.Context
import android.view.View
import ebag.core.base.App
import ebag.core.base.mvp.BasePresenter
import ebag.core.base.mvp.OnToastListener
import ebag.core.http.network.MsgException
import ebag.core.http.network.RequestCallBack
import ebag.core.util.L
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.SPUtils
import ebag.core.util.StringUtils
import ebag.hd.bean.response.UserEntity
import ebag.hd.http.EBagApi
import ebag.hd.ui.activity.account.BLoginActivity
import ebag.hd.ui.view.LoginView
import ebag.hd.widget.ModifyInfoDialog
import kotlinx.android.synthetic.main.dialog_modify_info.*


/**
 * presenter 登录页面的控制类
 * Created by caoyu on 2017/11/2.
 */
open class LoginPresenter(view: LoginView, listener: OnToastListener) : BasePresenter<LoginView>(view, listener) {
    private lateinit var account: String
    private lateinit var pwd: String
    private lateinit var roleCode: String
    /**判断账号和密码格式是否输入错误*/
    private fun isLoginInfoCorrect(account: String, pwd: String): Boolean =
            if (account.isEmpty() || pwd.isEmpty()) {
                showToast("请输入账号密码！", true)
                false
            } else if (account.length < 7) {
                showToast("请输入正确的账号！", true)
                false
            } else if (account.length == 11 && !StringUtils.isMobileNo(account)) {
                showToast("手机格式输入错误！", true)
                false
            } else if (!StringUtils.isPassword(pwd)) {
                showToast("请输入6~20位字母数字混合密码", true)
                false
            } else {
                true
            }

    private val loginRequest by lazy {
        createRequest(object : RequestCallBack<UserEntity>() {
            override fun onStart() {
                getView()?.onLoginStart()
            }

            override fun onSuccess(entity: UserEntity?) {
                if (entity != null) {
                    getView()?.onLoginSuccess(entity)
                    L.e(entity.token)
                } else {
                    getView()?.onLoginError(MsgException("1", "无数据返回，请稍后重试"))
                }
            }

            override fun onError(exception: Throwable) {
                LoadingDialogUtil.closeLoadingDialog()
//                        1002代表平板学生平板未激活
//                        1003 当移动端点击第三方登录时如果没有绑定第三方返回此状态码
//                        1004代表老师平板或者pc未激活
                if (exception is MsgException) {
                    when {
                        exception.code == "1002" -> {
                            var modifyStr = ""
                            val dialog = ModifyInfoDialog(getView() as Context)
                            val modifyDialog by lazy {
                                dialog.onConfirmClickListener = {
                                    dialog.dismiss()
                                    modifyStr = it
                                    EBagApi.bindingActivationCode(account, modifyStr, object : RequestCallBack<String>() {
                                        override fun onSuccess(entity: String?) {
                                            login(account, pwd, roleCode)
                                        }

                                        override fun onError(exception: Throwable) {
                                            showToast(exception.message.toString(), true)
                                        }
                                    })

                                }
                                dialog
                            }
                            modifyDialog.show("请输入激活码")
                            dialog.customerervice.visibility = View.VISIBLE
                            dialog.customerervice.text = exception.message.toString()
                        }
                        exception.code == "1004" -> {
                            val dialog = ModifyInfoDialog(getView() as Context)
                            val modifyDialog by lazy {
                                dialog.onConfirmClickListener = {
                                    dialog.dismiss()
                                }
                                dialog
                            }
                            modifyDialog.show()
                            dialog.customerervice.visibility = View.GONE
                            dialog.countEdit.visibility = View.GONE
                            dialog.countEdit.setText("0")
                            dialog.textViewContent.visibility = View.VISIBLE
                            dialog.textViewContent.text = exception.message.toString()
                        }
                        exception.code == "1003" ->
                            showToast(exception.message.toString(), true)
                        else ->
                            getView()?.onLoginError(exception)
                    }
                } else {
                    getView()?.onLoginError(exception)

                }

            }
        })
    }
    private var registerRequest: RequestCallBack<UserEntity>? = null
    /**
     * 登录
     */
    fun login(account: String, pwd: String, roleCode: String) {
        this.account = account
        this.pwd = pwd
        this.roleCode = roleCode
        var context: Context? = null
        if (getView() is Context)
            context = getView() as Context
        if (StringUtils.isMobileNo(account)) {
            EBagApi.login(SPUtils.get(App.mContext, ebag.core.util.Constants.IMEI, "") as String, BLoginActivity.ISHD, account, pwd, BLoginActivity.PHONE_TYPE, null, roleCode, null, null, loginRequest!!)
            if (context != null)
                SPUtils.put(context, ebag.hd.base.Constants.LOGIN_TYPE, BLoginActivity.PHONE_TYPE)
        } else {
            EBagApi.login(SPUtils.get(App.mContext, ebag.core.util.Constants.IMEI, "") as String, BLoginActivity.ISHD, account, pwd, BLoginActivity.EBAG_TYPE, null, roleCode, null, null, loginRequest!!)
            if (context != null)
                SPUtils.put(context, ebag.hd.base.Constants.LOGIN_TYPE, BLoginActivity.EBAG_TYPE)
        }
        if (context != null) {
            SPUtils.put(context, ebag.hd.base.Constants.USER_ACCOUNT, account)
            SPUtils.put(context, ebag.hd.base.Constants.USER_PASS_WORD, pwd)
            SPUtils.put(context, ebag.hd.base.Constants.ROLE_CODE, roleCode)
            SPUtils.put(context, ebag.hd.base.Constants.THIRD_PARTY_TYPE, "")
        }
    }


    /**
     * 判断注册信息是否填写正确
     */
    private fun isRegisterInfoCorrect(phone: String, code: String, pwd: String): Boolean =
            when {
                !StringUtils.isMobileNo(phone) -> {
                    showToast("手机号输入格式不正确", true)
                    false
                }
                code.length != 6 -> {
                    showToast("验证码长度不正确", true)
                    false
                }
                !StringUtils.isPassword(pwd) -> {
                    showToast("请输入6~20位字母数字混合密码", true)
                    false
                }
                else -> true
            }


    /**
     * 注册
     */
    fun register(name: String, phone: String, code: String, pwd: String, roleCode: String, thirdPartyToken: String?, thirdPartyUnionid: String?) {
        if (isRegisterInfoCorrect(phone, code, pwd)) {
            if (registerRequest == null)
                registerRequest = createRequest(object : RequestCallBack<UserEntity>() {
//                    override fun onStart() {
//                        getView()?.onRegisterStart()
//                    }

                    override fun onSuccess(entity: UserEntity?) {
                        getView()?.onRegisterSuccess(entity)
                    }

                    override fun onError(exception: Throwable) {
                        if (exception is MsgException) {
                            when {
                                exception.code == "1002" -> {
                                    showToast("注册成功", true)
                                    var modifyStr = ""
                                    val dialog = ModifyInfoDialog(getView() as Context)
                                    val modifyDialog by lazy {
                                        dialog.onConfirmClickListener = {
                                            dialog.dismiss()
                                            modifyStr = it
                                            EBagApi.bindingActivationCode(account, modifyStr, object : RequestCallBack<String>() {
                                                override fun onSuccess(entity: String?) {
                                                    login(account, pwd, roleCode)
                                                }

                                                override fun onError(exception: Throwable) {
                                                    showToast(exception.message.toString(), true)
                                                }
                                            })

                                        }
                                        dialog
                                    }
                                    modifyDialog.show("请输入激活码")
                                    dialog.customerervice.visibility = View.VISIBLE
                                    dialog.customerervice.text = exception.message.toString()
                                }
                                exception.code == "1004" -> {
                                    val dialog = ModifyInfoDialog(getView() as Context)
                                    val modifyDialog by lazy {
                                        dialog.onConfirmClickListener = {
                                            dialog.dismiss()
                                        }
                                        dialog
                                    }
                                    modifyDialog.show()
                                    dialog.customerervice.visibility = View.GONE
                                    dialog.countEdit.visibility = View.GONE
                                    dialog.countEdit.setText("0")
                                    dialog.textViewContent.visibility = View.VISIBLE
                                    dialog.textViewContent.text = exception.message.toString()
                                }
                                else -> {
                                    getView()?.onRegisterError(exception)
                                }
                            }
                        } else {
                            getView()?.onRegisterError(exception)
                        }
                    }

                })
            EBagApi.register(BLoginActivity.ISHD, name, null, null, phone, code, roleCode, pwd, thirdPartyToken, thirdPartyUnionid, BLoginActivity.PHONE_TYPE, null, registerRequest!!)
        }

    }
}