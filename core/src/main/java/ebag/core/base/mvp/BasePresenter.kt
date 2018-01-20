package ebag.core.base.mvp

import ebag.core.http.network.RequestCallBack
import java.lang.ref.Reference
import java.lang.ref.WeakReference
import java.util.*

/**
 * Presenter 的基类，主要封装了生命周期方法、toast 方法，
 * 以及资源的自动释放
 * 里面相应的方法会在 Activity 或者 Fragment 的生命周期里调用
 * Created by caoyu on 2017/11/1.
 */
abstract class BasePresenter<out V>(view: V, listener: OnToastListener) {

    private var mContextRef: Reference<V> = WeakReference(view)
    private var mViewRef: Reference<V> = WeakReference(view)
    private var mViewToast: Reference<OnToastListener> = WeakReference(listener)

    private val requests  = ArrayList<RequestCallBack<*>>()
    /**消息提示*/
    protected fun showToast(msg: String, isShort: Boolean) {
        mViewToast.get()?.toast(msg, isShort)
    }

    protected fun showToast(msgId: Int, isShort: Boolean) {
        mViewToast.get()?.toast(msgId, isShort)
    }

    protected fun <T> createRequest(request: RequestCallBack<T>): RequestCallBack<T>{
        requests.add(request)
        return request
    }

    /**
     * 获取MVP中的View
     */
    protected fun getView(): V? = mViewRef.get()

    /**对应Activity 的 onDestroy 生命周期*/
    open fun onDestroy(){
        requests.forEach { it.cancelRequest() }
        mViewRef.clear()
        mViewToast.clear()
    }

}