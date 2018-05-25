package com.yzy.ebag.teacher.activity.correcting

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.CommentBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.core.util.loadHead
import ebag.hd.activity.ReportClassActivity
import ebag.hd.activity.ReportTestActivity
import ebag.hd.base.BaseListActivity
import ebag.hd.base.Constants
import ebag.hd.http.EBagApi
import ebag.hd.widget.DialogOfferPresent

/**
 * Created by YZY on 2018/3/1.
 */
class CommentActivity : BaseListActivity<List<CommentBean>, CommentBean>() {
    companion object {
        fun jump(context: Context, homeworkId: String, type: String) {
            context.startActivity(
                    Intent(context, CommentActivity::class.java)
                            .putExtra("homeworkId", homeworkId)
                            .putExtra("type", type)
            )
        }
    }

    private var homeworkId = ""
    private var type = ""
    private val adapter = MyAdapter()
    private var currentPosition = 0
    private val uploadRequest = object : RequestCallBack<String>() {
        override fun onStart() {
            LoadingDialogUtil.showLoading(this@CommentActivity, "上传评语中...")
        }

        override fun onSuccess(entity: String?) {
            LoadingDialogUtil.closeLoadingDialog()
            adapter.data[currentPosition].correctState = Constants.CORRECT_TEACHER_REMARKED
            adapter.notifyDataSetChanged()
            T.show(this@CommentActivity, "提交评语成功")
        }

        override fun onError(exception: Throwable) {
            LoadingDialogUtil.closeLoadingDialog()
            exception.handleThrowable(this@CommentActivity)
        }
    }

    override fun loadConfig(intent: Intent) {
        homeworkId = intent.getStringExtra("homeworkId")
        type = intent.getStringExtra("type")
    }

    override fun getPageSize(): Int {
        return 9
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<CommentBean>>) {
        TeacherApi.commentList(homeworkId, page, getPageSize(), requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<CommentBean>?): List<CommentBean>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<CommentBean, BaseViewHolder> {
        return adapter
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<CommentBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return GridLayoutManager(this, 3)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        currentPosition = position
        val commentBean = adapter.data[position] as CommentBean
        when (view.id) {
            R.id.checkReportBtn -> {
                if (type == Constants.KSSJ_TYPE || type == Constants.KHZY_TYPE)
                    ReportTestActivity.jump(this, homeworkId, type, commentBean.uid, commentBean.studentName)
                else
                    ReportClassActivity.jump(this, homeworkId, type, commentBean.uid, commentBean.studentName)
            }
            R.id.commitCommentBtn -> {
                val commentEdit = view.tag as EditText
                val commentStr = commentEdit.text.toString()
                if (StringUtils.isEmpty(commentStr)) {
                    T.show(this, "评语不能为空")
                    return
                }
                TeacherApi.uploadComment(homeworkId, commentBean.uid, commentStr, uploadRequest)
                commentBean.comment = commentStr
            }

            R.id.give_gift -> {
                val dialog = DialogOfferPresent(this@CommentActivity, 1, homeworkId)
                dialog.setOnOfferSuccessListener { bean ->
                    if (bean.giftVos.isNotEmpty()) {
                        EBagApi.giveYsbMoneyGifg2User(bean, object : RequestCallBack<String>() {
                            override fun onSuccess(entity: String?) {
                                T.show(this@CommentActivity, "赠送成功")
                            }

                            override fun onError(exception: Throwable) {
                                exception.handleThrowable(this@CommentActivity)
                            }

                        }, commentBean.uid)
                    } else {
                        T.show(this@CommentActivity, "未选择礼物")
                    }

                }
                dialog.show()
            }
        }
    }

    inner class MyAdapter : BaseQuickAdapter<CommentBean, BaseViewHolder>(R.layout.item_comment) {
        override fun convert(helper: BaseViewHolder, item: CommentBean) {

            helper.getView<ImageView>(R.id.headImg).loadHead(item.headUrl)
            helper.setText(R.id.studentName, item.studentName)
                    .setText(R.id.scoreTv, if (StringUtils.isEmpty(item.totalScore)) "" else item.totalScore)
            val commentEdit = helper.getView<EditText>(R.id.commentEdit)
            val checkReportBtn = helper.getView<TextView>(R.id.checkReportBtn)
            val commitCommentBtn = helper.getView<TextView>(R.id.commitCommentBtn)
            if (!StringUtils.isEmpty(item.comment)) {
                commentEdit.setText(item.comment)
            } else {
                commentEdit.setText("")
            }
            helper.addOnClickListener(R.id.give_gift)
            helper.addOnClickListener(R.id.checkReportBtn)
            helper.addOnClickListener(R.id.commitCommentBtn)
            commitCommentBtn.tag = commentEdit
            when (item.correctState) {
                Constants.CORRECT_CORRECTED -> { //已批改
                    commentEdit.isEnabled = true
                    checkReportBtn.isEnabled = true
                    commitCommentBtn.isEnabled = true
                    checkReportBtn.text = "查看作业"
                    commitCommentBtn.text = "提交评语"
                }
                Constants.CORRECT_TEACHER_REMARKED -> { //已评价
                    commentEdit.isEnabled = false
                    checkReportBtn.isEnabled = true
                    commitCommentBtn.isEnabled = false
                    checkReportBtn.text = "查看作业"
                    commitCommentBtn.text = "已评价"
                }
                Constants.CORRECT_UNCORRECT -> { //未批改
                    commentEdit.isEnabled = false
                    checkReportBtn.isEnabled = false
                    commitCommentBtn.isEnabled = false
                    checkReportBtn.text = "未批改"
                    commitCommentBtn.text = "提交评语"
                }
                Constants.CORRECT_UNFINISH -> { //未完成
                    commentEdit.isEnabled = false
                    checkReportBtn.isEnabled = false
                    commitCommentBtn.isEnabled = false
                    checkReportBtn.text = "未完成"
                    commitCommentBtn.text = "提交评语"
                }
            }

            if (type == "1") {
                helper.setGone(R.id.commentEdit, false)
                helper.setGone(R.id.commitCommentBtn, false)
            }
        }
    }
}