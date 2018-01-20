package ebag.hd.ui.view

import ebag.hd.bean.response.UserEntity


/**
 * Created by caoyu on 2017/11/2.
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