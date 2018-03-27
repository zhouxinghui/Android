package com.yzy.ebag.student.activity.location

import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.yzy.ebag.student.R
import com.yzy.ebag.student.adapter.LocationAdapter
import com.yzy.ebag.student.bean.LocationBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.hd.http.EBagApi
import kotlinx.android.synthetic.main.activity_location.*
import kotlinx.android.synthetic.main.item_fragment_task_labour.view.*

/**
 * Created by fansan on 2018/3/26.
 */
class LocationActivity : BaseActivity() {

    private var mPage = 1

    override fun getLayoutId(): Int = R.layout.activity_location
    private var mData: MutableList<LocationBean.UserPositioningVosBean> = mutableListOf()
    private lateinit var mAdapter: LocationAdapter

    override fun initViews() {

        title_bar.setOnRightClickListener {

            startActivityForResult(Intent(this, UplodaLocationActivity::class.java), 999)
        }

        recyclerview.layoutManager = LinearLayoutManager(this)
        mAdapter = LocationAdapter(mData)
        recyclerview.adapter = mAdapter
        recyclerview.addItemDecoration(ebag.core.xRecyclerView.manager.DividerItemDecoration(DividerItemDecoration.VERTICAL, 10, Color.parseColor("#00000000")))
        request(mPage)
        mAdapter.setOnLoadMoreListener({
            request(mPage)
        }, recyclerview)

        refreshlayout.setOnRefreshListener {
            request(1)
        }

        stateview.setOnRetryClickListener {
            request(1)
        }

    }

    private fun request(page: Int) {

        StudentApi.searchLocation(page, object : RequestCallBack<LocationBean>() {

            override fun onStart() {
                if (!mAdapter.isLoading && !refreshlayout.isRefreshing)
                    stateview.showLoading()
            }

            override fun onSuccess(entity: LocationBean?) {
                if (refreshlayout.isRefreshing) {
                    mAdapter.setNewData(entity!!.userPositioningVos)
                    refreshlayout.isRefreshing = false
                    mPage = 1
                } else {
                    if (entity?.userPositioningVos!!.isEmpty()) {
                        if (mAdapter.isLoading) {
                            mAdapter.loadMoreEnd()
                        } else {
                            stateview.showEmpty()
                        }
                    } else {
                        mData.addAll(entity.userPositioningVos)
                        if (mAdapter.isLoading) {
                            mAdapter.loadMoreComplete()
                        } else {
                            mAdapter.notifyDataSetChanged()
                        }
                    }
                }
                mPage += 1
                stateview.showContent()
            }

            override fun onError(exception: Throwable) {
                stateview.showError()
            }

        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 1000) {
            refreshlayout.postDelayed({
                refreshlayout.isRefreshing = true
                request(1)
            }, 500)
        }
    }
}