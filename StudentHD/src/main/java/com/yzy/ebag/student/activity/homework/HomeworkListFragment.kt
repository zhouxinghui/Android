package com.yzy.ebag.student.activity.homework

import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AlertDialog
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
import ebag.hd.activity.ReportClassActivity
import ebag.hd.activity.ReportTestActivity
import ebag.hd.base.Constants
import ebag.hd.homework.DoHomeworkActivity
import java.util.*


/**
 * Created by caoyu on 2018/1/8.
 */
class HomeworkListFragment : BaseListFragment<List<SubjectBean>, SubjectBean.HomeWorkInfoBean>(){

    private lateinit var subCode: String
    private lateinit var type: String
    private lateinit var classId: String
    private var list: List<SubjectBean.HomeWorkInfoBean>? = null
    private val adapter = HomeWorkListAdapter()
    private var currentClickIndex = 0
    private val testDialog by lazy {
        AlertDialog.Builder(mContext).setCancelable(false)
                .setTitle("重要提示！！")
                .setMessage("你即将进行一场考试，在考试完成前（时间到达或完成全部题目）将不能退出！！")
                .setPositiveButton("现在开始"){ dialog, which ->
                    val bean = adapter.getItem(currentClickIndex)
                    DoHomeworkActivity.jump(mContext, bean?.id ?: "", type, null, bean?.endTime!!.toInt())
                }.setNegativeButton("稍后再做",null)
                .create()
    }
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
        return if(parent != null && parent.isNotEmpty()){
            parent[0].homeWorkInfoVos
        }else{
            null
        }
    }

    override fun getAdapter(): BaseQuickAdapter<SubjectBean.HomeWorkInfoBean,BaseViewHolder> {
        return adapter
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<SubjectBean.HomeWorkInfoBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        if((adapter as HomeWorkListAdapter).getItem(position)?.state == Constants.CORRECT_UNFINISH){// 未完成
            if (type == Constants.KSSJ_TYPE) {
                currentClickIndex = position
                testDialog.show()
            }else {
                DoHomeworkActivity.jump(mContext, adapter.getItem(position)?.id ?: "", type)
            }
        } else{
            when(type){
                com.yzy.ebag.student.base.Constants.STZY_TYPE -> {
                    ReportClassActivity.jump(mContext, adapter.getItem(position)?.id ?: "")
                }
                com.yzy.ebag.student.base.Constants.KHZY_TYPE,
                com.yzy.ebag.student.base.Constants.KSSJ_TYPE -> {
                    ReportTestActivity.jump(mContext, adapter.getItem(position)?.id ?: "")
                }
            }
        }
    }

    inner class HomeWorkListAdapter: BaseQuickAdapter<SubjectBean.HomeWorkInfoBean,BaseViewHolder>(R.layout.item_fragment_homework_list){

        override fun convert(helper: BaseViewHolder, item: SubjectBean.HomeWorkInfoBean?) {
//            val spannableString = SpannableString("完成： ${item?.questionComplete}/${item?.questionCount}")
//            spannableString.setSpan(ForegroundColorSpan(resources.getColor(R.color.color_homework_selected)), 4, 4 + "${entity.doneCount}".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            setter.setText(R.id.tvCount, spannableString)
            helper.setText(R.id.tvCount, Html.fromHtml("完成： <font color='#FF7800'>${item?.questionComplete}</font>/${item?.questionCount}"))
                    .setText(R.id.tvContent,"内容： ${item?.content ?: "无"}")
                    .setText(R.id.tvRequire,"要求： ${item?.remark ?: "无"}")
                    .setText(R.id.tvTime,if(type == Constants.KSSJ_TYPE) "考试时长：${item?.endTime ?: "00"}分钟" else "截止时间： ${item?.endTime ?: "无"}")
                    .setText(R.id.tvStatus,
                            when(item?.state){
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