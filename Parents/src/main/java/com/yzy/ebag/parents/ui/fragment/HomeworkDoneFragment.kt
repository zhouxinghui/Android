package com.yzy.ebag.parents.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.bean.GiftListBean
import com.yzy.ebag.parents.bean.HomeworkAbstractBean
import com.yzy.ebag.parents.http.ParentsAPI
import com.yzy.ebag.parents.ui.widget.DialogOfferPresent
import ebag.core.base.BaseFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.SPUtils
import ebag.core.util.SerializableUtils
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.mobile.base.Constants
import ebag.mobile.bean.MyChildrenBean
import ebag.mobile.bean.UserEntity
import ebag.mobile.module.homework.HomeworkDescActivity
import kotlinx.android.synthetic.main.fragment_homework_done.*
import java.text.DecimalFormat

@SuppressLint("ValidFragment")
class HomeworkDoneFragment(private val data: HomeworkAbstractBean, private val endTime: String, private val homeworkId: String, private val mType: String) : BaseFragment() {

    override fun getLayoutRes(): Int = R.layout.fragment_homework_done

    override fun getBundle(bundle: Bundle?) {

    }

    override fun initViews(rootView: View) {

        homework_sum.text = setSpan("${data.totalScore}分")
        homework_top.text = setSpan("${data.maxScore}分")
        homework_error.text = setSpan(DecimalFormat("0").format(data.errorNum) + "道")
        val time = endTime.split(" ")[0].split("-")

        var num = 0
        data.homeWorkRepDetailVos.forEach {
            num += it.questionNum
        }

        try {
            homework_error_content.text = "${time[1]}月${time[2]}日 本次作业共${num}道题,做错${data.errorNum}道"
        } catch (e: Exception) {
            homework_error_content.text = "本次作业共${num}道题,做错${data.errorNum}道"
        }

        if (data.errorNum > 0)
            error_btn.setOnClickListener {
                HomeworkDescActivity.jump(
                        mContext,
                        homeworkId,
                        Constants.ERROR_TOPIC_TYPE,
                        (SerializableUtils.getSerializable(Constants.CHILD_USER_ENTITY) as MyChildrenBean).uid)
            }

        if (data.teacherComment.isNullOrEmpty()) {
            homework_teacher_layout.visibility = View.GONE
        } else {
            homework_teacher_layout.visibility = View.VISIBLE
            homework_teacher_comment.text = data.teacherComment

        }

        if (data.parentComment.isNullOrEmpty()) {
            homework_parents_edit_layout.visibility = View.VISIBLE
            homework_parents_comment.visibility = View.GONE
        } else {
            homework_parents_edit_layout.visibility = View.GONE
            homework_parents_comment.visibility = View.VISIBLE
            homework_parents_comment.text = data.parentComment
        }

        homework_parents_name.text = "${SerializableUtils.getSerializable<UserEntity>(Constants.PARENTS_USER_ENTITY).name}家长"


        homework_parents_comment_comfirm.setOnClickListener {
            if (StringUtils.isEmpty(homework_parents_edit.text.toString())) {
                T.show(activity, "请先输入评语")
            } else {
                ParentsAPI.parentComment(SPUtils.get(activity, com.yzy.ebag.parents.common.Constants.CURRENT_CHILDREN_YSBCODE, "") as String, homeworkId, homework_parents_edit.text.toString(), true, object : RequestCallBack<String>() {
                    override fun onSuccess(entity: String?) {
                        T.show(activity, "评论成功")
                        homework_parents_edit_layout.visibility = View.GONE
                        homework_parents_comment.visibility = View.VISIBLE
                        homework_parents_comment.text = homework_parents_edit.text.toString()
                    }

                    override fun onError(exception: Throwable) {
                        T.show(activity, "评论失败")
                    }

                })
            }
        }


        error_preview.setOnClickListener {
            HomeworkDescActivity.jump(activity, homeworkId, mType, (SerializableUtils.getSerializable(Constants.CHILD_USER_ENTITY) as MyChildrenBean).uid)
        }

        homework_gift.setOnClickListener {

            val dialog = DialogOfferPresent(activity, 1, homeworkId)
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

                    }, SerializableUtils.getSerializable<MyChildrenBean>(Constants.CHILD_USER_ENTITY).uid)
                } else {
                    T.show(activity, "未选择礼物")
                }
            }
            dialog.show(1)
        }

        queryGiftList()

    }

    private fun setSpan(text: String): SpannableStringBuilder {
        val ssb = SpannableStringBuilder()
        ssb.append(text)
        ssb.setSpan(AbsoluteSizeSpan(35), text.length - 1, text.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        if (text.contains("道"))
            ssb.setSpan(ForegroundColorSpan(Color.parseColor("#ff7500")), 0, text.length - 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        return ssb
    }

    private fun queryGiftList() {
        ParentsAPI.getGiftDetail(homeworkId, object : RequestCallBack<List<GiftListBean>>() {
            override fun onSuccess(entity: List<GiftListBean>?) {

                if (entity!!.isNotEmpty()) {
                    val stringb = StringBuilder()
                    stringb.append("已赠送:")
                    entity?.forEach {
                        stringb.append("${it.giftName}*${it.giftNum};")
                    }
                    gift_record.text = stringb.toString()
                }
            }

            override fun onError(exception: Throwable) {

            }

        }, SerializableUtils.getSerializable<MyChildrenBean>(Constants.CHILD_USER_ENTITY).uid)
    }

}