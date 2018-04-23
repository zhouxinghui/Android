package com.yzy.ebag.parents.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.mvp.model.HomeworkAbsModel

class HomeworkAbstractAdapter(data: MutableList<HomeworkAbsModel>) : BaseQuickAdapter<HomeworkAbsModel, BaseViewHolder>(R.layout.item_homework_abstract,data) {

    override fun convert(helper: BaseViewHolder, item: HomeworkAbsModel?) {

        helper.setText(R.id.item_homework_label, item?.label)
                .setText(R.id.item_homework_content, item?.content)
    }

}