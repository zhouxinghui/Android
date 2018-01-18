package com.yzy.ebag.teacher.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.base.BaseListActivity
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.GroupBean
import com.yzy.ebag.teacher.http.TeacherApi
import com.yzy.ebag.teacher.widget.GroupManageDialog
import ebag.core.http.network.RequestCallBack

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
    private val groupDialog by lazy { GroupManageDialog(this, classId) }
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
            }
        }
        groupDialog.onGroupChangeListener = {
            onRetryClick()
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
        return GridLayoutManager(this, 3)
    }

    inner class MyAdapter: BaseQuickAdapter<GroupBean, BaseViewHolder>(R.layout.item_study_group){
        override fun convert(helper: BaseViewHolder, item: GroupBean) {
            helper.setText(R.id.groupNameTv, item.groupName)
                    .setText(R.id.studentCount, resources.getString(R.string.student_count, item.clazzUserVos.size))
            helper.addOnClickListener(R.id.manageGroup)
        }
    }
}
