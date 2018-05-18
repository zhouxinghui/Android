package com.yzy.ebag.parents.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.bean.ExcitationWorkBean
import com.yzy.ebag.parents.mvp.ExcitationJobContract
import com.yzy.ebag.parents.mvp.presenter.ExcitationPersenter
import com.yzy.ebag.parents.ui.adapter.ExcitationAdapter
import ebag.core.base.BaseFragment
import ebag.core.http.network.handleThrowable
import ebag.core.util.SPUtils
import kotlinx.android.synthetic.main.fragment_excitation.*

@SuppressLint("ValidFragment")
class ExcitationJobFragment(private val type: String) : BaseFragment(), ExcitationJobContract.ExcitationJobView {


    private lateinit var mPersenter: ExcitationJobContract.Persenter
    private var page = 1
    private val datas: ArrayList<ExcitationWorkBean> = arrayListOf()
    private lateinit var workAdapter: ExcitationAdapter


    override fun getLayoutRes(): Int = R.layout.fragment_excitation

    override fun getBundle(bundle: Bundle?) {
    }

    override fun initViews(rootView: View) {

        mPersenter = ExcitationPersenter(this)
        recyclerview.layoutManager = LinearLayoutManager(activity)
        workAdapter = ExcitationAdapter(datas)
        workAdapter.disableLoadMoreIfNotFullPage(recyclerview)

        recyclerview.adapter = workAdapter
        mPersenter.requestTask(page.toString(), SPUtils.get(activity, com.yzy.ebag.parents.common.Constants.CURRENT_CHILDREN_YSBCODE, "") as String)

        workAdapter.setOnLoadMoreListener({
            mPersenter.requestTask(page.toString(), SPUtils.get(activity, com.yzy.ebag.parents.common.Constants.CURRENT_CHILDREN_YSBCODE, "") as String)
        }, recyclerview)

        workAdapter.setOnItemChildClickListener { adapter, _, position ->
            mPersenter.finish((adapter.getItem(position) as ExcitationWorkBean).id, position)
        }

    }

    override fun showLoading() {
        stateview.showLoading()
    }

    override fun showEmpty() {
        stateview.showEmpty()
    }

    override fun <T> showContents(data: List<T>) {
        page++
        workAdapter.addData(data as List<ExcitationWorkBean>)
        stateview.showContent()
    }

    override fun <T> showMoreComplete(data: List<T>) {
        page++
        workAdapter.loadMoreComplete()
    }

    override fun loadmoreEnd() {
        workAdapter.loadMoreEnd()
    }

    override fun loadmoreFail() {
        workAdapter.loadMoreFail()
    }

    override fun <T> showContent(data: T?) {
        super.showContent(data)
    }

    override fun showError(e: Throwable?) {
        stateview.showError()
    }

    override fun notifyBtn(position: Int) {
        datas[position].completed = "Y"
        workAdapter.notifyDataSetChanged()
    }

    override fun finishError(e: Throwable?) {
        e?.handleThrowable(activity)
    }

    fun refresh() {
        page = 1
        datas.clear()
        mPersenter.requestTask(page.toString(), SPUtils.get(activity, com.yzy.ebag.parents.common.Constants.CURRENT_CHILDREN_YSBCODE, "") as String)
    }


}