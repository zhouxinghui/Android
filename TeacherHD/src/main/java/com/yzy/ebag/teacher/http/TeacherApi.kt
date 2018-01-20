package com.yzy.ebag.teacher.http

import com.yzy.ebag.teacher.bean.*
import ebag.core.http.network.RequestCallBack
import ebag.core.util.L
import ebag.hd.http.EBagApi
import ebag.hd.http.EBagClient
import org.json.JSONArray
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
     * 班级
     */
    fun clazzSpace(callback: RequestCallBack<List<SpaceBean>>){
        val jsonObject = JSONObject()
        EBagApi.request(teacherService.clazzSpace("v1", EBagApi.createBody(jsonObject)), callback)
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
    fun createGroup(classId: String,groupName: String, list: List<BaseStudentBean>, callback: RequestCallBack<String>){
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
        L.e("json：：：：：：" + jsonObject.toString())
        EBagApi.request(teacherService.createGroup("v1", EBagApi.createBody(jsonObject)), callback)
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

}