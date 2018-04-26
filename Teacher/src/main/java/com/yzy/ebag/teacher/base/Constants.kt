package com.yzy.ebag.teacher.base

/**
 * Created by YZY on 2018/4/17.
 */
object Constants {
    const val ASSIGN_CATEGORY = "assign_category"
    const val ASSIGN_TITLE = "assign_title"
    const val ASSIGN_WORK = 1
    const val ASSIGN_AFTER = 2
    const val ASSIGN_TEST_PAPER = 4

    const val QUESTION_REQUEST = 120
    const val QUESTION_RESULT = 121
    const val PREVIEW_REQUEST = 130

    val PUBLISH_REQUEST = 110
    val PUBLISH_RESULT = 110

    //状态 1 未批改 2 已批改 0 未完成 3 老师评语完成 4 家长签名和评语完成
    const val CORRECT_UNFINISH =  "0"
    const val CORRECT_UNCORRECT =  "1"
    const val CORRECT_CORRECTED =  "2"
    const val CORRECT_TEACHER_REMARKED = "3"
    const val CORRECT_PARENT_REMARKED = "4"

    //职责(duties) 1 班主任 2 科任老师 3 学生 4 班长 5 课代表 6 组长
    const val TEACHER_IN_CHARGE = "1"
    const val TEACHER = "2"
    const val STUDENT = "3"
    const val MONITOR_OF_CLASS = "4"
    const val CLASS_REPRESENTATIVE = "5"
    const val GROUP_LEADER = "6"

    val PRIMARY_SCHOOL = "1"
    val JUNIOR_HIGH_SCHOOL = "2"
    val HIGH_SCHOOL = "3"
}