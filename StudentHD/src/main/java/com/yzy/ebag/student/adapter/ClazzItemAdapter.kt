package com.yzy.ebag.student.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import ebag.hd.bean.BaseClassesBean

/**
 * Created by fansan on 2018/4/2.
 */
class ClazzItemAdapter(data: List<BaseClassesBean>) : BaseQuickAdapter<BaseClassesBean, BaseViewHolder>(R.layout.item_clazztitle, data) {


    override fun convert(helper: BaseViewHolder, item: BaseClassesBean?) {
        helper.setChecked(R.id.clazz_title, item!!.checked)
        helper.setText(R.id.clazz_title, item.className)
    }

}