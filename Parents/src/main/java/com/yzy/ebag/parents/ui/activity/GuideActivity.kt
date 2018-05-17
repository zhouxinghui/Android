package com.yzy.ebag.parents.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.parents.R
import ebag.core.base.BaseActivity
import kotlinx.android.synthetic.main.activity_guide.*

/**
 *  Created by fansan on 2018/5/17 9:03
 */

class GuideActivity : BaseActivity() {

    private val array: Array<String> = arrayOf("1.如何绑定孩子", "2.如何查看孩子位置", "3.如何查看孩子作业情况")

    override fun getLayoutId(): Int = R.layout.activity_guide

    override fun initViews() {

        recyclerview.layoutManager = LinearLayoutManager(this)
        val adapter = Adapter()
        recyclerview.adapter = adapter
        adapter.setNewData(array.toList())
        recyclerview.addItemDecoration(ebag.core.xRecyclerView.manager.DividerItemDecoration(DividerItemDecoration.VERTICAL, 1, Color.parseColor("#e0e0e0")))
        adapter.setOnItemClickListener { adapter, view, position ->


        }

    }


    inner class Adapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_guide) {

        override fun convert(helper: BaseViewHolder, item: String?) {
            helper.setText(R.id.tv, item)
        }

    }


    companion object {

        fun start(c: Context){
            c.startActivity(Intent(c,GuideActivity::class.java))
        }
    }


}