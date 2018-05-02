package com.yzy.ebag.parents.ui.fragment

import android.os.Bundle
import android.text.Html
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.bean.MyChildrenBean
import com.yzy.ebag.parents.bean.SubjectBean
import com.yzy.ebag.parents.http.ParentsAPI
import ebag.core.base.BaseFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.SerializableUtils
import ebag.mobile.base.Constants
import kotlinx.android.synthetic.main.activity_paper.*

@SuppressWarnings("ValidFragment")
class PaperFragment(private val code:String):BaseFragment(){

    override fun getLayoutRes(): Int = R.layout.fragment_paper

    override fun getBundle(bundle: Bundle?) {

    }

    override fun initViews(rootView: View) {

        request()
        stateview.setOnRetryClickListener {
            request()
        }

    }

    private fun request() {
        ParentsAPI.subjectWorkList("4", (SerializableUtils.getSerializable(Constants.CHILD_USER_ENTITY) as MyChildrenBean).classId, code, 1, 10, object : RequestCallBack<List<SubjectBean>>() {

            override fun onStart() {
                stateview.showLoading()
            }

            override fun onSuccess(entity: List<SubjectBean>?) {
                if (entity!![0].homeWorkInfoVos.size == 0) {
                    stateview.showEmpty()
                } else {

                }
            }

            override fun onError(exception: Throwable) {
                exception.handleThrowable(activity)
                stateview.showError()
            }

        })
    }


    inner class HomeWorkListAdapter: BaseQuickAdapter<SubjectBean.HomeWorkInfoBean, BaseViewHolder>(R.layout.item_paper){

        override fun convert(helper: BaseViewHolder, item: SubjectBean.HomeWorkInfoBean?) {
            helper.setText(R.id.completeNum, Html.fromHtml("完成： <font color='#FF7800'>${item?.questionComplete}</font>/${item?.questionCount}"))
                    .setText(R.id.tvContent,"内容： ${item?.content ?: "无"}")
                    .setText(R.id.classNameTv,"要求： ${item?.remark ?: "无"}")
                    .setText(R.id.tvTime,"考试时长：${item?.endTime ?: "00"}分钟")
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