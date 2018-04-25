package com.yzy.ebag.parents.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.parents.R

class RepresentAdapter(data: List<String>) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_represent, data) {

    override fun convert(helper: BaseViewHolder?, item: String?) {

    }

}