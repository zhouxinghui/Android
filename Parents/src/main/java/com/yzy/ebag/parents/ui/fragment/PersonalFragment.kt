package com.yzy.ebag.parents.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.mvp.model.PersonalItemModel
import com.yzy.ebag.parents.ui.activity.GuideActivity
import com.yzy.ebag.parents.ui.activity.PersonalInfoActivity
import com.yzy.ebag.parents.ui.activity.SettingActivity
import com.yzy.ebag.parents.ui.adapter.PersonalAdapter
import ebag.core.base.BaseFragment
import ebag.core.util.SerializableUtils
import ebag.core.util.loadHead
import ebag.mobile.base.Constants
import ebag.mobile.bean.UserEntity
import ebag.mobile.module.shop.YBActivity
import kotlinx.android.synthetic.main.fragment_personal.*

class PersonalFragment : BaseFragment() {

    private lateinit var mAdapter: PersonalAdapter
    private val iconList: ArrayList<Int> = arrayListOf(R.drawable.icon_courses_taught, R.drawable.icon_operating_instructions, R.drawable.icon_set_up)
    private val labelList: ArrayList<String> = arrayListOf("云币中心", "操作指南", "设置")
    private val datas: MutableList<PersonalItemModel> = mutableListOf()

    companion object {
        fun newInstance(): PersonalFragment {
            val fragment = PersonalFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutRes(): Int = R.layout.fragment_personal

    override fun getBundle(bundle: Bundle?) {

    }

    override fun initViews(rootView: View) {

        iconList.forEachIndexed { index, i ->
            datas.add(PersonalItemModel(i, labelList[index]))
        }

        mAdapter = PersonalAdapter(mContext,datas)
        val url = SerializableUtils.getSerializable<UserEntity>(Constants.PARENTS_USER_ENTITY).headUrl
        val name = SerializableUtils.getSerializable<UserEntity>(Constants.PARENTS_USER_ENTITY).name
        personal_head.loadHead(url, true)
        personal_name.text = name
        recyclerview.layoutManager = LinearLayoutManager(activity)
        recyclerview.adapter = mAdapter
        recyclerview.addItemDecoration(ebag.core.xRecyclerView.manager.DividerItemDecoration(DividerItemDecoration.VERTICAL, 1, Color.parseColor("#e0e0e0")))


        mAdapter.setOnItemClickListener { _, _, position ->
            when (position) {
                2 -> SettingActivity.start(activity)
                0 -> YBActivity.start(activity)
                1 -> GuideActivity.start(activity)
            }
        }

        personal_head.setOnClickListener {
            startActivityForResult(Intent(activity, PersonalInfoActivity::class.java), 998)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 999) {
            personal_head.loadHead(SerializableUtils.getSerializable<UserEntity>(Constants.PARENTS_USER_ENTITY).headUrl, true)
            personal_name.text = SerializableUtils.getSerializable<UserEntity>(Constants.PARENTS_USER_ENTITY).name
        }
    }

}