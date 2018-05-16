package com.yzy.ebag.student.http

import com.yzy.ebag.student.bean.*
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

    /**上报位置历史列表*/
    fun searchLocation(page: Int, callback: RequestCallBack<LocationBean>) {
        val jsonObject = JSONObject()
        jsonObject.put("page", page)
        jsonObject.put("pageSize", 10)
        EBagApi.request(studentService.searchLocation("1", EBagApi.createBody(jsonObject)), callback)
    }

    /**上报位置*/
    fun uploadLocation(address: String, remark: String, longitude: String, latitude: String, callback: RequestCallBack<String>) {
        val jsonObject = JSONObject()
        jsonObject.put("address", address)
        jsonObject.put("remark", remark)
        jsonObject.put("longitude", longitude)
        jsonObject.put("latitude", latitude)
        EBagApi.request(studentService.uploadLocation("1", EBagApi.createBody(jsonObject)), callback)
    }

    /**数学公式*/
    fun formula(formulaId: String, page: Int, pageSize: Int, callback: RequestCallBack<ArrayList<FormulaTypeBean>>) {
        val jsonObject = JSONObject()
        jsonObject.put("formulaId", formulaId)
        jsonObject.put("page", page)
        jsonObject.put("pageSize", pageSize)
        EBagApi.request(studentService.formula("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**获取练字列表*/
    fun getWordsList(unitCode: String, callback: RequestCallBack<WordsBean>) {
        val jsonObject = JSONObject()
        jsonObject.put("unitCode", unitCode)
        EBagApi.request(studentService.getWordsList("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**练字记录*/
    fun wordRecord(classId: String, pageSize: Int, page: Int, callback: RequestCallBack<WordRecordBean>) {
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        jsonObject.put("pageSize", pageSize)
        jsonObject.put("page", page)
        EBagApi.request(studentService.wordRecord("1", EBagApi.createBody(jsonObject)), callback)
    }

    /**上传练习生字*/
    fun uploadWord(classId: String, words: String, unitId: String, wordUrl: String, callback: RequestCallBack<String>) {
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        jsonObject.put("words", words)
        jsonObject.put("unitId", unitId)
        jsonObject.put("wordUrl", wordUrl)
        EBagApi.request(studentService.uploadWord("1", EBagApi.createBody(jsonObject)), callback)
    }
}