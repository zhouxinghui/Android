package com.yzy.ebag.teacher.base

import com.umeng.socialize.Config
import com.umeng.socialize.PlatformConfig
import com.umeng.socialize.UMShareAPI
import ebag.hd.base.BaseApp

/**
 * Created by YZY on 2017/12/20.
 */
class App : BaseApp() {
    override fun onCreate() {
        super.onCreate()
        UMShareAPI.get(this)//初始化sdk
        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        Config.DEBUG = true

    }

    init {
        //微信
        PlatformConfig.setWeixin("wx4adbb68ec1c80484", "f5b0f7b3460714522fd1e0d85bcfeb34")
        //新浪微博(第三个参数为回调地址)
        PlatformConfig.setSinaWeibo("2231742216", "f894753343fa80a5f11bcb06659f489e", "http://sns.whalecloud.com/sina2/callback")
        //QQ
        PlatformConfig.setQQZone("1105151620", "MWJSD2sjd20VTHUz")
    }
}