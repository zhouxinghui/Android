package com.yzy.ebag.student.module.homework

import android.content.Context
import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.yzy.ebag.student.R
import com.yzy.ebag.student.base.Constants
import com.yzy.ebag.student.bean.SubjectBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.BaseActivity
import ebag.core.http.network.MsgException
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import kotlinx.android.synthetic.main.activity_homework.*

/**
 * Created by YZY on 2018/5/16.
 */
class HomeworkActivity: BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_homework

    companion object {
        fun jump(content: Context, type: String, classId: String){
            content.startActivity(
                    Intent(content, HomeworkActivity::class.java)
                            .putExtra("type", type)
                            .putExtra("classId",classId)
            )
        }
    }
    private var type = "1"
    private var classId = ""
    private val requestCallBack = object : RequestCallBack<ArrayList<SubjectBean>>(){
        override fun onStart() {
            stateView.showLoading()
        }

        override fun onSuccess(entity: ArrayList<SubjectBean>?) {
            if (entity == null || entity.isEmpty()){
                stateView.showEmpty()
            }else{
                viewPager.adapter = ViewPagerAdapter(arrayOfNulls(entity.size), entity)
                tabLayout.setupWithViewPager(viewPager)
                if (entity.size > 4)
                    tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
                else
                    tabLayout.tabMode = TabLayout.MODE_FIXED
                stateView.showContent()
            }
        }

        override fun onError(exception: Throwable) {
            if (exception is MsgException){
                stateView.showEmpty(exception.message.toString())
            }else{
                stateView.showError()
                exception.handleThrowable(this@HomeworkActivity)
            }
        }
    }
    override fun initViews() {
        type = intent.getStringExtra("type") ?: ""
        classId = intent.getStringExtra("classId") ?: ""

        when(type){
            Constants.KHZY_TYPE -> {
                titleBar.setTitle("课后作业")
            }

            Constants.STZY_TYPE -> {
                titleBar.setTitle("随堂作业")
            }

            else -> {
                titleBar.setTitle("考试试卷")
            }
        }

        StudentApi.subjectWorkList(type, classId, "", 1, 1, requestCallBack)
    }

    private inner class ViewPagerAdapter(
            private val fragments: Array<HomeworkListFragment?>, private val entity: ArrayList<SubjectBean>?): FragmentPagerAdapter(supportFragmentManager){
        override fun getItem(position: Int): Fragment? {
            if (fragments[position] == null){
                fragments[position] = HomeworkListFragment.newInstance(type, classId, entity?.get(position)?.subCode ?: "")
            }
            return fragments[position]
        }

        override fun getCount(): Int = fragments.size

        override fun getPageTitle(position: Int): CharSequence? = entity?.get(position)?.subject
    }
}