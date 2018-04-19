package com.yzy.ebag.teacher.http

import com.yzy.ebag.teacher.bean.*
import ebag.core.bean.QuestionBean
import ebag.core.http.network.RequestCallBack
import ebag.core.util.StringUtils
import ebag.mobile.bean.UnitBean
import ebag.mobile.http.EBagApi
import ebag.mobile.http.EBagClient
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by caoyu on 2018/1/8.
 */
object TeacherApi {

    private val teacherService: TeacherService by lazy {
        EBagClient.createRetrofitService(TeacherService::class.java)
    }

    /**首页网络数据*/
    fun firstPage(callback: RequestCallBack<FirstPageBean>){
        val jsonObject = JSONObject()
        jsonObject.put("roleCode", "2")
        EBagApi.request(teacherService.firstPage("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**布置作业页面*/
    fun assignmentData(type: String, callback: RequestCallBack<AssignmentBean>){
        val jsonObject = JSONObject()
        jsonObject.put("type", type)
        EBagApi.request(teacherService.assignmentData("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**根据年级获取单元 和 题型信息*/
    fun unitAndQuestion(type: String, gradeCode: String, classIds: ArrayList<AssignClassBean>?, bookVersionId: String?, callback: RequestCallBack<AssignmentBean>){
        val jsonObject = JSONObject()
        jsonObject.put("type", type)
        jsonObject.put("gradeCode", gradeCode)
        if(bookVersionId != null)
            jsonObject.put("bookVersionId", bookVersionId)
        if (classIds != null){
            val jsonArray = JSONArray()
            classIds.forEach {
                jsonArray.put(it.classId)
            }
            jsonObject.put("classIds",jsonArray)
        }
        EBagApi.request(teacherService.assignmentData("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**布置作业页面-切换版本请求数据*/
    fun assignDataByVersion(type: String, versionId: String?, subCode: String?, unitCode: String?, callback: RequestCallBack<AssignmentBean>){
        val jsonObject = JSONObject()
        jsonObject.put("type", type)
        jsonObject.put("bookVersionId", versionId)
        jsonObject.put("subCode", subCode)
        if (!StringUtils.isEmpty(unitCode)){
            jsonObject.put("unitCode", unitCode)
        }
        EBagApi.request(teacherService.assignDataByVersion("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**试卷列表*/
    fun testPaperList(testPaperFlag: String, gradeCode: String, unitId: String?, subCode: String?, callback: RequestCallBack<List<TestPaperListBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("testPaperFlag", testPaperFlag)
        jsonObject.put("gradeCode", gradeCode)
        jsonObject.put("subCode", subCode)
        if (unitId != null)
            jsonObject.put("unitId", unitId)
        jsonObject.put("page",1)
        jsonObject.put("pageSize",100)
        EBagApi.request(teacherService.testPaperList("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**根据班级获取教材版本*/
    fun searchBookVersion(classesId: List<String>, callback: RequestCallBack<BookVersionBean>){
        val jsonObject = JSONObject()
        jsonObject.put("clazzIds", JSONArray(classesId))
        EBagApi.request(teacherService.searchBookVersion("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**查询试题*/
    fun searchQuestion(unitBean: UnitBean.UnitSubBean, difficulty: String?, type: String, gradeCode: String, semeterCode: String, course: String, bookVersionId: String, page: Int, callback: RequestCallBack<List<QuestionBean>>){
        val jsonObject = JSONObject()
        if (unitBean.unitCode != null) {
            if (unitBean.isUnit)
                jsonObject.put("bookUnit", unitBean.unitCode)
            else
                jsonObject.put("bookCatalog", unitBean.unitCode)
        }
        if (difficulty != null)
            jsonObject.put("level",difficulty)
        jsonObject.put("gradeCode",gradeCode)
        jsonObject.put("semesterCode",semeterCode)
        jsonObject.put("course",course)
        jsonObject.put("bookVersionId",bookVersionId)

        jsonObject.put("type",type)
        jsonObject.put("page",page)
        jsonObject.put("pageSize",10)
        EBagApi.request(teacherService.searchQuestion("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**组卷*/
    fun organizePaper(paperName: String, gradeCode: String, unitId: String?, subCode: String, questionList: ArrayList<QuestionBean>, callback: RequestCallBack<String>){
        val jsonObject = JSONObject()
        jsonObject.put("name",paperName)
        jsonObject.put("gradeCode",gradeCode)
        jsonObject.put("subCode",subCode)
        if (unitId != null)
            jsonObject.put("unitId",unitId)
        val jsonArray = JSONArray()
        questionList.forEach {
            val jsonObj = JSONObject()
            jsonObj.put("questionId", it.questionId)
            jsonArray.put(jsonObj)
        }
        jsonObject.put("questionVos", jsonArray)
        EBagApi.request(teacherService.organizePaper("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**智能推送*/
    fun smartPush(count: Int, unitBean: UnitBean.UnitSubBean, difficulty: String?, type: String, bookVersionId: String?, callback: RequestCallBack<List<QuestionBean>>){
        val jsonObject = JSONObject()
        if (unitBean.unitCode != null) {
            if (unitBean.isUnit)
                jsonObject.put("bookUnit", unitBean.unitCode)
            else
                jsonObject.put("bookCatalog", unitBean.unitCode)
        }
        if(bookVersionId != null){
            jsonObject.put("bookVersionId", bookVersionId)
        }
        difficulty ?: jsonObject.put("level",difficulty)
        jsonObject.put("type",type)
        jsonObject.put("count", count)
        EBagApi.request(teacherService.smartPush("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**预览试卷*/
    fun previewTestPaper(paperId: String, callback: RequestCallBack<List<QuestionBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("testPaperId",paperId)
        EBagApi.request(teacherService.previewTestPaper("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**发布作业*/
    fun publishHomework(
            classes: ArrayList<AssignClassBean>,
            groupIds: ArrayList<String>? = null,
            isGroup: Boolean,
            type: String,
            remark: String,
            content: String,
            endTime: String,
            subCode: String,
            bookVersionId: String,
            questionList: ArrayList<QuestionBean>? = null,
            testPaperId: String? = null,
            callback: RequestCallBack<String>){
        val jsonObject = JSONObject()
        val classArray = JSONArray()
        classes.forEach {
            classArray.put(it.classId)
        }
        if (groupIds != null){
            val groupArray = JSONArray()
            groupIds.forEach { groupArray.put(it) }
            jsonObject.put("groupIds", groupArray)
        }
        jsonObject.put("clazzIds", classArray)
        if (isGroup)
            jsonObject.put("groupType", "2")
        else
            jsonObject.put("groupType", "1")
        jsonObject.put("content", content)
        jsonObject.put("remark", remark)
        jsonObject.put("type", type)
        jsonObject.put("endTime", endTime)
        jsonObject.put("subCode", subCode)
        jsonObject.put("bookVersionId", bookVersionId)
        if (questionList != null) {
            val questionArray = JSONArray()
            questionList.forEach {
                val questionJson = JSONObject()
                questionJson.put("questionId", it.questionId)
                questionJson.put("questionType", it.type)
                questionArray.put(questionJson)
            }
            jsonObject.put("homeWorkQuestionDtos", questionArray)
        }
        if (testPaperId != null){
            jsonObject.put("testPaperId", testPaperId)
        }
        EBagApi.request(teacherService.publishHomework("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**根据班级查询班级下所有的学习小组*/
    fun studyGroup(classId: String, callback: RequestCallBack<List<GroupBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        EBagApi.request(teacherService.studyGroup("v1", EBagApi.createBody(jsonObject)), callback)
    }
}