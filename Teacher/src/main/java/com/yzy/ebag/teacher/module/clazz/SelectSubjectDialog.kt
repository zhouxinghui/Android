package com.yzy.ebag.teacher.module.clazz

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.BaseSubjectBean
import ebag.core.base.BaseDialog
import ebag.core.util.T
import kotlinx.android.synthetic.main.dialog_select_subject.*

/**
 * Created by YZY on 2018/1/11.
 */
class SelectSubjectDialog(context: Context): BaseDialog(context) {
    override fun getLayoutRes(): Int {
        return R.layout.dialog_select_subject
    }

    var onConfirmClick: ((subjectList: ArrayList<BaseSubjectBean>) -> Unit)? = null
    private var subjectAdapter: SubjectAdapter
    private val subjectList = ArrayList<BaseSubjectBean>()
    init {
        subjectAdapter = SubjectAdapter()
        recyclerView.adapter = subjectAdapter
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        confirmBtn.setOnClickListener {
            if (isReadyToAdd()) {
                onConfirmClick?.invoke(subjectList)
                dismiss()
            }
        }
    }

    override fun setWidth(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.x250)
    }

    override fun setHeight(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.y500)
    }
    fun show(subjectList: ArrayList<BaseSubjectBean>){
        subjectAdapter.data.clear()
        this.subjectList.clear()
        subjectAdapter.addData(subjectList)
        super.show()
    }

    private fun isReadyToAdd(): Boolean{
        return if (subjectList.isEmpty()){
            T.show(context, "请选择科目")
            false
        }else {
            true
        }
    }

    inner class SubjectAdapter: BaseQuickAdapter<BaseSubjectBean, BaseViewHolder>(R.layout.item_select_school){
        override fun convert(helper: BaseViewHolder, item: BaseSubjectBean) {
            helper.itemView.setOnClickListener {
                if (subjectList.contains(item)){
                    subjectList.remove(item)
                }else{
                    subjectList.add(item)
                }
                notifyDataSetChanged()
            }
            val checkBox = helper.getView<CheckBox>(R.id.checkbox)
            checkBox.text = item.keyValue
            checkBox.isChecked = subjectList.contains(item)
        }
    }
}