package com.yzy.ebag.teacher.ui.activity

import android.support.v7.widget.GridLayoutManager
import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseActivity
import ebag.hd.widget.CityPickerDialog
import kotlinx.android.synthetic.main.activity_select_school.*

class SelectSchoolActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_select_school
    }

    private val cityPickerDialog by lazy { CityPickerDialog(this) }
    override fun initViews() {
        val adapter = MyAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 5)
        val list = ArrayList<String>()
        for (i in 0..19){
            list.add("人民小学")
        }
        adapter.setNewData(list)

        cityBtn.setOnClickListener {
            cityPickerDialog.show()
        }
    }

    inner class MyAdapter: BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_select_school){
        private var selectPosition = -1
        set(value) {
            field = value
            notifyDataSetChanged()
        }
        override fun convert(helper: BaseViewHolder, item: String?) {
            val position = helper.adapterPosition
            helper.itemView.setOnClickListener {
                selectPosition = position
                campusName.text = item
            }
            val checkBox = helper.getView<CheckBox>(R.id.checkbox)
            checkBox.text = item
            checkBox.isChecked = selectPosition != -1 && selectPosition == position
        }

    }
}
