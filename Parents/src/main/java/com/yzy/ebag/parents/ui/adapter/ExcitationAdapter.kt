package com.yzy.ebag.parents.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.bean.ExcitationWorkBean

class ExcitationAdapter(data: List<ExcitationWorkBean>) : BaseQuickAdapter<ExcitationWorkBean, BaseViewHolder>(R.layout.item_excication, data) {

    override fun convert(helper: BaseViewHolder, item: ExcitationWorkBean?) {
        helper.setText(R.id.item_excitation_title, item?.title)
                .setText(R.id.item_excitation_date, item?.createDate)
                .setText(R.id.item_excitation_content, item?.content)

        if (item!!.yunCount < 1){
            helper.setGone(R.id.item_excitation_reward,false)
        }else{
            helper.setGone(R.id.item_excitation_reward,true)
            helper.setText(R.id.item_excitation_reward,"${item.yunCount}云币")
        }
        if (item.completed == "N") {
            helper.setText(R.id.item_excitation_progressbtn, "完成")
            helper.setBackgroundRes(R.id.item_excitation_progressbtn, R.drawable.excitation_task_bcg)
            helper.addOnClickListener(R.id.item_excitation_progressbtn)
        } else {
            helper.setText(R.id.item_excitation_progressbtn, "已完成")
            helper.setBackgroundRes(R.id.item_excitation_progressbtn, R.drawable.excitation_task_complete)
        }

    }

}