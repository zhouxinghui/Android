package com.yzy.ebag.parents.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.bean.GiftListBean
import com.yzy.ebag.parents.bean.HomeworkAbstractBean
import com.yzy.ebag.parents.http.ParentsAPI
import com.yzy.ebag.parents.mvp.model.HomeworkAbsModel
import com.yzy.ebag.parents.ui.adapter.HomeworkAbstractAdapter
import com.yzy.ebag.parents.ui.widget.DialogOfferPresent
import ebag.core.base.BaseFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.T
import kotlinx.android.synthetic.main.fragment_homework_abstract.*

@SuppressLint("ValidFragment")
class HomeworkAbstractFragment(private val bean: HomeworkAbstractBean, private val endTime: String, private val homeworkId: String) : BaseFragment() {

    private val labelArray: Array<String> = arrayOf("截止时间:", "作业内容:", "作业要求:", "练习内容:")
    private lateinit var adapter: HomeworkAbstractAdapter
    override fun getLayoutRes(): Int = R.layout.fragment_homework_abstract
    private val dataList: MutableList<HomeworkAbsModel> = mutableListOf()
    override fun getBundle(bundle: Bundle?) {

    }

    override fun initViews(rootView: View) {

        val sb = StringBuilder()
        sb.append("\n")
        bean.homeWorkRepDetailVos.forEach { homeWorkRepDetailVosBean ->
            sb.append(homeWorkRepDetailVosBean.questionTypeName)
            sb.append("共(${homeWorkRepDetailVosBean.questionNum})道\n")

        }

        labelArray.forEachIndexed { index, s ->
            when (index) {
                0 -> dataList.add(HomeworkAbsModel(s, endTime))
                1 -> dataList.add(HomeworkAbsModel(s, bean.content ?: ""))
                2 -> dataList.add(HomeworkAbsModel(s, bean.remark ?: ""))
                3 -> dataList.add(HomeworkAbsModel(s, sb.toString()))
            }
        }

        recyclerview.layoutManager = LinearLayoutManager(activity)
        adapter = HomeworkAbstractAdapter(dataList)
        recyclerview.adapter = adapter

        homework_gift.setOnClickListener {

            val dialog = DialogOfferPresent(activity, 0, homeworkId)
            dialog.setOnOfferSuccessListener { bean ->
                if (bean.giftVos.isNotEmpty()) {
                    ParentsAPI.giveYsbMoneyGifg2User(bean, object : RequestCallBack<String>() {
                        override fun onSuccess(entity: String?) {
                            T.show(activity, "赠送成功")
                            queryGiftList()
                        }

                        override fun onError(exception: Throwable) {
                            exception.handleThrowable(activity)
                        }

                    })
                }else{
                    T.show(activity,"未选择礼物")
                }
            }
            dialog.show()

        }


        queryGiftList()

    }

    private fun queryGiftList() {
        ParentsAPI.getGiftDetail(homeworkId, object : RequestCallBack<List<GiftListBean>>() {
            override fun onSuccess(entity: List<GiftListBean>?) {
                if (entity!!.isNotEmpty()) {
                    val stringb = StringBuilder()
                    stringb.append("已赠送:")
                    entity.forEach {
                        stringb.append("${it.giftName}*${it.giftNum};")
                    }
                    gift_record.text = stringb.toString()
                }
            }

            override fun onError(exception: Throwable) {

            }

        })
    }
}