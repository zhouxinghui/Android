package com.yzy.ebag.parents.http

import com.yzy.ebag.parents.bean.*
import ebag.core.bean.QuestionBean
import ebag.core.http.network.RequestCallBack
import ebag.core.util.StringUtils
import ebag.mobile.bean.MyChildrenBean
import ebag.mobile.bean.NoticeBean
import ebag.mobile.bean.UnitBean
import ebag.mobile.http.EBagApi
import ebag.mobile.http.EBagClient
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

object ParentsAPI {

    private val parentsService: ParentsService by lazy {
        EBagClient.createRetrofitService(ParentsService::class.java)
    }


    fun searchMyChildren(callback: RequestCallBack<List<MyChildrenBean>>) {
        val jsonObject = JSONObject()
        EBagApi.request(parentsService.searchChildren("v1", EBagApi.createBody(jsonObject)), callback)
    }

    fun onePageInfo(uid: String, callback: RequestCallBack<List<OnePageInfoBean>>) {
        val jsonObject = JSONObject()
        jsonObject.put("uid", uid)
        jsonObject.put("roleCode", "3")
        EBagApi.request(parentsService.getOnePageInfo("v1", EBagApi.createBody(jsonObject)), callback)
    }

    fun getHomeworkReport(homeWorkId: String, uid: String, callback: RequestCallBack<HomeworkAbstractBean>) {
        val jsonObject = JSONObject()
        jsonObject.put("uid", uid)
        jsonObject.put("homeWorkId", homeWorkId)
        EBagApi.request(parentsService.homeReport("v1", EBagApi.createBody(jsonObject)), callback)
    }

    fun parentComment(uid: String, homeWorkId: String, parentComment: String, isComment: Boolean, callback: RequestCallBack<String>) {

        val jsonObject = JSONObject()
        jsonObject.put("uid", uid)
        jsonObject.put("homeWorkId", homeWorkId)
        if (isComment) {
            jsonObject.put("parentComment", parentComment)
        } else {
            jsonObject.put("parentAutograph", parentComment)
        }

        EBagApi.request(parentsService.parentComment("v1", EBagApi.createBody(jsonObject)), callback)
    }

    fun createTask(title: String, content: String, uid: String,yunCount:String, callback: RequestCallBack<String>) {

        val jsonObject = JSONObject()
        jsonObject.put("title", title)
        jsonObject.put("content", content)
        jsonObject.put("yunCount", yunCount)
        val jay = JSONArray()
        jay.put(uid)
        jsonObject.put("studentUid", jay)

        EBagApi.request(parentsService.createTask("v1", EBagApi.createBody(jsonObject)), callback)
    }

    fun queryTask(uid: String, page: String, callback: RequestCallBack<List<ExcitationWorkBean>>) {

        val jsonObject = JSONObject()
        jsonObject.put("uid", uid)
        jsonObject.put("page", page)
        jsonObject.put("pageSize", 10)

        EBagApi.request(parentsService.queryTask("v1", EBagApi.createBody(jsonObject)), callback)
    }

    fun updateTask(id: String, callback: RequestCallBack<String>) {
        val jsonObject = JSONObject()
        jsonObject.put("id", id)
        EBagApi.request(parentsService.updateTask("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**查询最新公告*/
    fun newestNotice(classId: String, callback: RequestCallBack<NoticeBean>) {
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        EBagApi.request(parentsService.newestNotice("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**自习室-生字总览列表*/
    fun getLetterRecord(unitId: String, classId: String, callback: RequestCallBack<List<LetterRecordBaseBean>>,uid:String = "") {
        val jsonObject = JSONObject()
        if (!StringUtils.isEmpty(unitId))
            jsonObject.put("unitId", unitId)
        if (!StringUtils.isEmpty(uid))
            jsonObject.put("studentId", uid)
        jsonObject.put("classId", classId)
        EBagApi.request(parentsService.getLetterRecord("v1", EBagApi.createBody(jsonObject)), callback)
    }


    /**
     * 作业列表
     */
    fun subjectWorkList(type: String, classId: String?, subCode: String, page: Int, pageSize: Int, uid: String, callback: RequestCallBack<ArrayList<SubjectBean>>) {
        val jsonObj = JSONObject()

        jsonObj.put("classId", classId)
        jsonObj.put("type", type)
        jsonObj.put("subCode", subCode)
        jsonObj.put("page", page)
        jsonObj.put("pageSize", pageSize)
        jsonObj.put("uid", uid)
        EBagApi.request(parentsService.subjectWorkList("v1", EBagApi.createBody(jsonObj)), callback)
    }

    /**
     * 我的错题
     */
    fun errorTopic(classId: String, subCode: String, page: Int, pageSize: Int, uid: String, callback: RequestCallBack<ArrayList<ErrorTopicBean>>) {
        val jsonObj = JSONObject()
        jsonObj.put("classId", classId)
        jsonObj.put("subCode", subCode)
        jsonObj.put("page", page)
        jsonObj.put("pageSize", pageSize)
        jsonObj.put("uid", uid)
        EBagApi.request(parentsService.errorTopic("v1", EBagApi.createBody(jsonObj)), callback)
    }

    /**
     * 生成小孩
     */

    fun createChildCode(psw: String, name: String, callback: RequestCallBack<String>, relation: String = "家长") {
        val jsonObj = JSONObject()
        jsonObj.put("password", psw)
        jsonObj.put("relation", relation)
        val jsonObject = JSONObject()
        jsonObject.put("name", name)
        jsonObj.put("userInfoVo", jsonObject)

        EBagApi.request(parentsService.createChildCode("v1", EBagApi.createBody(jsonObj)), callback)
    }

    /**
     * 加入班级
     */

    fun joinClazz(code: String, studentId: String, callback: RequestCallBack<JoinClazzBean>) {

        val jsonObj = JSONObject()
        jsonObj.put("inviteCode", code)
        jsonObj.put("studentId", studentId)
        EBagApi.request(parentsService.joinClazz("v1", EBagApi.createBody(jsonObj)), callback)
    }

    /**
     * 查询科目
     */

    fun getTaughtCourses(clazzId: String, callback: RequestCallBack<List<StudentSubjectBean>>) {
        val jsonObj = JSONObject()
        jsonObj.put("classId", clazzId)
        EBagApi.request(parentsService.getTaughtCourses("v1", EBagApi.createBody(jsonObj)), callback)
    }

    /**
     * 送礼物
     */

    fun giveYsbMoneyGifg2User(bean: GiftPayBean, callback: RequestCallBack<String>,uid:String = "") {
        val jsonObj = JSONObject()
        jsonObj.put("givingUid", bean.givingUid)
        jsonObj.put("homeWorkId", bean.homeWorkId)
        jsonObj.put("giftsMoney", bean.giftsMoney)
        if (uid.isNotEmpty()){
            jsonObj.put("uid", uid)
        }
        val array = JSONArray()
        bean.giftVos.forEach {
            val j = JSONObject()
            j.put("giftName", it.giftName)
            j.put("giftNum", it.giftNum)
            array.put(j)
        }
        jsonObj.put("giftVos", array)

        EBagApi.request(parentsService.giveYsbMoneyGifg2User("v1", EBagApi.createBody(jsonObj)), callback)
    }

    /**
     * 查询礼物列表
     */
    fun getGiftDetail(homeWorkId: String, callback: RequestCallBack<List<GiftListBean>>,uid: String = "") {
        val jsonObj = JSONObject()
        jsonObj.put("homeWorkId", homeWorkId)
        if (uid.isNotEmpty()){
            jsonObj.put("uid", uid)
        }
        EBagApi.request(parentsService.getGiftDetail("v1", EBagApi.createBody(jsonObj)), callback)
    }

    fun getBookUnit(bookVersionId: String, callback: RequestCallBack<ArrayList<UnitBean>>) {
        val jsonObj = JSONObject()
        jsonObj.put("bookVersionId", bookVersionId)
        EBagApi.request(parentsService.getBookUnit("v1", EBagApi.createBody(jsonObj)), callback)
    }

    /**预览试卷*/
    fun previewTestPaper(paperId: String, callback: RequestCallBack<List<QuestionBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("testPaperId",paperId)
        EBagApi.request(parentsService.previewTestPaper("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**预览试卷*/
    fun queryUserDetail(uid: String, callback: RequestCallBack<MyChildrenBean>){
        val jsonObject = JSONObject()
        jsonObject.put("uid",uid)
        EBagApi.request(parentsService.queryUserDetail("v1", EBagApi.createBody(jsonObject)), callback)
    }


    /**智能推送*/
    fun smartPush(count: Int, unitBean: UnitBean.UnitSubBean, difficulty: String?, type: String, bookVersionId: String?, callback: RequestCallBack<List<QuestionBean>>){
        val jsonObject = JSONObject()
        if (unitBean.unitCode != null) {
            if (unitBean.isUnit)
                jsonObject.put("bookUnit", unitBean.unitCode)
            else
                jsonObject.put("bookCatalog", unitBean.unitCode)
        }
        if(bookVersionId != null){
            jsonObject.put("bookVersionId", bookVersionId)
        }
        difficulty ?: jsonObject.put("level",difficulty)
        jsonObject.put("type",type)
        jsonObject.put("count", count)
        EBagApi.request(parentsService.smartPush("v1", EBagApi.createBody(jsonObject)), callback)
    }


    /**发布作业*/
    fun publishHomework(
            classes: ArrayList<AssignClassBean>,
            groupIds: ArrayList<String>? = null,
            isGroup: Boolean,
            type: String,
            remark: String,
            content: String,
            endTime: String,
            subCode: String,
            bookVersionId: String,
            questionList: ArrayList<QuestionBean>? = null,
            testPaperId: String? = null,
            uid:String,
            callback: RequestCallBack<String>){
        val jsonObject = JSONObject()
        val classArray = JSONArray()
        classes.forEach {
            classArray.put(it.classId)
        }
        if (groupIds != null){
            val groupArray = JSONArray()
            groupIds.forEach { groupArray.put(it) }
            jsonObject.put("groupIds", groupArray)
        }
        jsonObject.put("clazzIds", classArray)
        /*if (isGroup)
            jsonObject.put("groupType", "2")
        else
            jsonObject.put("groupType", "1")*/
        jsonObject.put("groupType", "3")
        jsonObject.put("content", content)
        jsonObject.put("remark", remark)
        jsonObject.put("childId", uid)
        jsonObject.put("type", "3")
        jsonObject.put("endTime", endTime)
        jsonObject.put("subCode", subCode)
        jsonObject.put("bookVersionId", bookVersionId)
        if (questionList != null) {
            val questionArray = JSONArray()
            questionList.forEach {
                val questionJson = JSONObject()
                questionJson.put("questionId", it.questionId)
                questionJson.put("questionType", it.type)
                questionArray.put(questionJson)
            }
            jsonObject.put("homeWorkQuestionDtos", questionArray)
        }
        if (testPaperId != null){
            jsonObject.put("testPaperId", testPaperId)
        }
        EBagApi.request(parentsService.publishHomework("v1", EBagApi.createBody(jsonObject)), callback)
    }

}