package com.yzy.ebag.student.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.LocationBean
import ebag.core.util.DateUtil
import java.util.*

/**
 * Created by fansan on 2018/3/27.
 */
class LocationAdapter(data:MutableList<LocationBean.UserPositioningVosBean>) : BaseQuickAdapter<LocationBean.UserPositioningVosBean, BaseViewHolder>(R.layout.item_location,data) {

    override fun convert(helper: BaseViewHolder, item: LocationBean.UserPositioningVosBean?) {
        helper.setText(R.id.location_time, DateUtil.getFormatTime(Date(item!!.reportDate)))
                .setText(R.id.location_detail, item.address).setText(R.id.location_remark, item.remark)

    }


}