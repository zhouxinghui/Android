package ebag.hd.ui.view

import ebag.hd.bean.response.CodeEntity


/**
 * Created by unicho on 2017/11/11.
 */
interface CodeView {

    /**
     * 获取验证码之前的操作
     */
    fun onCodeStart()

    /**
     * 获取验证码成功后的操作
     */
    fun onCodeSuccess(codeEntity: CodeEntity)

    /**
     * 获取验证码失败后的操作
     */
    fun onCodeError(t: Throwable)
    /**
     * 获取验证码按钮是否是激活状态
     */
    fun enableCodeBtn(enable: Boolean)

    /**
     * 获取验证码按钮是否是激活状态
     */
    fun codeBtnText(text: String)

}