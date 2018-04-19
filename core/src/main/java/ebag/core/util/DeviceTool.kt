package ebag.core.util

import android.Manifest.permission.READ_PHONE_STATE
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresPermission
import android.telephony.TelephonyManager

object DeviceTool {

    @SuppressLint("MissingPermission", "HardwareIds")
    @RequiresPermission(READ_PHONE_STATE)
    fun getIMEI(context: Context): String? {

        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            telephonyManager.imei
        } else {
            telephonyManager.deviceId
        }

    }

}