package com.yzy.ebag.student.http

import com.alibaba.fastjson.JSON
import com.yzy.ebag.student.bean.*
import ebag.core.bean.TypeQuestionBean
import ebag.core.http.network.RequestCallBack
import ebag.hd.bean.request.CommitQuestionVo
import ebag.hd.bean.response.UserEntity
import ebag.hd.http.EBagApi
import ebag.hd.http.EBagClient
import org.json.JSONObject

/**
 * Created by caoyu on 2018/1/8.
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

    fun mainInfo(classId: String, callback: RequestCallBack<ClassesInfoBean>){
        val jsonObj = JSONObject()
        jsonObj.put("classId",classId)
        jsonObj.put("roleCode","student")
        EBagApi.request(studentService.mainInfo("v1", EBagApi.createBody(jsonObj)), callback)
    }

    fun subjectWorkList(type: String, classId: String, subCode: String, page: Int, pageSize: Int, callback: RequestCallBack<ArrayList<SubjectBean>>){
        val jsonObj = JSONObject()
        jsonObj.put("classId", classId)
        jsonObj.put("type", type)
        jsonObj.put("subCode", subCode)
        jsonObj.put("page", page)
        jsonObj.put("pageSize", pageSize)
        EBagApi.request(studentService.subjectWorkList("v1", EBagApi.createBody(jsonObj)), callback)
    }

    /**
     * 班级
     */
    fun clazzSpace(callback: RequestCallBack<List<SpaceBean>>){
        val jsonObject = JSONObject()
        EBagApi.request(studentService.clazzSpace("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 获取单元数据
     */
    fun getUint(classId: String, subCode: String, callback: RequestCallBack<EditionBean>){
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        jsonObject.put("subCode", subCode)
        EBagApi.request(studentService.getUint("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 获取作业详情
     */
    fun getQuestions(questionId: String, callback: RequestCallBack<List<TypeQuestionBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("id", questionId)
        EBagApi.request(studentService.getQuestions("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 提交作业
     */
    fun commitHomework(commitQuestionVo: CommitQuestionVo, callback: RequestCallBack<String>){
        EBagApi.request(studentService.commitHomework("v1", EBagApi.createBody(JSON.toJSONString(commitQuestionVo))), callback)
    }

    /**
     * 获取跟读列表
     */
    fun getReadList(unitCode: String, page: Int, pageSize: Int, callback: RequestCallBack<ReadOutBean>){
        val jsonObject = JSONObject()
        jsonObject.put("unitCode", unitCode)
        jsonObject.put("page", page)
        jsonObject.put("pageSize", pageSize)
        EBagApi.request(studentService.getReadList("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 获取跟读详情里头 句子的详情
     */
    fun getReadDetailList(languageId: String, callback: RequestCallBack<List<ReadDetailBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("languageId", languageId)
        EBagApi.request(studentService.getReadDetailList("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 获取练字列表
     */
    fun getWordsList(unitCode: String, callback: RequestCallBack<WordsBean>){
        val jsonObject = JSONObject()
        jsonObject.put("unitCode", unitCode)
        EBagApi.request(studentService.getWordsList("v1", EBagApi.createBody(jsonObject)), callback)

    }

    /**
     * 上传跟读录音文件
     */
    fun uploadRecord(classId: String, languageId: String, languageDetailId: String, myAudioUrl: String, callback: RequestCallBack<String>){
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        jsonObject.put("languageId", languageId)
        jsonObject.put("languageDetailId", languageDetailId)
        jsonObject.put("myAudioUrl", myAudioUrl)
        EBagApi.request(studentService.uploadRecord("v1", EBagApi.createBody(jsonObject)), callback)
    }

    fun recordHistory(languageId: String, callback: RequestCallBack<List<RecordHistory>>){
        val jsonObject = JSONObject()
        jsonObject.put("languageId", languageId)
        EBagApi.request(studentService.recordHistory("v1", EBagApi.createBody(jsonObject)), callback)
    }
}