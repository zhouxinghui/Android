package ebag.hd.widget

import android.content.Context
import android.view.WindowManager
import ebag.core.base.BaseDialog
import ebag.core.bean.QuestionBean
import ebag.core.bean.QuestionTypeUtils
import ebag.core.util.StringUtils
import ebag.hd.R
import ebag.hd.widget.questions.*
import ebag.hd.widget.questions.base.BaseQuestionView
import kotlinx.android.synthetic.main.dialog_question_analyse.*

/**
 * Created by YZY on 2018/3/13.
 */
class QuestionAnalyseDialog(private val mContext: Context): BaseDialog(mContext) {
    override fun getLayoutRes(): Int {
        return R.layout.dialog_question_analyse
    }

    override fun setWidth(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.x600)
    }

    override fun setHeight(): Int {
        return WindowManager.LayoutParams.MATCH_PARENT
    }

    fun show(questionBean: QuestionBean) {
        showAnalyse(questionBean)
        super.show()
    }

    private fun showAnalyse(questionBean: QuestionBean){
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
            QuestionTypeUtils.QUESTIONS_WRITE->{
                questionView = WriteView(mContext)
            }
        }
        rightAnswerLayout.addView(questionView)

        questionView?.setData(questionBean)
        questionView?.show(false)
        analyseContentTv.text = if (StringUtils.isEmpty(questionBean.analytical)) "" else questionBean.analytical
    }
}