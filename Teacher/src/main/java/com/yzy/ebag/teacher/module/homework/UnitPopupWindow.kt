package com.yzy.ebag.teacher.module.homework

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.WindowManager
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import ebag.core.base.BasePopupWindow
import ebag.core.widget.empty.StateView
import ebag.mobile.bean.UnitBean

/**
 * Created by YZY on 2018/4/18.
 */
class UnitPopupWindow(private val mContext: Context): BasePopupWindow(mContext) {
    override fun getLayoutRes(): Int = R.layout.popup_unit

    override fun setWidth(): Int {
        return WindowManager.LayoutParams.MATCH_PARENT
    }

    override fun setHeight(): Int {
        return contentView.resources.getDimensionPixelSize(R.dimen.y550)
    }
    private val unitAdapter = UnitAdapter()
    private val subAdapter = SubAdapter()
    var onConfirmClick: ((name: String?, unitBean: UnitBean.UnitSubBean?) -> Unit)? = null
    private val stateView = contentView.findViewById<StateView>(R.id.stateView)
    private val totalTv = contentView.findViewById<TextView>(R.id.totalTv)
    init {
        val unitRecycler = contentView.findViewById<RecyclerView>(R.id.unitRecycler)
        val subRecyclerView = contentView.findViewById<RecyclerView>(R.id.subRecycler)
        stateView.setBackgroundRes(R.color.white)
        unitRecycler.layoutManager = LinearLayoutManager(mContext)
        subRecyclerView.layoutManager = LinearLayoutManager(mContext)
        unitRecycler.adapter = unitAdapter
        subRecyclerView.adapter = subAdapter

        unitAdapter.setOnItemClickListener { adapter, view, position ->
            unitAdapter.selectPosition = position
        }
        subAdapter.setOnItemClickListener { adapter, view, position ->
            subAdapter.selectPosition = position
            onConfirmClick?.invoke("${unitAdapter.getItem(unitAdapter.selectPosition)?.name} - ${subAdapter.getItem(position)?.name}",subAdapter.getItem(position))
            dismiss()
        }
        totalTv.setOnClickListener {
            totalTv.isSelected = true
            unitAdapter.selectPosition = -1
            subAdapter.selectPosition = -1
            onConfirmClick?.invoke("全部", null)
            dismiss()
        }
    }

    fun setData(unitList: ArrayList<UnitBean>?){
        if (unitList == null || unitList.isEmpty()){
            stateView.showEmpty()
        }else{
            stateView.showContent()
            totalTv.isSelected = false
            unitAdapter.setNewData(unitList)
            unitAdapter.selectPosition = 0
            subAdapter.selectPosition = -1
        }
    }

    private inner class UnitAdapter: BaseQuickAdapter<UnitBean, BaseViewHolder>(R.layout.item_assign_popup){
        var selectPosition = -1
        set(value) {
            field = value
            if (selectPosition != -1)
                subAdapter.setNewData(unitAdapter.getItem(selectPosition)?.resultBookUnitOrCatalogVos)
            notifyDataSetChanged()
        }
        override fun convert(helper: BaseViewHolder, item: UnitBean?) {
            helper.setText(R.id.tv, item?.name)
            helper.getView<TextView>(R.id.tv).isSelected = selectPosition == helper.adapterPosition
        }
    }

    private inner class SubAdapter: BaseQuickAdapter<UnitBean.UnitSubBean, BaseViewHolder>(R.layout.item_assign_popup){
        var selectPosition = -1
            set(value) {
                field = value
                notifyDataSetChanged()
            }
        override fun convert(helper: BaseViewHolder, item: UnitBean.UnitSubBean?) {
            helper.setText(R.id.tv, item?.name)
            helper.getView<TextView>(R.id.tv).isSelected = selectPosition == helper.adapterPosition
        }
    }
}