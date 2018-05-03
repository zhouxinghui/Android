package com.yzy.ebag.parents.http

import com.yzy.ebag.parents.bean.*
import ebag.core.http.network.RequestCallBack
import ebag.core.util.StringUtils
import ebag.mobile.bean.MyChildrenBean
import ebag.mobile.bean.NoticeBean
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

    fun createTask(title: String, content: String, uid: String, callback: RequestCallBack<String>) {

        val jsonObject = JSONObject()
        jsonObject.put("title", title)
        jsonObject.put("content", content)
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
    fun getLetterRecord(unitId: String, classId: String, callback: RequestCallBack<List<LetterRecordBaseBean>>) {
        val jsonObject = JSONObject()
        if (!StringUtils.isEmpty(unitId))
            jsonObject.put("unitId", unitId)
        jsonObject.put("classId", classId)
        EBagApi.request(parentsService.getLetterRecord("v1", EBagApi.createBody(jsonObject)), callback)
    }


    /*成长轨迹，考试成绩*/

    fun examSocre(classId: String, homeType: String, rid: String, gradeCode: String, studentUid: String, callback: RequestCallBack<List<HomeworkBean>>) {
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        jsonObject.put("studentUid", studentUid)
        jsonObject.put("homeType", homeType)
        jsonObject.put("gradeCode", gradeCode)
        jsonObject.put("rid", rid)
        EBagApi.request(parentsService.yearStatisticsByHomeWork("1", EBagApi.createBody(jsonObject)), callback)
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

    fun createChildCode(psw: String,  name: String, callback: RequestCallBack<String>,relation: String = "家长") {
        val jsonObj = JSONObject()
        jsonObj.put("password", psw)
        jsonObj.put("relation", relation)
        val jsonObject = JSONObject()
        jsonObject.put("name", name)
        jsonObj.put("userInfoVo", jsonObject)

        EBagApi.request(parentsService.createChildCode("v1", EBagApi.createBody(jsonObj)), callback)
    }


}