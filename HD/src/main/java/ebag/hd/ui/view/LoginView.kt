package com.yzy.ebag.student.ui.view

import com.yzy.ebag.student.bean.response.UserEntity

/**
 * Created by unicho on 2017/11/2.
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
}