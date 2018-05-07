package com.yzy.ebag.teacher.base

import com.tencent.smtt.sdk.QbSdk
import com.umeng.socialize.PlatformConfig
import ebag.core.util.L
import ebag.mobile.base.BaseApp

/**
 * Created by YZY on 2018/4/16.
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

        //微信
        PlatformConfig.setWeixin("wx4adbb68ec1c80484", "f5b0f7b3460714522fd1e0d85bcfeb34")
        //新浪微博(第三个参数为回调地址)
        PlatformConfig.setSinaWeibo("2231742216", "f894753343fa80a5f11bcb06659f489e", "http://www.yun-bag.com/ebag-portal/oauth/sina/back")
        //QQ
        PlatformConfig.setQQZone("1105151620", "MWJSD2sjd20VTHUz")
    }
}