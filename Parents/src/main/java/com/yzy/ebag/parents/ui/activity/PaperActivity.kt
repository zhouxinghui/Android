package com.yzy.ebag.parents.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.bean.SubjectBean
import com.yzy.ebag.parents.http.ParentsAPI
import com.yzy.ebag.parents.ui.fragment.PaperFragment
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.SerializableUtils
import ebag.mobile.base.Constants
import ebag.mobile.bean.MyChildrenBean
import kotlinx.android.synthetic.main.activity_paper.*

class PaperActivity : BaseActivity() {

    private val titleList:ArrayList<String> = arrayListOf()
    private val subCodeList:MutableList<String> = mutableListOf()
    private val fragmentList: MutableList<Fragment> = mutableListOf()
    private var type = ""
    override fun getLayoutId(): Int = R.layout.activity_paper

    override fun initViews() {



        type = intent.getStringExtra("type")

        if (type == "2"){
            titlebar.setTitle("所有作业")
        }

        val childrenBean = SerializableUtils.getSerializable(Constants.CHILD_USER_ENTITY) as MyChildrenBean
        ParentsAPI.subjectWorkList(type,childrenBean.classId,"",1,10,childrenBean.uid,object:RequestCallBack<List<SubjectBean>>(){

            override fun onStart() {
                stateview.showLoading()
            }

            override fun onSuccess(entity: List<SubjectBean>?) {
                entity?.forEach {
                    titleList.add(it.subject)
                    subCodeList.add(it.subCode)
                }
                viewpager.offscreenPageLimit = entity!!.size
                initTab()
                stateview.showContent()
            }

            override fun onError(exception: Throwable) {
                exception.handleThrowable(this@PaperActivity)
                stateview.showError()
            }

        })







    }

    private fun initTab() {
        titleList.forEachIndexed { index, _ ->
            fragmentList.add(PaperFragment(subCodeList[index],type))
        }
        val mAdapter = PaperAdapter()
        viewpager.adapter = mAdapter
        tablayout.setViewPager(viewpager)
    }

    companion object {
        fun start(c: Context,type:String) {
            c.startActivity(Intent(c, PaperActivity::class.java).putExtra("type",type))
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