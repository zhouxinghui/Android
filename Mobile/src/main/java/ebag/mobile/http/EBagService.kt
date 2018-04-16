package ebag.mobile.http

import ebag.core.bean.ResponseBean
import ebag.mobile.bean.UserEntity
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Retrofit 主要使网络请求相关的接口、参数
 * Created by caoyu on 2017/11/1.
 */
interface EBagService {

    /**登录*/
    @Headers("EBag-Special-Url: special/url")
    @POST("user/login/{version}")
    fun login(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<UserEntity>>

    /**注册*/
    @Headers("EBag-Special-Url: special/url")
    @POST("user/register/{version}")
    fun register(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<UserEntity>>

    /**重置密码*/
    @Headers("EBag-Special-Url: special/url")
    @POST("user/updatePassword/{version}")
    fun resetPassword(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**检查用户是否存在*/
    @Headers("EBag-Special-Url: special/url")
    @POST("user/checkUserIsExist/{version}")
    fun checkUserExist(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**获取验证码*/
    @Headers("EBag-Special-Url: special/url")
    @POST("util/sendMessage/{version}")
    fun getCheckCode(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>
}