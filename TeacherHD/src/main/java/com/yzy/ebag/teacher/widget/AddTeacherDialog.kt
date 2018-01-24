package com.yzy.ebag.teacher.widget

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.BaseSubjectBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.base.BaseDialog
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.DensityUtil
import ebag.core.util.StringUtils
import ebag.core.util.T
import kotlinx.android.synthetic.main.dialog_add_teacher.*

/**
 * Created by YZY on 2018/1/11.
 */
class AddTeacherDialog(context: Context): BaseDialog(context) {
    override fun getLayoutRes(): Int {
        return R.layout.dialog_add_teacher
    }

    private val subjectRequest = object : RequestCallBack<List<BaseSubjectBean>>(){
        override fun onSuccess(entity: List<BaseSubjectBean>?) {
            if (entity == null || entity.isEmpty()) {
                T.show(context, "该年级没有科目数据")
            }else{
                subjectAdapter.setNewData(entity)
            }
        }

        override fun onError(exception: Throwable) {
            exception.handleThrowable(context)
        }
    }
    private val addRequest = object : RequestCallBack<String>(){
        override fun onStart() {
            T.showLong(context, "正在添加...")
        }

        override fun onSuccess(entity: String?) {
            onAddSuccess?.invoke()
            dismiss()
        }

        override fun onError(exception: Throwable) {
            exception.handleThrowable(context)
        }
    }
    var onAddSuccess: (() -> Unit)? = null
    private var subjectCode = ""
    private var classId = ""
    private var subjectAdapter: SubjectAdapter

    init {
        subjectAdapter = SubjectAdapter()
        recyclerView.adapter = subjectAdapter
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        confirmBtn.setOnClickListener {
            if (isReadyToAdd()){
                TeacherApi.addTeacher(classId, bagEdit.text.toString(), subjectCode, addRequest)
            }
        }
    }

    override fun setWidth(): Int {
        return DensityUtil.dip2px(context, context.resources.getDimensionPixelSize(R.dimen.x350).toFloat())
    }

    override fun setHeight(): Int {
        return DensityUtil.dip2px(context, context.resources.getDimensionPixelSize(R.dimen.y350).toFloat())
    }
    fun show(classId: String, gradeCode: String){
        this.classId = classId
        TeacherApi.getSubject(gradeCode, subjectRequest)
        super.show()
    }

    private fun isReadyToAdd(): Boolean{
        return when {
            StringUtils.isEmpty(bagEdit.text.toString()) -> {
                T.show(context, "请填写老师书包号")
                false
            }
            StringUtils.isEmpty(subjectCode) -> {
                T.show(context, "请选择科目")
                false
            }
            else -> true
        }
    }

    inner class SubjectAdapter: BaseQuickAdapter<BaseSubjectBean, BaseViewHolder>(R.layout.item_select_school){
        private var selectPosition = -1
            set(value) {
                field = value
                notifyDataSetChanged()
            }
        override fun convert(helper: BaseViewHolder, item: BaseSubjectBean) {
            val position = helper.adapterPosition
            helper.itemView.setOnClickListener {
                selectPosition = position
                subjectCode = item.keyCode
            }
            val checkBox = helper.getView<CheckBox>(R.id.checkbox)
            checkBox.text = item.keyValue
            checkBox.isChecked = selectPosition != -1 && selectPosition == position
        }
    }
}