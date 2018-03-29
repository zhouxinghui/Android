package com.yzy.ebag.teacher.widget

import android.content.Context
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.base.BaseDialog
import ebag.core.http.network.RequestCallBack
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.SerializableUtils
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.hd.base.Constants
import ebag.hd.bean.response.UserEntity
import kotlinx.android.synthetic.main.dialog_wrong_question_feedback.*

/**
 * Created by YZY on 2018/2/7.
 */
class QuestionFeedbackDialog(context: Context): BaseDialog(context) {
    private var uid = ""
    private var qid = ""
    private val feedbackRequest = object : RequestCallBack<String>(){
        override fun onStart() {
            dismiss()
            LoadingDialogUtil.showLoading(context, "正在上传...")
        }
        override fun onSuccess(entity: String?) {
            LoadingDialogUtil.closeLoadingDialog()
            T.show(context, "上传成功")
        }

        override fun onError(exception: Throwable) {
            LoadingDialogUtil.closeLoadingDialog()
            T.show(context, "网络请求失败，请稍后重试")
        }

    }
    override fun getLayoutRes(): Int {
        return R.layout.dialog_wrong_question_feedback
    }

    override fun setWidth(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.x600)
    }
    override fun setHeight(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.y400)
    }

    init {
        val userEntity = SerializableUtils.getSerializable(Constants.TEACHER_USER_ENTITY) as UserEntity
        uid = userEntity.uid
        confirmBtn.setOnClickListener {
            val content = feedbackEdit.text.toString()
            if (StringUtils.isEmpty(content)){
                T.show(context, "请填写你需要反馈的内容")
                return@setOnClickListener
            }
            TeacherApi.wrongQuestionFeedback(content, qid, uid, feedbackRequest)
        }
    }

    fun show(questionId: String) {
        qid = questionId
        super.show()
    }
}