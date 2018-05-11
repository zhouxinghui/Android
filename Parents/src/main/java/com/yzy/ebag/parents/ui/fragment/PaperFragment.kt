package com.yzy.ebag.parents.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.bean.OnePageInfoBean
import com.yzy.ebag.parents.bean.SubjectBean
import com.yzy.ebag.parents.http.ParentsAPI
import com.yzy.ebag.parents.ui.activity.HomeworkReportActivity
import com.yzy.ebag.parents.ui.adapter.HomeworkListAdapter
import ebag.core.base.BaseFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.SerializableUtils
import ebag.mobile.base.Constants
import ebag.mobile.bean.MyChildrenBean
import ebag.mobile.module.homework.HomeworkDescActivity
import kotlinx.android.synthetic.main.fragment_paper.*

@SuppressWarnings("ValidFragment")
class PaperFragment(private val code: String, private val type: String) : BaseFragment() {

    override fun getLayoutRes(): Int = R.layout.fragment_paper
    private val datas: MutableList<SubjectBean.HomeWorkInfoBean> = mutableListOf()
    private val homeworkdatas: MutableList<OnePageInfoBean.HomeWorkInfoVosBean> = mutableListOf()
    private lateinit var mAdapter: HomeWorkListAdapter
    private lateinit var homeWorkAdapter: HomeworkListAdapter
    private lateinit var childrenBean: MyChildrenBean
    private var page = 1
    override fun getBundle(bundle: Bundle?) {

    }

    override fun initViews(rootView: View) {

        childrenBean = SerializableUtils.getSerializable(Constants.CHILD_USER_ENTITY) as MyChildrenBean
        recyclerview.layoutManager = LinearLayoutManager(activity)
        mAdapter = HomeWorkListAdapter(datas)
        homeWorkAdapter = HomeworkListAdapter(homeworkdatas)
        if (type == "2") {
            recyclerview.adapter = homeWorkAdapter

            homeWorkAdapter.setOnItemChildClickListener { adapter, view, position ->

                if ((adapter.getItem(position) as OnePageInfoBean.HomeWorkInfoVosBean).state == "0") {
                    HomeworkDescActivity.jump(activity, homeworkdatas[position].id, "2", (SerializableUtils.getSerializable(Constants.CHILD_USER_ENTITY) as MyChildrenBean).uid)
                } else {
                    HomeworkReportActivity.start(activity, homeworkdatas[position].id, homeworkdatas[position].endTime)
                }
            }

        } else {
            recyclerview.adapter = mAdapter
            mAdapter.setOnItemClickListener { adapter, view, position ->

                HomeworkDescActivity.jump(activity, datas[position].id, "4", childrenBean.uid)
            }
        }

        request()

        stateview.setOnRetryClickListener {
            request()
        }

        mAdapter.setOnLoadMoreListener({ loadmore() }, recyclerview)
        homeWorkAdapter.setOnLoadMoreListener({ loadmore() }, recyclerview)
    }

    private fun request() {

        ParentsAPI.subjectWorkList(type, childrenBean.classId, code, page, 10, childrenBean.uid, object : RequestCallBack<List<SubjectBean>>() {

            override fun onStart() {
                stateview.showLoading()
            }

            override fun onSuccess(entity: List<SubjectBean>?) {
                if (entity!![0].homeWorkInfoVos.size == 0) {
                    stateview.showEmpty()
                } else {
                    if (type == "2") {
                        entity[0].homeWorkInfoVos.forEach {
                            val bean = OnePageInfoBean.HomeWorkInfoVosBean()
                            bean.state = it.state
                            bean.content = it.content
                            bean.endTime = it.endTime
                            bean.id = it.id
                            bean.questionComplete = it.questionComplete
                            bean.questionCount = it.questionCount
                            bean.remark = it.remark
                            homeworkdatas.add(bean)
                        }
                        homeWorkAdapter.notifyDataSetChanged()
                    } else {
                        datas.addAll(entity[0].homeWorkInfoVos)
                        mAdapter.notifyDataSetChanged()
                    }
                    page++
                    stateview.showContent()
                }
            }

            override fun onError(exception: Throwable) {
                exception.handleThrowable(activity)
                stateview.showError()
            }

        })
    }


    private fun loadmore() {
        ParentsAPI.subjectWorkList(type, childrenBean.classId, code, page, 10, childrenBean.uid, object : RequestCallBack<List<SubjectBean>>() {

            override fun onSuccess(entity: List<SubjectBean>?) {
                if (entity!![0].homeWorkInfoVos.size == 0) {
                    if (type == "2") {
                        homeWorkAdapter.loadMoreEnd()
                    } else {
                        mAdapter.loadMoreEnd()
                    }
                } else {
                    if (type == "2") {
                        entity[0].homeWorkInfoVos.forEach {
                            val bean = OnePageInfoBean.HomeWorkInfoVosBean()
                            bean.state = it.state
                            bean.content = it.content
                            bean.endTime = it.endTime
                            bean.id = it.id
                            bean.questionComplete = it.questionComplete
                            bean.questionCount = it.questionCount
                            bean.remark = it.remark
                            homeworkdatas.add(bean)
                        }
                        homeWorkAdapter.loadMoreComplete()
                    } else {
                        datas.addAll(entity[0].homeWorkInfoVos)
                        mAdapter.loadMoreComplete()
                    }

                }
            }

            override fun onError(exception: Throwable) {
                if (type == "2") homeWorkAdapter.loadMoreFail() else mAdapter.loadMoreFail()
            }

        })
    }


    inner class HomeWorkListAdapter(datas: MutableList<SubjectBean.HomeWorkInfoBean>) : BaseQuickAdapter<SubjectBean.HomeWorkInfoBean, BaseViewHolder>(R.layout.item_paper, datas) {

        override fun convert(helper: BaseViewHolder, item: SubjectBean.HomeWorkInfoBean?) {
            helper.setText(R.id.completeNum, Html.fromHtml("完成： <font color='#FF7800'>${item?.questionComplete}</font>/${item?.questionCount}"))
                    .setText(R.id.tvContent, "内容： ${item?.content ?: "无"}")
                    .setText(R.id.classNameTv, "要求： ${item?.remark ?: "无"}")
                    .setText(R.id.tvTime, "考试时长：${item?.endTime ?: "00"}分钟")
                    .setText(R.id.tvStatus,
                            when (item?.state) {
                                Constants.CORRECT_UNFINISH -> "未完成"
                                Constants.CORRECT_UNCORRECT -> "未批改"
                                Constants.CORRECT_CORRECTED -> "已批改"
                                Constants.CORRECT_TEACHER_REMARKED -> "老师评语完成"
                                Constants.CORRECT_PARENT_REMARKED -> "家长签名和评语完成"
                                else -> "未完成"
                            }
                    )
            helper.getView<View>(R.id.tvStatus).isSelected = item?.state == Constants.CORRECT_UNFINISH
        }
    }

}