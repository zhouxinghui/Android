package ebag.core.http.network

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by caoyu on 2017/11/9.
 */
class NormalRequestObserver<E>(private val requestCallBack: RequestCallBack<E>) : Observer<E>, OnRequestCancelListener {
    private var disposable: Disposable? = null
    init {
        this.requestCallBack.setOnRequestCancelListener(this)
    }

    override fun onSubscribe(@io.reactivex.annotations.NonNull d: Disposable) {
        this.requestCallBack.onStart()
        this.disposable = d
    }

    override fun onNext(e: E) {
        this.requestCallBack.onSuccess(e)
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