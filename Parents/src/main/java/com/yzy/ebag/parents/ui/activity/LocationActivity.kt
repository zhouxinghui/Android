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
import com.yzy.ebag.parents.R
import ebag.core.base.BaseActivity
import ebag.core.util.SerializableUtils
import ebag.core.util.loadHead
import ebag.mobile.base.Constants
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
    private lateinit var locationTv:TextView
    private val overLayList = ArrayList<OverlayOptions>()

    override fun getLayoutId(): Int = R.layout.activity_location

    override fun initViews() {
        map = mapview.map
        map.isMyLocationEnabled = true
        map.mapType = BaiduMap.MAP_TYPE_NORMAL
        myLocationView = View.inflate(this,R.layout.item_my_location,null)
        locationTv = myLocationView.findViewById(R.id.location_detail)
        val imgView = myLocationView.findViewById<ImageView>(R.id.head_img_id)
        imgView.loadHead(SerializableUtils.getSerializable<UserEntity>(Constants.PARENTS_USER_ENTITY).headUrl)
        val location = LocationClient(this)
        locationListener = MyLocationListener()
        location.registerLocationListener(locationListener)
        val option = LocationClientOption()
        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        option.openGps = true
        option.setCoorType("bd09ll")
        option.setIsNeedAddress(true)
        option.setIgnoreKillProcess(false)
        option.setIsNeedLocationPoiList(true)
        location.locOption = option
        location.start()

    }


    inner class MyLocationListener : BDAbstractLocationListener() {

        override fun onReceiveLocation(p0: BDLocation?) {

            val latitude = p0!!.latitude
            val longitude = p0.longitude
            val point  = LatLng(latitude,longitude)
            val latLngBuild = LatLngBounds.Builder()
            latLngBuild.include(point)
            val latLngBounds = latLngBuild.build()
            val mMapStatusUpdate = MapStatusUpdateFactory.newLatLngBounds(latLngBounds)
            //改变地图状态
            map.setMapStatus(mMapStatusUpdate)
            locationTv.text = p0.address.address
            val myOption =   MarkerOptions().perspective(true).position(point).icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromView(myLocationView,false)))
            map.addOverlay(myOption)
            map.setMapStatus(MapStatusUpdateFactory.zoomTo(20f))



        }

    }


    fun getBitmapFromView(view:View,isChild:Boolean): Bitmap {
        view.destroyDrawingCache()
        view.measure(View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED),View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED))
        view.layout(0,0,view.measuredWidth,view.measuredHeight)
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