package ebag.hd.ui.activity.account

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import ebag.core.base.mvp.MVPActivity
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.hd.R
import ebag.hd.dialog.MsgDialogFragment
import ebag.hd.ui.presenter.CodePresenter
import ebag.hd.ui.presenter.ForgetPresenter
import ebag.hd.ui.view.CodeView
import ebag.hd.ui.view.ForgetView
import kotlinx.android.synthetic.main.activity_forget.*

/**
 * Created by caoyu on 2017/11/13.
 * activity 忘记密码
 */
abstract class BForgetActivity : MVPActivity(), CodeView, ForgetView {

    private val fPresenterDelegate = lazy{ ForgetPresenter(this,this) }
    private val fPresenter: ForgetPresenter by fPresenterDelegate
    private val cPresenterDelegate = lazy { CodePresenter(this,this) }
    private val cPresenter: CodePresenter by cPresenterDelegate

    abstract fun resetSuccess()
    /**
     * 点击完成之前的操作
     */
    override fun onForgetStart() {
        LoadingDialogUtil.showLoading(this,"请稍等...")
    }

    /**
     * 点击完成 成功之后的操作
     */
    override fun onForgetSuccess(entity: String?) {
        LoadingDialogUtil.closeLoadingDialog()
        T.show(this,"修改密码成功")
//        startActivity(Intent(this, BLoginActivity::))
    }

    /**
     * 点击完成，请求失败之后的操作
     */
    override fun onForgetError(t: Throwable) {
        LoadingDialogUtil.closeLoadingDialog()
        t.handleThrowable(this)
    }

    override fun onCheckStart() {
        LoadingDialogUtil.showLoading(this,"检测账号是否存在...")
    }

    override fun onUserIsExist(string: String?) {
        cPresenter.getCode(phoneEdit.text.toString())
    }

    override fun onUserNotExist(string: String?) {
        T.show(this, string ?: "用户未注册")
        LoadingDialogUtil.closeLoadingDialog()
    }

    override fun onCheckError(t: Throwable) {
        t.handleThrowable(this)
        LoadingDialogUtil.closeLoadingDialog()
    }

    /**
     * 获取验证码开始之前的操作
     */
    override fun onCodeStart() {
        LoadingDialogUtil.showLoading(this,"获取验证码中...")
    }

    /**
     * 获取验证码成功后的操作
     */
    val msgDialogFragment by lazy { MsgDialogFragment() }
    override fun onCodeSuccess(codeEntity: String?) {
        msgDialogFragment.show(null,"$codeEntity","知道了", null, supportFragmentManager)
        T.show(this, "获取验证码成功")
        cPresenter.startCutDown()
        LoadingDialogUtil.closeLoadingDialog()
    }

    /**
     * 获取验证码失败后的操作
     */
    override fun onCodeError(t: Throwable) {
        t.handleThrowable(this)
        LoadingDialogUtil.closeLoadingDialog()
    }

    override fun enableCodeBtn(enable: Boolean) {
        codeBtn.isEnabled = enable
    }

    /**
     * 更新验证码文字信息
     */
    override fun codeBtnText(text: String) {
        codeBtn.text = text
    }

    override fun destroyPresenter() {
        //判断这个是否进行了初始化
        if(fPresenterDelegate.isInitialized())
            fPresenter.onDestroy()
        if(cPresenterDelegate.isInitialized())
            cPresenter.onDestroy()
    }

    override fun getLayoutId(): Int = R.layout.activity_forget

    override fun initViews() {

        imageSee.setOnClickListener {
            it.isSelected = !it.isSelected
            if(it.isSelected){//可见状态
                pwdEdit.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }else{
                pwdEdit.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        //获取验证码事件
        codeBtn.setOnClickListener {
            val account = phoneEdit.text.toString()
            if(account.length == 11 && StringUtils.isMobileNo(account)){//手机号
                cPresenter.checkExist(account)
            }else if(account.length == 7){//书包号  直接获取验证码
                cPresenter.getCode(account)
            }else{
                T.show(this,"请输入正确的账号")
            }
        }

        //点击完成事件
        completeBtn.setOnClickListener {
            fPresenter.startRequest(phoneEdit.text.toString(),codeEdit.text.toString(),pwdEdit.text.toString())
        }
    }

}