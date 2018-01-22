package com.yzy.ebag.student.base

import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.yzy.ebag.student.R

/**
 * @author caoyu
 * @date 2018/1/20
 * @description
 */
class UnitAdapter: BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> (null){

    companion object {
        const val LEVEL_ONE = 1
        const val LEVEL_TWO = 2
    }

    init {
        addItemType(LEVEL_ONE, R.layout.unit_group_item)
        addItemType(LEVEL_TWO, R.layout.unit_sub_item)
    }
    var selectSub: UnitBean.ChapterBean? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun convert(helper: BaseViewHolder?, item: MultiItemEntity) {
        val tv = helper!!.getView<TextView>(ebag.hd.R.id.text)
        when(helper.itemViewType){
            LEVEL_ONE ->{
                item as UnitBean
                tv.text = item.name
                tv.isSelected = item.isExpanded
            }
            LEVEL_TWO ->{
                item as UnitBean.ChapterBean
                tv.text = item.name
                tv.isSelected = selectSub == item
            }
        }
    }
}