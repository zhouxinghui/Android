package com.yzy.ebag.parents.http

import com.yzy.ebag.parents.bean.ExcitationWorkBean
import com.yzy.ebag.parents.bean.HomeworkAbstractBean
import com.yzy.ebag.parents.bean.MyChildrenBean
import com.yzy.ebag.parents.bean.OnePageInfoBean
import ebag.core.http.network.RequestCallBack
import ebag.mobile.bean.NoticeBean
import ebag.mobile.http.EBagApi
import ebag.mobile.http.EBagClient
import org.json.JSONArray
import org.json.JSONObject

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
    fun newestNotice(classId: String, callback: RequestCallBack<NoticeBean>){
        val jsonObject = JSONObject()
        jsonObject.put("classId",classId)
        EBagApi.request(parentsService.newestNotice("v1", EBagApi.createBody(jsonObject)), callback)
    }
}