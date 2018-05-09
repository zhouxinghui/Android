package com.yzy.ebag.parents.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.model.LatLngBounds
import com.baidu.mapapi.search.geocode.*
import com.baidu.trace.LBSTraceClient
import com.baidu.trace.api.track.HistoryTrackRequest
import com.baidu.trace.api.track.HistoryTrackResponse
import com.baidu.trace.api.track.OnTrackListener
import com.yzy.ebag.parents.R
import ebag.core.base.BaseActivity
import ebag.core.util.SerializableUtils
import ebag.core.util.loadHead
import ebag.mobile.base.Constants
import ebag.mobile.bean.MyChildrenBean
import ebag.mobile.bean.UserEntity
import kotlinx.android.synthetic.main.activity_location.*
import java.util.*

class LocationActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, LocationActivity::class.java))
        }
    }

    private lateinit var locationListener: MyLocationListener
    private lateinit var map: BaiduMap
    private lateinit var myLocationView: View
    private lateinit var locationTv: TextView
    private lateinit var childLocationTv: TextView
    private val overLayList = ArrayList<OverlayOptions>()
    override fun getLayoutId(): Int = R.layout.activity_location
    private var childLatitude: Double = 0.0
    private var childLongitude: Double = 0.0
    /*-------------------------------------------------------*/
    private val tag = 1
    private val serviceId: Long = 116060
    private lateinit var entityName: String
    private lateinit var childView: View
    private val latLngBuild = LatLngBounds.Builder()
    private lateinit var geo: GeoCoder

    override fun initViews() {
        map = mapview.map
        map.isMyLocationEnabled = true
        map.mapType = BaiduMap.MAP_TYPE_NORMAL
        myLocationView = View.inflate(this, R.layout.item_my_location, null)
        childView = View.inflate(this, R.layout.item_my_location, null)
        locationTv = myLocationView.findViewById(R.id.location_detail)
        childLocationTv = childView.findViewById(R.id.location_detail)
        val imgView = myLocationView.findViewById<ImageView>(R.id.head_img_id)
        val childImg = myLocationView.findViewById<ImageView>(R.id.head_img_id)
        imgView.loadHead(SerializableUtils.getSerializable<UserEntity>(Constants.PARENTS_USER_ENTITY).headUrl)
        childImg.loadHead(SerializableUtils.getSerializable<MyChildrenBean>(Constants.CHILD_USER_ENTITY).headUrl)
        val location = LocationClient(this)
        locationListener = MyLocationListener()
        location.registerLocationListener(locationListener)
        val option = LocationClientOption()
        option.setCoorType("bd09ll")
        option.setScanSpan(0)
        option.isOpenGps = true
        option.setIsNeedAddress(true)
        option.setIgnoreKillProcess(false)
        option.setIsNeedLocationPoiList(true)
        location.locOption = option
        location.start()
        /*------------------------------------------*/

        geo = GeoCoder.newInstance()
        geo.setOnGetGeoCodeResultListener(object : OnGetGeoCoderResultListener {
            override fun onGetGeoCodeResult(p0: GeoCodeResult?) {

            }

            override fun onGetReverseGeoCodeResult(p0: ReverseGeoCodeResult?) {
                childLocationTv.text = p0?.address
            }

        })
        entityName = SerializableUtils.getSerializable<MyChildrenBean>(Constants.CHILD_USER_ENTITY).uid
        val historyTrackRequest = HistoryTrackRequest(tag, serviceId, entityName)
        historyTrackRequest.startTime = System.currentTimeMillis() / 1000 - 60 * 60
        historyTrackRequest.endTime = System.currentTimeMillis() / 1000
        val client = LBSTraceClient(applicationContext)
        client.queryHistoryTrack(historyTrackRequest, object : OnTrackListener() {
            override fun onHistoryTrackCallback(p0: HistoryTrackResponse?) {

                childLatitude = p0?.endPoint?.location?.getLatitude() ?: 0.0
                childLongitude = p0?.endPoint?.location?.getLongitude() ?: 0.0
                geo.reverseGeoCode(ReverseGeoCodeOption().location(LatLng(childLatitude, childLongitude)))
            }


        })


    }

    inner class MyLocationListener : BDAbstractLocationListener() {

        override fun onReceiveLocation(p0: BDLocation?) {
            map.clear()
            overLayList.clear()
            val latitude = p0!!.latitude
            val longitude = p0.longitude
            val point = LatLng(latitude, longitude)
            val childPoint = LatLng(childLatitude, childLongitude)
            latLngBuild.include(point)
            latLngBuild.include(childPoint)
            val latLngBounds = latLngBuild.build()
            val mMapStatusUpdate = MapStatusUpdateFactory.newLatLngBounds(latLngBounds)
            //改变地图状态
            map.setMapStatus(mMapStatusUpdate)
            locationTv.text = p0.address.address
            val myOption = MarkerOptions().perspective(true).position(point).icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromView(myLocationView)))
            val childOption = MarkerOptions().perspective(true).position(childPoint).icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromView(childView)))
            overLayList.add(myOption)
            overLayList.add(childOption)
            map.addOverlays(overLayList)
        }
    }

    fun getBitmapFromView(view: View): Bitmap {
        view.destroyDrawingCache()
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.isDrawingCacheEnabled = true
        return view.getDrawingCache(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapview.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        mapview.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapview.onPause()
    }
}