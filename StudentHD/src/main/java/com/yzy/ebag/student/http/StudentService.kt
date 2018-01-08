package com.yzy.ebag.student.http

import ebag.hd.bean.response.UserEntity
import ebag.hd.http.baseBean.ResponseBean
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by unicho on 2018/1/8.
 */
interface StudentService {
    /**
     * 登录
     * @return
     */
    @POST("user/login/{version}")
    fun login(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<UserEntity>>
}