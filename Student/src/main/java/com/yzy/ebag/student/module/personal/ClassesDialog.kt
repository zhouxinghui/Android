package com.yzy.ebag.student.module.personal

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.ClassListInfoBean
import ebag.core.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_classes.*

/**
 * Created by YZY on 2018/5/14.
 */
class ClassesDialog(mContext: Context): BaseDialog(mContext) {
    override fun getLayoutRes(): Int = R.layout.dialog_classes
    var listener: ((ClassListInfoBean?) -> Unit)? = null
    private val adapter = Adapter()
    private var list: List<ClassListInfoBean>? = null
    private var classId = ""
    init {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(mContext)

        adapter.setOnItemClickListener { _, _, position ->
            this.classId = adapter.getItem(position)?.classId ?: ""
            adapter.notifyDataSetChanged()
            listener?.invoke(adapter.getItem(position))
            dismiss()
        }
    }

    fun show(list: List<ClassListInfoBean>?, classId: String) {
        if(list != null && list != this.list){
            this.list = list
            if(classId.isBlank() && list.isNotEmpty())
                this.classId = list[0].classId
            else
                this.classId = classId
            adapter.setNewData(list)
        }
        super.show()
    }

    inner class Adapter: BaseQuickAdapter<ClassListInfoBean, BaseViewHolder>(R.layout.item_dialog_classes){

        override fun convert(helper: BaseViewHolder, item: ClassListInfoBean?) {
            helper.setText(R.id.text,item?.className)
            helper.getView<View>(R.id.text).isSelected = item?.classId == classId
        }
    }
}