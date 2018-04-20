package com.yzy.ebag.parents.ui.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.bean.MyChildrenBean
import ebag.core.util.loadHead

class ChooseChildrenAdapter(data: List<MyChildrenBean>, private val ysbCode: String) : BaseQuickAdapter<MyChildrenBean, BaseViewHolder>(R.layout.item_choosechildren, data) {


    override fun convert(helper: BaseViewHolder, item: MyChildrenBean?) {
        helper.setText(R.id.child_name, item?.name)
                .setText(R.id.ysb_code, item?.ysbCode)
                .setText(R.id.child_class, item?.className)

        helper.getView<ImageView>(R.id.child_head).loadHead(item?.headUrl, true)

        if (ysbCode == item?.ysbCode) {
            helper.setGone(R.id.child_select, true)
        }else{
            helper.setGone(R.id.child_select, false)
        }

    }

}