package com.yzy.ebag.parents.ui.adapter

import android.content.Context
import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.mvp.model.PersonalItemModel

class PersonalAdapter(private val c: Context, datas: List<PersonalItemModel>) : BaseQuickAdapter<PersonalItemModel, BaseViewHolder>(R.layout.item_personal, datas) {

    override fun convert(helper: BaseViewHolder, item: PersonalItemModel?) {

        when {
            helper.layoutPosition == 0 -> helper.setBackgroundRes(R.id.personal_layout, R.drawable.clazz_item_bg)
            helper.layoutPosition == data.size - 1 -> helper.setBackgroundRes(R.id.personal_layout, R.drawable.clazz_item_bottom_bg)
            else -> helper.setBackgroundColor(R.id.personal_layout, Color.WHITE)
        }

        helper.setImageResource(R.id.icon, item!!.id).setText(R.id.label, item?.label)
    }

}