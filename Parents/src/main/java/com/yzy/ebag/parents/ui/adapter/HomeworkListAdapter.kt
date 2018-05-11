package com.yzy.ebag.parents.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.bean.OnePageInfoBean

class HomeworkListAdapter(data: List<OnePageInfoBean.HomeWorkInfoVosBean>) : BaseQuickAdapter<OnePageInfoBean.HomeWorkInfoVosBean, BaseViewHolder>(R.layout.item_homeworklist, data) {

    override fun convert(helper: BaseViewHolder, item: OnePageInfoBean.HomeWorkInfoVosBean?) {
        helper.setText(R.id.homework_item_date, item?.endTime!!.split(" ")[0])
        when (item.state) {
            "0" -> helper.setText(R.id.homework_item_state, "未完成")
            "1" -> helper.setText(R.id.homework_item_state, "未批改")
            "2" -> helper.setText(R.id.homework_item_state, "已批改")
            "3" -> helper.setText(R.id.homework_item_state, "老师评语完成")
            "4" -> helper.setText(R.id.homework_item_state, "家长签名和评语完成")
        }
        helper.setText(R.id.homework_item_endtime, item.endTime)
                .setText(R.id.homework_item_content, item.content)

        when (item.state) {
            "0" -> helper.setText(R.id.homework_item_btn, "查看作业")
            else -> {
                helper.setText(R.id.homework_item_btn, "查看作业报告")
            }

        }

        helper.addOnClickListener(R.id.homework_item_btn)
    }

}