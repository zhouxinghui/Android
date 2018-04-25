package com.yzy.ebag.parents.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.common.Constants
import com.yzy.ebag.parents.ui.adapter.RepresentAdapter
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.util.SPUtils
import ebag.mobile.bean.PersonalPerformanceBean
import ebag.mobile.http.EBagApi
import kotlinx.android.synthetic.main.activity_represent.*

class RepresentActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_represent

    override fun initViews() {

        recyclerview.layoutManager = LinearLayoutManager(this)
        val list: MutableList<String> = mutableListOf("1", "2", "3", "4", "5")
        val adapter = RepresentAdapter(list)
        recyclerview.adapter = adapter
        recyclerview.addItemDecoration(ebag.core.xRecyclerView.manager.DividerItemDecoration(DividerItemDecoration.VERTICAL,1, Color.parseColor("#e0e0e0")))

        EBagApi.personalPerformance(object:RequestCallBack<PersonalPerformanceBean>(){

            override fun onStart() {

            }

            override fun onSuccess(entity: PersonalPerformanceBean?) {

            }

            override fun onError(exception: Throwable) {

            }

        },SPUtils.get(this,Constants.CURRENT_CHILDREN_YSBCODE,"") as String)

    }

    companion object {
        fun start(c: Context) {
            c.startActivity(Intent(c, RepresentActivity::class.java))
        }
    }

}