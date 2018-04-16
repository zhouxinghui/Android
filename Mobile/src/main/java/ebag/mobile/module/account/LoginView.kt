package ebag.mobile.module.account

import ebag.mobile.bean.UserEntity


/**
 * Created by caoyu on 2017/11/2.
 */
interface LoginView {

    /**
     * 登录之前
     */
    fun onLoginStart()

    /**
     * 登录成功
     */
    fun onLoginSuccess(userEntity: UserEntity)

    /**
     * 登录失败
     */
    fun onLoginError(t: Throwable)


    fun onRegisterStart()

    fun onRegisterSuccess(userEntity: UserEntity?)

    fun onRegisterError(t: Throwable)
}