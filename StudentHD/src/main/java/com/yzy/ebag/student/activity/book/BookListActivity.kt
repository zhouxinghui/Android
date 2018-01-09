package com.yzy.ebag.student.activity.book

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.yzy.ebag.student.R
import com.yzy.ebag.student.base.BaseListActivity
import com.yzy.ebag.student.bean.response.BookEntity
import ebag.core.http.image.SingleImageLoader
import ebag.core.http.network.RequestCallBack
import ebag.core.xRecyclerView.adapter.RecyclerAdapter
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder


/**
 * Created by unicho on 2018/1/8.
 */
class BookListActivity: BaseListActivity<BookEntity>() {

    override fun loadConfig(intent: Intent) {
        setPageTitle("学习课本")
        loadMoreEnabled(false)
        refreshEnabled(false)
        onlyView(true)
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<BookEntity>>) {

    }

    override fun getAdapter(): RecyclerAdapter<BookEntity> {
        val adapter = BookListAdapter()
        val list = ArrayList<BookEntity>()
        list.add(BookEntity("人教版","2010-10-24","上学期","语文","三年级"))
        list.add(BookEntity("人教版","2010-10-24","上学期","英语","三年级"))
        list.add(BookEntity("人教版","2010-10-24","上学期","数学","三年级"))
        list.add(BookEntity("人教版","2010-10-24","上学期","生物","三年级"))
        list.add(BookEntity("人教版","2010-10-24","上学期","化学","三年级"))
        list.add(BookEntity("人教版","2010-10-24","上学期","历史","三年级"))
        list.add(BookEntity("人教版","2010-10-24","上学期","社会","三年级"))
        adapter.datas = list
        return adapter
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager? {
        return GridLayoutManager(this,3)
    }

    override fun onItemClick(holder: RecyclerViewHolder, view: View, position: Int) {
    }

    class BookListAdapter: RecyclerAdapter<BookEntity>(R.layout.activity_book_list_item){

        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: BookEntity) {
            SingleImageLoader.getInstance().setImage(entity.image, setter.getImageView(R.id.ivBook))
            setter.setText(R.id.tvEdition,entity.edition)
                    .setText(R.id.tvTime,"[添加时间:${entity.time}]")
                    .setText(R.id.tvSemester,entity.item)
                    .setText(R.id.tvSubject,entity.subject)
                    .setText(R.id.tvClass,entity.classX)
        }

    }
}