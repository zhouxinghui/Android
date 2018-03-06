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
     * 我的错题
     */
    @POST("homeWork/myErrorHomeWork/{version}")
    fun errorTopic(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ArrayList<ErrorTopicBean>>>
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
    @POST("homeWork/getHomeWorkQuestion/{version}")
    fun getQuestions(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<TypeQuestionBean>>>

    /**
     * 获取错题详情
     */
    @POST("homeWork/myErrorQuestion/{version}")
    fun getErrorDetail(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<TypeQuestionBean>>>

    /**
     * 提交作业
     */
    @POST("correctHome/currentHomeWork/{version}")
    fun commitHomework(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 错题纠正
     */
    @POST("homeWork/updateErrorQuestion/{version}")
    fun errorCorrection(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 获取跟读列表
     */
    @POST("util/getOralLanguage/{version}")
    fun getReadList(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ReadOutBean>>


    /**
     * 获取跟读列表里头的每个句子的详情
     */
    @POST("util/getOralLanguageDetail/{version}")
    fun getReadDetailList(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<ReadDetailBean>>>

    /**
     * 获取练字列表
     */
    @POST("util/getNewWord/{version}")
    fun getWordsList(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<WordsBean>>

    /**
     * 上传跟读录音文件
     */
    @POST("user/addMyOralLanguage/{version}")
    fun uploadRecord(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 获取当前段落的录音历史
     */
    @POST("user/searchMyOralLanguageByUid/{version}")
    fun recordHistory(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<RecordHistory>>>


    /**
     * 获取学习小组
     */
    @POST("clazz/getMyClazzByGroup/{version}")
    fun groups(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ArrayList<GroupBean>>>

    /**
     * 获取小组成员
     */
    @POST("clazz/getMyClazzByGroup/{version}")
    fun groupMember(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ArrayList<GroupUserBean>>>


}