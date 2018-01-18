package com.yzy.ebag.student.activity.main

import com.yzy.ebag.student.bean.ClassesInfoBean

/**
 * Created by unicho on 2018/1/12.
 */
interface MainView {

    fun mainInfoStart()

    fun mainInfoSuccess(classesInfoBean: ClassesInfoBean)

    fun mainInfoError(exception: Throwable)
}