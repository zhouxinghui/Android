package com.yzy.ebag.parents.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.mvp.NoticeListContract
import com.yzy.ebag.parents.mvp.presenter.NoticeListPresenter
import com.yzy.ebag.parents.ui.adapter.NoticeListAdapter
import ebag.core.base.BaseActivity
import ebag.core.http.network.handleThrowable
import ebag.core.util.SerializableUtils
import ebag.mobile.base.Constants
import ebag.mobile.bean.MyChildrenBean
import ebag.mobile.bean.NoticeBean
import kotlinx.android.synthetic.main.activity_noticelist.*

class NoticeListActivity : BaseActivity(), NoticeListContract.NoticeListView {


    override fun getLayoutId(): Int = R.layout.activity_noticelist
    private lateinit var mAdapter: NoticeListAdapter
    private val datas: MutableList<NoticeBean> = mutableListOf()
    private lateinit var mPresenter: NoticeListPresenter

    override fun initViews() {
        mPresenter = NoticeListPresenter(this)
        recyclerview.layoutManager = LinearLayoutManager(this)
        mAdapter = NoticeListAdapter(datas)
        recyclerview.adapter = mAdapter
        recyclerview.addItemDecoration(ebag.core.xRecyclerView.manager.DividerItemDecoration(DividerItemDecoration.VERTICAL, 1, Color.parseColor("#e0e0e0")))
        mAdapter.setOnLoadMoreListener({
            mPresenter.loadMore()
        }, recyclerview)

        mAdapter.disableLoadMoreIfNotFullPage(recyclerview)
        mPresenter.firstList(SerializableUtils.getSerializable<MyChildrenBean>(Constants.CHILD_USER_ENTITY).classId)

        stateview.setOnRetryClickListener {
            mPresenter.firstList(SerializableUtils.getSerializable<MyChildrenBean>(Constants.CHILD_USER_ENTITY).classId)
        }
    }


    companion object {
        fun start(c: Context) {
            c.startActivity(Intent(c, NoticeListActivity::class.java))
        }
    }


    override fun showLoading() {
        stateview.showLoading()
    }

    override fun showEmpty() {
        stateview.showEmpty()
    }

    override fun <T> showContents(data: List<T>) {
        datas.addAll(data as List<NoticeBean>)
        mAdapter.notifyDataSetChanged()
        stateview.showContent()
    }

    override fun <T> showMoreComplete(data: List<T>) {
        datas.addAll(data as List<NoticeBean>)
        mAdapter.loadMoreComplete()
    }

    override fun loadmoreEnd() {
        mAdapter.loadMoreEnd()
    }

    override fun loadmoreFail() {

        mAdapter.loadMoreFail()
    }

    override fun showError(e: Throwable?) {
        stateview.showError()
        e!!.handleThrowable(this)
    }
}