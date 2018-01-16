package com.yzy.ebag.teacher.ui.activity

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.base.BaseListActivity
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.NoticeHistoryBean
import ebag.core.http.network.RequestCallBack
import ebag.core.util.loadImage

/**
 * Created by YZY on 2018/1/16.
 */
class NoticeHistoryActivity: BaseListActivity<List<NoticeHistoryBean>, NoticeHistoryBean>() {
    override fun loadConfig(intent: Intent) {
        val list = ArrayList<NoticeHistoryBean>()
        for (i in 0..99){
            val bean = NoticeHistoryBean()
            bean.publishTime = "2018-01-18"
            bean.publishName = "周老师"
            bean.deadLine = "2018-01-18"
            bean.description = "发京东方爱打飞机哦发京东方爱打飞机哦发京东方爱打飞机哦发京东方爱打飞机哦发京东方爱打飞机哦发京东方爱打飞机哦发京东方爱打飞机哦发京东方爱打飞机哦发京东方爱打飞机哦发京东方爱打飞机哦发京东方爱打飞机哦发京东方爱打飞机哦发京东方爱打飞机哦发京东方爱打飞机哦发京东方爱打飞机哦"
            val imageList = ArrayList<String>()
            imageList.add("http://img07.tooopen.com/images/20170316/tooopen_sy_201956178977.jpg")
            imageList.add("http://img07.tooopen.com/images/20170316/tooopen_sy_201956178977.jpg")
            imageList.add("http://img07.tooopen.com/images/20170316/tooopen_sy_201956178977.jpg")
            imageList.add("http://img07.tooopen.com/images/20170316/tooopen_sy_201956178977.jpg")
            imageList.add("http://img07.tooopen.com/images/20170316/tooopen_sy_201956178977.jpg")
            imageList.add("http://img07.tooopen.com/images/20170316/tooopen_sy_201956178977.jpg")
            imageList.add("http://img07.tooopen.com/images/20170316/tooopen_sy_201956178977.jpg")
            imageList.add("http://img07.tooopen.com/images/20170316/tooopen_sy_201956178977.jpg")
            bean.urls = imageList
            list.add(bean)
        }
        withFirstPageData(list)
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<NoticeHistoryBean>>) {

    }

    override fun parentToList(isFirstPage: Boolean, parent: List<NoticeHistoryBean>?): List<NoticeHistoryBean>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<NoticeHistoryBean, BaseViewHolder> {
        return MyAdapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<NoticeHistoryBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return LinearLayoutManager(this)
    }

    inner class MyAdapter: BaseQuickAdapter<NoticeHistoryBean, BaseViewHolder>(R.layout.item_notice_history){
        override fun convert(helper: BaseViewHolder, item: NoticeHistoryBean) {
            helper.setText(R.id.publishTime, item.publishTime)
                    .setText(R.id.publishName, item.publishName)
                    .setText(R.id.deadLine, item.deadLine)
                    .setText(R.id.publishDesc, item.description)
            val recyclerView = helper.getView<RecyclerView>(R.id.recyclerView)
            recyclerView.layoutManager = GridLayoutManager(mContext, 8)
            recyclerView.adapter = ImageAdapter(item.urls)
        }
    }

    inner class ImageAdapter(list: List<String>): BaseQuickAdapter<String,BaseViewHolder>(R.layout.imageview, list){
        override fun convert(helper: BaseViewHolder, item: String?) {
            val imageView = helper.getView<ImageView>(R.id.imageView)
            imageView.loadImage(item)
        }
    }
}