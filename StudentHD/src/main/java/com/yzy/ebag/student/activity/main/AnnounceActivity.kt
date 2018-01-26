package com.yzy.ebag.student.activity.main

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.base.BaseListActivity
import ebag.core.base.PhotoPreviewActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.util.DateUtil
import ebag.core.util.loadHead
import ebag.core.util.loadImage
import ebag.hd.bean.response.NoticeBean
import ebag.hd.http.EBagApi

/**
 * @author caoyu
 * @date 2018/1/17
 * @description
 */
class AnnounceActivity: BaseListActivity<List<NoticeBean>, NoticeBean>() {

    companion object {
        fun jump(context: Activity, classId: String){
            context.startActivity(
                    Intent(context, AnnounceActivity::class.java)
                            .putExtra("classId", classId)
            )
        }
    }

    private lateinit var classId: String
    override fun loadConfig(intent: Intent) {
        setPageTitle("公告")
        classId = intent.getStringExtra("classId")
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<NoticeBean>>) {
        EBagApi.noticeList(page, getPageSize(), classId, requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<NoticeBean>?): List<NoticeBean>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<NoticeBean, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<NoticeBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    inner class Adapter: BaseQuickAdapter<NoticeBean,BaseViewHolder>(R.layout.item_activity_announce){

        override fun convert(helper: BaseViewHolder, item: NoticeBean?) {
            helper.setText(R.id.tvName, item?.name)
                    .setText(R.id.tvTime, DateUtil.getDateTime(item?.createDate ?: 0))
                    .setText(R.id.tvContent, item?.content)
//                    .setGone(R.id.recyclerView, !(item?.images?.isEmpty() ?: true))
                    .getView<ImageView>(R.id.ivHead).loadHead(item?.headUrl)

            val recycler = helper.getView<RecyclerView>(R.id.recyclerView)
            recycler.isNestedScrollingEnabled = false

            if(recycler.adapter == null) {
                recycler.adapter = ImageAdapter()
            }
            if(recycler.layoutManager == null){
                recycler.layoutManager = GridLayoutManager(mContext,6)
            }
            recycler.postDelayed({
                (recycler.adapter as ImageAdapter).setNewData(item?.photos)
            },20)

        }

    }

    inner class ImageAdapter: BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_activity_announce_image){
        override fun convert(helper: BaseViewHolder, item: String?) {
            helper.getView<ImageView>(R.id.image).loadImage(item)
            helper.itemView.setOnClickListener {
                PhotoPreviewActivity.jump(this@AnnounceActivity, data, helper.adapterPosition)
            }
        }
    }
}