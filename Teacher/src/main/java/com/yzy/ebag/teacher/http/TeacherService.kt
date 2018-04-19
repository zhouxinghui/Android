package com.yzy.ebag.teacher.http

import com.yzy.ebag.teacher.bean.*
import ebag.core.bean.QuestionBean
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
    /**主页*/
    @POST("user/getOnePageInfo/{version}")
    fun firstPage(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<FirstPageBean>>

    /**布置作业页面*/
    @POST("sendHome/sendHomePageData/{version}")
    fun assignmentData(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<AssignmentBean>>

    /**布置作业页面-切换版本请求数据*/
    @POST("sendHome/sendHomePageUnitAndQuestionData/{version}")
    fun assignDataByVersion(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<AssignmentBean>>

    /**试卷列表*/
    @POST("sendHome/queryTestPaper/{version}")
    fun testPaperList(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<TestPaperListBean>>>

    /**根据班级获取教材版本*/
    @POST("sendHome/getBookVersion/{version}")
    fun searchBookVersion(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<BookVersionBean>>

    /**
     * 查询试题
     */
    @POST("question/queryQuestion/{version}")
    fun searchQuestion(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<QuestionBean>>>

    /**组卷*/
    @POST("sendHome/addTestPaper/{version}")
    fun organizePaper(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**智能推送*/
    @POST("question/smartChoice/{version}")
    fun smartPush(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<QuestionBean>>>

    /**预览试卷*/
    @POST("sendHome/queryTestPaperQuestion/{version}")
    fun previewTestPaper(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<QuestionBean>>>

    /**发布作业*/
    @POST("sendHome/sendHome/{version}")
    fun publishHomework(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**根据班级查询班级下所有的学习小组*/
    @POST("clazz/searchClassByGroupAll/{version}")
    fun studyGroup(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<GroupBean>>>
}