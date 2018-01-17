package com.yzy.ebag.teacher.http

import com.yzy.ebag.teacher.bean.AssignmentBean
import com.yzy.ebag.teacher.bean.FirstPageBean
import com.yzy.ebag.teacher.bean.GroupBean
import com.yzy.ebag.teacher.bean.SpaceBean
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
}