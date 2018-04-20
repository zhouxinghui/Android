package com.yzy.ebag.parents.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.bean.MyChildrenBean
import com.yzy.ebag.parents.common.Constants
import com.yzy.ebag.parents.mvp.ChooseChildrenContract
import com.yzy.ebag.parents.mvp.presenter.ChooseChildrenPersenter
import com.yzy.ebag.parents.ui.adapter.ChooseChildrenAdapter
import ebag.core.base.BaseActivity
import ebag.core.util.SPUtils
import kotlinx.android.synthetic.main.activity_choosechildren.*


@Suppress("UNCHECKED_CAST")
class ChooseChildrenActivity : BaseActivity(), ChooseChildrenContract.ChooseChildrenView {

    private val mPersenter: ChooseChildrenPersenter by lazy { ChooseChildrenPersenter(this, this) }
    private lateinit var mAdapter: ChooseChildrenAdapter
    override fun getLayoutId(): Int = R.layout.activity_choosechildren


    override fun initViews() {

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(ebag.core.xRecyclerView.manager.DividerItemDecoration(DividerItemDecoration.VERTICAL, 1, Color.parseColor("#cccccc")))
        stateview.setOnRetryClickListener { mPersenter.start() }
        mPersenter.start()
    }

    override fun showLoading() {
        stateview.showLoading()
    }

    override fun showEmpty() {
        stateview.showEmpty()
    }

    override fun showError(e: Throwable?) {
        stateview.showError()
    }

    override fun showContents(data: List<*>) {
        mAdapter = ChooseChildrenAdapter(data as List<MyChildrenBean>, SPUtils.get(this, Constants.CURRENT_CHILDREN_YSBCODE, "") as String)
        recyclerView.adapter = mAdapter
        stateview.showContent()
    }

    override fun showMoreComplete() {

    }

    override fun loadmoreEnd() {

    }

    override fun loadmoreFail() {

    }


    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, ChooseChildrenActivity::class.java))
        }
    }
}