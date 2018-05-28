package com.yzy.ebag.student.group

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.GroupBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.http.network.RequestCallBack
import ebag.mobile.base.BaseListActivity

/**
 * 学习小组列表
 * Created by unicho on 2018/3/5.
 */
class GroupListActivity: BaseListActivity<ArrayList<GroupBean>, GroupBean>() {

    companion object {
        fun jump(context: Context, classId: String){
            context.startActivity(
                    Intent(context, GroupListActivity::class.java)
                            .putExtra("classId", classId)
            )
        }
    }

    private lateinit var classId: String
    override fun loadConfig(intent: Intent) {
        classId = intent.getStringExtra("classId") ?: ""

        titleBar.setTitle("学习小组")

        loadMoreEnabled(false)
        refreshEnabled(false)
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<ArrayList<GroupBean>>) {
        StudentApi.groups(classId, requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: ArrayList<GroupBean>?): List<GroupBean>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<GroupBean, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<GroupBean, BaseViewHolder>): RecyclerView.LayoutManager? = null

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        MemberListActivity.jump(this, (adapter as Adapter).getItem(position))
    }

    inner class Adapter: BaseQuickAdapter<GroupBean, BaseViewHolder>(R.layout.item_activity_study_group){
        override fun convert(helper: BaseViewHolder, item: GroupBean?) {
            helper.setText(R.id.groupNameTv, item?.groupName)
                    .setText(R.id.studentCount, "学生人数：${item?.studentCount ?: 0}")
            val user = item?.clazzUserVos?.filter { it.duties == "6" }
            if(user != null && user.isNotEmpty()){
                helper.setVisible(R.id.tagView, true)
                        .setText(R.id.leaderTv, user[0].name)
            }else{
                helper.setVisible(R.id.tagView, false)
                        .setText(R.id.leaderTv, "暂无组长")
            }
        }

    }
}