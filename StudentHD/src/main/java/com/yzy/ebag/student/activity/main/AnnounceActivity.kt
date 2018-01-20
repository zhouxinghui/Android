package com.yzy.ebag.student.activity.main

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.base.BaseListActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.util.loadHead
import ebag.core.util.loadImage

/**
 * @author caoyu
 * @date 2018/1/17
 * @description
 */
class AnnounceActivity: BaseListActivity<String, AnnounceActivity.Announce>() {
    override fun loadConfig(intent: Intent) {
        setPageTitle("公告")
        val list = ArrayList<Announce>()
        val list2 = ArrayList<String>()
        list2.add("http://pic32.nipic.com/20130827/12906030_123121414000_2.png")
        list2.add("http://pic32.nipic.com/20130827/12906030_123121414000_2.png")
        list2.add("http://pic32.nipic.com/20130827/12906030_123121414000_2.png")
        list2.add("http://pic32.nipic.com/20130827/12906030_123121414000_2.png")
        list2.add("http://pic32.nipic.com/20130827/12906030_123121414000_2.png")

        list.add(Announce("ad福利卡就是东方丽景阿里斯顿解放路案件速度发就发ad福利" +
                "卡就是东方丽景阿里斯顿解放路案件速度发就" +
                "ad福利卡就是东方丽景阿里斯顿解放路案件速度发就发点击" +
                "ad福利卡就是东方丽景阿里斯顿解放路案件速度发就发点击" +
                "ad福利卡就是东方丽景阿里斯顿解放路案件速度发就发点击发点击点击",list2))
        list.add(Announce("ad福利卡就是东方丽景阿里斯顿解放路案件速度发就发点击"))
        list.add(Announce("ad福利卡就是东方丽景阿里斯顿解放路案件速度发就发点击",list2))
        list2.add("http://pic32.nipic.com/20130827/12906030_123121414000_2.png")
        list.add(Announce("ad福利卡就是东方丽景阿里斯顿解放路案件速度发就发点击",list2))
        list.add(Announce("ad福利卡就是东方丽景阿里斯顿解放路案件速度发就发点击"))
        list.add(Announce("ad福利卡就是东方丽景阿里斯顿解放路案件速度发就发点击",list2))
        list2.add("http://pic32.nipic.com/20130827/12906030_123121414000_2.png")
        list2.add("http://pic32.nipic.com/20130827/12906030_123121414000_2.png")
        list.add(Announce("ad福利卡就是东方丽景阿里斯顿解放路案件速度发就发点击",list2))
        list.add(Announce("ad福利卡就是东方丽景阿里斯顿解放路案件速度发就发点击",list2))
        list.add(Announce("ad福利卡就是东方丽景阿里斯顿解放路案件速度发就发点击"))
        list.add(Announce("ad福利卡就是东方丽景阿里斯顿解放路案件速度发就发点击"))
        list2.add("http://pic32.nipic.com/20130827/12906030_123121414000_2.png")
        list.add(Announce("ad福利卡就是东方丽景阿里斯顿解放路案件速度发就发点击",list2))
        withFirstPageData(list)
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<String>) {
    }

    override fun parentToList(isFirstPage: Boolean, parent: String?): List<Announce>? {
        return null
    }

    override fun getAdapter(): BaseQuickAdapter<Announce, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<Announce, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    class Adapter: BaseQuickAdapter<AnnounceActivity.Announce,BaseViewHolder>(R.layout.item_activity_announce){

        val adapter = ImageAdapter()

        override fun convert(helper: BaseViewHolder, item: Announce?) {
            helper.setText(R.id.tvName, item?.name)
                    .setText(R.id.tvTime, item?.time)
                    .setText(R.id.tvContent, item?.content)
//                    .setGone(R.id.recyclerView, !(item?.images?.isEmpty() ?: true))
                    .getView<ImageView>(R.id.ivHead).loadHead(item?.head)

            val recycler = helper.getView<RecyclerView>(R.id.recyclerView)
            recycler.isNestedScrollingEnabled = false

            if(recycler.adapter == null){
                recycler.adapter = adapter
            }
            if(recycler.layoutManager == null){
                recycler.layoutManager = GridLayoutManager(mContext,6)
            }
            recycler.postDelayed({
                adapter.setNewData(item?.images)
            },10)

        }

    }

    class ImageAdapter: BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_activity_announce_image){
        override fun convert(helper: BaseViewHolder, item: String?) {
            helper.getView<ImageView>(R.id.image).loadImage(item)
        }
    }

    data class Announce(
            val content: String = "",
            val images: List<String>? = ArrayList(),
            val head: String = "http://pic32.nipic.com/20130827/12906030_123121414000_2.png",
            val name: String = "一年级语文教师",
            val time: String = "2017-07-28 15:35:29"

    )
}