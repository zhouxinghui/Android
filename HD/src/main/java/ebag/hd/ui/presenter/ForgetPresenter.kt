package ebag.hd.ui.presenter

import ebag.core.base.mvp.BasePresenter
import ebag.core.base.mvp.OnToastListener
import ebag.core.util.StringUtils
import ebag.hd.ui.view.ForgetView


/**
 * Created by caoyu on 2017/11/11.
 * presenter 忘记密码
 */
class ForgetPresenter(view: ForgetView, listener: OnToastListener) : BasePresenter<ForgetView>(view,listener) {


    fun startRequest(account: String, code: String, pwd: String){

        var phone = ""
        var ysbCode = ""
        if(StringUtils.isMobileNo(account)){
            phone = account
        }else{
            ysbCode = account
        }

        if(ysbCode.length != 7){
            showToast("请输入正确的手机号或书包号", true)
            return
        }

        if(StringUtils.isPassword(pwd)){

        }else{
            showToast("请输入6~20位字母数字混合密码", true)
        }


    }
}