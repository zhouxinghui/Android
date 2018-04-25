package com.yzy.ebag.parents.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.mvp.model.PersonalItemModel

class PersonalAdapter(datas: List<PersonalItemModel>) : BaseQuickAdapter<PersonalItemModel, BaseViewHolder>(R.layout.item_personal, datas) {

    override fun convert(helper: BaseViewHolder, item: PersonalItemModel?) {
        helper.setImageResource(R.id.icon, item!!.id).setText(R.id.label, item?.label)
    }

}