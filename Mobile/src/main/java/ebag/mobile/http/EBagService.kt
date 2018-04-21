package ebag.mobile.http

import ebag.core.bean.ResponseBean
import ebag.mobile.bean.*
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Retrofit 主要使网络请求相关的接口、参数
 * Created by caoyu on 2017/11/1.
 */
interface EBagService {

    /**登录*/
    @Headers("EBag-Special-Url: special/url")
    @POST("user/login/{version}")
    fun login(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<UserEntity>>

    /**注册*/
    @Headers("EBag-Special-Url: special/url")
    @POST("user/register/{version}")
    fun register(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<UserEntity>>

    /**重置密码*/
    @Headers("EBag-Special-Url: special/url")
    @POST("user/updatePassword/{version}")
    fun resetPassword(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**检查用户是否存在*/
    @Headers("EBag-Special-Url: special/url")
    @POST("user/checkUserIsExist/{version}")
    fun checkUserExist(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**获取验证码*/
    @Headers("EBag-Special-Url: special/url")
    @POST("util/sendMessage/{version}")
    fun getCheckCode(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**获取用户的所有所在班级*/
    @POST("clazz/queryAllClazzInfo/{version}")
    fun getMyClasses(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<BaseClassesBean>>>

    /**根据班级查询班级下所有的成员（老师，学生，家长）*/
    @POST("clazz/getClassUserByAll/{version}")
    fun clazzMember(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ClassMemberBean>>

    /**
     * 相册
     */
    @POST("clazz/queryPhotoGroup/{version}")
    fun albums(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ArrayList<AlbumBean>>>

    /**
     * 创建相册
     */
    @POST("clazz/addAlbum/{version}")
    fun createAlbum(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>


    /**
     * 相册详情
     */
    @POST("clazz/queryPhotos/{version}")
    fun albumDetail(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ArrayList<PhotoBean>>>

    /**
     * 照片分享
     */
    @POST("clazz/sharePhoto/{version}")
    fun photosShare(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 照片删除
     */
    @POST("clazz/deletePhoto/{version}")
    fun photosDelete(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 照片上传
     */
    @POST("clazz/uploadPhotos/{version}")
    fun photosUpload(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**公告列表*/
    @POST("notice/queryClassNotice/{version}")
    fun noticeList(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<NoticeBean>>>

    /**课程表*/
    @POST("clazzSpace/queryScheduleCard/{version}")
    fun classSchedule(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ClassScheduleBean>>

    /**编辑课程表*/
    @POST("clazzSpace/addScheduleCard/{version}")
    fun editSchedule(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 编辑课程表
     */
    @POST("data/queryBaserData/{version}")
    fun getBaseInfo(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ArrayList<BaseInfoEntity>>>

    /**课堂表现-个人详情*/
    @POST("clazzSpace/queryUserClazzRoomShow/{version}")
    fun personalPerformance(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<PersonalPerformanceBean>>
}