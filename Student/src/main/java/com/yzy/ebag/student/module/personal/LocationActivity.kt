package com.yzy.ebag.student.module.personal

import android.content.Intent
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.LocationBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.http.network.RequestCallBack
import ebag.core.util.DateUtil
import ebag.mobile.base.BaseListActivity
import java.util.*

/**
 * Created by YZY on 2018/5/15.
 */
class LocationActivity: BaseListActivity<LocationBean, LocationBean.UserPositioningVosBean>() {
    override fun loadConfig(intent: Intent) {
        titleBar.setTitle("定位")
        titleBar.setRightText("上报位置", {
            startActivityForResult(Intent(this, UploadLocationActivity::class.java), 999)
        })
    }

    override fun getPageSize(): Int = 10

    override fun requestData(page: Int, requestCallBack: RequestCallBack<LocationBean>) {
        StudentApi.searchLocation(page, requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: LocationBean?): List<LocationBean.UserPositioningVosBean>? {
        return parent?.userPositioningVos
    }

    override fun getAdapter(): BaseQuickAdapter<LocationBean.UserPositioningVosBean, BaseViewHolder> {
        return LocationAdapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<LocationBean.UserPositioningVosBean, BaseViewHolder>)
            : RecyclerView.LayoutManager? = null

    private inner class LocationAdapter:
            BaseQuickAdapter<LocationBean.UserPositioningVosBean, BaseViewHolder>(R.layout.item_location) {
        override fun convert(helper: BaseViewHolder, item: LocationBean.UserPositioningVosBean?) {
            helper.setText(R.id.location_time, DateUtil.getFormatTime(Date(item!!.reportDate)))
                    .setText(R.id.location_detail, item.address).setText(R.id.location_remark, item.remark)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 1000) {
            onRetryClick()
        }
    }
}