package com.yzy.ebag.parents.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.bean.ErrorTopicBean
import com.yzy.ebag.parents.http.ParentsAPI
import com.yzy.ebag.parents.ui.fragment.ErrorBookFragment
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.SerializableUtils
import ebag.mobile.base.Constants
import ebag.mobile.bean.MyChildrenBean
import kotlinx.android.synthetic.main.activity_errorbook.*


class ErrorBookActivity:BaseActivity(){

    private val titleList:ArrayList<String> = arrayListOf()
    private val subCodeList:MutableList<String> = mutableListOf()
    private val fragmentList: MutableList<Fragment> = mutableListOf()

    override fun getLayoutId(): Int = R.layout.activity_errorbook

    override fun initViews() {

        val childrenBean = SerializableUtils.getSerializable(Constants.CHILD_USER_ENTITY) as MyChildrenBean

        viewpager.offscreenPageLimit  = 3
        init(childrenBean)

        stateview.setOnRetryClickListener {
            init(childrenBean)
        }


    }

    private fun init(childrenBean: MyChildrenBean) {
        ParentsAPI.errorTopic(childrenBean.classId, "", 1, 10, childrenBean.uid, object : RequestCallBack<ArrayList<ErrorTopicBean>>() {

            override fun onStart() {
                stateview.showLoading()
            }

            override fun onSuccess(entity: ArrayList<ErrorTopicBean>?) {
                entity?.forEach {
                    titleList.add(it.subject)
                    subCodeList.add(it.subCode)
                }

                initTab()
                stateview.showContent()
            }

            override fun onError(exception: Throwable) {
                exception.handleThrowable(this@ErrorBookActivity)
                stateview.showError()
            }

        })
    }

    private fun initTab() {
        titleList.forEachIndexed { index, _ ->
            fragmentList.add(ErrorBookFragment(subCodeList[index]))
        }
        val mAdapter = PaperAdapter()
        viewpager.adapter = mAdapter
        tablayout.setViewPager(viewpager)
    }

    companion object {
        fun start(c: Context) {
            c.startActivity(Intent(c, ErrorBookActivity::class.java))
        }
    }

    inner class PaperAdapter : FragmentPagerAdapter(supportFragmentManager) {


        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return titleList[position]
        }

    }
}