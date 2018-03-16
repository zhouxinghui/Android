package com.yzy.ebag.teacher.base

import cn.jpush.android.api.JPushInterface
import com.umeng.socialize.Config
import com.umeng.socialize.PlatformConfig
import com.umeng.socialize.UMShareAPI
import com.yun.vnc.bVNC.VNCApp

/**
 * Created by YZY on 2017/12/20.
 */
class App : VNCApp() {
    override fun onCreate() {
        super.onCreate()
        UMShareAPI.get(this)//初始化sdk
        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        Config.DEBUG = true

        init()
    }

    private fun init(){
        //微信
        PlatformConfig.setWeixin("wx4adbb68ec1c80484", "f5b0f7b3460714522fd1e0d85bcfeb34")
        //新浪微博(第三个参数为回调地址)
        PlatformConfig.setSinaWeibo("2231742216", "f894753343fa80a5f11bcb06659f489e", "http://www.yun-bag.com/ebag-portal/oauth/sina/back")
        //QQ
        PlatformConfig.setQQZone("1105151620", "MWJSD2sjd20VTHUz")

        JPushInterface.setDebugMode(true)    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this)            // 初始化 JPush
    }
}