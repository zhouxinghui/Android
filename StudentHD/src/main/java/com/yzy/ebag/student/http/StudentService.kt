package com.yzy.ebag.student.http

import com.yzy.ebag.student.bean.*
import ebag.core.bean.ResponseBean
import ebag.core.bean.TypeQuestionBean
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

    /**
     * 获取单元数据
     */
    @POST("util/getBookVersionOrUnit/{version}")
    fun getUint(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<EditionBean>>

    /**
     * 获取作业详情
     */
    @POST("homeWork/getHomeWorkByQuestion/{version}")
    fun getQuestions(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<TypeQuestionBean>>>


    /**
     * 获取跟读列表
     */
    @POST("util/getOralLanguage/{version}")
    fun getReadList(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ReadOutBean>>

    /**
     * 获取练字列表
     */
    @POST("util/getNewWord/{version}")
    fun getWordsList(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<WordsBean>>

}