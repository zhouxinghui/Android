package com.yzy.ebag.teacher.http

import com.yzy.ebag.teacher.bean.*
import ebag.core.bean.ResponseBean
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by caoyu on 2018/1/8.
 */
interface TeacherService {
    /**
     * 主页
     * @return
     */
    @POST("user/getOnePageInfo/{version}")
    fun firstPage(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<FirstPageBean>>

    /**
     * 班级
     */
    @POST("clazz/queryMyClassInfo/{version}")
    fun clazzSpace(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<SpaceBean>>>

    /**
     * 布置作业页面
     */
    @POST("sendHome/sendHomePageData/{version}")
    fun assignmentData(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<AssignmentBean>>

    /**
     * 根据班级查询班级下所有的学习小组
     */
    @POST("clazz/searchClassByGroupAll/{version}")
    fun studyGroup(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<GroupBean>>>

    /**
     * 根据班级查询班级下所有的成员（老师，学生，家长）
     */
    @POST("clazz/getClassUserByAll/{version}")
    fun clazzMember(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ClassMemberBean>>

    /**
     * 创建学习小组
     */
    @POST("clazz/createByClazzGroup/{version}")
    fun createGroup(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>
}