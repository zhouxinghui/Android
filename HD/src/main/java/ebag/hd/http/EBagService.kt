package ebag.hd.http

import ebag.core.bean.ResponseBean
import ebag.hd.bean.*
import ebag.hd.bean.response.NoticeBean
import ebag.hd.bean.response.UserEntity
import io.reactivex.Observable
import okhttp3.MultipartBody
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

    /**
     * 省市区县基础数据
     * @return
     */
    @POST("data/getAreaData/{version}")
    fun cityData(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<ChildNodeBean>>>

    /**
     * 获取验证码
     * @return
     */

    @Headers("EBag-Special-Url: special/url")
    @POST("util/sendMessage/{version}")
    fun getCheckCode(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>


    /**
     * 加入班级
     */
    @POST("clazz/joinByClass/{version}")
    fun joinClass(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

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
     * 照片分享
     */
    @POST("clazz/deletePhoto/{version}")
    fun photosDelete(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 登录
     * @return
     */
    @Headers("EBag-Special-Url: special/url")
    @POST("user/login/{version}")
    fun login(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<UserEntity>>

    /**
     * 登录
     * @return
     */
    @Headers("EBag-Special-Url: special/url")
    @POST("user/register/{version}")
    fun register(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<UserEntity>>

    /**
     * 重置密码
     */
    @Headers("EBag-Special-Url: special/url")
    @POST("user/checkUserIsExist/{version}")
    fun checkUserExist(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 重置密码
     */
    @Headers("EBag-Special-Url: special/url")
    @POST("user/updatePwdByPhone/{version}")
    fun resetPassword(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     *  查询学校
     * @return
     */
    @POST("data/getSchool/{version}")
    fun getSchool(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<SchoolBean>>>

    /**
     * 公告列表
     */
    @POST("notice/queryClassNotice/{version}")
    fun noticeList(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<NoticeBean>>>

    /**
     * 作业报告
     */
    @POST("correctHome/createHomeWorkRep/{version}")
    fun homeworkReport(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ReportBean>>
    /**
     * 我的课本
     */
    @POST("book/myBook/{version}")
    fun myBookList(@Path("version") version: String): Observable<ResponseBean<List<BookBean>>>

    //TODO 接口名称，注意替换
    /**
     * 上传文件,多文件 和 单文件
     *
     * @param file
     * @return
     */
    @POST("Ui/Image/uploadUnlimited.html")
    fun uploadHead(@Body file: MultipartBody): Observable<String>
}