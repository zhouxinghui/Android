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
import ebag.core.util.LoadingDialogUtil
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
            dismiss()
            LoadingDialogUtil.showLoading(context, "正在添加...")
        }

        override fun onSuccess(entity: String?) {
            onAddSuccess?.invoke()
            LoadingDialogUtil.closeLoadingDialog()
            T.show(context, "添加成功")
        }

        override fun onError(exception: Throwable) {
            LoadingDialogUtil.closeLoadingDialog()
            exception.handleThrowable(context)
        }
    }
    var onAddSuccess: (() -> Unit)? = null
    private var classId = ""
    private var subjectAdapter: SubjectAdapter
    private val subCodeList = ArrayList<String>()
    init {
        subjectAdapter = SubjectAdapter()
        recyclerView.adapter = subjectAdapter
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        confirmBtn.setOnClickListener {
            if (isReadyToAdd()){
                TeacherApi.addTeacher(classId, bagEdit.text.toString(), subCodeList, addRequest)
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
        subjectAdapter.data.clear()
        subjectAdapter.notifyDataSetChanged()
        TeacherApi.getSubject(gradeCode, subjectRequest)
        super.show()
    }

    private fun isReadyToAdd(): Boolean{
        return when {
            StringUtils.isEmpty(bagEdit.text.toString()) -> {
                T.show(context, "请填写老师书包号")
                false
            }
            subCodeList.isEmpty() -> {
                T.show(context, "请选择科目")
                false
            }
            else -> true
        }
    }

    inner class SubjectAdapter: BaseQuickAdapter<BaseSubjectBean, BaseViewHolder>(R.layout.item_select_school){
        override fun convert(helper: BaseViewHolder, item: BaseSubjectBean) {
            helper.itemView.setOnClickListener {
                if (subCodeList.contains(item.keyCode)){
                    subCodeList.remove(item.keyCode)
                }else{
                    subCodeList.add(item.keyCode)
                }
                notifyDataSetChanged()
            }
            val checkBox = helper.getView<CheckBox>(R.id.checkbox)
            checkBox.text = item.keyValue
            checkBox.isChecked = subCodeList.contains(item.keyCode)
        }
    }
}