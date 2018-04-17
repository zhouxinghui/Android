package com.yzy.ebag.teacher.http

import com.yzy.ebag.teacher.bean.FirstPageBean
import ebag.core.http.network.RequestCallBack
import ebag.mobile.http.EBagApi
import ebag.mobile.http.EBagClient
import org.json.JSONObject

/**
 * Created by caoyu on 2018/1/8.
 */
object TeacherApi {

    private val teacherService: TeacherService by lazy {
        EBagClient.createRetrofitService(TeacherService::class.java)
    }

    /**
     * 首页网络数据
     */
    fun firstPage(callback: RequestCallBack<FirstPageBean>){
        val jsonObject = JSONObject()
        jsonObject.put("roleCode", "2")
        EBagApi.request(teacherService.firstPage("v1", EBagApi.createBody(jsonObject)), callback)
    }
}