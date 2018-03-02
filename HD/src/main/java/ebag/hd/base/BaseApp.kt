package ebag.hd.base

import com.tencent.smtt.sdk.QbSdk
import ebag.core.base.App
import ebag.core.util.L

/**
 * Created by YZY on 2017/12/20.
 */
open class BaseApp : App(){
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

}