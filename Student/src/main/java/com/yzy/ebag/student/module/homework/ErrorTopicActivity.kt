package com.yzy.ebag.student.module.homework

import android.content.Context
import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.ErrorTopicBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.BaseActivity
import ebag.core.http.network.MsgException
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import kotlinx.android.synthetic.main.activity_homework.*

/**
 * Created by YZY on 2018/5/31.
 */
class ErrorTopicActivity: BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_homework
    companion object {
        fun jump(content: Context, classId: String){
            content.startActivity(
                    Intent(content, ErrorTopicActivity::class.java)
                            .putExtra("classId",classId)
            )
        }
    }
    private var classId = ""
    private val requestCallBack = object : RequestCallBack<ArrayList<ErrorTopicBean>>(){
        override fun onStart() {
            stateView.showLoading()
        }

        override fun onSuccess(entity: ArrayList<ErrorTopicBean>?) {
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
                exception.handleThrowable(this@ErrorTopicActivity)
            }
        }
    }
    override fun initViews() {
        classId = intent.getStringExtra("classId") ?: ""
        titleBar.setTitle("我的错题")
        StudentApi.errorTopic(classId, "", 1, 1, requestCallBack)
        stateView.setOnRetryClickListener {
            StudentApi.errorTopic(classId, "", 1, 1, requestCallBack)
        }
    }

    private inner class ViewPagerAdapter(
            private val fragments: Array<ErrorTopicFragment?>, private val entity: ArrayList<ErrorTopicBean>?): FragmentPagerAdapter(supportFragmentManager){
        override fun getItem(position: Int): Fragment? {
            if (fragments[position] == null){
                fragments[position] = ErrorTopicFragment.newInstance(classId, entity?.get(position)?.subCode ?: "")
            }
            return fragments[position]
        }

        override fun getCount(): Int = fragments.size

        override fun getPageTitle(position: Int): CharSequence? = entity?.get(position)?.subject
    }
}