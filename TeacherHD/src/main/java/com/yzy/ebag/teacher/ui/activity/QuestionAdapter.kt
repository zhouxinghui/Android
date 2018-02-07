package com.yzy.ebag.teacher.ui.activity

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import ebag.core.bean.QuestionBean
import ebag.core.bean.QuestionTypeUtils
import ebag.hd.widget.questions.base.BaseQuestionView

/**
 * Created by YZY on 2018/1/30.
 */
class QuestionAdapter(private val isPreviewPage: Boolean = false): BaseMultiItemQuickAdapter<QuestionBean, BaseViewHolder>(null) {
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
    var selectItem = -1
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var previewList = ArrayList<QuestionBean>()

    override fun convert(helper: BaseViewHolder, item: QuestionBean?) {
        val questionView = helper.getView<BaseQuestionView>(R.id.questionView)
        questionView.setData(item)
        questionView.show(false)

        helper.addOnClickListener(R.id.feedBackTv)
        helper.addOnClickListener(R.id.analyseTv)
        helper.addOnClickListener(R.id.selectTv)

//        helper.getView<View>(R.id.question_item_root).isSelected = selectItem != -1 && selectItem == helper.adapterPosition
        if(selectItem != -1 && selectItem == helper.adapterPosition){
            helper.getView<ViewGroup>(R.id.question_item_root).setBackgroundColor(Color.parseColor("#FFF9F0"))
        }else
            helper.getView<ViewGroup>(R.id.question_item_root).setBackgroundColor(Color.parseColor("#FFFFFF"))
        val selectTv = helper.getView<TextView>(R.id.selectTv)
        if (item!!.isChoose){
            selectTv.text = "移除"
            selectTv.isSelected = true
        }else{
            selectTv.text = "选入"
            selectTv.isSelected = false
        }
        if (previewList.contains(item)){
            item.isChoose = true
            selectTv.isSelected = true
            selectTv.text = "移除"
        }
        if (isPreviewPage) {
            if (helper.adapterPosition != 0 && item.type == mData[helper.adapterPosition - 1].type)
                helper.getView<TextView>(R.id.typeNameTv).visibility = View.GONE
            else {
                helper.setText(R.id.typeNameTv, QuestionTypeUtils.getTitle(item.type))
                helper.getView<TextView>(R.id.typeNameTv).visibility = View.VISIBLE
            }
        }else{
            helper.getView<TextView>(R.id.typeNameTv).visibility = View.GONE
        }
    }
}