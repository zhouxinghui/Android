package ebag.hd.ui.presenter
import ebag.core.base.mvp.BasePresenter
import ebag.core.base.mvp.OnToastListener
import ebag.core.http.network.RequestCallBack
import ebag.core.util.StringUtils
import ebag.hd.bean.response.UserEntity
import ebag.hd.http.EBagApi
import ebag.hd.ui.view.LoginView


/**
 * presenter 登录页面的控制类
 * Created by unicho on 2017/11/2.
 */
internal class LoginPresenter(view: LoginView, listener: OnToastListener): BasePresenter<LoginView>(view,listener) {

    /**判断账号和密码格式是否输入错误*/
    private fun isLoginInfoCorrect(account: String, pwd: String): Boolean =
            if (account.isEmpty() || pwd.isEmpty()){
                showToast("请输入账号密码！",true)
                false
            }else if(account.length != 11 || account.length != 7) {
                showToast("请输入正确的账号！", true)
                false
            }else if(account.length == 11 && !StringUtils.isMobileNo(account)){
                showToast("手机格式输入错误！", true)
                false
            }else if(pwd.length < 6 || pwd.length > 20){
                showToast("请输入6~20位密码", true)
                false
            }else {
                true
            }

    private var loginRequest: RequestCallBack<UserEntity>? = null
    private var registerRequest: RequestCallBack<UserEntity>? = null
    /**
     * 登录
     */
    fun login(account: String, pwd: String){
        if(isLoginInfoCorrect(account,pwd)) {
            if(loginRequest == null)
                loginRequest = object: RequestCallBack<UserEntity>(){
                    override fun onStart() {
                        getView()?.onLoginStart()
                    }
                    override fun onSuccess(entity: UserEntity) {
                        getView()?.onLoginSuccess(entity)
                    }

                    override fun onError(exception: Throwable) {
                        getView()?.onLoginError(exception)
                    }

                }
           EBagApi.login(account,pwd, loginRequest!!)
        }
    }


    /**
     * 判断注册信息是否填写正确
     */
    private fun isRegisterInfoCorrect(phone: String, code: String, pwd: String): Boolean =
            when {
                !StringUtils.isMobileNo(phone) -> {
                    showToast("手机号输入格式不正确",true)
                    false
                }
                code.length != 6 -> {
                    showToast("验证码长度不正确",true)
                    false
                }
                StringUtils.isPassword(pwd) -> {
                    showToast("请输入数字字母混输，6~20位密码",true)
                    false
                }
                else -> true
            }


    /**
     * 注册
     */
    fun register(name: String, phone: String, code: String, pwd: String){
        if(isRegisterInfoCorrect(phone,code,pwd)){
            if(registerRequest == null)
                registerRequest = object: RequestCallBack<UserEntity>(){
                    override fun onStart() {
                    }
                    override fun onSuccess(entity: UserEntity) {

                    }

                    override fun onError(exception: Throwable) {
                    }

                }
            EBagApi.register(name,phone,code,pwd,registerRequest!!)
        }

    }

    /**
     * 调用onDestroy方法 终止网络请求
     */
    override fun onDestroy(){
        loginRequest?.cancelRequest()
        registerRequest?.cancelRequest()
        super.onDestroy()
    }

}