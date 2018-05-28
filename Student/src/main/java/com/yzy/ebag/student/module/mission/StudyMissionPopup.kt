package com.yzy.ebag.student.module.mission

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.SubjectBean
import ebag.core.base.BasePopupWindow

/**
 * Created by YZY on 2018/5/28.
 */
class StudyMissionPopup(mContext: Context): BasePopupWindow(mContext) {
    override fun getLayoutRes(): Int = R.layout.recycler_view_layout

    var onItemClick: ((subCode: String, subName: String) -> Unit)? = null
    private val adapter = MyAdapter()
    init {
        val recyclerView = contentView.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener { _, view, position ->
            onItemClick?.invoke(adapter.data[position].subCode, adapter.data[position].subject)
            dismiss()
        }
    }

    fun setData(list: List<SubjectBean>){
        adapter.setNewData(list)
    }

    private inner class MyAdapter: BaseQuickAdapter<SubjectBean, BaseViewHolder>(android.R.layout.simple_list_item_1){
        override fun convert(helper: BaseViewHolder, item: SubjectBean?) {
            helper.setText(android.R.id.text1, item?.subject)
        }
    }
}