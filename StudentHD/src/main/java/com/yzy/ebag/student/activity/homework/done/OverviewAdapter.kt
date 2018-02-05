package com.yzy.ebag.student.activity.homework.done

import android.support.annotation.LayoutRes
import android.util.SparseIntArray
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter.TYPE_NOT_FOUND
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.yzy.ebag.student.R
import ebag.core.bean.QuestionBean
import ebag.core.bean.QuestionTypeUtils
import ebag.core.bean.TypeQuestionBean

/**
 * @author caoyu
 * @date 2018/2/3
 * @description
 */
class OverviewAdapter: BaseQuickAdapter<MultiItemEntity, BaseViewHolder>(null){

    init {
        addItemType(TypeQuestionBean.TYPE, R.layout.item_activity_overview_type)
        addItemType(TypeQuestionBean.ITEM, R.layout.item_activity_overview_detail)
    }

    private var layouts: SparseIntArray? = null

    override fun getDefItemViewType(position: Int): Int {
        if(mData[position]?.itemType == TypeQuestionBean.TYPE)
            return TypeQuestionBean.TYPE
        return TypeQuestionBean.ITEM
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return createBaseViewHolder(parent, getLayoutId(viewType))
    }

    private fun getLayoutId(viewType: Int): Int {
        return layouts!!.get(viewType, TYPE_NOT_FOUND)
    }

    private fun addItemType(type: Int, @LayoutRes layoutResId: Int) {
        if (layouts == null) {
            layouts = SparseIntArray()
        }
        layouts!!.put(type, layoutResId)
    }


    override fun convert(helper: BaseViewHolder, item: MultiItemEntity?) {
        val tv = helper.getView<TextView>(R.id.tv)
        when(helper.itemViewType){
            TypeQuestionBean.TYPE ->{
                item as TypeQuestionBean?
                tv.text = QuestionTypeUtils.getTitle(item?.type ?: "")
//                tv.isSelected = item.isExpanded
            }
            else ->{
                item as QuestionBean?
                tv.text = ((item?.position ?: 0) + 1).toString()
            }
        }
    }
}