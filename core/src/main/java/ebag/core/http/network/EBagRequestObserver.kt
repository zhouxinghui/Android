package ebag.core.http.network

import ebag.core.bean.ResponseBean
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by caoyu on 2017/11/9.
 */
class EBagRequestObserver<E>(private val requestCallBack: RequestCallBack<E>) : Observer<ResponseBean<E>>, OnRequestCancelListener {
    private var disposable: Disposable? = null
    init {
        this.requestCallBack.setOnRequestCancelListener(this)
    }

    override fun onSubscribe(@io.reactivex.annotations.NonNull d: Disposable) {
        this.requestCallBack.onStart()
        this.disposable = d
    }

    override fun onNext(response: ResponseBean<E>) {
        if(response.success == "200")
            this.requestCallBack.onSuccess(response.data)
        else
            this.requestCallBack.onError(MsgException(response.success ?: "0", response.message ?: "服务器异常"))
    }

    override fun onComplete() {
        this.requestCallBack.onFinish()
    }

    override fun onError(e: Throwable) {
        this.requestCallBack.onError(e)
        this.requestCallBack.onFinish()
    }

    override fun onCancel() {
        if (disposable != null && !disposable!!.isDisposed)
            disposable!!.dispose()
    }
}