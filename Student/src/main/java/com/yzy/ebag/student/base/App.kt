package com.yzy.ebag.student.base

import android.content.Context
import android.support.multidex.MultiDex
import ebag.mobile.base.BaseApp

/**
 * Created by YZY on 2018/5/14.
 */
class App: BaseApp() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}