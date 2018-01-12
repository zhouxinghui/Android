package ebag.core.http.network

/**
 * Created by unicho on 2017/11/9.
 */
abstract class RequestCallBack<in T>: IRequest<T>{

    private var onRequestCancelListener: OnRequestCancelListener? = null

    override fun onStart() {

    }

    override fun onFinish() {

    }

    fun setOnRequestCancelListener(onRequestCancelListener: OnRequestCancelListener) {
        this.onRequestCancelListener = onRequestCancelListener
    }

    /**
     * 取消网络请求的方法
     */
    fun cancelRequest() {
        if (onRequestCancelListener != null) {
            onRequestCancelListener!!.onCancel()
        }
    }
}