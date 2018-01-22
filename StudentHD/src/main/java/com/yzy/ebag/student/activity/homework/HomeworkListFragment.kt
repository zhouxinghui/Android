package com.yzy.ebag.student.activity.homework

import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.SubjectBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.util.DateUtil
import java.util.*


/**
 * Created by caoyu on 2018/1/8.
 */
class HomeworkListFragment : BaseListFragment<List<SubjectBean>, SubjectBean.HomeWorkInfoBean>(){

    private lateinit var subCode: String
    private lateinit var type: String
    private lateinit var classId: String
    private var list: List<SubjectBean.HomeWorkInfoBean>? = null
    companion object {
        fun newInstance(type: String, classId: String, subCode: String, list: List<SubjectBean.HomeWorkInfoBean>?): HomeworkListFragment{
            val fragment = HomeworkListFragment()
            val bundle = Bundle()
            bundle.putString("subject",subCode)
            bundle.putString("type",type)
            bundle.putString("classId",classId)
            if(list != null)
                bundle.putParcelableArrayList("list", list as ArrayList<out Parcelable>)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getPageSize(): Int {
        return 1
    }

    override fun getBundle(bundle: Bundle?) {
        subCode = bundle?.getString("subject") ?: ""
        type = bundle?.getString("type") ?: ""
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
    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<SubjectBean>>) {
        StudentApi.subjectWorkList(type,classId,subCode,page,getPageSize(),requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<SubjectBean>?): List<SubjectBean.HomeWorkInfoBean>? {
        return parent?.get(0)?.homeWorkInfoVos
    }

    override fun getAdapter(): BaseQuickAdapter<SubjectBean.HomeWorkInfoBean,BaseViewHolder> {
        return HomeWorkListAdapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<SubjectBean.HomeWorkInfoBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    inner class HomeWorkListAdapter: BaseQuickAdapter<SubjectBean.HomeWorkInfoBean,BaseViewHolder>(R.layout.item_fragment_homework_list){

        override fun convert(helper: BaseViewHolder, item: SubjectBean.HomeWorkInfoBean?) {
//            val spannableString = SpannableString("完成： ${item?.questionComplete}/${item?.questionCount}")
//            spannableString.setSpan(ForegroundColorSpan(resources.getColor(R.color.color_homework_selected)), 4, 4 + "${entity.doneCount}".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            setter.setText(R.id.tvCount, spannableString)
            val time = DateUtil.getDateTime(item?.endTime ?: 0)
            helper.setText(R.id.tvCount, Html.fromHtml("完成： <font color='#FF7800'>${item?.questionComplete}</font>/${item?.questionCount}"))
                    .setText(R.id.tvContent,"内容： ${item?.content}")
                    .setText(R.id.tvRequire,"要求： ${item?.remark}")
                    .setText(R.id.tvTime,"截止时间： $time")
                    .setText(R.id.tvStatus,if(item?.questionComplete == item?.questionCount) "已完成" else "未完成")
            helper.getView<View>(R.id.tvStatus).isSelected = item?.questionComplete == item?.questionCount
        }
    }
}