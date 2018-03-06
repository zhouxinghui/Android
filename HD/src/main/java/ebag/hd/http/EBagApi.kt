package ebag.hd.http
import com.alibaba.fastjson.JSON
import ebag.core.base.App
import ebag.core.bean.ResponseBean
import ebag.core.http.baseBean.RequestBean
import ebag.core.http.network.*
import ebag.hd.bean.*
import ebag.hd.bean.response.NoticeBean
import ebag.hd.bean.response.UserEntity
import ebag.hd.http.EBagClient.eBagService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File


/**
 * Created by caoyu on 2017/11/9.
 */
object EBagApi {

    private val JSON_TYPE = MediaType.parse("application/json; charset=utf-8")


    /**返回的数据格式是按照我们自己定义的数据格式时调用此方法*/
    fun <T> request(ob: Observable<ResponseBean<T>>, callback: RequestCallBack<T>){
        if (!App.mContext!!.hasNetwork()){
            callback.onError(MsgException("500", "当前网络不可用，请检查网络设置！"))
            return
        }
        ob.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(EBagRequestObserver(callback))
    }

    /**返回的数据格式是按照我们自己定义的数据格式时调用此方法*/
    fun <T> startNormalRequest(ob: Observable<T>, callback: RequestCallBack<T>){
        if (!App.mContext!!.hasNetwork()){
            callback.onError(MsgException("500", "当前网络不可用，请检查网络设置！"))
            return
        }
        ob.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(NormalRequestObserver(callback))
    }

    fun <T> createBody(request: T): RequestBody {
        return RequestBody.create(JSON_TYPE, JSON.toJSONString(request))
    }

    fun createBody(jsonObject: JSONObject): RequestBody {
        return RequestBody.create(JSON_TYPE, jsonObject.toString())
    }

    fun createBody(string: String): RequestBody {
        return RequestBody.create(JSON_TYPE, string)
    }

    fun <T> getRequestBean(body: T): RequestBean<T> {
        val request = RequestBean<T>()
        request.setBody(body)
        return request
    }

    /**
     * 文件上传的测试写法
     *
     * @param test
     * @param file
     * @param callBack
     */
    fun testFile(test: String, file: File, callBack: RequestCallBack<String>) {

        val requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file)

        val multipartBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", file.name, requestFile)
                .addFormDataPart("test", test)
                .build()

        startNormalRequest(EBagClient.eBagService.uploadHead(multipartBody), callBack)
    }

    /**
     * 登录
     */
    fun login(account: String?, pwd: String?, loginType: Int, roleCode: String,thirdPartyToken: String?,thirdPartyUnionid:String?, callback: RequestCallBack<UserEntity>){
        val jsonObj = JSONObject()
        jsonObj.put("loginAccount",account)
        jsonObj.put("password",pwd)
        jsonObj.put("loginType",loginType)
        jsonObj.put("roleCode",roleCode)
        jsonObj.put("thirdPartyUnionid",thirdPartyUnionid)
        jsonObj.put("thirdPartyToken",thirdPartyToken)
        request(EBagClient.eBagService.login("v1", createBody(jsonObj)), callback)
    }

    /**
     * 注册
     */
    fun register(name: String, phone: String?, code: String,roleCode:Int?, pwd: String,thirdPartyToken: String?,thirdPartyUnionid:String?, loginType:Int,callback: RequestCallBack<UserEntity>){
        val jsonObj = JSONObject()
        jsonObj.put("name",name)
        jsonObj.put("phone",phone)
        jsonObj.put("verifyCode",code)
        jsonObj.put("password",pwd)
        jsonObj.put("loginType",loginType)
        jsonObj.put("thirdPartyToken",thirdPartyToken)
        jsonObj.put("thirdPartyUnionid",thirdPartyUnionid)
        jsonObj.put("roleCode",roleCode)
        request(EBagClient.eBagService.register("v1", createBody(jsonObj)), callback)
    }

    fun resetPassword(phone: String, ysbCode: String, code: String, password: String,callback: RequestCallBack<String>){
        val jsonObj = JSONObject()
        jsonObj.put("ysbCode",ysbCode)
        jsonObj.put("phone",phone)
        jsonObj.put("verifyCode",code)
        jsonObj.put("password",password)
        request(EBagClient.eBagService.resetPassword("v1", createBody(jsonObj)), callback)
    }

    fun checkUserExist(phone: String, callback: RequestCallBack<String>){
        val jsonObj = JSONObject()
        jsonObj.put("loginAccount",phone)
        request(EBagClient.eBagService.checkUserExist("v1", createBody(jsonObj)), callback)
    }

    /**
     * 获取验证码
     */
    fun getCode(phone: String, ysbCode: String,  callback: RequestCallBack<String>){
        val jsonObject = JSONObject()
        jsonObject.put("phone", phone)
        jsonObject.put("ysbCode", ysbCode)
        request(EBagClient.eBagService.getCheckCode("v1", createBody(jsonObject)), callback)
    }

    /**
     * 加入班级
     */
    fun joinClass(code: String, callback: RequestCallBack<String>, role: String = "student"){
        val jsonObject = JSONObject()
        jsonObject.put("inviteCode", code)
        request(EBagClient.eBagService.joinClass("v1", createBody(jsonObject)), callback)
    }

    /**
     * 获取相册
     */
    fun albums(classId: String, groupType: String, page: Int, pageSize: Int, callback: RequestCallBack<ArrayList<AlbumBean>>, role: String = "student"){
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        jsonObject.put("groupType", groupType)
        jsonObject.put("page", page)
        jsonObject.put("pageSize", pageSize)
        request(EBagClient.eBagService.albums("v1", createBody(jsonObject)), callback)
    }

    /**
     * 创建相册
     */
    fun createAlbum(classId: String, groupType: String, photosName: String, callback: RequestCallBack<String>){
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        jsonObject.put("groupType", groupType)
        jsonObject.put("photosName", photosName)
        request(EBagClient.eBagService.createAlbum("v1", createBody(jsonObject)), callback)
    }

    /**
     * 相册详情
     */
    fun albumDetail(photoGroupId: String, groupType: String, page: Int, pageSize: Int, callback: RequestCallBack<ArrayList<PhotoBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("photoGroupId", photoGroupId)
        jsonObject.put("groupType", groupType)
        jsonObject.put("page", page)
        jsonObject.put("pageSize", pageSize)
        request(EBagClient.eBagService.albumDetail("v1", createBody(jsonObject)), callback)
    }

    /**
     * 照片分享
     */
    fun photosShare(photoShareBean: PhotoRequestBean, callback: RequestCallBack<String>){
        request(EBagClient.eBagService.photosShare("v1", EBagApi.createBody(JSON.toJSONString(photoShareBean))), callback)
    }

    /**
     * 照片分享
     */
    fun photosDelete(photoShareBean: PhotoRequestBean, callback: RequestCallBack<String>){
        request(EBagClient.eBagService.photosDelete("v1", EBagApi.createBody(JSON.toJSONString(photoShareBean))), callback)
    }


    /**
     * 省市区县基础数据
     */
    fun cityData(callback: RequestCallBack<List<ChildNodeBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("id", 1)
        request(EBagClient.eBagService.cityData("v1", createBody(jsonObject)), callback)
    }

    /**
     * 查询学校
     */
    fun getSchool(province: String?, city: String?, county: String?, callback: RequestCallBack<List<SchoolBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("province", province)
        jsonObject.put("city", city)
        jsonObject.put("county", county)
        request(EBagClient.eBagService.getSchool("v1", createBody(jsonObject)), callback)
    }

    /**
     * 公告列表
     */
    fun noticeList(page: Int, pageSize: Int, classId: String, callback: RequestCallBack<List<NoticeBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("pageSize", pageSize)
        jsonObject.put("page", page)
        jsonObject.put("classId", classId)
        EBagApi.request(eBagService.noticeList("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 我的课本
     */
    fun myBookList(callback: RequestCallBack<List<BookBean>>){
        EBagApi.request(eBagService.myBookList("v1"), callback)
    }

    /**
     * 作业报告
     */
    fun homeworkReport(homeworkId: String, callback: RequestCallBack<ReportBean>){
        val jsonObject = JSONObject()
        jsonObject.put("homeWorkId", homeworkId)
        EBagApi.request(eBagService.homeworkReport("v1", EBagApi.createBody(jsonObject)), callback)
    }

}