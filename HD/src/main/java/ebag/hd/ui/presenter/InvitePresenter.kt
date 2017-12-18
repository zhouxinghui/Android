package ebag.hd.ui.presenter

import ebag.core.base.mvp.BasePresenter
import ebag.core.base.mvp.OnToastListener
import ebag.hd.ui.view.InviteView

/**
 * Created by unicho on 2017/11/21.
 * presenter 输入注册码
 */
class InvitePresenter(view: InviteView, listener: OnToastListener) : BasePresenter<InviteView>(view,listener) {

    fun startRequest(phone: String, code: String, pwd: String){

    }
}