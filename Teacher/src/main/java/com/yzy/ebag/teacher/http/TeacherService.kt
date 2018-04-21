package com.yzy.ebag.teacher.http

import com.yzy.ebag.teacher.bean.*
import ebag.core.bean.QuestionBean
import ebag.core.bean.ResponseBean
import ebag.mobile.bean.NoticeBean
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
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

    /**打分*/
    @POST("correctHome/teacherCurrent/{version}")
    fun markScore(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**班级列表*/
    @POST("clazz/queryMyClassInfo/{version}")
    fun clazzSpace(@Path("version") version: String): Observable<ResponseBean<List<SpaceBean>>>

    /**添加老师*/
    @POST("clazz/joinTeacherBySubject/{version}")
    fun addTeacher(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 获取基础数据的接口
     */
    @Headers("EBag-Special-Url: special/url")
    @POST("data/queryBaserData/{version}")
    fun getBaseData(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<BaseSubjectBean>>>

    /**查询最新公告*/
    @POST("notice/queryNewClassNotice/{version}")
    fun newestNotice(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<NoticeBean>>

    /**创建学习小组*/
    @POST("clazz/createByClazzGroup/{version}")
    fun createGroup(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**修改小组*/
    @POST("clazz/modifyClassByGroup/{version}")
    fun modifyGroup(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**删除学习小组*/
    @POST("clazz/deleteClassByGroup/{version}")
    fun deleteGroup(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**发布公告*/
    @POST("notice/sendClassNotice/{version}")
    fun publishNotice(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**老师添加班级所教科目*/
    @POST("clazz/addTaughtCoruses/{version}")
    fun addCourse(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**添加所教课程-教材版本数据*/
    @POST("clazz/getPublishedByGrade/{version}")
    fun courseVersionData(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<AddCourseTextbookBean>>

    /**删除所教课程*/
    @POST("clazz/deleteTaughtCourses/{version}")
    fun deleteCourse(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**查询所教课程*/
    @POST("clazz/getTaughtCourses/{version}")
    fun searchCourse(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<MyCourseBean>>>

    /**课堂表现列表*/
    @POST("clazzSpace/queryUserClazzRoomShowAll/{version}")
    fun classPerformance(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<PerformanceBean>>>

    /**修改学生课堂表现*/
    @POST("clazzSpace/addClazzRoomShow/{version}")
    fun modifyPerformance(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>
}