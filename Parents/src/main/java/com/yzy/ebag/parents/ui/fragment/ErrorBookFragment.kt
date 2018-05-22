package com.yzy.ebag.parents.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.bean.ErrorTopicBean
import com.yzy.ebag.parents.http.ParentsAPI
import ebag.core.base.BaseFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.DateUtil
import ebag.core.util.SerializableUtils
import ebag.mobile.base.Constants
import ebag.mobile.bean.MyChildrenBean
import ebag.mobile.module.homework.HomeworkDescActivity
import kotlinx.android.synthetic.main.fragment_paper.*

@SuppressWarnings("ValidFragment")
class ErrorBookFragment(private val code: String) : BaseFragment() {

    override fun getLayoutRes(): Int = R.layout.fragment_paper
    private val datas: MutableList<ErrorTopicBean.ErrorHomeWorkVosBean> = mutableListOf()
    private lateinit var mAdapter: ErrorBookFragment.HomeWorkListAdapter
    private lateinit var childrenBean: MyChildrenBean
    private var page = 1
    override fun getBundle(bundle: Bundle?) {

    }

    override fun initViews(rootView: View) {

        childrenBean = SerializableUtils.getSerializable(Constants.CHILD_USER_ENTITY) as MyChildrenBean
        recyclerview.layoutManager = LinearLayoutManager(activity)
        mAdapter = HomeWorkListAdapter(datas)
        recyclerview.adapter = mAdapter
        request()
        stateview.setOnRetryClickListener {
            request()
        }

        mAdapter.setOnItemClickListener { adapter, view, position ->
            HomeworkDescActivity.jump(mContext, datas[position].homeWorkId, Constants.ERROR_TOPIC_TYPE, childrenBean.uid)
        }

        refresh.setOnRefreshListener {
            datas.clear()
            page = 1
            request()
        }

    }

    private fun request() {


        ParentsAPI.errorTopic(childrenBean.classId, code, page, 10, childrenBean.uid, object : RequestCallBack<ArrayList<ErrorTopicBean>>() {

            override fun onStart() {
                if (!refresh.isRefreshing)
                    stateview.showLoading()
            }

            override fun onSuccess(entity: ArrayList<ErrorTopicBean>?) {
                if (entity!![0].errorHomeWorkVos.size == 0) {
                    stateview.showEmpty()
                } else {
                    datas.addAll(entity[0].errorHomeWorkVos)
                    mAdapter.notifyDataSetChanged()
                    if (refresh.isRefreshing) {
                        refresh.isRefreshing = false
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


    inner class HomeWorkListAdapter(datas: List<ErrorTopicBean.ErrorHomeWorkVosBean>) : BaseQuickAdapter<ErrorTopicBean.ErrorHomeWorkVosBean, BaseViewHolder>(R.layout.item_fragment_error_topic_list, datas) {

        override fun convert(helper: BaseViewHolder, item: ErrorTopicBean.ErrorHomeWorkVosBean?) {

            val time = DateUtil.getDateTime(item?.createDate ?: 0)
            helper.setText(R.id.tvCount, Html.fromHtml("错题： <font color='#FF7800'>${(item?.errorQuestionNumber
                    ?: 0) - (item?.notRevisedQuestionNum
                    ?: 0)}</font>/${item?.errorQuestionNumber}"))
                    .setText(R.id.tvContent, "内容： ${item?.content}")
                    .setText(R.id.tvTime, "作业提交时间： $time")
                    .setText(R.id.tvStatus, if (item?.notRevisedQuestionNum == 0) "已纠正" else "未纠正")
            helper.getView<View>(R.id.tvStatus).isSelected = item?.notRevisedQuestionNum == 0
        }
    }
}