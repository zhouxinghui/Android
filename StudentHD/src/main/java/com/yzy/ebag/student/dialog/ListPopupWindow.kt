package com.yzy.ebag.student.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R

/**
 * Created by unicho on 2018/3/9.
 */
abstract class ListPopupWindow<SourceBean>(
        val mContext: Context,
        private val dropView: View,
        val width: Int,
        val height: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
        recyclerLayoutRes: Int = R.layout.layout_popup_list,
        val itemLayoutRes: Int =R.layout.item_study_task_spinner_item) {

    private val adapter: SpinnerAdapter
    private val popup = PopupWindow(mContext)

    init {
        val contentView = LayoutInflater.from(mContext).inflate(recyclerLayoutRes, null)
        popup.setOnDismissListener {
            popupVisible(false)
        }
        popup.contentView = contentView
        popup.width = width
        popup.height = height

        // 设置SelectPicPopupWindow弹出窗体可点击
        popup.isFocusable = true
        popup.isOutsideTouchable = true
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        popup.setBackgroundDrawable(ColorDrawable(0))
        popup.animationStyle = R.style.AnimationPreview
        val recyclerView: RecyclerView = contentView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerView.addItemDecoration(
                ebag.core.xRecyclerView.manager.DividerItemDecoration(
                        LinearLayoutManager.VERTICAL,
                        mContext.resources.getDimensionPixelSize(R.dimen.x2),
                        Color.parseColor("#F4F4F4")
                )
        )
        adapter = SpinnerAdapter()
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener { _, view, position ->
            itemClick(adapter.getItem(position))
            popup.dismiss()
        }
    }

    public fun setData(list :List<SourceBean>){
        adapter.setNewData(list)
    }

    public fun show(){
        popup.showAsDropDown(dropView)
        popupVisible(true)
    }


    abstract fun itemClick(item: SourceBean?)

    abstract fun fillData(helper: BaseViewHolder, item: SourceBean?)

    abstract fun popupVisible(visible: Boolean)

    inner class SpinnerAdapter: BaseQuickAdapter<SourceBean, BaseViewHolder>(itemLayoutRes){
        override fun convert(helper: BaseViewHolder, item: SourceBean?) {
            fillData(helper, item)
        }
    }

}