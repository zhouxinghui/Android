package com.yzy.ebag.student.ui.activity.account

import android.view.View
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.response.CodeEntity
import com.yzy.ebag.student.bean.response.UserEntity
import com.yzy.ebag.student.ui.presenter.CodePresenter
import com.yzy.ebag.student.ui.presenter.LoginPresenter
import com.yzy.ebag.student.ui.view.CodeView
import com.yzy.ebag.student.ui.view.LoginView
import ebag.core.base.mvp.MVPActivity
import ebag.hd.bean.response.CodeEntity
import ebag.hd.bean.response.UserEntity
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by unicho on 2017/11/2.
 * Activity 登录&注册
 */

internal class LoginActivity : MVPActivity(), LoginView,CodeView {


    override fun destroyPresenter() {
        if(lDelegate.isInitialized())
            loginPresenter.onDestroy()
        if(cDelegate.isInitialized())
            codePresenter.onDestroy()
    }

    /**懒加载*/
    private val lDelegate = lazy { LoginPresenter(this,this)}
    private val cDelegate = lazy { CodePresenter(this,this)}
    private val loginPresenter: LoginPresenter by lDelegate
    private val codePresenter: CodePresenter by cDelegate

    override fun onLoginStart() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCodeStart() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoginSuccess(userEntity: UserEntity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCodeSuccess(codeEntity: CodeEntity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoginError(t: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCodeError(t: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun enableCodeBtn(enable: Boolean) {
        registerCodeBtn.isEnabled = enable
    }

    override fun codeBtnText(text: String) {
        registerCodeBtn.text = text
    }

    private var isLoginState = true

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initViews() {

        toggleLogin(true)

        //点击切换选中状态
        radio.setOnCheckedChangeListener { _, checkedId ->
            run {
                when (checkedId) {
                //选中了登陆
                    R.id.login_choose -> toggleLogin(true)
                //选中了注册
                    R.id.register_choose -> toggleLogin(false)
                }
            }
        }

        //点击获取注册码
        registerCodeBtn.setOnClickListener {
            codePresenter.getCode(registerPhone.text.toString())
        }

        //点击登陆
        loginBtn.setOnClickListener {
            if (isLoginState){//登陆状态
                loginPresenter.login(loginAccount.text.toString(),loginPwd.text.toString())
            }else{//注册
                if(serveCheck.isChecked)
                    loginPresenter.register(registerAccount.text.toString(), registerPhone.text.toString()
                            , registerCode.text.toString(),registerPwd.text.toString())
                else
                    toast("请勾选服务条款",true)
            }
        }
    }

    /**切换是登陆或者注册*/
    private fun toggleLogin(boolean: Boolean){
        isLoginState = boolean
        if (boolean){
            radio.setPadding(0,resources.getDimension(R.dimen.y40).toInt(),0,0)
            loginAccount.visibility = View.VISIBLE
            loginPwdLayout.visibility = View.VISIBLE
            forgetPwd.visibility = View.VISIBLE
            registerAccount.visibility = View.GONE
            registerPhone.visibility = View.GONE
            registerCodeLayout.visibility = View.GONE
            registerPwd.visibility = View.GONE
            registerTip.visibility = View.GONE
            loginBtn.text = getText(R.string.login_login)
        }else{
            radio.setPadding(0,resources.getDimension(R.dimen.y10).toInt(),0,0)
            loginAccount.visibility = View.GONE
            loginPwdLayout.visibility = View.GONE
            forgetPwd.visibility = View.GONE
            registerAccount.visibility = View.VISIBLE
            registerPhone.visibility = View.VISIBLE
            registerCodeLayout.visibility = View.VISIBLE
            registerPwd.visibility = View.VISIBLE
            registerTip.visibility = View.VISIBLE
            loginBtn.text = getText(R.string.login_register)
        }
    }
}