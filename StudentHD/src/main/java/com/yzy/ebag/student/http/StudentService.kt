package com.yzy.ebag.student.http

import com.yzy.ebag.student.bean.ClassesInfoBean
import com.yzy.ebag.student.bean.SpaceBean
import com.yzy.ebag.student.bean.SubjectBean
import ebag.core.bean.ResponseBean
import ebag.hd.bean.response.UserEntity
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by caoyu on 2018/1/8.
 */
interface StudentService {
    /**
     * 登录
     * @return
     */
    @POST("user/login/{version}")
    fun login(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<UserEntity>>

    /**
     * 首页
     * @return
     */
    @POST("user/getOnePageInfo/{version}")
    fun mainInfo(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ClassesInfoBean>>
    /**
     * 随堂 课后作业
     * @return
     */
    @POST("homeWork/getMyHomeWork/{version}")
    fun subjectWorkList(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ArrayList<SubjectBean>>>

    /**
     * 班级
     */
    @POST("clazz/queryMyClassInfo/{version}")
    fun clazzSpace(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<SpaceBean>>>
}