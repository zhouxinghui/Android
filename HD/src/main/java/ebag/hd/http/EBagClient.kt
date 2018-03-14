package ebag.hd.http

import android.os.Environment
import ebag.core.base.App
import ebag.core.http.network.FastJsonConverterFactory
import ebag.core.http.network.HttpLoggingInterceptor
import ebag.core.util.StringUtils
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
 * Created by caoyu on 2017/11/1.
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

    fun <T> createRetrofitService(clazz: Class<T>): T{
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
                .addInterceptor {
                    val original = it.request()

                    val request = original.newBuilder()

                    //如果没有配置 Content-Type，这边统一添加这个配置
                    var header = original.header("Content-Type")
                    if(StringUtils.isEmpty(header)){
                        request.addHeader("Content-Type", "application/json")
                    }

                    //如果没有配置 Accept，这边统一添加这个配置
                    header= original.header("Accept")
                    if(StringUtils.isEmpty(header)){
                        request.addHeader("Accept", "application/json")
                    }
                    //如果没有配置 Accept，这边统一添加这个配置
                    header= original.header("EBag-Special-Url")
                    if(StringUtils.isEmpty(header)){
                        if(StringUtils.isEmpty(original.url().queryParameter("access_token"))){
                            val url = original.url().toString()
                            request.url("${if(url.contains("?")) "$url&" else "$url?"}access_token=${App.TOKEN}")
                        }
                    }else{
                        request.removeHeader("EBag-Special-Url")
                    }

                    return@addInterceptor it.proceed(request.build())
                }
//                .cache(cache)
                .build()
        val retrofit = Retrofit.Builder()
                .client(builder)
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .baseUrl("http://www.yun-bag.com/ebag-portal/")
//                .baseUrl("http://192.168.1.155:9001/")
                .baseUrl("http://192.168.1.139:9001/")
//                .baseUrl("http://192.168.1.144:9001/")
//                .baseUrl("http://192.168.1.186:9001/")
                .build()
        return retrofit.create(clazz)
    }

}