package com.yzy.ebag.teacher.module.homework

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseDialog
import ebag.core.bean.QuestionBean
import ebag.core.bean.QuestionTypeUtils
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.mobile.widget.questions.*
import ebag.mobile.widget.questions.base.BaseQuestionView
import kotlinx.android.synthetic.main.dialog_question_analyse.*

/**
 * Created by YZY on 2018/4/19.
 */
class QuestionAnalyseDialog(private val mContext: Context): BaseDialog(mContext) {
    override fun getLayoutRes(): Int = R.layout.dialog_question_analyse

    override fun setWidth(): Int {
        return WindowManager.LayoutParams.MATCH_PARENT
    }

    override fun setHeight(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.y800)
    }

    override fun getGravity(): Int = Gravity.BOTTOM

    fun show(questionBean: QuestionBean?) {
        super.show()
        if(questionBean == null){
            emptyAnalyseLayout.visibility = View.VISIBLE
            T.show(mContext, "暂无解析")
            return
        }
        if(StringUtils.isEmpty(questionBean.rightAnswer)){ //正确答案为空
            emptyAnalyseLayout.visibility = View.VISIBLE
            T.show(mContext, "暂无解析")
        }
        showAnalyse(questionBean)
    }

    private fun showAnalyse(questionBean: QuestionBean){
        emptyAnalyseLayout.visibility = View.GONE
        rightAnswerLayout.removeAllViews()

        var questionView: BaseQuestionView? = null
        when(QuestionTypeUtils.getIntType(questionBean)){
            QuestionTypeUtils.QUESTIONS_CHOOSE_PIC_BY_WORD,
            QuestionTypeUtils.QUESTIONS_CHOOSE_WORD_BY_PIC,
            QuestionTypeUtils.QUESTIONS_JUDGE,
            QuestionTypeUtils.QUESTIONS_CHOICE,
            QuestionTypeUtils.QUESTIONS_CHOOSE_BY_VOICE->{
                questionView = ChoiceView(mContext)
            }
            QuestionTypeUtils.QUESTIONS_WRITE_WORD_BY_PIC,
            QuestionTypeUtils.QUESTIONS_COMPLETION_BY_VOICE,
            QuestionTypeUtils.QUESTIONS_COMPLETION->{
                questionView = CompleteView(mContext)
            }
            QuestionTypeUtils.QUESTIONS_EN_ORDER_SENTENCE->{
                questionView = SortHorizontalView(mContext)
            }
            QuestionTypeUtils.QUESTIONS_CN_ORDER_SENTENCE->{
                questionView = SortVerticalView(mContext)
            }
            QuestionTypeUtils.QUESTIONS_DRAW_LINE->{
                questionView = ConnectionView(mContext)
            }
            QuestionTypeUtils.QUESTIONS_CLASSIFICATION->{
                questionView = ClassificationView(mContext)
            }
            QuestionTypeUtils.QUESTIONS_READ_ALOUD, QuestionTypeUtils.QUESTIONS_FOLLOW_READ->{
                questionView = RecorderView(mContext)
            }
            QuestionTypeUtils.QUESTIONS_WRITE_COMPOSITION_BY_PIC,
            QuestionTypeUtils.QUESTIONS_CHINESE_SENTENCE,
            QuestionTypeUtils.QUESTIONS_CHINESE_WRITE_BY_VOICE,
            QuestionTypeUtils.QUESTION_MATH_APPLICATION->{
                questionView = SentenceView(mContext)
            }
            QuestionTypeUtils.QUESTION_MATH_VERTICAL->{
                questionView = MathVerticalView(mContext)
            }
            QuestionTypeUtils.QUESTION_MATH_EQUATION->{
                questionView = MathEquationView(mContext)
            }
            QuestionTypeUtils.QUESTION_MATH_FRACTION->{
                questionView = MathFractionView(mContext)
            }
            QuestionTypeUtils.QUESTIONS_CHINESE_READ_UNDERSTAND->{
                questionView = UnderstandView(mContext)
            }
        }
        rightAnswerLayout.addView(questionView)

        questionView?.setData(questionBean)
        questionView?.show(false)
        analyseContentTv.text = if (StringUtils.isEmpty(questionBean.analytical)) "" else questionBean.analytical
    }
}