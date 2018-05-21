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
import ebag.core.util.SerializableUtils
import ebag.mobile.base.Constants
import ebag.mobile.bean.MyChildrenBean
import kotlinx.android.synthetic.main.activity_homeworkreport.*

class HomeworkReportActivity : BaseActivity(), HomeworkReportContract.HomeworkReportView {

    private val titleArray: Array<String> = arrayOf("作业简介", "完成情况")
    private val mFragmentList: ArrayList<Fragment> = arrayListOf()
    private lateinit var mPersenter: HomeworkReportContract.Presenter
    private lateinit var mHomeworkId: String
    private lateinit var mEndTime: String
    private lateinit var mType: String
    private lateinit var mState: String
    override fun getLayoutId(): Int = R.layout.activity_homeworkreport

    override fun initViews() {

        mHomeworkId = intent.getStringExtra("homeworkid")
        mEndTime = intent.getStringExtra("endTime")
        mType = intent.getStringExtra("type")
        mState = intent.getStringExtra("state")
        mPersenter = HomeworkReportPersenter(this)
        mPersenter.request(mHomeworkId, SerializableUtils.getSerializable<MyChildrenBean>(Constants.CHILD_USER_ENTITY).uid)
    }


    companion object {
        fun start(context: Context, homeworkid: String, endTime: String,type:String,state:String) {
            context.startActivity(Intent(context, HomeworkReportActivity::class.java).putExtra("homeworkid", homeworkid).putExtra("endTime", endTime).putExtra("type",type).putExtra("state",state))
        }
    }

    override fun showLoading() {
        stateview.showLoading()
    }

    override fun showEmpty() {

    }

    override fun <T> showContent(data: T?) {

        mFragmentList.add(HomeworkAbstractFragment(data as HomeworkAbstractBean, mEndTime,mHomeworkId))
        mFragmentList.add(HomeworkDoneFragment(data as HomeworkAbstractBean,mEndTime,mHomeworkId,mType,mState))
        tablayout.setViewPager(viewpager, titleArray, this, mFragmentList)
        stateview.showContent()
    }

    override fun showError(e: Throwable?) {
        stateview.showError()
        e!!.handleThrowable(this)
    }

}