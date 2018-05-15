package com.yzy.ebag.student.http

import com.yzy.ebag.student.bean.ClassesInfoBean
import com.yzy.ebag.student.bean.LabourBean
import com.yzy.ebag.student.bean.ParentBean
import com.yzy.ebag.student.bean.SubjectBean
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

    /**劳动任务*/
    fun labourTasks(page: Int, pageSize: Int, callback: RequestCallBack<ArrayList<LabourBean>>) {
        val jsonObject = JSONObject()
        jsonObject.put("page", page)
        jsonObject.put("pageSize", pageSize)
        EBagApi.request(studentService.labourTasks("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**作业列表*/
    fun subjectWorkList(type: String, classId: String?, subCode: String, page: Int, pageSize: Int, callback: RequestCallBack<ArrayList<SubjectBean>>) {
        val jsonObj = JSONObject()
        jsonObj.put("classId", classId)
        jsonObj.put("type", type)
        jsonObj.put("subCode", subCode)
        jsonObj.put("page", page)
        jsonObj.put("pageSize", pageSize)
        EBagApi.request(studentService.subjectWorkList("v1", EBagApi.createBody(jsonObj)), callback)
    }

    /**查询家长*/
    fun searchFamily(callback: RequestCallBack<List<ParentBean>>) {
        val jsonObject = JSONObject()
        EBagApi.request(studentService.searchFamily("1", EBagApi.createBody(jsonObject)), callback)
    }

    /**绑定家长*/
    fun bindParent(ysbCode: String, relationType: String, callback: RequestCallBack<String>) {
        val jsonObject = JSONObject()
        jsonObject.put("ysbCode", ysbCode)
        jsonObject.put("relationType", relationType)
        EBagApi.request(studentService.bindParent("1", EBagApi.createBody(jsonObject)), callback)
    }
}