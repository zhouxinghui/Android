package ebag.mobile.http

import com.alibaba.fastjson.JSON
import ebag.core.base.App
import ebag.core.bean.ResponseBean
import ebag.core.http.baseBean.RequestBean
import ebag.core.http.network.*
import ebag.core.util.StringUtils
import ebag.mobile.bean.*
import ebag.mobile.http.EBagClient.eBagService
import ebag.mobile.request.ClassScheduleEditVo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject


/**
 * Created by caoyu on 2017/11/9.
 */
object EBagApi {

    private val JSON_TYPE = MediaType.parse("application/json; charset=utf-8")


    /**返回的数据格式是按照我们自己定义的数据格式时调用此方法*/
    fun <T> request(ob: Observable<ResponseBean<T>>, callback: RequestCallBack<T>) {
        if (!App.mContext!!.hasNetwork()) {
            callback.onError(MsgException("500", "当前网络不可用，请检查网络设置！"))
            return
        }
        ob.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(EBagRequestObserver(callback))
    }

    /**返回的数据格式是按照我们自己定义的数据格式时调用此方法*/
    fun <T> startNormalRequest(ob: Observable<T>, callback: RequestCallBack<T>) {
        if (!App.mContext!!.hasNetwork()) {
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

    /**登录*/
    fun login(isHDorPHONE: String, account: String?, pwd: String?, loginType: String?, thirdPartyType: String?,
              roleCode: String, thirdPartyToken: String?, thirdPartyUnionid: String?, callback: RequestCallBack<UserEntity>) {
        val jsonObj = JSONObject()
        jsonObj.put("isHDorPHONE",isHDorPHONE)
        jsonObj.put("password", pwd)
        jsonObj.put("loginAccount", account)
        jsonObj.put("loginType", loginType)
        jsonObj.put("thirdPartyType", thirdPartyType)
        jsonObj.put("roleCode", roleCode)
        jsonObj.put("thirdPartyUnionid", thirdPartyUnionid)
        jsonObj.put("thirdPartyToken", thirdPartyToken)
        request(EBagClient.eBagService.login("v1", createBody(jsonObj)), callback)
    }

    /*fun bindingActivationCode(loginAccount:String,activation:String,callback: RequestCallBack<String>){
        val jsonObj = JSONObject()
        jsonObj.put("loginAccount",loginAccount)
        jsonObj.put("activation",activation)
        request(EBagClient.eBagService.bindingActivationCode("v1",createBody(jsonObj)),callback)
    }*/

    /**注册*/
    fun register(deviceCode:String,isHDorPHONE: String,name: String, headUrl: String?, sex: String?, phone: String?, verifyCode: String?, roleCode: String?, pwd: String, thirdPartyToken: String?, thirdPartyUnionid: String?, loginType: String?, thirdPartyType: String?, callback: RequestCallBack<UserEntity>) {
        val jsonObj = JSONObject()
        jsonObj.put("isHDorPHONE",isHDorPHONE)
        jsonObj.put("nickName", name)
        jsonObj.put("headUrl", headUrl)
        jsonObj.put("sex", sex)
        jsonObj.put("phone", phone)
        jsonObj.put("verifyCode", verifyCode)
        jsonObj.put("password", pwd)
        jsonObj.put("loginType", loginType)
        jsonObj.put("thirdPartyType", thirdPartyType)
        jsonObj.put("thirdPartyToken", thirdPartyToken)
        jsonObj.put("thirdPartyUnionid", thirdPartyUnionid)
        jsonObj.put("roleCode", roleCode)
        jsonObj.put("deviceCode",deviceCode)
        request(EBagClient.eBagService.register("v1", createBody(jsonObj)), callback)
    }

    /**忘记密码*/
    fun resetPassword(phone: String, ysbCode: String, code: String, password: String, callback: RequestCallBack<String>) {
        val jsonObj = JSONObject()
        jsonObj.put("ysbCode", ysbCode)
        jsonObj.put("phone", phone)
        jsonObj.put("verifyCode", code)
        jsonObj.put("password", password)
        request(EBagClient.eBagService.resetPassword("v1", createBody(jsonObj)), callback)
    }

    /**检查用户是否存在*/
    fun checkUserExist(phone: String, callback: RequestCallBack<String>) {
        val jsonObj = JSONObject()
        jsonObj.put("loginAccount", phone)
        request(EBagClient.eBagService.checkUserExist("v1", createBody(jsonObj)), callback)
    }

    /**获取验证码*/
    fun getCode(phone: String, ysbCode: String, callback: RequestCallBack<String>) {
        val jsonObject = JSONObject()
        if (phone.isNotEmpty()) {
            jsonObject.put("phone", phone)
        } else {
            jsonObject.put("ysbCode", ysbCode)
        }
        request(EBagClient.eBagService.getCheckCode("v1", createBody(jsonObject)), callback)
    }

    /**获取用户的所有所在班级 queryType不传，查询所有所教班级，传“1”：查询教授语文的班级（练字），传“2”：查询教授语文和英语的班级（跟读）*/
    fun getMyClasses(callback: RequestCallBack<List<BaseClassesBean>>, queryType: String? = null,uid:String? = null) {
        val jsonObject = JSONObject()
        if (!StringUtils.isEmpty(queryType)){
            jsonObject.put("queryType", queryType)
        }
        if (!StringUtils.isEmpty(uid)){
            jsonObject.put("uid", uid)
        }
        EBagApi.request(eBagService.getMyClasses("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**根据班级查询班级下所有的成员（老师，学生，家长）*/
    fun clazzMember(classId: String, callback: RequestCallBack<ClassMemberBean>) {
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        EBagApi.request(eBagService.clazzMember("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**获取相册*/
    fun albums(classId: String, groupType: String, page: Int, pageSize: Int, callback: RequestCallBack<ArrayList<AlbumBean>>, role: String = "student") {
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        jsonObject.put("groupType", groupType)
        jsonObject.put("page", page)
        jsonObject.put("pageSize", pageSize)
        request(EBagClient.eBagService.albums("v1", createBody(jsonObject)), callback)
    }

    /**创建相册*/
    fun createAlbum(classId: String, groupType: String, photosName: String, callback: RequestCallBack<String>) {
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        jsonObject.put("groupType", groupType)
        jsonObject.put("photosName", photosName)
        request(EBagClient.eBagService.createAlbum("v1", createBody(jsonObject)), callback)
    }

    /**相册详情*/
    fun albumDetail(photoGroupId: String, groupType: String, page: Int, pageSize: Int, callback: RequestCallBack<ArrayList<PhotoBean>>) {
        val jsonObject = JSONObject()
        jsonObject.put("photoGroupId", photoGroupId)
        jsonObject.put("groupType", groupType)
        jsonObject.put("page", page)
        jsonObject.put("pageSize", pageSize)
        request(EBagClient.eBagService.albumDetail("v1", createBody(jsonObject)), callback)
    }

    /**照片分享*/
    fun photosShare(photoShareBean: PhotoRequestBean, callback: RequestCallBack<String>) {
        request(EBagClient.eBagService.photosShare("v1", EBagApi.createBody(JSON.toJSONString(photoShareBean))), callback)
    }

    /**照片删除*/
    fun photosDelete(photoShareBean: PhotoRequestBean, callback: RequestCallBack<String>) {
        request(EBagClient.eBagService.photosDelete("v1", EBagApi.createBody(JSON.toJSONString(photoShareBean))), callback)
    }

    /**照片上传*/
    fun photosUpload(photoUploadBean: PhotoUploadBean, callback: RequestCallBack<String>) {
        request(EBagClient.eBagService.photosUpload("v1", EBagApi.createBody(JSON.toJSONString(photoUploadBean))), callback)
    }

    /**公告列表*/
    fun noticeList(page: Int, pageSize: Int, classId: String, callback: RequestCallBack<List<NoticeBean>>) {
        val jsonObject = JSONObject()
        jsonObject.put("pageSize", pageSize)
        jsonObject.put("page", page)
        jsonObject.put("classId", classId)
        EBagApi.request(eBagService.noticeList("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**课程表*/
    fun classSchedule(classId: String, callback: RequestCallBack<ClassScheduleBean>) {
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        EBagApi.request(eBagService.classSchedule("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**编辑课程表*/
    fun editSchedule(schedulesVo: ClassScheduleEditVo, callback: RequestCallBack<String>) {
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

    /**课堂表现-个人详情*/
    fun personalPerformance(callback: RequestCallBack<PersonalPerformanceBean>, uid: String? = null) {
        val jsonObject = JSONObject()
        if (!StringUtils.isEmpty(uid)) {
            jsonObject.put("uid", uid)
        }
        EBagApi.request(eBagService.personalPerformance("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**个人中心*/
    fun queryUserInfo(call: RequestCallBack<UserInfoBean>) {
        val jsonObject = JSONObject()
        EBagApi.request(eBagService.queryPersonalCenter("1", EBagApi.createBody(jsonObject)), call)
    }

    /**查询学校*/
    fun getSchool(province: String?, city: String?, county: String?, callback: RequestCallBack<List<SchoolBean>>) {
        val jsonObject = JSONObject()
        jsonObject.put("province", province)
        jsonObject.put("city", city)
        jsonObject.put("county", county)
        request(EBagClient.eBagService.getSchool("v1", createBody(jsonObject)), callback)
    }

    /**省市区县基础数*/
    fun cityData(callback: RequestCallBack<List<ChildNodeBean>>) {
        val jsonObject = JSONObject()
        jsonObject.put("id", 1)
        request(EBagClient.eBagService.cityData("v1", createBody(jsonObject)), callback)
    }

    /**我的课本*/
    fun myBookList(callback: RequestCallBack<List<BookBean>>) {
        EBagApi.request(eBagService.myBookList("v1"), callback)
    }

    fun studentBookList(classId: String?, callback: RequestCallBack<List<BookBean>>) {
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        EBagApi.request(eBagService.studentBookList("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**删除笔记*/
    fun deleteNote(noteId: String, callback: RequestCallBack<String>) {
        val jsonObject = JSONObject()
        jsonObject.put("id", noteId)
        EBagApi.request(eBagService.deleteNote("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**查询课本笔记列表*/
    fun bookNoteList(bookId: String, page: Int, pageSize: Int, callback: RequestCallBack<List<BookNoteBean>>) {
        val jsonObject = JSONObject()
        jsonObject.put("bookId", bookId)
        jsonObject.put("page", page)
        jsonObject.put("pageSize", pageSize)
        EBagApi.request(eBagService.bookNoteList("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**修改笔记*/
    fun modifyNote(noteId: String, note: String, callback: RequestCallBack<String>) {
        val jsonObject = JSONObject()
        jsonObject.put("note", note)
        jsonObject.put("id", noteId)
        EBagApi.request(eBagService.modifyNote("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**新增课本笔记*/
    fun addBookNote(bookId: String, note: String, callback: RequestCallBack<String>) {
        val jsonObject = JSONObject()
        jsonObject.put("bookId", bookId)
        jsonObject.put("note", note)
        EBagApi.request(eBagService.addBookNote("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**课本目录*/
    fun bookCategory(bookId: Int, callback: RequestCallBack<BookCategoryBean>) {
        val jsonObject = JSONObject()
        jsonObject.put("bookId", bookId)
        EBagApi.request(eBagService.bookCategory("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**获取单元数据*/
    fun getUnit(classId: String?, subCode: String, callback: RequestCallBack<EditionBean>) {
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        if (!StringUtils.isEmpty(subCode))
            jsonObject.put("subCode", subCode)
        EBagApi.request(eBagService.getUnit("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**获取单元数据*/
    fun getUnit(bookVersionId: String, callback: RequestCallBack<EditionBean>) {
        val jsonObject = JSONObject()
        jsonObject.put("bookVersionId", bookVersionId)
        EBagApi.request(eBagService.getUnit("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**跟读-报告-切换版本*/
    fun readRecordVersion(classId: String?, subCode: String?, callback: RequestCallBack<ReadRecordVersionBean>) {
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        if (!StringUtils.isEmpty(subCode))
            jsonObject.put("subCode", subCode)
        EBagApi.request(eBagService.readRecordVersion("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**自习室-生字详情*/
    fun getLetterDesc(unitId: String, createDate: Long, classId: String, callback: RequestCallBack<LetterDescBean>) {
        val jsonObject = JSONObject()
        jsonObject.put("unitId", unitId)
        jsonObject.put("createDate", createDate)
        jsonObject.put("classId", classId)
        EBagApi.request(eBagService.getLetterDesc("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**自习室-上传生字评分*/
    fun uploadReadScore(scoreList: ArrayList<LetterDescBean.NewWordsBean>, callback: RequestCallBack<String>) {
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

    /**自习室-口语总览列表*/
    fun getReadRecord(unitCode: String, classId: String, callback: RequestCallBack<List<ReadRecordBaseBean>>) {
        val jsonObject = JSONObject()
        jsonObject.put("unitCode", unitCode)
        jsonObject.put("classId", classId)
        EBagApi.request(eBagService.getReadRecord("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**自习-口语学生作答详情*/
    fun getReadRecordDesc(classId: String, languageDetailId: String, dateTime: Long, callback: RequestCallBack<List<ReadRecordAnswerBean>>) {
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        jsonObject.put("languageDetailId", languageDetailId)
        jsonObject.put("dateTime", dateTime)
        EBagApi.request(eBagService.getReadRecordDesc("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**获取跟读详情里头 句子的详情*/
    fun getReadDetailList(unitCode: String, callback: RequestCallBack<List<ReadRecordVoiceBean>>) {
        val jsonObject = JSONObject()
        jsonObject.put("unitCode", unitCode)
        EBagApi.request(eBagService.getReadDetailList("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**修改个人信息*/
    fun modifyPersonalInfo(key: String, value: String, callback: RequestCallBack<String>) {
        val jsonObject = JSONObject()
        jsonObject.put(key, value)
        EBagApi.request(eBagService.modifyPersonalInfo("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**用户反馈*/
    fun userFeedback(content: String, callback: RequestCallBack<String>) {
        val jsonObject = JSONObject()
        jsonObject.put("content", content)
        EBagApi.request(eBagService.userFeedback("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 官方公告
     * @param type 1-IOS,2-android,3-android-HD
     * @param versionCode 老师-teacher,学生-student,家长-parent
     */
    fun officialAnnounce(type: String, versionCode: String, page: Int, pageSize: Int, callback: RequestCallBack<OfficialAnnounceBean>) {
        val jsonObject = JSONObject()
        jsonObject.put("type", type)
        jsonObject.put("versionCode", versionCode)
        jsonObject.put("page", page)
        jsonObject.put("pageSize", pageSize)
        EBagApi.request(eBagService.officialAnnounce("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**作业报告*/
    fun homeworkReport(homeworkId: String, studentId: String?, callback: RequestCallBack<ReportBean>) {
        val jsonObject = JSONObject()
        if (!StringUtils.isEmpty(studentId)) {
            jsonObject.put("uid", studentId)
        }
        jsonObject.put("homeWorkId", homeworkId)
        EBagApi.request(eBagService.homeworkReport("v1", EBagApi.createBody(jsonObject)), callback)
    }
}