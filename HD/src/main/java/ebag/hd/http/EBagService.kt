package ebag.hd.http

import ebag.core.bean.ResponseBean
import ebag.core.bean.TypeQuestionBean
import ebag.hd.bean.*
import ebag.hd.bean.response.BaseInfoEntity
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
     * 照片删除
     */
    @POST("clazz/deletePhoto/{version}")
    fun photosDelete(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 照片上传
     */
    @POST("clazz/uploadPhotos/{version}")
    fun photosUpload(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>


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
     * 检查用户是否存在
     */
    @Headers("EBag-Special-Url: special/url")
    @POST("user/checkUserIsExist/{version}")
    fun checkUserExist(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 重置密码
     */
    @Headers("EBag-Special-Url: special/url")
    @POST("user/updatePassword/{version}")
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
    @POST("book/getstudentBook/{version}")
    fun studentBookList(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<BookBean>>>

    /**
     * 课程表
     */
    @POST("clazzSpace/queryScheduleCard/{version}")
    fun classSchedule(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ClassScheduleBean>>

    /**
     * 编辑课程表
     */
    @POST("clazzSpace/addScheduleCard/{version}")
    fun editSchedule(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 编辑课程表
     */
    @POST("data/queryBaserData/{version}")
    fun getBaseInfo(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ArrayList<BaseInfoEntity>>>

    /**
     * 新增课本笔记
     */
    @POST("book/addNotebook/{version}")
    fun addBookNote(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 查询课本笔记列表
     */
    @POST("book/queryNotebook/{version}")
    fun bookNoteList(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<BookNoteBean>>>

    /**
     * 修改笔记
     */
    @POST("book/updateNotebook/{version}")
    fun modifyNote(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 删除笔记
     */
    @POST("book/deleteNotebook/{version}")
    fun deleteNote(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

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
     * 获取单元数据
     */
    @POST("util/getBookVersionOrUnit/{version}")
    fun getUnit(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<EditionBean>>

    /**
     * 获取用户的所有所在班级
     */
    @POST("clazz/queryAllClazzInfo/{version}")
    fun getMyClasses(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<BaseClassesBean>>>

    /**
     * 自习室-生字总览列表
     */
    @POST("util/queryNewWordsTime/{version}")
    fun getLetterRecord(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<LetterRecordBaseBean>>>

    /**
     * 自习室-生字详情
     */
    @POST("util/queryNewWords/{version}")
    fun getLetterDesc(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<LetterDescBean>>

    /**
     * 自习室-上传生字评分
     */
    @POST("util/correctWords/{version}")
    fun uploadReadScore(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 自习室-口语总览列表
     */
    @POST("/util/searchOralCount/{version}")
    fun getReadRecord(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<ReadRecordBaseBean>>>

    /**
     * 自习-口语学生作答详情
     */
    @POST("/util/searchOralRecord/{version}")
    fun getReadRecordDesc(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<ReadRecordAnswerBean>>>

    /**
     * 检查版本更新
     */
    @Headers("EBag-Special-Url: special/url")
    @POST("user/MobileAPPVersion/{version}")
    fun checkUpdate(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<VersionUpdateBean>>

    /**
     * 课本目录
     */
    @POST("book/myBookChapter/{version}")
    fun bookCategory(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<BookCategoryBean>>

    /**
     * 修改个人信息
     */
    @POST("user/modifyPersonalCenter/{version}")
    fun modifyPersonalInfo(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 用户反馈
     */
    @POST("user/addUserFeedback/{version}")
    fun userFeedback(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

    /**
     * 课堂表现-个人详情
     */
    @POST("clazzSpace/queryUserClazzRoomShow/{version}")
    fun personalPerformance(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<PersonalPerformanceBean>>
    /**
     * 获取跟读列表里头的每个句子的详情
     */
    @POST("util/getOralLanguageDetailByUnitCode/{version}")
    fun getReadDetailList(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<ReadRecordVoiceBean>>>

    /**
     * 跟读-报告-切换版本
     */
    @POST("util/getBookVersion/{version}")
    fun readRecordVersion(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ReadRecordVersionBean>>

    /**
     * 官方公告
     */
    @POST("user/APPVersion/{version}")
    fun officialAnnounce(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<OfficialAnnounceBean>>

    /**查询个人中心*/
    @POST("user/queryPersonalCenter/{version}")
    fun queryPersonalCenter(@Path("version") version: String, @Body requestBody: RequestBody):Observable<ResponseBean<UserInfoBean>>


    //TODO 接口名称，注意替换
    /**
     * 上传文件,多文件 和 单文件
     *
     * @param file
     * @return
     */
    @POST("Ui/Image/uploadUnlimited.html")
    fun uploadHead(@Body file: MultipartBody): Observable<String>

    @POST("user/serchYsbMoneyDetail/{version}")
    fun queryYBCurrent(@Path("version")version: String, @Body requestBody: RequestBody):Observable<ResponseBean<YBCurrentBean>>
    @POST("shop/serchShopAddress/{version}")
    fun queryAddress(@Path("version")version: String,@Body requestBody: RequestBody):Observable<ResponseBean<MutableList<AddressListBean>>>
    @POST("shop/deleteShopAddress/{version}")
    fun deleteAddress(@Path("version")version: String,@Body requestBody: RequestBody):Observable<ResponseBean<String>>
    @POST("shop/saveShopAddress/{version}")
    fun saveAddress(@Path("version")version: String,@Body requestBody: RequestBody):Observable<ResponseBean<String>>
    @POST("shop/updateShopAddress/{version}")
    fun updateAddress(@Path("version")version: String,@Body requestBody: RequestBody):Observable<ResponseBean<String>>
    @POST("shop/serchOrderByStatus/{version}")
    fun queryOrder(@Path("version")version: String,@Body requestBody: RequestBody):Observable<ResponseBean<QueryOrderBean>>
    @POST("shop/getShopList/{version}")
    fun getShopList(@Path("version")version: String,@Body requestBody: RequestBody):Observable<ResponseBean<ShopListBean>>
    @POST("shop/serchShopCart/{version}")
    fun queryShopCar(@Path("version")version: String,@Body requestBody: RequestBody):Observable<ResponseBean<MutableList<ShopListBean.ListBean>>>
    @POST("shop/addShop2Cart/{version}")
    fun addGoods2Car(@Path("version")version: String,@Body requestBody: RequestBody):Observable<ResponseBean<String>>
    @POST("shop/getShopDetailInfo/{version}")
    fun shopDeatils(@Path("version")version: String,@Body requestBody: RequestBody):Observable<ResponseBean<GoodsDetailsBean>>
    @POST("shop/updateShopCart/{version}")
    fun updateShopCar(@Path("version")version: String,@Body requestBody: RequestBody):Observable<ResponseBean<String>>
    @POST("shop/createShopOrderNo/{version}")
    fun createShopOrderNo(@Path("version")version: String,@Body requestBody: RequestBody):Observable<ResponseBean<String>>
    @POST("shop/WXPay/{version}")
    fun getPrepayid(@Path("version")version: String,@Body requestBody: RequestBody):Observable<ResponseBean<WXPayBean>>
    /*保存订单*/
    @POST("shop/saveOrder/{version}")
    fun saveOrder(@Path("version") version: String,@Body requestBody: RequestBody): Observable<ResponseBean<String>>
    /*支付宝*/
    @POST("shop/AliPay/{version}")
    fun getAiliPrepayid(@Path("version") version: String,@Body requestBody: RequestBody): Observable<ResponseBean<String>>
    @POST("shop/removeShopCart/{version}")
    fun removeShopCar(@Path("version") version: String,@Body requestBody: RequestBody): Observable<ResponseBean<String>>
    //云币支付
    @POST("shop/ybPay/{version}")
    fun ybPay(@Path("version") version: String,@Body requestBody: RequestBody): Observable<ResponseBean<String>>


    /**
     * 根据班级查询班级下所有的成员（老师，学生，家长）
     */
    @POST("clazz/getClassUserByAll/{version}")
    fun clazzMember(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<ClassMemberBean>>

    //更新订单状态
    @POST("shop/updateShopOrderStatus/{version}")
    fun updateShopOrderStaus(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<String>>

}