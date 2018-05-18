package com.yzy.ebag.parents.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.bean.SubjectBean
import com.yzy.ebag.parents.http.ParentsAPI
import ebag.core.base.BaseFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.SerializableUtils
import ebag.mobile.base.Constants
import ebag.mobile.bean.MyChildrenBean
import kotlinx.android.synthetic.main.activity_paper.*

/**
 *  Created by fansan on 2018/5/18 17:44
 */
 
class HomeworkJobFragment:BaseFragment(){

    private val titleList:ArrayList<String> = arrayListOf()
    private val subCodeList:MutableList<String> = mutableListOf()
    private val fragmentList: MutableList<Fragment> = mutableListOf()
    private var type = "3"

    override fun getLayoutRes(): Int  = R.layout.activity_paper

    override fun getBundle(bundle: Bundle?) {

    }

    override fun initViews(rootView: View) {

        titlebar.visibility = View.GONE
        imageview.visibility = View.VISIBLE

        val childrenBean = SerializableUtils.getSerializable(Constants.CHILD_USER_ENTITY) as MyChildrenBean
        ParentsAPI.subjectWorkList(type,childrenBean.classId,"",1,10,childrenBean.uid,object: RequestCallBack<List<SubjectBean>>(){

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
                exception.handleThrowable(activity)
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


    inner class PaperAdapter : FragmentPagerAdapter(childFragmentManager) {


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