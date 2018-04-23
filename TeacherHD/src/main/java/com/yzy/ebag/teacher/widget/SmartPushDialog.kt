package com.yzy.ebag.teacher.widget

import android.content.Context
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.CorrectAnswerBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.base.BaseDialog
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.StringUtils
import ebag.core.util.T
import kotlinx.android.synthetic.main.dialog_smart_push.*

/**
 * Created by YZY on 2018/2/24.
 */
class SmartPushDialog(context: Context): BaseDialog(context) {
    override fun getLayoutRes(): Int {
        return R.layout.dialog_smart_push
    }
    override fun setWidth(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.x600)
    }
    override fun setHeight(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.y400)
    }

    private var isSmartPush = true
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
    var onConfirmClickListener: ((count: Int) -> Unit)? = null
    init {
        title_tv.text = "智能推送"
        confirmBtn.setOnClickListener {
            if (isSmartPush) {
                val count = countEdit.text.toString()
                if (StringUtils.isEmpty(count)) {
                    T.show(context, "请输入题目数量")
                    return@setOnClickListener
                }
                if (count.toInt() < 10) {
                    T.show(context, "题目数量范围为：10-100")
                    return@setOnClickListener
                }
                if (count.toInt() > 100) {
                    T.show(context, "题目数量范围为：10-100")
                    return@setOnClickListener
                }
                onConfirmClickListener?.invoke(count.toInt())
            }else{
                score = countEdit.text.toString()
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
    }

    fun show(bean: CorrectAnswerBean?, homeworkId: String, questionId: String) {
        title_tv.text = "打分"
        countEdit.hint = "请输入分数(0-100)"
        countEdit.setText("")
        isSmartPush = false
        this.bean = bean
        this.homeworkId = homeworkId
        this.questionId = questionId
        uid = bean?.uid ?: ""
        super.show()
    }
}