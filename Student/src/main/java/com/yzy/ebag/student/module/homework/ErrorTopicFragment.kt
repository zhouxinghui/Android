package com.yzy.ebag.student.module.homework

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.base.Constants
import com.yzy.ebag.student.bean.ErrorTopicBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.util.DateUtil

/**
 * Created by YZY on 2018/5/31.
 */
class ErrorTopicFragment: BaseListFragment<List<ErrorTopicBean>, ErrorTopicBean.ErrorHomeWorkVosBean>() {
    companion object {
        fun newInstance(classId: String, subCode: String): ErrorTopicFragment{
            val fragment = ErrorTopicFragment()
            val bundle = Bundle()
            bundle.putString("classId", classId)
            bundle.putString("subCode", subCode)
            fragment.arguments = bundle
            return fragment
        }
    }
    private lateinit var subCode: String
    private lateinit var classId: String
    override fun getBundle(bundle: Bundle?) {
        subCode = bundle?.getString("subject") ?: ""
        classId = bundle?.getString("classId") ?: ""
    }

    override fun loadConfig() {
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<ErrorTopicBean>>) {
        StudentApi.errorTopic(classId,subCode,page,getPageSize(),requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<ErrorTopicBean>?): List<ErrorTopicBean.ErrorHomeWorkVosBean>? {
        return if(parent != null && parent.isNotEmpty()){
            parent[0].errorHomeWorkVos
        }else{
            null
        }
    }

    override fun getAdapter(): BaseQuickAdapter<ErrorTopicBean.ErrorHomeWorkVosBean, BaseViewHolder> {
        return HomeWorkListAdapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<ErrorTopicBean.ErrorHomeWorkVosBean, BaseViewHolder>): RecyclerView.LayoutManager? = null

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        DoHomeworkActivity.jump(mContext, (adapter as HomeWorkListAdapter).getItem(position)?.homeWorkId ?: "", Constants.ERROR_TOPIC_TYPE)
    }

    inner class HomeWorkListAdapter: BaseQuickAdapter<ErrorTopicBean.ErrorHomeWorkVosBean, BaseViewHolder>(R.layout.item_fragment_error_topic_list){

        override fun convert(helper: BaseViewHolder, item: ErrorTopicBean.ErrorHomeWorkVosBean?) {
            val time = DateUtil.getDateTime(item?.createDate ?: 0)
            helper.setText(R.id.tvCount, Html.fromHtml("错题： <font color='#FF7800'>${(item?.errorQuestionNumber ?: 0) - (item?.notRevisedQuestionNum ?: 0)}</font>/${item?.errorQuestionNumber}"))
                    .setText(R.id.tvContent,"内容： ${item?.content}")
                    .setText(R.id.tvTime,"作业提交时间： $time")
                    .setText(R.id.tvStatus,if(item?.notRevisedQuestionNum == 0) "已纠正" else "未纠正")
            helper.getView<View>(R.id.tvStatus).isSelected = item?.notRevisedQuestionNum == 0
        }
    }
}