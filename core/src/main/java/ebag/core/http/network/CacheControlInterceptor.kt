package ebag.core.http.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Created by caoyu on 2017/11/13.
 */
class CacheControlInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response? {
        val request : Request? = chain?.request()
//        if () {//判断没有网络连接
//            request = request?.newBuilder()
//                    ?.cacheControl(CacheControl.FORCE_CACHE)
//                    ?.build()
//        }

        var response: Response? = chain?.proceed(request)

//        if (AppUtils.isNetworkConnected(mContext)) {
//            val maxAge = 60 * 60 // read from cache for 1 minute
//            response?.newBuilder()
//                    ?.removeHeader("Pragma")
//                    ?.header("Cache-Control", "public, max-age=" + maxAge)
//                    ?.build();
//        } else {
//            val maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
//            response?.newBuilder()
//                    ?.removeHeader("Pragma")
//                    ?.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                    ?.build();
//        }
        return response
    }

}