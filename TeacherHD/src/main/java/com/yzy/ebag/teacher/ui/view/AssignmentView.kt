package com.yzy.ebag.teacher.ui.view

import com.yzy.ebag.teacher.bean.AssignmentBean

/**
 * Created by YZY on 2018/1/8.
 */
interface AssignmentView {
    fun loadStart()
    fun showBaseData(assignmentBean: AssignmentBean?)
    fun loadError(t: Throwable)
}