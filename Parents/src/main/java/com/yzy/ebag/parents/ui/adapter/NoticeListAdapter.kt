package com.yzy.ebag.parents.ui.adapter

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.parents.R
import ebag.core.base.PhotoPreviewActivity
import ebag.core.util.DateUtil
import ebag.core.util.loadHead
import ebag.core.util.loadImage
import ebag.mobile.bean.NoticeBean

class NoticeListAdapter(private val c: Context, datas: List<NoticeBean>) : BaseQuickAdapter<NoticeBean, BaseViewHolder>(R.layout.item_notice, datas) {


    override fun convert(helper: BaseViewHolder, item: NoticeBean?) {
        helper.setText(R.id.name, item?.name)
                .setText(R.id.date, DateUtil.getDateTime(item!!.createDate))
                .setText(R.id.content, item?.content)
        helper.getView<ImageView>(R.id.head).loadHead(item?.headUrl)
        if (item.photos.isNotEmpty()) {
            val recyclerview = helper.getView<RecyclerView>(R.id.recyclerview)
            recyclerview.layoutManager = GridLayoutManager(c, 5)
            val adapter = Adapter(item.photos)
            recyclerview.adapter = adapter
        }

    }

    inner class Adapter(datas:List<String>):BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_notice_img,datas){

        override fun convert(helper: BaseViewHolder, item: String?) {

            helper.getView<ImageView>(R.id.imageView).loadImage(item)
            helper.itemView.setOnClickListener {
                PhotoPreviewActivity.jump(c, data, helper.adapterPosition)
            }
        }

    }

}