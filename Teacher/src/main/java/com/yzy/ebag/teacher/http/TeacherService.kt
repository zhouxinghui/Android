package com.yzy.ebag.teacher.http

import com.yzy.ebag.teacher.bean.AssignmentBean
import com.yzy.ebag.teacher.bean.BookVersionBean
import com.yzy.ebag.teacher.bean.FirstPageBean
import com.yzy.ebag.teacher.bean.TestPaperListBean
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
}