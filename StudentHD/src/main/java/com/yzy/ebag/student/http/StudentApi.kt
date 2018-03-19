package com.yzy.ebag.student.http

import android.util.Base64
import com.alibaba.fastjson.JSON
import com.yzy.ebag.student.bean.*
import ebag.core.http.network.RequestCallBack
import ebag.core.util.L
import ebag.hd.bean.response.UserEntity
import ebag.hd.http.EBagApi
import ebag.hd.http.EBagClient
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream



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

    /**
     * 首页
     */
    fun mainInfo(classId: String, callback: RequestCallBack<ClassesInfoBean>){
        val jsonObj = JSONObject()
        jsonObj.put("classId",classId)
        jsonObj.put("roleCode","1")
        EBagApi.request(studentService.mainInfo("v1", EBagApi.createBody(jsonObj)), callback)
    }

    /**
     * 作业列表
     */
    fun subjectWorkList(type: String, classId: String?, subCode: String, page: Int, pageSize: Int, callback: RequestCallBack<ArrayList<SubjectBean>>){
        val jsonObj = JSONObject()
        jsonObj.put("classId", classId)
        jsonObj.put("type", type)
        jsonObj.put("subCode", subCode)
        jsonObj.put("page", page)
        jsonObj.put("pageSize", pageSize)
        EBagApi.request(studentService.subjectWorkList("v1", EBagApi.createBody(jsonObj)), callback)
    }

    /**
     * 我的错题
     */
    fun errorTopic(classId: String, subCode: String, page: Int, pageSize: Int, callback: RequestCallBack<ArrayList<ErrorTopicBean>>){
        val jsonObj = JSONObject()
        jsonObj.put("classId", classId)
        jsonObj.put("subCode", subCode)
        jsonObj.put("page", page)
        jsonObj.put("pageSize", pageSize)
        EBagApi.request(studentService.errorTopic("v1", EBagApi.createBody(jsonObj)), callback)
    }

    /**
     * 班级
     */
    fun clazzSpace(callback: RequestCallBack<List<SpaceBean>>){
        val jsonObject = JSONObject()
        EBagApi.request(studentService.clazzSpace("v1", EBagApi.createBody(jsonObject)), callback)
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
    fun uploadRecord(classId: String, languageId: String, languageDetailId: String, language: String, myAudioUrl: String, callback: RequestCallBack<String>){
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        jsonObject.put("language", language)
        jsonObject.put("languageId", languageId)
        jsonObject.put("languageDetailId", languageDetailId)
        jsonObject.put("myAudioUrl", myAudioUrl)
        EBagApi.request(studentService.uploadRecord("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 获取当前段落的录音历史
     */
    fun recordHistory(languageId: String, callback: RequestCallBack<List<RecordHistory>>){
        val jsonObject = JSONObject()
        jsonObject.put("languageId", languageId)
        EBagApi.request(studentService.recordHistory("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 学习小组列表
     */
    fun groups(classId: String, callback: RequestCallBack<ArrayList<GroupBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        EBagApi.request(studentService.groups("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 学习小组列表
     */
    fun groupMember(groupId: String, callback: RequestCallBack<ArrayList<GroupUserBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("groupId", groupId)
        EBagApi.request(studentService.groupMember("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 劳动任务
     */
    fun labourTasks(page: Int, pageSize: Int, callback: RequestCallBack<ArrayList<LabourBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("page", page)
        jsonObject.put("pageSize", pageSize)
        EBagApi.request(studentService.labourTasks("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 数学公式
     */
    fun formula(formulaId: String, page: Int, pageSize: Int, callback: RequestCallBack<ArrayList<FormulaTypeBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("formulaId", formulaId)
        jsonObject.put("page", page)
        jsonObject.put("pageSize", pageSize)
        EBagApi.request(studentService.formula("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 百度授权
     */
    fun baiduOauth(callback: RequestCallBack<BaiduOauthBean>){
        EBagApi.startNormalRequest(
                studentService.baiduOauth(
                        "client_credentials",
                        "QOgyuYouUa3L7WECI8B44dOX",
                        "b8c2cff26fda439e47eae9bc46c17ed2"),
                callback
        )
    }

    /**
     * 语音识别
     */
    fun speechRecognize(filePath: String, token: String, callback: RequestCallBack<SpeechRecognizeBean>){
        val file = File(filePath)
        if(!file.exists() || !file.isFile){
            return
        }
        val speech = base64(file)
        if(speech == ""){
            return
        }
        val speechRecognizeVo = SpeechRecognizeVo()
        speechRecognizeVo.token = token
        speechRecognizeVo.len = file.length()
        speechRecognizeVo.speech = speech
        EBagApi.startNormalRequest(
                studentService.speachRecognize(EBagApi.createBody(JSON.toJSONString(speechRecognizeVo))),
                callback
        )
    }

    private fun base64(file: File): String{
        try {
            var inputFile = FileInputStream(file)
            val buffer = ByteArray(file.length().toInt())
            inputFile.read(buffer)
            inputFile.close()
            val encodedString = Base64.encodeToString(buffer, Base64.DEFAULT)
            L.e("Base64", "Base64---->" + encodedString)
            return encodedString
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}