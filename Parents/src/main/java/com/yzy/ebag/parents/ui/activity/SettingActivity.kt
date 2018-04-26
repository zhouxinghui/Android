package com.yzy.ebag.parents.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.ui.adapter.SettingAdapter
import ebag.core.base.App
import ebag.core.base.BaseActivity
import ebag.core.util.AppManager
import ebag.core.util.SPUtils
import ebag.core.util.SerializableUtils
import ebag.mobile.base.Constants
import ebag.mobile.module.AboutUsActivity
import ebag.mobile.widget.UserFeedbackDialog
import kotlinx.android.synthetic.main.activity_setting.*


class SettingActivity : BaseActivity() {
    private val datas: MutableList<String> = mutableListOf("版本更新", "官方公告", "用户反馈", "关于我们")
    private lateinit var mAdapter: SettingAdapter
    override fun getLayoutId(): Int = R.layout.activity_setting

    override fun initViews() {


        recyclerview.layoutManager = LinearLayoutManager(this)
        mAdapter = SettingAdapter(datas)
        recyclerview.addItemDecoration(ebag.core.xRecyclerView.manager.DividerItemDecoration(DividerItemDecoration.VERTICAL, 1, Color.parseColor("#e0e0e0")))
        recyclerview.adapter = mAdapter

        logout.setOnClickListener {
            App.deleteToken()
            SerializableUtils.deleteSerializable(Constants.CHILD_USER_ENTITY)
            SPUtils.remove(this, com.yzy.ebag.parents.common.Constants.CURRENT_CHILDREN_YSBCODE)
            startActivity(Intent(this, LoginActivity::class.java).putExtra(Constants.KEY_TO_MAIN, true))
            AppManager.finishAllActivity()
        }

        mAdapter.setOnItemClickListener { adapter, view, position ->
            when (position) {
                3 -> {
                    startActivity(Intent(this,AboutUsActivity::class.java))

                }

                2 -> UserFeedbackDialog(this).show()


            }
        }

    }


    companion object {

        fun start(c: Context) {
            c.startActivity(Intent(c, SettingActivity::class.java))
        }
    }
}