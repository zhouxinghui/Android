package com.yzy.ebag.student.base

import cn.jpush.android.api.JPushInterface
import ebag.hd.base.BaseApp

/**
 * Created by YZY on 2017/12/20.
 */
class App : BaseApp(){
    override fun onCreate() {
        super.onCreate()
        JPushInterface.setDebugMode(true)    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this)            // 初始化 JPush
    }
}