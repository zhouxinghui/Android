package com.yzy.ebag.parents.ui.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.bean.StudentSubjectBean
import com.yzy.ebag.parents.mvp.model.ChooseUnitModel
import ebag.core.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_selectunit.*

/**
 *  Created by fansan on 2018/5/16 10:07
 */

class ChooseUnitDialog(c: Context) : BaseDialog(c) {

    private val firstList: ArrayList<ChooseUnitModel> = arrayListOf()
    private val secondList: ArrayList<ChooseUnitModel> = arrayListOf()
    private val mAdapter: Adapter = Adapter()
    private val beanList: ArrayList<StudentSubjectBean.SubjectListBean> = arrayListOf()

    override fun setWidth(): Int = context.resources.getDimensionPixelOffset(R.dimen.x200)

    override fun setHeight(): Int = context.resources.getDimensionPixelOffset(R.dimen.y600)

    override fun getLayoutRes(): Int = R.layout.dialog_selectunit

    var onSelectedListener: ((bookId: String,bookName:String) -> Unit)? = null

    private fun switchVersion(index: Int) {
        if (index == 1) {
            mAdapter.setNewData(firstList)
        } else {
            mAdapter.setNewData(secondList)
        }
    }


    init {
        recyclerview.layoutManager = LinearLayoutManager(c)
        recyclerview.adapter = mAdapter

        first.performClick()
        first.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                second.isChecked = false
                second.isClickable = true
                first.isClickable = false
                switchVersion(1)
            }
        }


        second.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                first.isChecked = false
                first.isClickable = true
                second.isClickable = false
                switchVersion(2)
            }
        }

        mAdapter.setEmptyView(R.layout.base_empty,recyclerview)

        mAdapter.setOnItemClickListener { adapter, _, position ->
            onSelectedListener?.invoke((adapter.getItem(position) as ChooseUnitModel).code,((adapter.getItem(position) as ChooseUnitModel).name))
        }
    }

    fun updateList(list:List<StudentSubjectBean.SubjectListBean>){
        beanList.clear()
        beanList.addAll(list)
        setList()
    }

    private fun setList(){
        firstList.clear()
        secondList.clear()
        for (i in beanList.indices) {

            beanList[i].bookversionList.filter {
                it.semeter == "1"
            }.forEach {
                firstList.add(ChooseUnitModel(beanList[i].versionName, it.bookVersionId))
            }

            beanList[i].bookversionList.filter {
                it.semeter == "2"
            }.forEach {
                secondList.add(ChooseUnitModel(beanList[i].versionName, it.bookVersionId))
            }
        }

        switchVersion(1)
    }

    override fun show() {

        first.isChecked = true
        super.show()
    }


    inner class Adapter : BaseQuickAdapter<ChooseUnitModel, BaseViewHolder>(R.layout.item_chooseunit) {

        override fun convert(helper: BaseViewHolder, item: ChooseUnitModel?) {
            helper.setText(R.id.textview, item?.name)
        }

    }
}