package com.yzy.ebag.student.http

import android.util.Base64
import com.alibaba.fastjson.JSON
import com.yzy.ebag.student.bean.*
import ebag.core.http.network.RequestCallBack
import ebag.core.util.L
import ebag.hd.bean.ParentBean
import ebag.hd.bean.PrepareFileBean
import ebag.hd.bean.response.UserEntity
import ebag.hd.http.EBagApi
import ebag.hd.http.EBagClient
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream


/**
 * Created by caoyu on 2018/1/8.
 */
object StudentApi {

    private val studentService: StudentService by lazy {
        EBagClient.createRetrofitService(StudentService::class.java)
    }

    fun login(account: String, pwd: String, roleCode: String, callback: RequestCallBack<UserEntity>) {
        val jsonObj = JSONObject()
        jsonObj.put("loginAccount", account)
        jsonObj.put("password", pwd)
        jsonObj.put("loginType", 1)
        jsonObj.put("roleCode", roleCode)
        EBagApi.request(studentService.login("v1", EBagApi.createBody(jsonObj)), callback)
    }

    /**
     * 首页
     */
    fun mainInfo(classId: String, callback: RequestCallBack<ClassesInfoBean>) {
        val jsonObj = JSONObject()
        jsonObj.put("classId", classId)
        jsonObj.put("roleCode", "1")
        EBagApi.request(studentService.mainInfo("v1", EBagApi.createBody(jsonObj)), callback)
    }

    /**
     * 作业列表
     */
    fun subjectWorkList(type: String, classId: String?, subCode: String, page: Int, pageSize: Int, callback: RequestCallBack<ArrayList<SubjectBean>>) {
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
    fun errorTopic(classId: String, subCode: String, page: Int, pageSize: Int, callback: RequestCallBack<ArrayList<ErrorTopicBean>>) {
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
    fun clazzSpace(callback: RequestCallBack<List<SpaceBean>>) {
        val jsonObject = JSONObject()
        EBagApi.request(studentService.clazzSpace("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 获取跟读列表
     */
    fun getReadList(unitCode: String, page: Int, pageSize: Int, callback: RequestCallBack<ReadOutBean>) {
        val jsonObject = JSONObject()
        jsonObject.put("unitCode", unitCode)
        jsonObject.put("page", page)
        jsonObject.put("pageSize", pageSize)
        EBagApi.request(studentService.getReadList("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 获取跟读详情里头 句子的详情
     */
    fun getReadDetailList(languageId: String, callback: RequestCallBack<List<ReadDetailBean>>) {
        val jsonObject = JSONObject()
        jsonObject.put("languageId", languageId)
        EBagApi.request(studentService.getReadDetailList("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 获取练字列表
     */
    fun getWordsList(unitCode: String, callback: RequestCallBack<WordsBean>) {
        val jsonObject = JSONObject()
        jsonObject.put("unitCode", unitCode)
        EBagApi.request(studentService.getWordsList("v1", EBagApi.createBody(jsonObject)), callback)

    }

    /**
     * 上传跟读录音文件
     */
    fun uploadRecord(classId: String, languageId: String, languageDetailId: String, score: String,  myAudioUrl: String, callback: RequestCallBack<ReadUploadResponseBean>) {
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        jsonObject.put("score", score)
        jsonObject.put("languageId", languageId)
        jsonObject.put("languageDetailId", languageDetailId)
        jsonObject.put("myAudioUrl", myAudioUrl)
        EBagApi.request(studentService.uploadRecord("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 获取当前段落的录音历史
     */
    fun recordHistory(languageId: String, callback: RequestCallBack<List<RecordHistory>>) {
        val jsonObject = JSONObject()
        jsonObject.put("languageId", languageId)
        EBagApi.request(studentService.recordHistory("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 学习小组列表
     */
    fun groups(classId: String, callback: RequestCallBack<ArrayList<GroupBean>>) {
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        EBagApi.request(studentService.groups("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 学习小组列表
     */
    fun groupMember(groupId: String, callback: RequestCallBack<ArrayList<GroupUserBean>>) {
        val jsonObject = JSONObject()
        jsonObject.put("groupId", groupId)
        EBagApi.request(studentService.groupMember("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 劳动任务
     */
    fun labourTasks(page: Int, pageSize: Int, callback: RequestCallBack<ArrayList<LabourBean>>) {
        val jsonObject = JSONObject()
        jsonObject.put("page", page)
        jsonObject.put("pageSize", pageSize)
        EBagApi.request(studentService.labourTasks("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 数学公式
     */
    fun formula(formulaId: String, page: Int, pageSize: Int, callback: RequestCallBack<ArrayList<FormulaTypeBean>>) {
        val jsonObject = JSONObject()
        jsonObject.put("formulaId", formulaId)
        jsonObject.put("page", page)
        jsonObject.put("pageSize", pageSize)
        EBagApi.request(studentService.formula("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 备课文件预习
     */
    fun prepareList(classId: String, callback: RequestCallBack<List<PrepareFileBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        EBagApi.request(studentService.prepareList("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 百度授权
     */
    fun baiduOauth(callback: RequestCallBack<BaiduOauthBean>) {
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
    fun speechRecognize(filePath: String, token: String, subCode: String, callback: RequestCallBack<SpeechRecognizeBean>) {
        val file = File(filePath)
        if (!file.exists() || !file.isFile) {
            return
        }
        val speech = base64(file)
        if (speech == "") {
            return
        }
        val speechRecognizeVo = SpeechRecognizeVo()
        if (subCode == "yy")
            speechRecognizeVo.dev_pid = 1737
        else
            speechRecognizeVo.dev_pid = 1536
        speechRecognizeVo.token = token
        speechRecognizeVo.len = file.length()
        speechRecognizeVo.speech = speech
        EBagApi.startNormalRequest(
                studentService.speachRecognize(EBagApi.createBody(JSON.toJSONString(speechRecognizeVo))),
                callback
        )
    }

    private fun base64(file: File): String {
        try {
            var inputFile = FileInputStream(file)
            val buffer = ByteArray(file.length().toInt())
            inputFile.read(buffer)
            inputFile.close()
            val encodedString = Base64.encodeToString(buffer, Base64.DEFAULT)
            L.e("Base64", "Base64---->$encodedString")
            return encodedString.replace("\n","")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun addUserGrowth(className: String, type: String, title: String, content: String, image: String, gradeCode: String, callback: RequestCallBack<String>) {
        val jsonObject = JSONObject()
        jsonObject.put("className", className)
        jsonObject.put("type", type)
        jsonObject.put("title", title)
        jsonObject.put("content", content)
        jsonObject.put("image", image)
        jsonObject.put("gradeCode", gradeCode)
        EBagApi.request(studentService.addUserGrowth("1", EBagApi.createBody(jsonObject)), callback)
    }

    fun searchUserGrowthList(page: Int, pageSize: Int, gradeCode: String, type: String, callback: RequestCallBack<List<Diary>>) {
        val jsonObject = JSONObject()
        jsonObject.put("page", page)
        jsonObject.put("pageSize", pageSize)
        jsonObject.put("gradeCode", gradeCode)
        jsonObject.put("type", type)
        EBagApi.request(studentService.searchUserGrowthList("1", EBagApi.createBody(jsonObject)), callback)
    }

    fun uploadLocation(address: String, remark: String, longitude: String, latitude: String, callback: RequestCallBack<String>) {
        val jsonObject = JSONObject()
        jsonObject.put("address", address)
        jsonObject.put("remark", remark)
        jsonObject.put("longitude", longitude)
        jsonObject.put("latitude", latitude)
        EBagApi.request(studentService.uploadLocation("1", EBagApi.createBody(jsonObject)), callback)
    }

    fun searchLocation(page: Int, callback: RequestCallBack<LocationBean>) {
        val jsonObject = JSONObject()
        jsonObject.put("page", page)
        jsonObject.put("pageSize", 10)
        EBagApi.request(studentService.searchLocation("1", EBagApi.createBody(jsonObject)), callback)
    }

    fun uploadWord(classId: String, words: String, unitId: String, wordUrl: String, callback: RequestCallBack<String>) {

        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        jsonObject.put("words", words)
        jsonObject.put("unitId", unitId)
        jsonObject.put("wordUrl", wordUrl)
        EBagApi.request(studentService.uploadWord("1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 练字记录
     */
    fun wordRecord(classId: String, pageSize: Int, page: Int, callback: RequestCallBack<WordRecordBean>) {
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        jsonObject.put("pageSize", pageSize)
        jsonObject.put("page", page)
        EBagApi.request(studentService.wordRecord("1", EBagApi.createBody(jsonObject)), callback)
    }

    /*查询家长*/

    fun searchFamily(callback: RequestCallBack<List<ParentBean>>) {
        val jsonObject = JSONObject()
        EBagApi.request(studentService.searchFamily("1", EBagApi.createBody(jsonObject)), callback)
    }

    /*绑定家长*/
    fun bindParent(ysbCode: String, relationType: String, callback: RequestCallBack<String>) {
        val jsonObject = JSONObject()
        jsonObject.put("ysbCode", ysbCode)
        jsonObject.put("relationType", relationType)
        EBagApi.request(studentService.bindParent("1", EBagApi.createBody(jsonObject)), callback)
    }

    fun learningProcess(gradeCode:String,call: RequestCallBack<List<LeaningProgressBean>>) {
        val jsonObject = JSONObject()
        jsonObject.put("gradeCode",gradeCode)
        EBagApi.request(studentService.learningProcess("1", EBagApi.createBody(jsonObject)), call)
    }

    /*成长轨迹，考试成绩*/

    fun examSocre(classId: String, homeType: String, rid: String,gradeCode:String, callback: RequestCallBack<List<HomeworkBean>>) {
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        jsonObject.put("homeType", homeType)
        jsonObject.put("gradeCode", gradeCode)
        jsonObject.put("rid", rid)
        EBagApi.request(studentService.yearStatisticsByHomeWork("1", EBagApi.createBody(jsonObject)), callback)
    }
}