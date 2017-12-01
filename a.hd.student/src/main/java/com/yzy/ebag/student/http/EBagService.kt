package com.yzy.ebag.student.http

import com.yzy.ebag.student.bean.request.CodeVo
import com.yzy.ebag.student.bean.request.LoginVo
import com.yzy.ebag.student.bean.response.CodeEntity
import com.yzy.ebag.student.bean.response.UserEntity
import com.yzy.ebag.student.http.baseBean.QuestionErrEntity
import com.yzy.ebag.student.http.baseBean.RequestBean
import com.yzy.ebag.student.http.baseBean.ResponseBean
import com.yzy.ebag.student.http.baseBean.ResponseEntity
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Retrofit 主要使网络请求相关的接口、参数
 * Created by unicho on 2017/11/1.
 */
interface EBagService {

    /**
     * 城市标签
     * @return
     */
    @POST("services/exam/searchQuestionErr")
    fun getSmscode(@Body requestBody: RequestBean<QuestionErrEntity>): Observable<ResponseBean<List<ResponseEntity>>>

    /**
     * 登录
     * @return
     */
    @POST("services/login/{version}")
    fun login(@Path("version") version: String,  @Body requestBody: RequestBean<LoginVo>): Observable<ResponseBean<UserEntity>>

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
    @POST("services/exam/getCode")
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