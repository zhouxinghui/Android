package com.yzy.ebag.parents.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.parents.R

class SettingAdapter(datas: List<String>) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_setting, datas) {

    override fun convert(helper: BaseViewHolder, item: String?) {

        helper.setText(R.id.label, item)
        if (helper.layoutPosition == 0) {
            helper.setText(R.id.mark, "已是最新版本")
        }
    }

}