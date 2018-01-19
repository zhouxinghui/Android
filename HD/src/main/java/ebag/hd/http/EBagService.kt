package ebag.hd.http

import ebag.core.bean.ResponseBean
import ebag.core.http.baseBean.RequestBean
import ebag.hd.bean.ChildNodeBean
import ebag.hd.bean.request.CodeVo
import ebag.hd.bean.request.LoginVo
import ebag.hd.bean.response.CodeEntity
import ebag.hd.bean.response.UserEntity
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Retrofit 主要使网络请求相关的接口、参数
 * Created by unicho on 2017/11/1.
 */
interface EBagService {

    /**
     * 省市区县基础数据
     * @return
     */
    @POST("data/getProvincCityReginCascade/{version}")
    fun cityData(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<ChildNodeBean>>>

    /**
     * 登录
     * @return
     */
    @Headers("EBag-Special-Url: special/url")
    @POST("user/login/{version}")
    fun login(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<UserEntity>>

    /**
     * 登录
     * @return
     */
    @POST("services/register")
    fun register(@Body requestBody: RequestBean<LoginVo>): Observable<ResponseBean<UserEntity>>

    /**
     * 城市标签
     * @return
     */
    @POST("services/exam/getSuccess")
    fun getPhoneCode(@Body requestBody: RequestBean<CodeVo>): Observable<ResponseBean<CodeEntity>>

    //TODO 接口名称，注意替换
    /**
     * 上传文件,多文件 和 单文件
     *
     * @param file
     * @return
     */
    @POST("Ui/Image/uploadUnlimited.html")
    fun uploadHead(@Body file: MultipartBody): Observable<String>
}