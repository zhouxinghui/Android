package ebag.hd.ui.presenter

import ebag.core.base.mvp.BasePresenter
import ebag.core.base.mvp.OnToastListener
import ebag.core.http.network.RequestCallBack
import ebag.core.util.L
import ebag.core.util.StringUtils
import ebag.hd.http.EBagApi
import ebag.hd.ui.view.CodeView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * Created by caoyu on 2017/11/11.
 * presenter 验证码
 */
open class CodePresenter(view: CodeView, listener: OnToastListener): BasePresenter<CodeView>(view,listener) {

    private var codeDisposable: Disposable? = null
    private var requestRequest: RequestCallBack<String>? = null

    /**
     * 获取验证码
     */
    fun getCode(ysbCode: String, phone: String){

        if(StringUtils.isMobileNo(phone) || ysbCode.length == 7) {
            if(requestRequest == null)
                requestRequest = createRequest(object: RequestCallBack<String>() {

                    override fun onStart() {
                        getView()?.onCodeStart()
                    }

                    override fun onSuccess(entity: String?) {
                        startCutDown()
                        getView()?.onCodeSuccess(entity)
                    }

                    override fun onError(exception: Throwable) {
                        getView()?.onCodeError(exception)
                    }

                })
            EBagApi.getCode(phone, ysbCode, requestRequest!!)
        } else
            showToast("手机号或书包号输入错误",true)
    }

    /**
     * 验证码按钮倒计时
     */
    fun startCutDown(){
        //从0 开始，每一秒发送一次数据
        codeDisposable = Observable
                //从0 开始 一秒钟 加一 加60次
                .intervalRange(0, 60, 0, 1, TimeUnit.SECONDS)
                .map { 60 - it }//转化为倒计时的模式
                //开始倒计时时，状态设置为不激活状态
                .doOnSubscribe { getView()?.enableCodeBtn(false) }
                .observeOn(AndroidSchedulers.mainThread())
                //一秒钟改变一次文字
                .doOnNext {
                    L.e("code", "" + it)
                    getView()?.codeBtnText("重新获取($it)")
                }.doOnComplete {//全部完成之后改变状态
                    getView()?.codeBtnText("获取验证码")
                    getView()?.enableCodeBtn(true)
                }.doOnDispose {//取消这个事件后 将其状态重置
                    getView()?.codeBtnText("获取验证码")
                    getView()?.enableCodeBtn(true)
                }.subscribe()
    }

    override fun onDestroy() {
        if(codeDisposable?.isDisposed == false)
            codeDisposable?.dispose()

        requestRequest?.cancelRequest()

        super.onDestroy()
    }
}