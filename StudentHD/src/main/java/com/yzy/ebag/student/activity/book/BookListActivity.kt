package com.yzy.ebag.student.activity.book

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.base.BaseListActivity
import com.yzy.ebag.student.bean.response.BookBean
import ebag.core.http.image.SingleImageLoader
import ebag.core.http.network.RequestCallBack


/**
 * Created by unicho on 2018/1/8.
 */
class BookListActivity: BaseListActivity<BookBean>() {

    override fun loadConfig(intent: Intent) {
        setPageTitle("学习课本")
        loadMoreEnabled(false)
        refreshEnabled(false)
        onlyView(true)
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<BookBean>>) {

    }

    override fun getAdapter(): BaseQuickAdapter<BookBean, BaseViewHolder> {
        val adapter = BookListAdapter()
        val list = ArrayList<BookBean>()
        list.add(BookBean("人教版", "2010-10-24", "上学期", "语文", "三年级"))
        list.add(BookBean("人教版", "2010-10-24", "上学期", "英语", "三年级"))
        list.add(BookBean("人教版", "2010-10-24", "上学期", "数学", "三年级"))
        list.add(BookBean("人教版", "2010-10-24", "上学期", "生物", "三年级"))
        list.add(BookBean("人教版", "2010-10-24", "上学期", "化学", "三年级"))
        list.add(BookBean("人教版", "2010-10-24", "上学期", "历史", "三年级"))
        list.add(BookBean("人教版", "2010-10-24", "上学期", "社会", "三年级"))
        adapter.setNewData(list)
        return adapter
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager? {
        return GridLayoutManager(this,3)
    }

    class BookListAdapter: BaseQuickAdapter<BookBean, BaseViewHolder>(R.layout.activity_book_list_item){
        override fun convert(helper: BaseViewHolder, item: BookBean) {
            SingleImageLoader.getInstance().setImage(item.image, helper.getView(R.id.ivBook))
            helper.setText(R.id.tvEdition,item.edition)
                    .setText(R.id.tvTime,"[添加时间:${item.time}]")
                    .setText(R.id.tvSemester,item.item)
                    .setText(R.id.tvSubject,item.subject)
                    .setText(R.id.tvClass,item.classX)
        }
    }
}