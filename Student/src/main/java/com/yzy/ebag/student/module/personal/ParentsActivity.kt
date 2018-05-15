package com.yzy.ebag.student.module.personal

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.ParentBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.http.network.RequestCallBack
import ebag.core.util.loadHead
import ebag.mobile.base.BaseListActivity

/**
 * Created by YZY on 2018/5/15.
 */
class ParentsActivity: BaseListActivity<List<ParentBean>, ParentBean>() {
    private val addParentDialog by lazy {
        val dialog = ParentAddDialog(this)
        dialog.listener = {
            onRetryClick()
        }
        dialog
    }

    override fun loadConfig(intent: Intent) {
        titleBar.setTitle("我的家长")
        loadMoreEnabled(false)
        val button = Button(this)
        button.text = "添加家长"
        button.setTextColor(resources.getColor(R.color.white))
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.tv_normal))
        button.setBackgroundResource(R.drawable.my_class_btn_add)
        button.setPadding(resources.getDimensionPixelOffset(R.dimen.x10), 0,
                resources.getDimensionPixelSize(R.dimen.x10), 0)
        val p = RelativeLayout.LayoutParams(resources.getDimensionPixelOffset(R.dimen.x54),
                resources.getDimensionPixelOffset(R.dimen.x54))
        p.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
        p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        rootLayout.addView(button, p)

        button.setOnClickListener {
            addParentDialog.show()
        }
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<ParentBean>>) {
        StudentApi.searchFamily(requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<ParentBean>?): List<ParentBean>? {

        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<ParentBean, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<ParentBean, BaseViewHolder>): RecyclerView.LayoutManager? = null

    private inner class Adapter : BaseQuickAdapter<ParentBean, BaseViewHolder>(R.layout.item_activity_parents) {
        override fun convert(helper: BaseViewHolder, item: ParentBean?) {
            helper.setText(R.id.tvName, item?.name)
                    .setText(R.id.tvStudentName, item?.relationType)
                    .setText(R.id.tvEBagCode, "书包号: ${item?.ysbCode}")
                    .setText(R.id.tvPhone, item?.phone)
                    .setText(R.id.tvAddress, item?.address ?: "暂无地址")
                    .getView<ImageView>(R.id.ivAvatar).loadHead(item?.headUrl)

        }
    }
}