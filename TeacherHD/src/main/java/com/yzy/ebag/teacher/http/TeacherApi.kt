package com.yzy.ebag.teacher.http

import com.yzy.ebag.teacher.bean.*
import ebag.core.bean.QuestionBean
import ebag.core.http.network.RequestCallBack
import ebag.hd.bean.response.NoticeBean
import ebag.hd.http.EBagApi
import ebag.hd.http.EBagClient
import org.json.JSONArray
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
        jsonObject.put("roleCode", "teacher")
        EBagApi.request(teacherService.firstPage("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 班级
     */
    fun clazzSpace(callback: RequestCallBack<List<SpaceBean>>){
        EBagApi.request(teacherService.clazzSpace("v1"), callback)
    }

    /**
     * 布置作业页面
     */
    fun assignmentData(type: String, callback: RequestCallBack<AssignmentBean>){
        val jsonObject = JSONObject()
        jsonObject.put("type", type)
        EBagApi.request(teacherService.assignmentData("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 根据班级查询班级下所有的学习小组
     */
    fun studyGroup(classId: String, callback: RequestCallBack<List<GroupBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        EBagApi.request(teacherService.studyGroup("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 根据班级查询班级下所有的成员（老师，学生，家长）
     */
    fun clazzMember(classId: String, callback: RequestCallBack<ClassMemberBean>){
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        EBagApi.request(teacherService.clazzMember("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 创建学习小组
     */
    fun createGroup(classId: String, groupName: String, list: List<BaseStudentBean>, callback: RequestCallBack<String>){
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        jsonObject.put("groupName", groupName)
        jsonObject.put("studentCount", list.size)
        val jsonArray = JSONArray()
        list.forEach {
            val jsonObj = JSONObject()
            jsonObj.put("uid", it.uid)
            jsonObj.put("duties", it.duties)
            jsonArray.put(jsonObj)
        }
        jsonObject.put("clazzUserGroupVos", jsonArray)
        EBagApi.request(teacherService.createGroup("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 修改小组
     */
    fun modifyGroup(groupId: String, classId: String, groupName: String, list: List<BaseStudentBean>, callback: RequestCallBack<String>){
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        jsonObject.put("groupName", groupName)
        jsonObject.put("studentCount", list.size)
        jsonObject.put("groupId", groupId)
        val jsonArray = JSONArray()
        list.forEach {
            val jsonObj = JSONObject()
            jsonObj.put("uid", it.uid)
            jsonObj.put("duties", it.duties)
            jsonArray.put(jsonObj)
        }
        jsonObject.put("clazzUserGroupVos", jsonArray)
        EBagApi.request(teacherService.modifyGroup("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 根据年级 获取科目
     */
    fun getSubject(code: String, callback: RequestCallBack<List<BaseSubjectBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("groupCode", "grade_subject")
        jsonObject.put("parentCode", code)
        EBagApi.request(teacherService.getBaseData("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 创建班级
     */
    fun createClass(schoolCode: String?, gradeCode: String?, className: String?, subjectCode: String?, callback: RequestCallBack<String>){
        val jsonObject = JSONObject()
        jsonObject.put("gradeCode", gradeCode)
        jsonObject.put("className", className)
        jsonObject.put("introduce", "")
        jsonObject.put("school", schoolCode)
        jsonObject.put("subCode", subjectCode)
        EBagApi.request(teacherService.createClass("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 根据班级获取教材版本
     */
    fun searchBookVersion(classesId: List<String>, callback: RequestCallBack<List<BookVersionBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("clazzIds", JSONArray(classesId))
        EBagApi.request(teacherService.searchBookVersion("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 根据年级获取单元 和 题型信息
     */
    fun unitAndQuestion(type: String, gradeCode: String, bookVersionId: String?, callback: RequestCallBack<AssignmentBean>){
        val jsonObject = JSONObject()
        jsonObject.put("type", type)
        jsonObject.put("gradeCode", gradeCode)
        if(bookVersionId != null)
            jsonObject.put("bookVersionId", bookVersionId)
        EBagApi.request(teacherService.assignmentData("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 发布公告
     */
    fun publishNotice(classId: String, content: String, urls: String, callback: RequestCallBack<String>){
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        jsonObject.put("content", content)
        jsonObject.put("photoUrl", urls)
        EBagApi.request(teacherService.publishNotice("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 添加老师
     */
    fun addTeacher(classId: String, ysbCode: String, subCode: String, callback: RequestCallBack<String>){
        val jsonObject = JSONObject()
        jsonObject.put("ysbCode",ysbCode)
        jsonObject.put("classId",classId)
        jsonObject.put("subCode",subCode)
        EBagApi.request(teacherService.addTeacher("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 查询最新公告
     */
    fun newestNotice(classId: String, callback: RequestCallBack<NoticeBean>){
        val jsonObject = JSONObject()
        jsonObject.put("classId",classId)
        EBagApi.request(teacherService.newestNotice("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 查询试题
     */
    fun searchQuestion(unitBean: AssignUnitBean.UnitSubBean, difficulty: String?, type: String, page: Int, callback: RequestCallBack<List<QuestionBean>>){
        val jsonObject = JSONObject()
        if (unitBean.unitCode != null) {
            if (unitBean.isUnit)
                jsonObject.put("bookUnit", unitBean.unitCode)
            else
                jsonObject.put("bookCatalog", unitBean.unitCode)
        }
        difficulty ?: jsonObject.put("level",difficulty)
        jsonObject.put("type",type)
        jsonObject.put("page",page)
        jsonObject.put("pageSize",10)
        EBagApi.request(teacherService.searchQuestion("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 智能推送
     */
    fun smartPush(count: Int, unitBean: AssignUnitBean.UnitSubBean, difficulty: String?, type: String, bookVersionId: String?, callback: RequestCallBack<List<QuestionBean>>){
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

    /**
     * 试卷列表
     */
    fun testPaperList(testPaperFlag: String, gradeCode: String, unitId: String?, callback: RequestCallBack<List<TestPaperListBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("testPaperFlag",testPaperFlag)
        jsonObject.put("gradeCode",gradeCode)
        if (unitId != null)
            jsonObject.put("unitId",unitId)
        jsonObject.put("page",1)
        jsonObject.put("pageSize",100)
        EBagApi.request(teacherService.testPaperList("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 组卷
     */
    fun organizePaper(paperName: String, gradeCode: String, unitId: String?, questionList: ArrayList<QuestionBean>, callback: RequestCallBack<String>){
        val jsonObject = JSONObject()
        jsonObject.put("name",paperName)
        jsonObject.put("gradeCode",gradeCode)
        if (unitId != null)
            jsonObject.put("unitId",unitId)
        val jsonArray = JSONArray()
        questionList.forEach {
            val jsonObj = JSONObject()
            jsonObj.put("questionId", it.id)
            jsonArray.put(jsonObj)
        }
        jsonObject.put("questionVos", jsonArray)
        EBagApi.request(teacherService.organizePaper("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 预览试卷
     */
    fun previewTestPaper(paperId: String, callback: RequestCallBack<List<QuestionBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("testPaperId",paperId)
        EBagApi.request(teacherService.previewTestPaper("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 发布作业
     */
    fun publishHomework(
            classes: ArrayList<AssignClassBean>,
            groupIds: ArrayList<String>? = null,
            isGroup: Boolean,
            type: String,
            content: String,
            endTime: String,
            subCode: String,
            bookVersionId: String,
            questionList: ArrayList<QuestionBean>,
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
        jsonObject.put("type", type)
        jsonObject.put("endTime", endTime)
        jsonObject.put("subCode", subCode)
        jsonObject.put("bookVersionId", bookVersionId)
        val questionArray = JSONArray()
        questionList.forEach {
            val questionJson = JSONObject()
            questionJson.put("questionId", it.questionId)
            questionJson.put("questionType", it.type)
            questionArray.put(questionJson)
        }
        jsonObject.put("homeWorkQuestionDtos", questionArray)
        EBagApi.request(teacherService.publishHomework("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 查询已经布置的作业列表
     */
    fun searchPublish(type: String, callback: RequestCallBack<List<CorrectingBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("type", type)
        EBagApi.request(teacherService.searchPublish("v1", EBagApi.createBody(jsonObject)), callback)
    }

    /**
     * 查询所教科目
     */
    fun searchCourse(classId: String, callback: RequestCallBack<List<MyCourseBean>>){
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        EBagApi.request(teacherService.searchCourse("v1", EBagApi.createBody(jsonObject)), callback)
    }

    fun addCourse(classId: String,
                  bookVersionId: String,
                  bookVersionCode: String,
                  bookVersionName: String,
                  bookCode: String,
                  bookName: String,
                  semeterCode: String,
                  semeterName: String,
                  callback: RequestCallBack<String>){
        val jsonObject = JSONObject()
        jsonObject.put("classId", classId)
        jsonObject.put("bookVersionId", bookVersionId)
        jsonObject.put("bookVersionCode", bookVersionCode)
        jsonObject.put("bookVersionName", bookVersionName)
        jsonObject.put("bookCode", bookCode)
        jsonObject.put("bookName", bookName)
        jsonObject.put("semeterCode", semeterCode)
        jsonObject.put("semeterName", semeterName)
        EBagApi.request(teacherService.addCourse("v1", EBagApi.createBody(jsonObject)), callback)
    }
}