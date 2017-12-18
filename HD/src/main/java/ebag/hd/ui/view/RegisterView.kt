package com.yzy.ebag.student.ui.view

import com.yzy.ebag.student.bean.response.UserEntity

/**
 * Created by unicho on 2017/11/2.
 */
interface RegisterView {

    /**
     * 注册之前
     */
    fun onRegisterStart()

    /**
     * 注册成功
     */
    fun onRegisterSuccess(userEntity: UserEntity)

    /**
     * 注册失败
     */
    fun onRegisterError(t: Throwable)
}