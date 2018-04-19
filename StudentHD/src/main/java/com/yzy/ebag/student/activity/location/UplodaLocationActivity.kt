package com.yzy.ebag.student.activity.location

import android.os.CountDownTimer
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.yzy.ebag.student.R
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.T
import kotlinx.android.synthetic.main.activity_uploadlocation.*


/**
 * Created by fansan on 2018/3/26.
 */
class UplodaLocationActivity : BaseActivity() {
    private lateinit var listener: MyLocationListener
    override fun getLayoutId(): Int = R.layout.activity_uploadlocation
    private var flag = false
    private var latitude = ""
    private var longitude = ""
    private var address = ""
    private lateinit var timeCountDown: TimeCountDown
    private lateinit var locationClient: LocationClient

    override fun initViews() {


        locationClient = LocationClient(application)
        listener = MyLocationListener()
        locationClient.registerLocationListener(listener)
        val option = LocationClientOption()
        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        option.openGps = true
        option.setIsNeedAddress(true)
        option.setIgnoreKillProcess(false)
        option.setIsNeedLocationPoiList(true)
        locationClient.locOption = option
        locationClient.start()
        timeCountDown = TimeCountDown(1000 * 10, 1000)
        timeCountDown.start()
        title_bar.setOnRightClickListener {
            if (flag) {
                if (contentEdit.text.isNotEmpty()) {
                    StudentApi.uploadLocation(address, contentEdit.text.toString(), longitude, latitude, object : RequestCallBack<String>() {

                        override fun onSuccess(entity: String?) {
                            T.show(this@UplodaLocationActivity, "上传成功")
                            setResult(1000)
                            finish()
                        }

                        override fun onError(exception: Throwable) {
                            exception.handleThrowable(this@UplodaLocationActivity)
                        }

                    })
                } else {
                    T.show(this@UplodaLocationActivity, "请输入备注")
                }
            } else {
                T.show(this@UplodaLocationActivity, "尚未获取到位置信息")
            }

        }
    }


    inner class MyLocationListener : BDAbstractLocationListener() {

        override fun onReceiveLocation(p0: BDLocation?) {
            var location = ""
            val addr = p0?.poiList
            val province = p0?.province    //获取省份
            val city = p0?.city    //获取城市
            val district = p0?.district    //获取区县
            val a = p0?.address
            latitude = p0?.latitude.toString()    //获取纬度信息
            longitude = p0?.longitude.toString()  //获取经度信息
            if (a != null ) {
                //location = addr[0].name
                address = "$province$city$district ${a.address}"
                uploadlocation_tv.text = address
                flag = true
                timeCountDown.cancel()
            } else {
                getLocationFailed()
            }
        }

    }

    private fun getLocationFailed() {
        uploadlocation_tv.text = "获取地址失败，点击重试"
        uploadlocation_tv.setOnClickListener {
            uploadlocation_tv.text = "正在获取..."
            locationClient.restart()
            timeCountDown.start()
        }
    }


    inner class TimeCountDown(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onFinish() {
            getLocationFailed()
        }

        override fun onTick(millisUntilFinished: Long) {

        }

    }
}