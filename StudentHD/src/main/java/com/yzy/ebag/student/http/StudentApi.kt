package com.yzy.ebag.student.http

import com.yzy.ebag.student.bean.response.ClassesInfoBean
import com.yzy.ebag.student.bean.response.SubjectBean
import ebag.core.http.network.RequestCallBack
import ebag.hd.bean.response.UserEntity
import ebag.hd.http.EBagApi
import ebag.hd.http.EBagClient
import org.json.JSONObject

/**
 * Created by unicho on 2018/1/8.
 */
object StudentApi{

    private val studentService: StudentService by lazy {
        EBagClient.createRetrofitService(StudentService::class.java)
    }

    fun login(account: String, pwd: String, roleCode: String, callback: RequestCallBack<UserEntity>){
        val jsonObj = JSONObject()
        jsonObj.put("loginAccount",account)
        jsonObj.put("password",pwd)
        jsonObj.put("loginType",1)
        jsonObj.put("roleCode",roleCode)
        EBagApi.request(studentService.login("v1", EBagApi.createBody(jsonObj)), callback)
    }

    fun mainInfo(callback: RequestCallBack<ClassesInfoBean>){
        val jsonObj = JSONObject()
        jsonObj.put("roleCode","student")
        EBagApi.request(studentService.mainInfo("v1", EBagApi.createBody(jsonObj)), callback)
    }

    fun subjectWorkList(type: String, classId: String, subject: String, page: Int, pageSize: Int, callback: RequestCallBack<List<SubjectBean>>){
        val jsonObj = JSONObject()
        jsonObj.put("classId", classId)
        jsonObj.put("type", type)
        jsonObj.put("subject", subject)
        jsonObj.put("page", page)
        jsonObj.put("pageSize", pageSize)
        EBagApi.request(studentService.subjectWorkList("v1", EBagApi.createBody(jsonObj)), callback)
    }

}