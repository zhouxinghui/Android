package com.yzy.ebag.student.http

import com.yzy.ebag.student.bean.ClassesInfoBean
import ebag.core.bean.ResponseBean
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by YZY on 2018/5/14.
 */
interface StudentService {
    /**
     * 首页
     * @return
     */
    @POST("user/getOnePageInfo/{version}")
    fun mainInfo(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ClassesInfoBean>>
}