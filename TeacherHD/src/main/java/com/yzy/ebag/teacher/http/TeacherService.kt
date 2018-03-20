package com.yzy.ebag.teacher.http

import com.yzy.ebag.teacher.bean.*
import ebag.core.bean.QuestionBean
import ebag.core.bean.ResponseBean
import ebag.hd.bean.response.NoticeBean
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
    /**
     * 主页
     * @return
     */
    @POST("user/getOnePageInfo/{version}")
    fun firstPage(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<FirstPageBean>>

    /**
     * 班级列表
     */
    @POST("clazz/queryMyClassInfo/{version}")
    fun clazzSpace(@Path("version") version: String): Observable<ResponseBean<List<SpaceBean>>>

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

    /**
     * 修改小组
     */
    @POST("clazz/modifyClassByGroup/{version}")
    fun modifyGroup(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 删除学习小组
     */
    @POST("clazz/deleteClassByGroup/{version}")
    fun deleteGroup(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 获取基础数据的接口
     */
    @Headers("EBag-Special-Url: special/url")
    @POST("data/queryBaserData/{version}")
    fun getBaseData(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<BaseSubjectBean>>>

    /**
     * 创建班级
     */
    @POST("clazz/createClazz/{version}")
    fun createClass(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 根据班级获取教材版本
     */
    @POST("sendHome/getBookVersion/{version}")
    fun searchBookVersion(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<BookVersionBean>>>

    /**
     * 查询最新公告
     */
    @POST("notice/queryNewClassNotice/{version}")
    fun newestNotice(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<NoticeBean>>

    /**
     * 发布公告
     */
    @POST("notice/sendClassNotice/{version}")
    fun publishNotice(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 添加老师
     */
    @POST("clazz/joinTeacherBySubject/{version}")
    fun addTeacher(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 查询试题
     */
    @POST("question/queryQuestion/{version}")
    fun searchQuestion(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<QuestionBean>>>

    /**
     * 智能推送
     */
    @POST("question/smartChoice/{version}")
    fun smartPush(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<QuestionBean>>>

    /**
     * 试卷列表
     */
    @POST("sendHome/queryTestPaper/{version}")
    fun testPaperList(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<TestPaperListBean>>>

    /**
     * 组卷
     */
    @POST("sendHome/addTestPaper/{version}")
    fun organizePaper(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 预览试卷
     */
    @POST("sendHome/queryTestPaperQuestion/{version}")
    fun previewTestPaper(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<QuestionBean>>>

    /**
     * 发布作业
     */
    @POST("sendHome/sendHome/{version}")
    fun publishHomework(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 查询已经布置的作业列表
     */
    @POST("sendHome/searchSendHomeWork/{version}")
    fun searchPublish(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<CorrectingBean>>>

    /**
     * 查询所教课程
     */
    @POST("clazz/getTaughtCourses/{version}")
    fun searchCourse(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<MyCourseBean>>>

    /**
     * 添加所教课程-教材版本数据
     */
    @POST("clazz/getPublishedByGrade/{version}")
    fun courseVersionData(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<BookVersionBean>>>

    /**
     * 老师添加班级所教科目
     */
    @POST("clazz/addTaughtCoruses/{version}")
    fun addCourse(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 检查作业
     */
    @POST("homeWork/getHomeWorkByQuestion/{version}")
    fun correctWork(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<QuestionBean>>>

    /**
     * 检查作业学生答案
     */
    @POST("correctHome/studentHomeWorkAnswer/{version}")
    fun correctStudentAnswer(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<CorrectAnswerBean>>>

    /**
     * 打分
     */
    @POST("correctHome/teacherCurrent/{version}")
    fun markScore(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 评语列表
     */
    @POST("correctHome/studentHomeWorkComment/{version}")
    fun commentList(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<CommentBean>>>

    /**
     * 提交评语
     */
    @POST("correctHome/correctComment/{version}")
    fun uploadComment(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 备课-默认数据
     */
    @POST("clazzSpace/getBaseData/{version}")
    fun prepareBaseData(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<PrepareBaseBean>>

    /**
     * 备课文件列表
     */
    @POST("clazzSpace/getLessonFileInfoByPage/{version}")
    fun prepareList(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<PrepareFileBean>>>

    /**
     * 删除指定备课文件
     */
    @POST("clazzSpace/delLessonFileInfoById/{version}")
    fun deletePrepareFile(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 修改个人信息
     */
    @POST("user/modifyPersonalCenter/{version}")
    fun modifyPersonalInfo(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 课堂表现列表
     */
    @POST("clazzSpace/queryUserClazzRoomShowAll/{version}")
    fun classPerformance(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<PerformanceBean>>>

}