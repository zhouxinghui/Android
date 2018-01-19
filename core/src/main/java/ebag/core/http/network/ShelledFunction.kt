package ebag.core.http.network

import ebag.core.bean.ResponseBean
import ebag.core.http.network.MsgException
import io.reactivex.functions.Function

/**
 * 返回数据的去壳操作
 * Created by unicho on 2017/11/13.
 */
class ShelledFunction<T>: Function<ResponseBean<T>,T> {
    override fun apply(t: ResponseBean<T>): T {
        if("200" != t.success)
            throw MsgException(t.success,t.message)
        return t.data
    }
}