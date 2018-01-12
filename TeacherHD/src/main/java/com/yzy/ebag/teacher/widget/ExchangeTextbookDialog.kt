package com.yzy.ebag.teacher.widget

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import com.yzy.ebag.teacher.R
import ebag.core.util.DensityUtil
import ebag.core.util.T
import ebag.core.xRecyclerView.adapter.RecyclerAdapter
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder
import kotlinx.android.synthetic.main.dialog_textbook_version.*

/**
 * Created by YZY on 2018/1/10.
 */
class ExchangeTextbookDialog(context: Context): Dialog(context) {

    init {
        val contentView = LayoutInflater.from(context).inflate(R.layout.dialog_textbook_version, null)
        setContentView(contentView)
        window.setBackgroundDrawable(BitmapDrawable())
        val params = window.attributes
        params.width = DensityUtil.dip2px(context, context.resources.getDimensionPixelSize(R.dimen.x400).toFloat())
        params.height = DensityUtil.dip2px(context, context.resources.getDimensionPixelSize(R.dimen.y400).toFloat())
        window.attributes = params

        confirmBtn.setOnClickListener {
            T.show(context, "确定")
        }
        val gradeAdapter = GradeAdapter()
        gradeRecycler.layoutManager = LinearLayoutManager(context)
        gradeRecycler.adapter = gradeAdapter
        gradeAdapter.setOnItemClickListener { holder, view, position ->
            gradeAdapter.selectItem(position)
        }

        val versionAdapter = VersionAdapter()
        versionRecycler.layoutManager = LinearLayoutManager(context)
        versionRecycler.adapter = versionAdapter
        versionAdapter.setOnItemClickListener { holder, view, position ->
            versionAdapter.selectItem(position)
        }
        val list = ArrayList<String>()
        for (i in 0..9){
            list.add("一年级")
        }
        gradeAdapter.datas = list
        versionAdapter.datas = list
    }

    inner class GradeAdapter: RecyclerAdapter<String>(R.layout.item_textbook_grade){
        private var selectPosition = -1
        fun selectItem(selectPosition: Int){
            this.selectPosition = selectPosition
            notifyDataSetChanged()
        }
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: String?) {
            val gradeTv = setter.getTextView(R.id.gradeTv)
            gradeTv.text = entity
            gradeTv.isSelected = selectPosition != -1 && selectPosition == position
        }
    }
    inner class VersionAdapter : RecyclerAdapter<String>(R.layout.item_textbook_version){
        private var selectPosition = -1
        fun selectItem(selectPosition: Int){
            this.selectPosition = selectPosition
            notifyDataSetChanged()
        }
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: String?) {
            val versionTv = setter.getTextView(R.id.versionTv)
            versionTv.text = entity
            versionTv.isSelected = selectPosition != -1 && selectPosition == position
        }

    }
}