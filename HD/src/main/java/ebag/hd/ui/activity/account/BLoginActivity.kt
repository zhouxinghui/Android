package ebag.hd.ui.activity.account

import android.app.Activity
import android.content.Intent
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import com.umeng.socialize.UMAuthListener
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA
import ebag.core.base.App
import ebag.core.base.mvp.MVPActivity
import ebag.core.http.network.MsgException
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.*
import ebag.hd.R
import ebag.hd.activity.ProtocolActivity
import ebag.hd.base.Constants
import ebag.hd.bean.response.UserEntity
import ebag.hd.dialog.MsgDialogFragment
import ebag.hd.http.EBagApi
import ebag.hd.ui.presenter.CodePresenter
import ebag.hd.ui.presenter.LoginPresenter
import ebag.hd.ui.view.CodeView
import ebag.hd.ui.view.LoginView
import kotlinx.android.synthetic.main.activity_login.*


/**
 * Created by caoyu on 2017/11/2.
 * Activity 登录&注册
 */
abstract class BLoginActivity : MVPActivity(), LoginView, CodeView {

    companion object {

        const val STUDENT_ROLE = "1"
        const val TEACHER_ROLE = "2"
        const val PARENT_ROLE = "3"

        const val EBAG_TYPE = "1"
        const val EMAIL_TYPE = "2"
        const val PHONE_TYPE = "3"
        const val QQ_TYPE = "4"
        const val WEIXIN_TYPE = "5"
        const val SIAN_TYPE = "6"

        //        登录标识：hd为平板，phone为手机
        const val ISHD = "HD"
        const val ISPHONE = "PHONE"

        var mActivity: Activity? = null
    }

    private var isToMain = false

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun destroyPresenter() {
        if (lDelegate.isInitialized())
            loginPresenter.onDestroy()
        if (cDelegate.isInitialized())
            codePresenter.onDestroy()
    }

    /**懒加载*/
    private val lDelegate = lazy { LoginPresenter(this, this) }
    private val cDelegate = lazy { CodePresenter(this, this) }
    private val loginPresenter: LoginPresenter by lDelegate
    private val codePresenter: CodePresenter by cDelegate

    override fun onLoginStart() {
        LoadingDialogUtil.showLoading(this)
    }


    override fun onLoginSuccess(userEntity: UserEntity) {
        LoadingDialogUtil.closeLoadingDialog()

        App.modifyToken(userEntity.token)
        userEntity.roleCode = getRoleCode()
        /*SerializableUtils.deleteSerializable(
                if (getRoleCode() == STUDENT_ROLE) {
                    Constants.STUDENT_USER_ENTITY
                } else {
                    Constants.TEACHER_USER_ENTITY
                }
        )*/
        SerializableUtils.setSerializable(
                if (getRoleCode() == STUDENT_ROLE) {
                    Constants.STUDENT_USER_ENTITY
                } else {
                    Constants.TEACHER_USER_ENTITY
                }, userEntity)
        if (isToMain)
            startActivity(getJumpIntent())
        else
            setResult(Constants.CODE_LOGIN_RESULT)
        finish()
    }

    override fun onLoginError(t: Throwable) {
        LoadingDialogUtil.closeLoadingDialog()
        t.handleThrowable(this)
    }

    override fun onCheckStart() {
        LoadingDialogUtil.showLoading(this, "获取验证码中...")
    }

    override fun onUserIsExist(string: String?) {
        LoadingDialogUtil.closeLoadingDialog()
        T.show(this, string ?: "用户已注册")
    }

    override fun onUserNotExist(string: String?) {
        codePresenter.getCode(registerPhone.text.toString())
    }

    override fun onCheckError(t: Throwable) {
        t.handleThrowable(this)
        LoadingDialogUtil.closeLoadingDialog()
    }

    override fun onCodeStart() {

    }

    val msgDialogFragment by lazy { MsgDialogFragment() }
    override fun onCodeSuccess(codeEntity: String?) {
        msgDialogFragment.show(null, "$codeEntity", "知道了", null, supportFragmentManager)
        LoadingDialogUtil.closeLoadingDialog()
        codePresenter.startCutDown()
    }


    override fun onCodeError(t: Throwable) {
        t.handleThrowable(this)
        LoadingDialogUtil.closeLoadingDialog()
    }

    override fun onRegisterStart() {
        LoadingDialogUtil.showLoading(this, "正在注册中...")
    }

    override fun onRegisterSuccess(userEntity: UserEntity?) {

        if (userEntity != null) {
            LoadingDialogUtil.closeLoadingDialog()
            App.modifyToken(userEntity.token)
            userEntity.roleCode = getRoleCode()
            SerializableUtils.deleteSerializable(
                    if (getRoleCode() == STUDENT_ROLE) {
                        Constants.STUDENT_USER_ENTITY
                    } else {
                        Constants.TEACHER_USER_ENTITY
                    }
            )
            SerializableUtils.setSerializable(
                    if (getRoleCode() == STUDENT_ROLE) {
                        Constants.STUDENT_USER_ENTITY
                    } else {
                        Constants.TEACHER_USER_ENTITY
                    }, userEntity)
            startActivity(getJumpIntent())
            finish()
        } else {
            onRegisterError(MsgException("1", "无数据返回"))
        }
    }

    override fun onRegisterError(t: Throwable) {
        LoadingDialogUtil.closeLoadingDialog()
        t.handleThrowable(this)
    }


    override fun enableCodeBtn(enable: Boolean) {
        registerCodeBtn.isEnabled = enable
    }

    override fun codeBtnText(text: String) {
        registerCodeBtn.text = text
    }


    override fun onBackPressed() {
        if (!isToMain)
            super.onBackPressed()
    }

    private var isLoginState = true

    lateinit var loginEdit: EditText
    lateinit var pwdEdit: EditText
    override fun initViews() {
        mActivity = this
        loginEdit = loginAccount
        pwdEdit = loginPwd
        isToMain = intent.getBooleanExtra(Constants.KEY_TO_MAIN, false)

        launcher.setImageResource(getLogoResId())

        if (getRoleCode() == STUDENT_ROLE) {
            loginQQ.visibility = View.GONE
            loginSina.visibility = View.GONE
            loginWeChat.visibility = View.GONE
        }

        toggleLogin(true)

        //点击切换选中状态
        radio.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
            //选中了登陆
                R.id.login_choose -> toggleLogin(true)
            //选中了注册
                R.id.register_choose -> toggleLogin(false)
            }
        }

        //点击获取注册码
        registerCodeBtn.setOnClickListener {
            codePresenter.checkExist(registerPhone.text.toString())
        }

        //点击登陆
        loginBtn.setOnClickListener {
            if (isLoginState) {//登陆状态
                loginPresenter.login(loginAccount.text.toString(), loginPwd.text.toString(), getRoleCode())
            } else {//注册
                if (serveCheck.isChecked)
                    loginPresenter.register(registerAccount.text.toString(), registerPhone.text.toString()
                            , registerCode.text.toString(), registerPwd.text.toString(), getRoleCode(), null, null)
                else
                    toast("请勾选服务条款", true)
            }
        }
        loginWeChat.setOnClickListener {
            if (UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.WEIXIN)) {
                authorization(SHARE_MEDIA.WEIXIN, it)
            } else {
                toast("请安装微信客户端", true)
            }
        }
        loginSina.setOnClickListener {
            if (UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.SINA)) {
                authorization(SHARE_MEDIA.SINA, it)
            } else {
                toast("请安装微信客户端", true)
            }
        }
        loginQQ.setOnClickListener {
            if (UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.QQ)) {
                authorization(SHARE_MEDIA.QQ, it)
            } else {
                toast("请安装QQ客户端", true)
            }
        }

        imageSee.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {//可见状态
                loginPwd.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                loginPwd.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        registerImageSee.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {//可见状态
                registerPwd.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                registerPwd.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
        //用户服务协议
        serveBtn.setOnClickListener {
            startActivity(Intent(this, ProtocolActivity::class.java))
        }

        forgetPwd.setOnClickListener { forgetClick(it) }
        EditTextLimitUtil.inputChineseAndEnglish(registerAccount, this, 16)
    }

    /**切换是登陆或者注册*/
    private fun toggleLogin(boolean: Boolean) {
        isLoginState = boolean
        if (boolean) {
//            radio.setPadding(0,resources.getDimension(R.dimen.y40).toInt(),0,0)
            loginAccount.visibility = View.VISIBLE
            loginPwdLayout.visibility = View.VISIBLE
            forgetPwd.visibility = View.VISIBLE
            registerAccount.visibility = View.GONE
            registerPhone.visibility = View.GONE
            registerCodeLayout.visibility = View.GONE
            registerPwdLayout.visibility = View.GONE
            registerTip.visibility = View.GONE
            loginBtn.text = getText(R.string.login_login)
        } else {
//            radio.setPadding(0,resources.getDimension(R.dimen.y10).toInt(),0,0)
            loginAccount.visibility = View.GONE
            loginPwdLayout.visibility = View.GONE
            forgetPwd.visibility = View.GONE
            registerAccount.visibility = View.VISIBLE
            registerPhone.visibility = View.VISIBLE
            registerCodeLayout.visibility = View.VISIBLE
            registerPwdLayout.visibility = View.VISIBLE
            registerTip.visibility = View.VISIBLE
            loginBtn.text = getText(R.string.login_register)
        }
    }

    /**
     * logo图标
     */
    abstract protected fun getLogoResId(): Int

    /**
     * 登录角色名
     */
    abstract protected fun getRoleCode(): String

    abstract protected fun getJumpIntent(): Intent

    abstract protected fun forgetClick(view: View)

    abstract protected fun threeParty(view: View, uid: String?, accessToken: String?, name: String?, iconurl: String?, gender: String?, share_media: String?)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }

    //第三方授权
    private fun authorization(share_media: SHARE_MEDIA, view: View) {
        UMShareAPI.get(this).getPlatformInfo(this, share_media, object : UMAuthListener {
            //            授权完成
            override fun onComplete(p0: SHARE_MEDIA?, p1: Int, p2: MutableMap<String, String>?) {
                //sdk是6.4.4的,但是获取值的时候用的是6.2以前的(access_token)才能获取到值,未知原因

                var uid = p2?.get("uid")
                var openid = p2?.get("openid")//微博没有
                var unionid = p2?.get("unionid")//微博没有
                var access_token = p2?.get("access_token")
                var refresh_token = p2?.get("refresh_token")//微信,qq,微博都没有获取到
                var expires_in = p2?.get("expires_in")
                var name = p2?.get("name")
                var gender = p2?.get("gender")
                var iconurl = p2?.get("iconurl")
                EBagApi.login(SPUtils.get(App.mContext, ebag.core.util.Constants.IMEI, "") as String, BLoginActivity.ISHD, null, null, null, queryThirdPartyType(share_media.toString()), getRoleCode(), access_token, uid, object : RequestCallBack<UserEntity>() {
                    override fun onSuccess(entity: UserEntity?) {
                        if (entity != null) {
                            this@BLoginActivity.onLoginSuccess(entity)
                            L.e(entity.token)
                        } else {
                            this@BLoginActivity.onLoginError(MsgException("1", "无数据返回，请稍后重试"))
                        }

                    }

                    override fun onError(exception: Throwable) {
                        LoadingDialogUtil.closeLoadingDialog()
//                        1002代表平板学生平板未激活
//                        1003 当移动端点击第三方登录时如果没有绑定第三方返回此状态码
//                        1004代表老师平板或者pc未激活
                        if (exception is MsgException) {
                            when {
                                exception.code == "1002" ->
                                    toast(exception.message.toString())
                                exception.code == "1004" ->
                                    toast(exception.message.toString())
                                exception.code == "1003" ->
                                    threeParty(view, uid, access_token, name, iconurl, gender, share_media.toString())

                            }
                        }
                    }
                })
                SPUtils.put(this@BLoginActivity, ebag.hd.base.Constants.USER_ACCOUNT, "")
                SPUtils.put(this@BLoginActivity, ebag.hd.base.Constants.ROLE_CODE, BLoginActivity.TEACHER_ROLE)
                SPUtils.put(this@BLoginActivity, ebag.hd.base.Constants.THIRD_PARTY_TOKEN, if (access_token.isNullOrEmpty()) "" else access_token)
                SPUtils.put(this@BLoginActivity, ebag.hd.base.Constants.THIRD_PARTY_UNION_ID, if (uid.isNullOrEmpty()) "" else uid)

            }

            //            授权取消
            override fun onCancel(p0: SHARE_MEDIA?, p1: Int) {
                toast("取消")
            }

            //            授权失败
            override fun onError(p0: SHARE_MEDIA?, p1: Int, p2: Throwable?) {
                toast("失败")
            }

            //            授权开始
            override fun onStart(p0: SHARE_MEDIA?) {
                toast("开始")
            }
        })
    }

    private fun queryThirdPartyType(thirdPartyType: String): String {
        return if (thirdPartyType.equals("qq", true)) {
            BLoginActivity.QQ_TYPE
        } else if (thirdPartyType.equals("sina", true)) {
            BLoginActivity.SIAN_TYPE
        } else {
            BLoginActivity.WEIXIN_TYPE
        }
    }
}