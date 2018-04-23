package com.yzy.ebag.parents.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.bean.OnePageInfoBean
import com.yzy.ebag.parents.ui.adapter.HomeworkListAdapter
import ebag.core.base.BaseActivity
import kotlinx.android.synthetic.main.activity_homeworklist.*

class HomeworkListActivity : BaseActivity() {


    override fun getLayoutId(): Int = R.layout.activity_homeworklist

    override fun initViews() {

        val datas = intent.getSerializableExtra("datas") as ArrayList<OnePageInfoBean.HomeWorkInfoVosBean>
        val subject = intent.getStringExtra("subject")
        recyclerview.layoutManager = LinearLayoutManager(this)
        val adapter = HomeworkListAdapter(datas)
        recyclerview.adapter = adapter
        val headerView = View.inflate(this, R.layout.header_homeworklist, null)
        headerView.findViewById<TextView>(R.id.homework_item_subject).text = subject.substring(0, 1)
        adapter.addHeaderView(headerView)

        adapter.setOnItemChildClickListener { adapter, view, position ->
            HomeworkReportActivity.start(this, datas[0].id, datas[0].endTime,subject)
        }
    }

}