package com.yzy.ebag.student.base

import android.content.Context
import android.support.multidex.MultiDex
import com.tencent.smtt.sdk.QbSdk
import ebag.core.util.L
import ebag.mobile.base.BaseApp

/**
 * Created by YZY on 2018/5/14.
 */
class App: BaseApp() {
    override fun onCreate() {
        super.onCreate()
        //初始化腾讯X5内核，浏览Office文件用的
        QbSdk.initX5Environment(this, object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {

            }

            override fun onViewInitFinished(b: Boolean) {
                L.e("onViewInitFinished", "X5内核初始化：" + b)
            }
        })
    }
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}