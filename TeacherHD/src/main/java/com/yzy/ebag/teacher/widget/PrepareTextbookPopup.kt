package com.yzy.ebag.teacher.widget

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R

/**
 * Created by YZY on 2018/3/6.
 */
class PrepareTextbookPopup(context: Context): PopupWindow(context) {
    private var semesterCode = "1"
    private var semesterName = "上学期"
    private var textbookCode = ""
    private var textbookName = ""
    var onTextbookSelectListener : (() -> Unit)? = null
    init {
        contentView = LayoutInflater.from(context).inflate(R.layout.prepare_textbook_popup, null)
        width = context.resources.getDimensionPixelSize(R.dimen.x400)
        height = context.resources.getDimensionPixelSize(R.dimen.y300)
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable())

        val bookRecycler = contentView.findViewById<RecyclerView>(R.id.versionRecycler)
        bookRecycler.layoutManager = LinearLayoutManager(context)
        val adapter = MyAdapter()
        bookRecycler.adapter = adapter
        val list = ArrayList<String>()
        for (i in 0..9){
            list.add("")
        }
        adapter.setNewData(list)
        adapter.setOnItemClickListener { adapter, view, position ->
            adapter as MyAdapter
            adapter.currentVersionBean = adapter.data[position]
            onTextbookSelectListener?.invoke()
        }
    }

    inner class MyAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_prepare_subject){
        var currentVersionBean: String? = null
            set(value) {
                field = value
                notifyDataSetChanged()
            }
        override fun convert(helper: BaseViewHolder, item: String) {
            val textView = helper.getView<TextView>(R.id.text)
            textView.text = "人教版"
            textView.isSelected = currentVersionBean == item
            helper.getView<View>(R.id.dot).isSelected = currentVersionBean == item
        }
    }
}