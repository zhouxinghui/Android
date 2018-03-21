package ebag.hd.http
import com.alibaba.fastjson.JSON
import ebag.core.base.App
import ebag.core.bean.ResponseBean
import ebag.core.bean.TypeQuestionBean
import ebag.core.http.baseBean.RequestBean
import ebag.core.http.network.*
import ebag.core.util.StringUtils
import ebag.hd.bean.*
import ebag.hd.bean.request.ClassScheduleEditVo
import ebag.hd.bean.request.CommitQuestionVo
import ebag.hd.bean.response.BaseInfoEntity
import ebag.hd.bean.response.NoticeBean
import ebag.hd.bean.response.UserEntity
import ebag.hd.http.EBagClient.eBagService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
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
    fun login(account: String?, pwd: String?, loginType: String?, thirdPartyType:String?,
              roleCode: String,thirdPartyToken: String?,thirdPartyUnionid:String?, callback: RequestCallBack<UserEntity>){
        val jsonObj = JSONObject()
        jsonObj.put("password",pwd)
        jsonObj.put("loginAccount",account)
        jsonObj.put("loginType",loginType)
        jsonObj.put("thirdPartyType",thirdPartyType)
        jsonObj.put("roleCode",roleCode)
        jsonObj.put("thirdPartyUnionid",thirdPartyUnionid)
        jsonObj.put("thirdPartyToken",thirdPartyToken)
        request(EBagClient.eBagService.login("v1", createBody(jsonObj)), callback)
    }

    /**
     * 注册
     */
    fun register(name: String,headUrl:String?,sex:String?, phone: String?, verifyCode: String?,roleCode:String?, pwd: String,thirdPartyToken: String?,thirdPartyUnionid:String?, loginType:String?,thirdPartyType:String?,callback: RequestCallBack<UserEntity>){
        val jsonObj = JSONObject()
        jsonObj.put("nickName",name)
        jsonObj.put("headUrl",headUrl)
        jsonObj.put("sex",sex)
        jsonObj.put("phone",phone)
        jsonObj.put("verifyCode",verifyCode)
        jsonObj.put("password",pwd)
        jsonObj.put("loginType",loginType)
        jsonObj.put("thirdPartyType",thirdPartyType)
        jsonObj.put("thirdPartyToken",thirdPartyToken)
        jsonObj.put("thirdPartyUnionid",thirdPartyUnionid)
        jsonObj.put("roleCode",roleCode)
        request(EBagClient.eBagService.register("v1", createBody(jsonObj)), callback)
    }

    /**
     * 忘记密码
     */
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
     * 照片删除
     */
    fun photosDelete(photoShareBean: PhotoRequestBean, callback: RequestCallBack<String>){
        request(EBagClient.eBagService.photosDelete("v1", EBagApi.createBody(JSON.toJSONString(photoShareBean))), callback)
    }

    /**
     * 照片上传
     */
    fun photosUpload(photoUploadBean: PhotoUploadBean, callback: RequestCallBack<String>){
        request(EBagClient.eBagService.photosUpload("v1", EBagApi.createBody(JSON.toJSONString(photoUploadBean))), callback)
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
    fun homeworkReport(homeworkId: String, studentId: String?, callback: RequestCallBack<ReportBean>){
        val jsonObject = JSONObject()
        if(!StringUtils.isEmpty(studentId)){
            jsonObject.put("uid", studentId)
        }
        jsonObject.put("homeWorkId", homeworkId)
        EBagApi.request(eBagService.homeworkReport("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 课程表
     */
    fun classSchedule(classId: String, callback: RequestCallBack<ClassScheduleBean>){
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        EBagApi.request(eBagService.classSchedule("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 编辑课程表
     */
    fun editSchedule(schedulesVo: ClassScheduleEditVo, callback: RequestCallBack<String>){
        EBagApi.request(eBagService.editSchedule("v1", EBagApi.createBody(JSON.toJSONString(schedulesVo))), callback)
    }

    /**
     * 获取基础数据信息
     * subject 科目信息
     */
    fun getBaseInfo(groupCode: String, callback: RequestCallBack<ArrayList<BaseInfoEntity>>) {
        val jsonObject = JSONObject()
        jsonObject.put("groupCode", groupCode)
        EBagApi.request(eBagService.getBaseInfo("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 检查版本更新
     */
    fun checkUpdate(roleName: String, versionCode: String, callback: RequestCallBack<VersionUpdateBean>){
        val jsonObject = JSONObject()
        jsonObject.put("type", "3")
        jsonObject.put("versionCode", roleName)
        jsonObject.put("versionNumber", versionCode)
        EBagApi.request(eBagService.checkUpdate("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 课本目录
     */
    fun bookCategory(bookId: Int, callback: RequestCallBack<BookCategoryBean>){
        val jsonObject = JSONObject()
        jsonObject.put("bookId", bookId)
        EBagApi.request(eBagService.bookCategory("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 新增课本笔记
     */
    fun addBookNote(bookId: String, note: String, callback: RequestCallBack<String>){
        val jsonObject = JSONObject()
        jsonObject.put("bookId", bookId)
        jsonObject.put("note", note)
        EBagApi.request(eBagService.addBookNote("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 查询课本笔记列表
     */
    fun bookNoteList(bookId: String, page: Int, pageSize: Int, callback: RequestCallBack<List<BookNoteBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("bookId", bookId)
        jsonObject.put("page", page)
        jsonObject.put("pageSize", pageSize)
        EBagApi.request(eBagService.bookNoteList("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 修改笔记
     */
    fun modifyNote(noteId: String, note: String, callback: RequestCallBack<String>){
        val jsonObject = JSONObject()
        jsonObject.put("note", note)
        jsonObject.put("id", noteId)
        EBagApi.request(eBagService.modifyNote("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 删除笔记
     */
    fun deleteNote(noteId: String, callback: RequestCallBack<String>){
        val jsonObject = JSONObject()
        jsonObject.put("id", noteId)
        EBagApi.request(eBagService.deleteNote("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 提交作业
     */
    fun commitHomework(commitQuestionVo: CommitQuestionVo, callback: RequestCallBack<String>){
        EBagApi.request(eBagService.commitHomework("v1", EBagApi.createBody(JSON.toJSONString(commitQuestionVo))), callback)
    }

    /**
     * 错题纠正
     */
    fun errorCorrection(commitQuestionVo: CommitQuestionVo, callback: RequestCallBack<String>){
        EBagApi.request(eBagService.errorCorrection("v1", EBagApi.createBody(JSON.toJSONString(commitQuestionVo))), callback)
    }

    /**
     * 获取作业详情
     */
    fun getQuestions(homeWorkId: String, type: String, studentId: String?, callback: RequestCallBack<List<TypeQuestionBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("homeWorkId", homeWorkId)
        jsonObject.put("type", type)
        if(!StringUtils.isEmpty(studentId)){
            jsonObject.put("uid", studentId)
        }
        EBagApi.request(eBagService.getQuestions("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 获取错题详情
     */
    fun getErrorDetail(homeWorkId: String, callback: RequestCallBack<List<TypeQuestionBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("homeWorkId", homeWorkId)
        EBagApi.request(eBagService.getErrorDetail("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 获取单元数据
     */
    fun getUnit(classId: String, subCode: String, callback: RequestCallBack<EditionBean>){
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        jsonObject.put("subCode", subCode)
        EBagApi.request(eBagService.getUnit("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 获取用户的所有所在班级
     */
    fun getMyClasses(callback: RequestCallBack<List<BaseClassesBean>>){
        val jsonObject = JSONObject()
        EBagApi.request(eBagService.getMyClasses("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 自习室-生字总览列表
     */
    fun getLetterRecord(unitId: String, classId: String, callback: RequestCallBack<List<LetterRecordBaseBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("unitId", unitId)
        jsonObject.put("classId", classId)
        EBagApi.request(eBagService.getLetterRecord("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 自习室-生字详情
     */
    fun getLetterDesc(unitId: String, createDate: Long, classId: String, callback: RequestCallBack<LetterDescBean>){
        val jsonObject = JSONObject()
        jsonObject.put("unitId", unitId)
        jsonObject.put("createDate", createDate)
        jsonObject.put("classId", classId)
        EBagApi.request(eBagService.getLetterDesc("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 自习室-上传生字评分
     */
    fun uploadReadScore(scoreList: ArrayList<LetterDescBean.NewWordsBean>, callback: RequestCallBack<String>){
        val jsonObject = JSONObject()
        val jsonArray = JSONArray()
        scoreList.forEach {
            val jsonObj = JSONObject()
            jsonObj.put("id", it.id)
            jsonObj.put("score", it.score)
            jsonArray.put(jsonObj)
        }
        jsonObject.put("wordrecordVoList", jsonArray)
        EBagApi.request(eBagService.uploadReadScore("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /*查询云币中心*/

    /**
     * 自习室-口语总览列表
     */
    fun getReadRecord(unitCode: String, classId: String, callback: RequestCallBack<List<ReadRecordBaseBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("unitCode", unitCode)
        jsonObject.put("classId", classId)
        EBagApi.request(eBagService.getReadRecord("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**获取跟读详情里头 句子的详情*/
    fun getReadDetailList(languageId: String, callback: RequestCallBack<List<ReadRecordVoiceBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("languageId", languageId)
        EBagApi.request(eBagService.getReadDetailList("v1", EBagApi.createBody(jsonObject)), callback)
    }

    fun queryYBCurrent(page:Int,pageSize:Int,callback: RequestCallBack<YBCurrentBean>){
        val jsonObject = JSONObject()
        jsonObject.put("page",page)
        jsonObject.put("pageSize",pageSize)
        EBagApi.request(eBagService.queryYBCurrent("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**查询云币交易记录*/

    fun queryYB(page:Int,pageSize:Int,type:String,callback: RequestCallBack<YBCurrentBean>){
        val jsonObject = JSONObject()
        jsonObject.put("page",page)
        jsonObject.put("pageSize",pageSize)
        jsonObject.put("type",type)
        EBagApi.request(eBagService.queryYBCurrent("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**查询用户地址*/
    fun queryAddress(callback: RequestCallBack<MutableList<AddressListBean>>){
        val jsonObject = JSONObject()
        EBagApi.request(eBagService.queryAddress("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**删除地址*/
    fun deleteAddress(id:String,callback: RequestCallBack<String>){
        val jsonObject = JSONObject()
        jsonObject.put("id",id)
        EBagApi.request(eBagService.deleteAddress("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**新增地址*/
    fun saveAddress(consignee:String,phone:String,preAddress:String,address:String,callback: RequestCallBack<String>){
        val jsonObject = JSONObject()
        jsonObject.put("consignee",consignee)
        jsonObject.put("phone",phone)
        jsonObject.put("preAddress",preAddress)
        jsonObject.put("address",address)
        EBagApi.request(eBagService.saveAddress("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**更新地址*/
    fun updateAddress(id:String,consignee:String,phone:String,preAddress:String,address:String,callback: RequestCallBack<String>){
        val jsonObject = JSONObject()
        jsonObject.put("consignee",consignee)
        jsonObject.put("id",id)
        jsonObject.put("phone",phone)
        jsonObject.put("preAddress",preAddress)
        jsonObject.put("address",address)
        EBagApi.request(eBagService.updateAddress("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**查询订单*/
    fun queryOrder(callback: RequestCallBack<QueryOrderBean>){
        val jsonObject = JSONObject()
        EBagApi.request(eBagService.queryOrder("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**查询商店商品列表*/
    fun getShopList(page:Int,callback: RequestCallBack<ShopListBean>){
        val jsonObject = JSONObject()
        jsonObject.put("page",page)
        jsonObject.put("pageSize",10)
        EBagApi.request(eBagService.getShopList("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**查询购物车*/
    fun queryShopCar(callback: RequestCallBack<MutableList<ShopListBean.ListBean>>){
        val jsonObject = JSONObject()
        EBagApi.request(eBagService.queryShopCar("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**添加商品到购物车*/
    fun addGoods2Car(id:String,count:String,callback: RequestCallBack<String>){
        val jsonObject = JSONObject()
        jsonObject.put("id",id)
        jsonObject.put("numbers",count)
        EBagApi.request(eBagService.addGoods2Car("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**商品详情信息*/
    fun shopDetails(id:String,callback: RequestCallBack<GoodsDetailsBean>){
        val jsonObject = JSONObject()
        jsonObject.put("id",id)
        EBagApi.request(eBagService.shopDeatils("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**更新购物车*/
    fun updateShopCar(numbers:String,id:String,callback: RequestCallBack<String>){
        val jsonObject = JSONObject()
        jsonObject.put("id",id)
        jsonObject.put("numbers",numbers)
        EBagApi.request(eBagService.updateShopCar("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**获取订单编号*/
    fun createShopOrderNo(callback: RequestCallBack<String>){
        val jsonObject = JSONObject()
        EBagApi.request(eBagService.createShopOrderNo("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**微信支付请求*/
    fun getPrepayid(id:String,price:String,callback: RequestCallBack<WXPayBean>){
        val jsonObject = JSONObject()
        jsonObject.put("oid",id)
        jsonObject.put("allPrice",price)
        EBagApi.request(eBagService.getPrepayid("v1", EBagApi.createBody(jsonObject)), callback)
    }
}