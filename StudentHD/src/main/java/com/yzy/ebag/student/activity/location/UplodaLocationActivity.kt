package com.yzy.ebag.student.activity.location

import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.yzy.ebag.student.R
import ebag.core.base.BaseActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_uploadlocation.*


/**
 * Created by fansan on 2018/3/26.
 */
class UplodaLocationActivity : BaseActivity() {
    private lateinit var listener: MyLocationListener
    override fun getLayoutId(): Int = R.layout.activity_uploadlocation

    override fun initViews() {


        val locationClient = LocationClient(application)
        listener = MyLocationListener()
        locationClient.registerLocationListener(listener)
        val option = LocationClientOption()
        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        option.openGps = true
        option.setIsNeedAddress(true)
        option.setIgnoreKillProcess(false)
        locationClient.locOption = option
        locationClient.start()

        title_bar.setOnRightClickListener {


        }
    }


    inner class MyLocationListener : BDAbstractLocationListener() {

        override fun onReceiveLocation(p0: BDLocation?) {
            val addr = p0?.addrStr    //获取详细地址信息
            uploadlocation_tv.text = addr
        }

    }
}