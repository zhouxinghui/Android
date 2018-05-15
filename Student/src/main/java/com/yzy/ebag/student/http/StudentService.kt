package com.yzy.ebag.student.http

import com.yzy.ebag.student.bean.ClassesInfoBean
import com.yzy.ebag.student.bean.LabourBean
import com.yzy.ebag.student.bean.ParentBean
import com.yzy.ebag.student.bean.SubjectBean
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
    /**首页*/
    @POST("user/getOnePageInfo/{version}")
    fun mainInfo(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ClassesInfoBean>>

    /**劳动任务*/
    @POST("user/searchChildTargetList/{version}")
    fun labourTasks(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ArrayList<LabourBean>>>

    /**随堂 课后作业*/
    @POST("homeWork/getMyHomeWork/{version}")
    fun subjectWorkList(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ArrayList<SubjectBean>>>

    /**查询家长*/
    @POST("user/searchFamily/{version}")
    fun searchFamily(@Path("version") version: String, @Body requestBody: RequestBody):Observable<ResponseBean<List<ParentBean>>>

    /**绑定家长*/
    @POST("user/bindingParent/{version}")
    fun bindParent(@Path("version") version: String, @Body requestBody: RequestBody):Observable<ResponseBean<String>>
}