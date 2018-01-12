package com.yzy.ebag.teacher.http

import com.yzy.ebag.teacher.bean.AssignmentBean
import com.yzy.ebag.teacher.bean.FirstPageBean
import ebag.hd.http.baseBean.ResponseBean
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by unicho on 2018/1/8.
 */
interface TeacherService {
    /**
     * 主页
     * @return
     */
    @POST("user/getOnePageInfo/{version}")
    fun firstPage(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<FirstPageBean>>

    /**
     * 布置作业页面
     */
    @POST("sendHome/sendHomePageData/{version}")
    fun assignmentData(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<AssignmentBean>>
}