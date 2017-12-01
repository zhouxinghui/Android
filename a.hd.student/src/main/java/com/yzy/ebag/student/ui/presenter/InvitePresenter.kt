package com.yzy.ebag.student.ui.presenter

import com.yzy.ebag.student.ui.view.InviteView
import ebag.core.base.mvp.BasePresenter
import ebag.core.base.mvp.OnToastListener

/**
 * Created by unicho on 2017/11/21.
 */
class InvitePresenter(view: InviteView, listener: OnToastListener) : BasePresenter<InviteView>(view,listener) {

    fun startRequest(phone: String, code: String, pwd: String){

    }
}