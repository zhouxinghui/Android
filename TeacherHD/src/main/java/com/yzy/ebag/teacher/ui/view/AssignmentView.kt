package com.yzy.ebag.teacher.ui.view

import com.yzy.ebag.teacher.bean.AssignmentBean

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
}