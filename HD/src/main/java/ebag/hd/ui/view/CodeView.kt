package ebag.hd.ui.view


/**
 * Created by caoyu on 2017/11/11.
 */
interface CodeView {

    /**
     * 获取验证码之前的操作
     */
    fun onCodeStart()

    /**
     * 获取验证码成功后的操作
     */
    fun onCodeSuccess(codeEntity: String?)

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

    fun onCheckStart()

    fun onUserIsExist(string: String?)

    fun onUserNotExist(string: String?)

    fun onCheckError(t: Throwable)

}