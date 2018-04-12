package ebag.core.base

import android.annotation.SuppressLint
import android.support.multidex.MultiDexApplication
import ebag.core.util.Constants
import ebag.core.util.DeviceTool
import ebag.core.util.SPUtils

/**
 * Created by YZY on 2018/1/8.
 */
open class App: MultiDexApplication(){

    companion object {
        lateinit var TOKEN: String
        var mContext: App? = null

        @SuppressLint("MissingPermission")
        fun modifyToken(token: String){
            TOKEN = token
            SPUtils.put(mContext, Constants.USER_TOKEN, token)
            SPUtils.put(mContext,Constants.IMEI,DeviceTool.getIMEI(mContext!!))
        }
        fun deleteToken(){
            TOKEN = ""
            SPUtils.remove(mContext, Constants.USER_TOKEN)
        }

    }



    override fun onCreate() {
        super.onCreate()
        mContext = this
        TOKEN = SPUtils.get(this, Constants.USER_TOKEN,"") as String
    }
}