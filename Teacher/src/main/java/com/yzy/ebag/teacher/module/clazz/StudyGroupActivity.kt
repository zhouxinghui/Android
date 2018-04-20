package com.yzy.ebag.teacher.module.clazz

import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.GroupBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.http.network.RequestCallBack
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.T
import ebag.mobile.base.BaseListActivity

class StudyGroupActivity : BaseListActivity<List<GroupBean>, GroupBean>() {
    companion object {
        fun jump(context: Context, classId: String){
            context.startActivity(
                    Intent(context, StudyGroupActivity::class.java)
                            .putExtra("classId", classId)
            )
        }
    }
    private lateinit var classId: String
    private val adapter by lazy { MyAdapter() }
    private val groupDialog by lazy {
        val dialog = GroupManageDialog(this, classId)
        dialog.onGroupChangeListener = {
            onRetryClick()
        }
        dialog
    }
    private var currentGroupId = ""
    private val deleteDialog by lazy {
        val dialog = AlertDialog.Builder(this)
                .setMessage("确定删除小组？")
                .setNegativeButton("取消", { dialog, which ->
                    dialog.dismiss()
                })
                .setPositiveButton("删除", {dialog, which ->
                    dialog.dismiss()
                    TeacherApi.deleteGroup(classId, currentGroupId, deleteGroupRequest)
                })
                .create()
        dialog
    }
    private val deleteGroupRequest = object : RequestCallBack<String>(){
        override fun onStart() {
            LoadingDialogUtil.showLoading(this@StudyGroupActivity, "正在删除...")
        }
        override fun onSuccess(entity: String?) {
            LoadingDialogUtil.closeLoadingDialog()
            T.show(this@StudyGroupActivity, "删除成功")
            onRetryClick()
        }

        override fun onError(exception: Throwable) {
            LoadingDialogUtil.closeLoadingDialog()
            T.show(this@StudyGroupActivity, "请求失败")
        }

    }
    override fun loadConfig(intent: Intent) {
        loadMoreEnabled(false)
        refreshEnabled(false)
        titleBar.setTitle(R.string.study_group)
        titleBar.setRightText(resources.getString(R.string.create_group), {
            groupDialog.show()
        })
        classId = intent.getStringExtra("classId") ?: ""

        adapter.setOnItemChildClickListener { _, view, position ->
            when(view.id){
                R.id.manageGroup ->{
                    val item = adapter.getItem(position)
                    groupDialog.show(item?.clazzUserVos!!, item.groupName, item.groupId)
                }
                R.id.deleteTv ->{
                    currentGroupId = adapter.data[position].groupId
                    deleteDialog.show()
                }
            }
        }
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<GroupBean>>) {
        TeacherApi.studyGroup(classId, requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<GroupBean>?): List<GroupBean>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<GroupBean, BaseViewHolder> {
        return adapter
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<GroupBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return GridLayoutManager(this, 2)
    }

    inner class MyAdapter: BaseQuickAdapter<GroupBean, BaseViewHolder>(R.layout.item_study_group){
        override fun convert(helper: BaseViewHolder, item: GroupBean) {
            helper.setText(R.id.groupNameTv, item.groupName)
                    .setText(R.id.studentCount, resources.getString(R.string.student_count, item.clazzUserVos.size))
            helper.addOnClickListener(R.id.manageGroup)
            helper.addOnClickListener(R.id.deleteTv)
        }
    }
}
