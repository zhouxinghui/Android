package com.yzy.ebag.student.ui.presenter

import com.yzy.ebag.student.ui.view.ForgetView
import ebag.core.base.mvp.BasePresenter
import ebag.core.base.mvp.OnToastListener

/**
 * Created by unicho on 2017/11/11.
 */
class ForgetPresenter(view: ForgetView, listener: OnToastListener) : BasePresenter<ForgetView>(view,listener) {

    fun startRequest(phone: String, code: String, pwd: String){

    }
}