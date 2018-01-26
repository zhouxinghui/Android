package com.yzy.ebag.teacher.ui.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.base.BaseListActivity
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.base.Constants
import ebag.core.base.PhotoPreviewActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.util.DateUtil
import ebag.core.util.loadImage
import ebag.hd.bean.response.NoticeBean
import ebag.hd.http.EBagApi
import java.util.*

/**
 * Created by YZY on 2018/1/16.
 */
class NoticeHistoryActivity: BaseListActivity<List<NoticeBean>, NoticeBean>() {
    private var isPublished = false
    companion object {
        fun jump(context: Activity, classId: String){
            context.startActivityForResult(
                    Intent(context, NoticeHistoryActivity::class.java)
                            .putExtra("classId", classId),
                    Constants.PUBLISH_REQUEST
            )
        }
    }
    private val classId by lazy { intent.getStringExtra("classId") }
    override fun loadConfig(intent: Intent) {
        titleBar.setTitle(R.string.notice_history)
        titleBar.setRightText(resources.getString(R.string.publish_notice), {
            PublishContentActivity.jump(this, classId)
        })
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<NoticeBean>>) {
        EBagApi.noticeList(page, getPageSize(), classId, requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<NoticeBean>?): List<NoticeBean>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<NoticeBean, BaseViewHolder> {
        return MyAdapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<NoticeBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return LinearLayoutManager(this)
    }

    inner class MyAdapter: BaseQuickAdapter<NoticeBean, BaseViewHolder>(R.layout.item_notice_history){
        override fun convert(helper: BaseViewHolder, item: NoticeBean) {
            helper.setText(R.id.publishTime, DateUtil.getFormatDateTime(Date(item.createDate), "yyyy-MM-dd"))
                    .setText(R.id.publishName, item.name)
                    .setText(R.id.publishDesc, item.content)
            val recyclerView = helper.getView<RecyclerView>(R.id.recyclerView)
            if(recyclerView.adapter == null) {
                recyclerView.adapter = ImageAdapter()
            }
            if(recyclerView.layoutManager == null){
                recyclerView.layoutManager = GridLayoutManager(mContext,8)
            }
            recyclerView.postDelayed({
                (recyclerView.adapter as ImageAdapter).setNewData(item.photos)
            },20)
        }
    }

    inner class ImageAdapter: BaseQuickAdapter<String,BaseViewHolder>(R.layout.imageview){
        override fun convert(helper: BaseViewHolder, item: String?) {
            val imageView = helper.getView<ImageView>(R.id.imageView)
            imageView.loadImage(item)
            helper.itemView.setOnClickListener {
                PhotoPreviewActivity.jump(this@NoticeHistoryActivity, data, helper.adapterPosition)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.PUBLISH_REQUEST && resultCode == Constants.PUBLISH_RESULT) {
            onRefresh()
            isPublished = true
        }
    }

    override fun onDestroy() {
        isPublished = true
        setResult(Constants.PUBLISH_RESULT)
        super.onDestroy()
    }
}