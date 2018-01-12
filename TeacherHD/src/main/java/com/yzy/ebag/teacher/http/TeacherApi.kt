package com.yzy.ebag.teacher.http

import com.yzy.ebag.teacher.bean.AssignmentBean
import com.yzy.ebag.teacher.bean.FirstPageBean
import ebag.core.http.network.RequestCallBack
import ebag.hd.http.EBagApi
import ebag.hd.http.EBagClient
import org.json.JSONObject

/**
 * Created by unicho on 2018/1/8.
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
        jsonObject.put("roleCode", "teacher")
        EBagApi.request(teacherService.firstPage("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 布置作业页面
     */
    fun assignmentData(type: String, callback: RequestCallBack<AssignmentBean>){
        val jsonObject = JSONObject()
        jsonObject.put("type", type)
        EBagApi.request(teacherService.assignmentData("v1", EBagApi.createBody(jsonObject)), callback)
    }

}