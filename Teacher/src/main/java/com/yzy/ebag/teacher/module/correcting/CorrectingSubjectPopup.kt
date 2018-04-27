package com.yzy.ebag.teacher.module.correcting

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.CorrectingBean
import ebag.core.base.BasePopupWindow

/**
 * Created by YZY on 2018/4/27.
 */
class CorrectingSubjectPopup(private val mContext: Context): BasePopupWindow(mContext) {
    override fun getLayoutRes(): Int = R.layout.popup_correcting_subject

    override fun setWidth(): Int {
        return WindowManager.LayoutParams.MATCH_PARENT
    }

    override fun setHeight(): Int {
        return contentView.resources.getDimensionPixelSize(R.dimen.y400)
    }
    private var subjectAdapter: SubjectAdapter
    private var classAdapter: ClassAdapter
    private var classId = ""
    private var className = ""
    private var classList: List<CorrectingBean>? = null
    var onSubjectSelect : ((classId: String, className: String, subCode: String, subName: String) -> Unit)? = null
    init {
        val classRecycler = contentView.findViewById<RecyclerView>(R.id.classRecycler)
        val subjectRecycler = contentView.findViewById<RecyclerView>(R.id.subjectRecycler)
        classRecycler.layoutManager = LinearLayoutManager(mContext)
        subjectRecycler.layoutManager = LinearLayoutManager(mContext)
        classAdapter = ClassAdapter()
        subjectAdapter = SubjectAdapter()
        classRecycler.adapter = classAdapter
        subjectRecycler.adapter = subjectAdapter

        classAdapter.setOnItemClickListener { _, _, position ->
            classAdapter.selectPosition = position
        }
        subjectAdapter.setOnItemClickListener { _, _, position ->
            subjectAdapter.selectPosition = position
            val item = subjectAdapter.getItem(position)
            onSubjectSelect?.invoke(classId, className, item?.subCode ?: "", item?.subject ?: "")
            dismiss()
        }
    }

    fun show(classList: List<CorrectingBean>, view: View){
        if (this.classList == null){
            this.classList = classList
            classAdapter.setNewData(classList)
            classAdapter.selectPosition = 0
        }
        showAsDropDown(view)
    }

    private inner class ClassAdapter: BaseQuickAdapter<CorrectingBean, BaseViewHolder>(R.layout.item_assign_popup){
        var selectPosition = -1
        set(value) {
            field = value
            val item = getItem(selectPosition)
            classId = item?.classId ?: ""
            className = item?.className ?: ""
            subjectAdapter.setNewData(getItem(selectPosition)?.subjectVos)
            notifyDataSetChanged()
        }
        override fun convert(helper: BaseViewHolder, item: CorrectingBean?) {
            val textView = helper.getView<TextView>(R.id.tv)
            textView.text = item?.className
            textView.isSelected = selectPosition != -1 && selectPosition == helper.adapterPosition
        }
    }

    private inner class SubjectAdapter: BaseQuickAdapter<CorrectingBean.SubjectVosBean, BaseViewHolder>(R.layout.item_assign_popup){
        var selectPosition = -1
            set(value) {
                field = value
                notifyDataSetChanged()
            }
        override fun convert(helper: BaseViewHolder, item: CorrectingBean.SubjectVosBean?) {
            val textView = helper.getView<TextView>(R.id.tv)
            textView.text = item?.subject
            textView.isSelected = selectPosition != -1 && selectPosition == helper.adapterPosition
        }
    }
}