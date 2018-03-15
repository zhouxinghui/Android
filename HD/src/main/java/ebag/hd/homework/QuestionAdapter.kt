package ebag.hd.homework

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.bean.QuestionBean
import ebag.core.bean.QuestionTypeUtils
import ebag.hd.R
import ebag.hd.widget.questions.base.BaseQuestionView

/**
 * @author caoyu
 * @date 2018/2/2
 * @description
 */
class QuestionAdapter: BaseMultiItemQuickAdapter<QuestionBean, BaseViewHolder>(null) {

    var onDoingListener: BaseQuestionView.OnDoingListener? = null
    var isShowAnalyseTv = false
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    var canDo = true
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    init {
        //看单词选图
        addItemType(QuestionTypeUtils.QUESTIONS_CHOOSE_PIC_BY_WORD, R.layout.item_question_choice)
        //看图选单词
        addItemType(QuestionTypeUtils.QUESTIONS_CHOOSE_WORD_BY_PIC, R.layout.item_question_choice)
        //判断题
        addItemType(QuestionTypeUtils.QUESTIONS_JUDGE, R.layout.item_question_choice)
        //选择题
        addItemType(QuestionTypeUtils.QUESTIONS_CHOICE, R.layout.item_question_choice)
        //听录音选择
        addItemType(QuestionTypeUtils.QUESTIONS_CHOOSE_BY_VOICE, R.layout.item_question_choice)
        //看图写单词
        addItemType(QuestionTypeUtils.QUESTIONS_WRITE_WORD_BY_PIC, R.layout.item_question_complete)
        //听录音填空
        addItemType(QuestionTypeUtils.QUESTIONS_COMPLETION_BY_VOICE, R.layout.item_question_complete)
        //填空题
        addItemType(QuestionTypeUtils.QUESTIONS_COMPLETION, R.layout.item_question_complete)
        //排列句子-英语
        addItemType(QuestionTypeUtils.QUESTIONS_EN_ORDER_SENTENCE, R.layout.item_question_sort_horizontal)
        //排列句子-语文
        addItemType(QuestionTypeUtils.QUESTIONS_CN_ORDER_SENTENCE, R.layout.item_question_sort_vertical)
        //连线
        addItemType(QuestionTypeUtils.QUESTIONS_DRAW_LINE, R.layout.item_question_connection)
        //分类
        addItemType(QuestionTypeUtils.QUESTIONS_CLASSIFICATION, R.layout.item_question_classification)
        //朗读
        addItemType(QuestionTypeUtils.QUESTIONS_READ_ALOUD, R.layout.item_question_recorder)
        //跟读
        addItemType(QuestionTypeUtils.QUESTIONS_FOLLOW_READ, R.layout.item_question_recorder)
        //作文
        addItemType(QuestionTypeUtils.QUESTIONS_WRITE_COMPOSITION_BY_PIC, R.layout.item_question_sentence)
        //词组或句子
        addItemType(QuestionTypeUtils.QUESTIONS_CHINESE_SENTENCE, R.layout.item_question_sentence)
        //听写
        addItemType(QuestionTypeUtils.QUESTIONS_CHINESE_WRITE_BY_VOICE, R.layout.item_question_sentence)
        //应用题
        addItemType(QuestionTypeUtils.QUESTION_MATH_APPLICATION, R.layout.item_question_sentence)
        //计算题-竖式（乘法和除法）
        addItemType(QuestionTypeUtils.QUESTION_MATH_VERTICAL, R.layout.item_question_math_vertical)
        //计算题-等式
        addItemType(QuestionTypeUtils.QUESTION_MATH_EQUATION, R.layout.item_question_math_equation)
        //计算题-分式
        addItemType(QuestionTypeUtils.QUESTION_MATH_FRACTION, R.layout.item_question_math_fraction)
        //阅读理解
        addItemType(QuestionTypeUtils.QUESTIONS_CHINESE_READ_UNDERSTAND, R.layout.item_question_understand)
        //书写
        addItemType(QuestionTypeUtils.QUESTIONS_WRITE, R.layout.item_question_write)
    }

    private var onItemChildClickListener: ebag.core.xRecyclerView.adapter.OnItemChildClickListener? = null

    fun setOnItemChildClickListener(onItemChildClickListener: ebag.core.xRecyclerView.adapter.OnItemChildClickListener){
        this.onItemChildClickListener = onItemChildClickListener
    }

    override fun convert(helper: BaseViewHolder, item: QuestionBean?) {
        val questionView = helper.getView<BaseQuestionView>(R.id.questionView)
        questionView.setData(item)
        questionView.tag = item
        questionView.setOnDoingListener(onDoingListener)
        questionView.show(canDo)

        helper.getView<TextView>(R.id.analyseTv).visibility = if (isShowAnalyseTv) View.VISIBLE else View.GONE
        helper.addOnClickListener(R.id.analyseTv)

        when(helper.itemViewType){
            QuestionTypeUtils.QUESTIONS_CHINESE_WRITE_BY_VOICE,
            QuestionTypeUtils.QUESTIONS_CHOOSE_BY_VOICE,
            QuestionTypeUtils.QUESTIONS_COMPLETION_BY_VOICE ->{
                questionView.setOnItemChildClickListener(onItemChildClickListener)
            }
            QuestionTypeUtils.QUESTIONS_FOLLOW_READ ->{
                questionView.setOnItemChildClickListener(onItemChildClickListener)

                val recorderBtn = helper.getView<ImageView>(R.id.recorder_id)
                val playBtn = helper.getView<TextView>(R.id.recorder_play_id)
                val uploadBtn = helper.getView<TextView>(R.id.recorder_upload_id)
                recorderBtn.setTag(R.id.recorder_upload_id, uploadBtn)
                recorderBtn.setTag(R.id.recorder_play_id, playBtn)
                helper.addOnClickListener(R.id.recorder_id)
                helper.addOnClickListener(R.id.recorder_upload_id)
                helper.addOnClickListener(R.id.recorder_play_id)
            }
        }
    }
}