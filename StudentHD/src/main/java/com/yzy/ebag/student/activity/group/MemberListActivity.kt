package com.yzy.ebag.student.activity.group

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.base.BaseListActivity
import com.yzy.ebag.student.bean.GroupBean
import com.yzy.ebag.student.bean.GroupUserBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.http.network.RequestCallBack
import ebag.core.util.T
import ebag.core.util.loadHead

/**
 * Created by unicho on 2018/3/6.
 */
class MemberListActivity: BaseListActivity<ArrayList<GroupUserBean>, GroupUserBean>() {

    companion object {
        fun jump(context: Context, groupBean: GroupBean?){
            context.startActivity(
                    Intent(context, MemberListActivity::class.java)
                            .putExtra("groupBean", groupBean)
            )
        }
    }

    private var groupBean: GroupBean? = null
    override fun loadConfig(intent: Intent) {
        window.setBackgroundDrawableResource(R.color.white)
        groupBean = intent.getSerializableExtra("groupBean") as GroupBean?
        if(groupBean == null || (groupBean?.groupId == null && groupBean?.clazzUserVos == null)){
            T.show(this,"未知错误，稍后重试")
            finish()
            return
        }

        titleBar.setTitle(groupBean?.groupName)
        loadMoreEnabled(false)
        refreshEnabled(false)
//        if(groupBean?.clazzUserVos != null){
//            withFirstPageData(groupBean?.clazzUserVos)
//        }
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<ArrayList<GroupUserBean>>) {
        StudentApi.groupMember(groupBean?.groupId ?: "", requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: ArrayList<GroupUserBean>?): ArrayList<GroupUserBean>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<GroupUserBean, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<GroupUserBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return GridLayoutManager(this, 8)
    }

    private val dialog by lazy { MemberDetailDialog.newInstance() }
    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        dialog.updateData((adapter as Adapter).getItem(position))
        dialog.show(supportFragmentManager, "MemberDetail")
    }

    inner class Adapter: BaseQuickAdapter<GroupUserBean, BaseViewHolder>(R.layout.item_group_member){
        override fun convert(helper: BaseViewHolder, item: GroupUserBean?) {
            helper.setText(R.id.name,item?.name)
                    .getView<ImageView>(R.id.image).loadHead(item?.headUrl)
        }
    }
}