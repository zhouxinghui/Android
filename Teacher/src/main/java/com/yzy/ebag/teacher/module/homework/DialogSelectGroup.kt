package com.yzy.ebag.teacher.module.homework

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.GroupBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.base.BaseDialog
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.DensityUtil
import kotlinx.android.synthetic.main.dialog_select_group.*

/**
 * Created by YZY on 2018/2/3.
 */
class DialogSelectGroup(context: Context): BaseDialog(context) {
    override fun getLayoutRes(): Int {
        return R.layout.dialog_select_group
    }
    override fun setWidth(): Int {
        return DensityUtil.dip2px(context, context.resources.getDimensionPixelSize(R.dimen.x400).toFloat())
    }

    override fun setHeight(): Int {
        return DensityUtil.dip2px(context, context.resources.getDimensionPixelSize(R.dimen.y400).toFloat())
    }
    private val request = object : RequestCallBack<List<GroupBean>>(){
        override fun onStart() {
            stateView.showLoading()
        }
        override fun onSuccess(entity: List<GroupBean>?) {
            if (entity == null || entity.isEmpty()){
                stateView.showEmpty()
                return
            }
            adapter.setNewData(entity)
            stateView.showContent()
        }

        override fun onError(exception: Throwable) {
            exception.handleThrowable(context)
        }

    }
    private val selectList = ArrayList<GroupBean>()
    private val adapter = MyAdapter()
    private var classId = ""
    init {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        confirmBtn.setOnClickListener {
            onConfirmClick?.invoke(selectList)
            dismiss()
        }
        stateView.setOnRetryClickListener { TeacherApi.studyGroup(classId, request) }
    }

    var onConfirmClick : ((groupList: ArrayList<GroupBean>) -> Unit)? = null

    fun show(classId: String) {
        if (this.classId != classId) {
            this.classId = classId
            TeacherApi.studyGroup(classId, request)
        }
        super.show()
    }

    inner class MyAdapter: BaseQuickAdapter<GroupBean, BaseViewHolder>(R.layout.item_select_school){
        override fun convert(helper: BaseViewHolder, item: GroupBean) {
            val checkBox = helper.getView<CheckBox>(R.id.checkbox)
            checkBox.text = item.groupName
            helper.itemView.setOnClickListener {
                if (selectList.contains(item))
                    selectList.remove(item)
                else
                    selectList.add(item)
            }
            checkBox.isChecked = selectList.contains(item)
        }
    }
}