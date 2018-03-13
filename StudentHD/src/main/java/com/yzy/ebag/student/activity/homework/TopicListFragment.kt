package com.yzy.ebag.student.activity.homework

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
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
import ebag.hd.homework.DoHomeworkActivity
import java.util.*

/**
 * @author caoyu
 * @date 2018/1/20
 * @description
 */
class TopicListFragment: BaseListFragment<List<ErrorTopicBean>, ErrorTopicBean.ErrorHomeWorkVosBean>() {

    private val REQUEST_CODE = 11
    private lateinit var subCode: String
    private lateinit var classId: String
    private var list: List<ErrorTopicBean.ErrorHomeWorkVosBean>? = null
    companion object {
        fun newInstance(classId: String, subCode: String, list: List<ErrorTopicBean.ErrorHomeWorkVosBean>?): TopicListFragment{
            val fragment = TopicListFragment()
            val bundle = Bundle()
            bundle.putString("subject",subCode)
            bundle.putString("classId",classId)
            if(list != null)
                bundle.putParcelableArrayList("list", list as ArrayList<out Parcelable>)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getPageSize(): Int {
        return 10
    }

    override fun getBundle(bundle: Bundle?) {
        subCode = bundle?.getString("subject") ?: ""
        classId = bundle?.getString("classId") ?: ""
        list = bundle?.getParcelableArrayList("list")
    }

    override fun loadConfig() {
        setPadding(0,
                resources.getDimensionPixelSize(R.dimen.x10), 0,0)

        if(list != null && list!!.isNotEmpty()){
            withFirstPageData(list,true)
        }
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

    override fun getLayoutManager(adapter: BaseQuickAdapter<ErrorTopicBean.ErrorHomeWorkVosBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        DoHomeworkActivity.jumpForResult(
                activity,
                (adapter as HomeWorkListAdapter).getItem(position)?.homeWorkId ?: "",
                Constants.ERROR_TOPIC_TYPE,
                REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE && resultCode == DoHomeworkActivity.RESULT_CODE){
            onRefresh()
        }
    }

    inner class HomeWorkListAdapter: BaseQuickAdapter<ErrorTopicBean.ErrorHomeWorkVosBean, BaseViewHolder>(R.layout.item_fragment_error_topic_list){

        override fun convert(helper: BaseViewHolder, item: ErrorTopicBean.ErrorHomeWorkVosBean?) {
//            val spannableString = SpannableString("完成： ${item?.questionComplete}/${item?.questionCount}")
//            spannableString.setSpan(ForegroundColorSpan(resources.getColor(R.color.color_homework_selected)), 4, 4 + "${entity.doneCount}".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            setter.setText(R.id.tvCount, spannableString)
            val time = DateUtil.getDateTime(item?.createDate ?: 0)
            helper.setText(R.id.tvCount, Html.fromHtml("错题： <font color='#FF7800'>${(item?.errorQuestionNumber ?: 0) - (item?.notRevisedQuestionNum ?: 0)}</font>/${item?.errorQuestionNumber}"))
                    .setText(R.id.tvContent,"内容： ${item?.content}")
                    .setText(R.id.tvTime,"作业提交时间： $time")
                    .setText(R.id.tvStatus,if(item?.notRevisedQuestionNum == 0) "已纠正" else "未纠正")
            helper.getView<View>(R.id.tvStatus).isSelected = item?.notRevisedQuestionNum == 0
        }
    }
}