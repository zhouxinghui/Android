package com.yzy.ebag.teacher.module.homework

import android.content.Context
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.base.BaseDialog
import ebag.core.bean.QuestionBean
import ebag.core.http.network.RequestCallBack
import ebag.core.util.StringUtils
import ebag.core.util.T
import kotlinx.android.synthetic.main.dialog_organize_paper.*

/**
 * Created by YZY on 2018/2/24.
 */
class OrganizePaperDialog(context: Context): BaseDialog(context) {
    override fun getLayoutRes(): Int {
        return R.layout.dialog_organize_paper
    }
    override fun setWidth(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.x200)
    }
    override fun setHeight(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.y500)
    }
    private var gradeCode: String? = null
    private var unitId: String? = null
    private var questionList: ArrayList<QuestionBean>? = null
    private var subCode = "yw"
    private val request = object : RequestCallBack<String>(){
        override fun onStart() {
            T.showLong(context, "正在上传...")
            confirmBtn.isEnabled = false
        }
        override fun onSuccess(entity: String?) {
            T.show(context, "上传成功")
            confirmBtn.isEnabled = true
            dismiss()
            onOrganizeSuccess?.invoke()
        }

        override fun onError(exception: Throwable) {
            T.show(context, "上传失败，请稍后重试")
            confirmBtn.isEnabled = true
        }
    }
    var onOrganizeSuccess: (() -> Unit)? = null
    init {
        confirmBtn.setOnClickListener {
            val paperName = paperNameEdit.text.toString()
            if (StringUtils.isEmpty(paperName)){
                T.show(context, "请填写试卷名称")
                return@setOnClickListener
            }
            TeacherApi.organizePaper(paperName, gradeCode!!, unitId, subCode, questionList!!, request)
        }
    }

    fun show(gradeCode: String, unitId: String?, questionList: ArrayList<QuestionBean>, subCode: String) {
        this.gradeCode = gradeCode
        this.unitId = unitId
        this.questionList = questionList
        this.subCode = subCode
        super.show()
    }
}