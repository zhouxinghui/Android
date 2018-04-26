package com.yzy.ebag.parents.ui.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.parents.R
import ebag.core.util.DateUtil
import ebag.core.util.loadHead
import ebag.mobile.bean.NoticeBean

class NoticeListAdapter(datas: List<NoticeBean>) : BaseQuickAdapter<NoticeBean, BaseViewHolder>(R.layout.item_notice, datas) {


    override fun convert(helper: BaseViewHolder, item: NoticeBean?) {
        helper.setText(R.id.name, item?.name)
                .setText(R.id.date, DateUtil.getDateTime(item!!.createDate))
                .setText(R.id.content, item?.content)
        helper.getView<ImageView>(R.id.head).loadHead(item?.headUrl)
    }

}