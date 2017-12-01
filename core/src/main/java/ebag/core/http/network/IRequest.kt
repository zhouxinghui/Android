package ebag.core.http.network

/**
 * Created by unicho on 2017/11/9.
 */
interface IRequest<in T> {
    /**
     * 网络请求前
     */
    fun onStart()

    /**
     * 网络请求成功
     * @param entity
     */
    fun onSuccess(entity: T)

    /**
     * 异常处理
     */
    fun onError(exception: Throwable)

    /**
     * 网络请求成功
     */
    fun onFinish()
}