package com.yzy.ebag.parents.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.bean.HomeworkAbstractBean
import com.yzy.ebag.parents.mvp.HomeworkReportContract
import com.yzy.ebag.parents.mvp.presenter.HomeworkReportPersenter
import com.yzy.ebag.parents.ui.fragment.HomeworkAbstractFragment
import com.yzy.ebag.parents.ui.fragment.HomeworkDoneFragment
import ebag.core.base.BaseActivity
import ebag.core.http.network.handleThrowable
import ebag.core.util.SPUtils
import kotlinx.android.synthetic.main.activity_homeworkreport.*

class HomeworkReportActivity : BaseActivity(), HomeworkReportContract.HomeworkReportView {

    private val titleArray: Array<String> = arrayOf("作业简介", "完成情况")
    private val mFragmentList: ArrayList<Fragment> = arrayListOf()
    private lateinit var mPersenter: HomeworkReportContract.Persenter
    private lateinit var mHomeworkId: String
    private lateinit var mEndTime: String
    private lateinit var mSubject: String
    override fun getLayoutId(): Int = R.layout.activity_homeworkreport

    override fun initViews() {

        mHomeworkId = intent.getStringExtra("homeworkid")
        mEndTime = intent.getStringExtra("endTime")
        mSubject = intent.getStringExtra("subject")
        mPersenter = HomeworkReportPersenter(this)
        mPersenter.request(mHomeworkId, SPUtils.get(this, com.yzy.ebag.parents.common.Constants.CURRENT_CHILDREN_YSBCODE, "") as String)
    }


    companion object {
        fun start(context: Context, homeworkid: String, endTime: String,subject:String) {
            context.startActivity(Intent(context, HomeworkReportActivity::class.java).putExtra("homeworkid", homeworkid).putExtra("endTime", endTime).putExtra("subject",subject))
        }
    }

    override fun showLoading() {
        stateview.showLoading()
    }

    override fun showEmpty() {

    }

    override fun <T> showContent(data: T?) {

        mFragmentList.add(HomeworkAbstractFragment(data as HomeworkAbstractBean, mEndTime))
        mFragmentList.add(HomeworkDoneFragment(data as HomeworkAbstractBean,mEndTime,mSubject))
        tablayout.setViewPager(viewpager, titleArray, this, mFragmentList)
        stateview.showContent()
    }

    override fun showError(e: Throwable?) {
        stateview.showError()
        e!!.handleThrowable(this)
    }

}