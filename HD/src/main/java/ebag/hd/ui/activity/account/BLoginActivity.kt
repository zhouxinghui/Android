package ebag.hd.ui.activity.account

import android.content.Intent
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import ebag.core.base.App
import ebag.core.base.mvp.MVPActivity
import ebag.core.http.network.MsgException
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.SerializableUtils
import ebag.core.util.T
import ebag.hd.R
import ebag.hd.base.Constants
import ebag.hd.bean.response.UserEntity
import ebag.hd.dialog.MsgDialogFragment
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

    private var isToMain = false

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun destroyPresenter() {
        if(lDelegate.isInitialized())
            loginPresenter.onDestroy()
        if(cDelegate.isInitialized())
            codePresenter.onDestroy()
    }

    /**懒加载*/
    private val lDelegate = lazy { LoginPresenter(this, this) }
    private val cDelegate = lazy { CodePresenter(this,this) }
    private val loginPresenter : LoginPresenter by lDelegate
    private val codePresenter : CodePresenter by cDelegate

    override fun onLoginStart() {
        LoadingDialogUtil.showLoading(this)
    }



    override fun onLoginSuccess(userEntity: UserEntity) {
        LoadingDialogUtil.closeLoadingDialog()
        App.modifyToken(userEntity.token)
        userEntity.roleCode = getRoleCode()
        SerializableUtils.setSerializable(
                if (getRoleCode() == "student")
                    Constants.STUDENT_USER_ENTITY
                else
                    Constants.TEACHER_USER_ENTITY,
                userEntity)
        if(isToMain)
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
        LoadingDialogUtil.showLoading(this,"获取验证码中...")
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
        msgDialogFragment.show(null,"$codeEntity","知道了", null, supportFragmentManager)
        LoadingDialogUtil.closeLoadingDialog()
        codePresenter.startCutDown()
    }



    override fun onCodeError(t: Throwable) {
        t.handleThrowable(this)
        LoadingDialogUtil.closeLoadingDialog()
    }

    override fun onRegisterStart() {
        LoadingDialogUtil.showLoading(this,"正在注册中...")
    }

    override fun onRegisterSuccess(userEntity: UserEntity?) {

        if(userEntity != null){
            LoadingDialogUtil.closeLoadingDialog()
            App.modifyToken(userEntity.token)
            userEntity.roleCode = getRoleCode()
            SerializableUtils.setSerializable(
                    if (getRoleCode() == "student")
                        Constants.STUDENT_USER_ENTITY
                    else
                        Constants.TEACHER_USER_ENTITY,
                    userEntity)
            startActivity(getJumpIntent())
            finish()
        }else{
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
        if(!isToMain)
            super.onBackPressed()
    }

    private var isLoginState = true

    lateinit var loginEdit: EditText
    lateinit var pwdEdit: EditText
    override fun initViews() {
        loginEdit = loginAccount
        pwdEdit = loginPwd

        isToMain = intent.getBooleanExtra(Constants.KEY_TO_MAIN,false)

        launcher.setImageResource(getLogoResId())
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
            if (isLoginState){//登陆状态
                loginPresenter.login(loginAccount.text.toString(), loginPwd.text.toString(), getRoleCode())
            }else{//注册
                if(serveCheck.isChecked)
                    loginPresenter.register(registerAccount.text.toString(), registerPhone.text.toString()
                            , registerCode.text.toString(),registerPwd.text.toString())
                else
                    toast("请勾选服务条款",true)
            }
        }

        imageSee.setOnClickListener {
            it.isSelected = !it.isSelected
            if(it.isSelected){//可见状态
                loginPwd.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }else{
                loginPwd.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        registerImageSee.setOnClickListener {
            it.isSelected = !it.isSelected
            if(it.isSelected){//可见状态
                registerPwd.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }else{
                registerPwd.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        forgetPwd.setOnClickListener { forgetClick(it) }
    }

    /**切换是登陆或者注册*/
    private fun toggleLogin(boolean: Boolean){
        isLoginState = boolean
        if (boolean){
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
        }else{
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
    abstract protected fun getLogoResId() : Int

    /**
     * 登录角色名
     */
    abstract protected fun getRoleCode(): String
    abstract protected fun getJumpIntent(): Intent

    abstract protected fun forgetClick(view: View)
}