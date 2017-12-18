package ebag.hd.http

import android.os.Environment
import ebag.core.http.network.FastJsonConverterFactory
import ebag.core.http.network.HttpLoggingInterceptor
import okhttp3.Cache
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 网络请求的全局配置
 * Created by unicho on 2017/11/1.
 */
object EBagClient {

    private fun getLogInterceptor(): Interceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private fun getCache(): Cache {
        val cachePath: String = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            (Environment.getExternalStorageDirectory().absolutePath
                    + File.separator + "ktravel" + File.separator + "cache" + File.separator)
        } else {
            (Environment.getExternalStorageDirectory().absolutePath
                    + File.separator + "ktravel" + File.separator + "cache" + File.separator)
            //            cachePath = MtaTravelApplication.getInstance().getFilesDir().getAbsolutePath()+ File.separator+"cache"+ File.separator;
        }
        val cacheFile = File(cachePath)
        if (!cacheFile.exists()) {
            cacheFile.mkdirs()
        }
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        return Cache(cacheFile, cacheSize.toLong())
    }

    val eBagService: EBagService by lazy {
        createRetrofitService(EBagService::class.java)
    }

    private fun <T> createRetrofitService(clazz: Class<T>): T{
        val builder = OkHttpClient.Builder()
                //错误重连
                .retryOnConnectionFailure(true)
                //设置超时
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                // 支持HTTPS
                .connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS)) //明文Http与比较新的Https
                .addInterceptor(getLogInterceptor())
//                .cache(cache)
                .build()
        val retrofit = Retrofit.Builder()
                .client(builder)
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://www.yun-bag.com/ebag-portal/")
                .build()
        return retrofit.create(clazz)
    }

}