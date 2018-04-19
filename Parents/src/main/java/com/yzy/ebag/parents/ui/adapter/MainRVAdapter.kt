package com.yzy.ebag.parents.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.model.MainRVModel

class MainRVAdapter(list: List<MainRVModel>) : BaseQuickAdapter<MainRVModel, BaseViewHolder>(R.layout.item_main, list) {

    override fun convert(helper: BaseViewHolder, item: MainRVModel?) {
        helper.setText(R.id.item_tv, item?.label)
                .setImageResource(R.id.item_iv, item!!.icon)
    }

}