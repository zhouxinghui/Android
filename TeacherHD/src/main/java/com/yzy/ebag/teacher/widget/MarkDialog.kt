package com.yzy.ebag.teacher.widget

import android.content.Context
import android.view.View
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.CorrectAnswerBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.base.BaseDialog
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.DateUtil
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.StringUtils
import ebag.core.util.T
import kotlinx.android.synthetic.main.dialog_mark.*

/**
 * Created by YZY on 2018/2/27.
 */
class MarkDialog(context: Context): BaseDialog(context) {
    override fun getLayoutRes(): Int {
        return R.layout.dialog_mark
    }
    private var homeworkId = ""
    private var questionId = ""
    private var uid = ""
    var onMarkSuccess: (() -> Unit)? = null
    private var bean: CorrectAnswerBean? = null
    private var score = ""
    private val markRequest = object : RequestCallBack<String>(){
        override fun onStart() {
            LoadingDialogUtil.showLoading(context, "上传分数...")
            dismiss()
        }

        override fun onSuccess(entity: String?) {
            LoadingDialogUtil.closeLoadingDialog()
            bean?.questionScore = score
            bean?.homeWorkState = "5"
            onMarkSuccess?.invoke()
        }

        override fun onError(exception: Throwable) {
            LoadingDialogUtil.closeLoadingDialog()
            exception.handleThrowable(context)
        }
    }
    init {
        confirmBtn.setOnClickListener {
            score = markEdit.text.toString()
            if (StringUtils.isEmpty(score)){
                T.show(context, "请输入分数")
                return@setOnClickListener
            }
            if (score.toInt() > 100){
                T.show(context, "分数范围：0-100")
                return@setOnClickListener
            }
            TeacherApi.markScore(homeworkId, uid, questionId, score, markRequest)
        }
    }

    fun show(bean: CorrectAnswerBean?, homeworkId: String, questionId: String) {
        this.bean = bean
        this.homeworkId = homeworkId
        this.questionId = questionId
        markEdit.setText("")
        uid = bean?.uid ?: ""
        nameAndBagId.text = "${bean?.studentName}    书包号：${bean?.ysbCode}"
        val answer = bean?.studentAnswer
        if (StringUtils.isEmpty(answer)){
            commitTime.visibility = View.GONE
            studentAnswer.text = "未完成"
        }else{
            commitTime.visibility = View.VISIBLE
            commitTime.text = "提交时间：${DateUtil.getDateTime(bean?.endTime ?: 0, "yyyy-MM-dd HH:mm")}"
            studentAnswer.text = bean?.studentAnswer?.replace("#R#", "、")
        }
        super.show()
    }
}