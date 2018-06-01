package com.yzy.ebag.student.base

import cn.jpush.android.api.JPushInterface
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechUtility
import ebag.hd.base.BaseApp

/**
 * Created by YZY on 2017/12/20.
 */
class App : BaseApp(){
    override fun onCreate() {
        super.onCreate()
        JPushInterface.setDebugMode(true)    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this)            // 初始化 JPush

        //         将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
//         请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5b10e297")
    }
}