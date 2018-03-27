package com.yzy.ebag.student.activity.location

import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.yzy.ebag.student.R
import ebag.core.base.BaseActivity
import android.util.Log
import com.yzy.ebag.student.http.StudentApi
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

    override fun initViews() {


        val locationClient = LocationClient(application)
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

        title_bar.setOnRightClickListener {
            if (flag) {
                if (contentEdit.text.isNotEmpty()) {
                    StudentApi.uploadLocation(address, contentEdit.text.toString(), longitude, latitude, object : RequestCallBack<String>() {

                        override fun onSuccess(entity: String?) {
                            T.show(this@UplodaLocationActivity, "上传成功")
                            setResult(1000)
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
            latitude = p0?.latitude.toString()    //获取纬度信息
            longitude = p0?.longitude.toString()  //获取经度信息
            if (addr!!.isNotEmpty()) {
                location = addr[0].name
                address = province + city + district + " $location"
                uploadlocation_tv.text = address
                flag = true
            } else {
                uploadlocation_tv.text = "获取地址失败"
            }
        }

    }
}