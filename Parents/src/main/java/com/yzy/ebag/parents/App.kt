package com.yzy.ebag.parents

import com.umeng.socialize.PlatformConfig
import com.umeng.socialize.UMShareAPI
import ebag.mobile.base.BaseApp

class App : BaseApp() {


    override fun onCreate() {
        super.onCreate()
        UMShareAPI.get(this)
        PlatformConfig.setWeixin("wx626a1c084ecd9ca9", "b0552424630c4c0b78059a0dcc8a4131")
        PlatformConfig.setSinaWeibo("2409001064","fda00af9dbd3e39112c8c63ad1aebef3","http://www.yun-bag.com/ebag-portal/oauth/sina/back")
        PlatformConfig.setQQZone("1105151620", "MWJSD2sjd20VTHUz")
    }
}