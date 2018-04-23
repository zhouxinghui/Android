package com.yzy.ebag.parents.http

import com.yzy.ebag.parents.bean.HomeworkAbstractBean
import com.yzy.ebag.parents.bean.MyChildrenBean
import com.yzy.ebag.parents.bean.OnePageInfoBean
import ebag.core.http.network.RequestCallBack
import ebag.mobile.http.EBagApi
import ebag.mobile.http.EBagClient
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

    fun getHomeworkReport(homeWorkId:String,uid: String,callback: RequestCallBack<HomeworkAbstractBean>){
        val jsonObject = JSONObject()
        jsonObject.put("uid", uid)
        jsonObject.put("homeWorkId", homeWorkId)
        EBagApi.request(parentsService.homeReport("v1", EBagApi.createBody(jsonObject)), callback)
    }
}