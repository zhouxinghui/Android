package com.yzy.ebag.student.http

import com.yzy.ebag.student.bean.ClassesInfoBean
import ebag.core.http.network.RequestCallBack
import ebag.mobile.http.EBagApi
import ebag.mobile.http.EBagClient
import org.json.JSONObject

/**
 * Created by YZY on 2018/5/14.
 */
object StudentApi {
    private val studentService: StudentService by lazy {
        EBagClient.createRetrofitService(StudentService::class.java)
    }
    /**首页*/
    fun mainInfo(classId: String, callback: RequestCallBack<ClassesInfoBean>) {
        val jsonObj = JSONObject()
        jsonObj.put("classId", classId)
        jsonObj.put("roleCode", "1")
        EBagApi.request(studentService.mainInfo("v1", EBagApi.createBody(jsonObj)), callback)
    }
}