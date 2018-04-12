package com.yzy.ebag.student.http

import com.yzy.ebag.student.bean.*
import ebag.core.bean.ResponseBean
import ebag.hd.bean.ParentBean
import ebag.hd.bean.response.UserEntity
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

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
    @POST("util/addMyOralLanguage/{version}")
    fun uploadRecord(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 获取当前段落的录音历史
     */
    @POST("util/searchMyOralLanguageByUid/{version}")
    fun recordHistory(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<RecordHistory>>>


    /**
     * 获取学习小组
     */
    @POST("clazz/getMyClazzByGroup/{version}")
    fun groups(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ArrayList<GroupBean>>>

    /**
     * 获取小组成员
     */
    @POST("clazz/getClazzUserByGroup/{version}")
    fun groupMember(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ArrayList<GroupUserBean>>>

    /**
     * 劳动任务
     */
    @POST("user/searchChildTargetList/{version}")
    fun labourTasks(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ArrayList<LabourBean>>>

    /**
     * 数学公式
     */
    @POST("util/queryFormulaTool/{version}")
    fun formula(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ArrayList<FormulaTypeBean>>>

    /**
     *
     */
    @Headers("EBag-Special-Url: special/url")
    @GET("https://openapi.baidu.com/oauth/2.0/token")
    fun baiduOauth(
            @Query("grant_type") grant_type: String,
            @Query("client_id") client_id: String,
            @Query("client_secret") client_secret: String
        ): Observable<BaiduOauthBean>

    @Headers("EBag-Special-Url: special/url")
    @POST("http://vop.baidu.com/server_api")
    fun speachRecognize(@Body requestBody: RequestBody): Observable<SpeechRecognizeBean>
    @POST("user/growth/addUserGrowth/{version}")
    fun addUserGrowth(@Path("version") version: String, @Body requestBody: RequestBody):Observable<ResponseBean<String>>
    @POST("user/searchUserGrowthList/{version}")
    fun searchUserGrowthList(@Path("version") version: String, @Body requestBody: RequestBody):Observable<ResponseBean<List<Diary>>>

    @POST("user/addUserPositioning/{version}")
    fun uploadLocation(@Path("version") version: String, @Body requestBody: RequestBody):Observable<ResponseBean<String>>

    @POST("user/searchUserPositioningList/{version}")
    fun searchLocation(@Path("version") version: String, @Body requestBody: RequestBody):Observable<ResponseBean<LocationBean>>

    @POST("util/insetWordrecord/{version}")
    fun uploadWord(@Path("version") version: String, @Body requestBody: RequestBody):Observable<ResponseBean<String>>

    @POST("util/queryWordrecord/{version}")
    fun wordRecord(@Path("version") version: String, @Body requestBody: RequestBody):Observable<ResponseBean<WordRecordBean>>
    /*查询家长*/
    @POST("user/searchFamily/{version}")
    fun searchFamily(@Path("version") version: String, @Body requestBody: RequestBody):Observable<ResponseBean<List<ParentBean>>>
    /*绑定家长*/
    @POST("user/bindingParent/{version}")
    fun bindParent(@Path("version") version: String, @Body requestBody: RequestBody):Observable<ResponseBean<String>>

    @POST("user/learningProcess/{version}")
    fun learningProcess(@Path("version") version: String, @Body requestBody: RequestBody):Observable<ResponseBean<List<LeaningProgressBean>>>

    @POST("homeStatistics/yearStatisticsByHomeWork/{version}")
    fun yearStatisticsByHomeWork(@Path("version") version: String, @Body requestBody: RequestBody):Observable<ResponseBean<List<HomeworkBean>>>



}