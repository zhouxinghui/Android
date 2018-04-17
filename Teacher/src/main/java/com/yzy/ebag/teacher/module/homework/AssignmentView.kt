package com.yzy.ebag.teacher.module.homework

import com.yzy.ebag.teacher.bean.AssignmentBean
import com.yzy.ebag.teacher.bean.TestPaperListBean

/**
 * Created by YZY on 2018/1/8.
 */
interface AssignmentView {
    fun loadBaseStart()
    fun showBaseData(assignmentBean: AssignmentBean?)
    fun loadBaseError(t: Throwable)

    fun loadUnitAndQuestionStart()
    fun getUnitAndQuestion(assignmentBean: AssignmentBean?)
    fun loadUnitAndQuestionError(t: Throwable)

    fun loadTestListStart()
    fun getTestList(testList: List<TestPaperListBean>?)
    fun loadTestListError(t: Throwable)
}