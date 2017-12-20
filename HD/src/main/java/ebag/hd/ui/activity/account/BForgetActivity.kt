package ebag.hd.ui.activity.account

import ebag.core.base.mvp.MVPActivity
import ebag.hd.R
import ebag.hd.bean.response.CodeEntity
import ebag.hd.dialog.UpdateDialog
import ebag.hd.ui.presenter.CodePresenter
import ebag.hd.ui.presenter.ForgetPresenter
import ebag.hd.ui.view.CodeView
import ebag.hd.ui.view.ForgetView
import kotlinx.android.synthetic.main.activity_forget.*

/**
 * Created by unicho on 2017/11/13.
 * activity 忘记密码
 */
class BForgetActivity : MVPActivity(), CodeView, ForgetView {

    private val fPresenterDelegate = lazy{ ForgetPresenter(this,this) }
    private val fPresenter: ForgetPresenter by fPresenterDelegate
    private val cPresenterDelegate = lazy { CodePresenter(this,this) }
    private val cPresenter: CodePresenter by cPresenterDelegate

    /**
     * 点击完成之前的操作
     */
    override fun onForgetStart() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 点击完成 成功之后的操作
     */
    override fun onForgetSuccess() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 点击完成，请求失败之后的操作
     */
    override fun onForgetError(t: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 获取验证码开始之前的操作
     */
    override fun onCodeStart() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 获取验证码成功后的操作
     */
    override fun onCodeSuccess(codeEntity: CodeEntity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 获取验证码失败后的操作
     */
    override fun onCodeError(t: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

        var updateDialog: UpdateDialog = UpdateDialog()
        updateDialog.show(supportFragmentManager,"update")

        //获取验证码事件
        codeBtn.setOnClickListener { cPresenter.startCutDown() }

        //点击完成事件
        completeBtn.setOnClickListener { fPresenter.startRequest(phoneEdit.text.toString(),codeEdit.text.toString(),pwdEdit.text.toString()) }
    }
}