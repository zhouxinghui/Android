package com.yzy.ebag.student.ui.presenter

import com.yzy.ebag.student.bean.response.CodeEntity
import com.yzy.ebag.student.http.EBagApi
import com.yzy.ebag.student.ui.view.CodeView
import ebag.core.base.mvp.BasePresenter
import ebag.core.base.mvp.OnToastListener
import ebag.core.http.network.RequestCallBack
import ebag.core.util.L
import ebag.core.util.StringUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * Created by unicho on 2017/11/11.
 */
open class CodePresenter(view: CodeView, listener: OnToastListener): BasePresenter<CodeView>(view,listener) {

    private var codeDisposable: Disposable? = null
    private var requestRequest: RequestCallBack<CodeEntity>? = null

    /**
     * 获取验证码
     */
    fun getCode(phone: String){

        if(StringUtils.isMobileNo(phone)) {
            if(requestRequest == null)
                requestRequest = object: RequestCallBack<CodeEntity>() {
                    override fun onStart() {
                        getView()?.onCodeStart()
                    }
                    override fun onSuccess(t: CodeEntity) {
                        getView()?.onCodeSuccess(t)
                    }

                    override fun onError(e: Throwable) {
                        getView()?.onCodeError(e)
                    }

                }
            EBagApi.getCode(phone,requestRequest!!)
        } else
            showToast("手机号码格式输入错误",true)
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

    private fun success(codeEntity: CodeEntity){
        startCutDown()
        getView()?.onCodeSuccess(codeEntity)
    }

    override fun onDestroy() {
        if(codeDisposable?.isDisposed == false)
            codeDisposable?.dispose()

        requestRequest?.cancelRequest()

        super.onDestroy()
    }
}