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
    private val answerList = ArrayList<CorrectAnswerBean>()
    private var currentPosition = 0
    private var homeworkId = ""
    private var isNextPress = true
    private var currentScore = ""
    private val markRequest = object : RequestCallBack<String>(){
        override fun onStart() {
            LoadingDialogUtil.showLoading(context, "上传分数...")
        }

        override fun onSuccess(entity: String?) {
            LoadingDialogUtil.closeLoadingDialog()
            answerList[currentPosition].questionScore = currentScore
            if (isNextPress){
                if (!isLast())
                    currentPosition ++
            }else{
                if (!isFirst())
                    currentPosition --
            }
            setDesc(answerList[currentPosition])
        }

        override fun onError(exception: Throwable) {
            LoadingDialogUtil.closeLoadingDialog()
            exception.handleThrowable(context)
        }
    }
    init {
        nextStudent.setOnClickListener {
            isNextPress = true
            if (markEdit.isEnabled && !StringUtils.isEmpty(markEdit.text.toString())){//提交分数
                currentScore = markEdit.text.toString()
                val uid = answerList[currentPosition].uid
                if(uid == null) {
                    T.show(context, "学生uid为null！！")
                    return@setOnClickListener
                }
                TeacherApi.markScore(homeworkId, answerList[currentPosition].uid, answerList[currentPosition].qid, currentScore, markRequest)
            }else{
                if (isLast()){
                    return@setOnClickListener
                }
                currentPosition ++
                setDesc(answerList[currentPosition])
            }
        }
        previewStudent.setOnClickListener {
            isNextPress = false
            if (markEdit.isEnabled && !StringUtils.isEmpty(markEdit.text.toString())){//提交分数
                currentScore = markEdit.text.toString()
                TeacherApi.markScore(homeworkId, answerList[currentPosition].uid, answerList[currentPosition].qid, currentScore, markRequest)
            }else{
                if (isFirst()){
                    return@setOnClickListener
                }
                currentPosition --
                setDesc(answerList[currentPosition])
            }
        }
    }

    fun show(answerList: List<CorrectAnswerBean>, position: Int, homeworkId: String) {
        this.answerList.clear()
        this.answerList.addAll(answerList)
        this.homeworkId = homeworkId
        val bean = answerList[position]
        currentPosition = position
        setDesc(bean)
        super.show()
    }

    private fun isLast(): Boolean{
        return answerList.isEmpty() || currentPosition >= answerList.size - 1
    }

    private fun isFirst(): Boolean{
        return answerList.isEmpty() || currentPosition <= 0
    }

    private fun setDesc(bean: CorrectAnswerBean){
        nameAndBagId.text = "${bean.studentName}    书包号：${bean.ysbCode}"
        val answer = bean.studentAnswer
        if (StringUtils.isEmpty(answer)){
            commitTime.visibility = View.GONE
            studentAnswer.text = "未完成"
        }else{
            commitTime.visibility = View.VISIBLE
            commitTime.text = "提交时间：${DateUtil.getDateTime(bean.endTime, "yyyy-MM-dd HH:mm")}"
            studentAnswer.text = bean.studentAnswer?.replace("#R#", "、")
        }
        val score = bean.questionScore
        if (!StringUtils.isEmpty(score) && score?.toInt() != 0){
            markEdit.setText(score)
            markEdit.isEnabled = false
        }else{
            markEdit.setText("")
            markEdit.isEnabled = true
        }
        questionNum.text = "${currentPosition + 1}/${answerList.size}"
        if (isLast()){
            nextStudent.text = "提交"
        }else{
            nextStudent.text = "下一个"
        }
        if (isFirst()){
            previewStudent.text = "提交"
        }else{
            previewStudent.text = "上一个"
        }
    }
}