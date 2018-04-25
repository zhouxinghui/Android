package com.yzy.ebag.parents.ui.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.bean.MyChildrenBean
import ebag.core.util.loadHead

class CreateTaskAdapter(datas: List<MyChildrenBean>) : BaseQuickAdapter<MyChildrenBean, BaseViewHolder>(R.layout.item_createtask_child, datas) {


    override fun convert(helper: BaseViewHolder, item: MyChildrenBean?) {
        helper.getView<ImageView>(R.id.child_head).loadHead(item?.headUrl)
        helper.setText(R.id.child_name, item?.name)
        helper.setGone(R.id.isselected, item!!.isSelected)
    }

}