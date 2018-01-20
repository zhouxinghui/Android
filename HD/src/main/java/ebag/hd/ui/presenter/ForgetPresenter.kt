package ebag.hd.ui.presenter

import ebag.core.base.mvp.BasePresenter
import ebag.core.base.mvp.OnToastListener
import ebag.hd.ui.view.ForgetView


/**
 * Created by caoyu on 2017/11/11.
 * presenter 忘记密码
 */
class ForgetPresenter(view: ForgetView, listener: OnToastListener) : BasePresenter<ForgetView>(view,listener) {

    fun startRequest(phone: String, code: String, pwd: String){

    }
}