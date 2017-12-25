package com.yzy.ebag.student

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ebag.core.bean.QuestionBean
import kotlinx.android.synthetic.main.activity_question_test.*

class QuestionTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_test)
        setJudge()
        setCompletion()
        setWriteView()
        showResult.setOnClickListener {
            completeView.showResult()
        }
    }

    private fun setJudge(){
        val questionBean = QuestionBean()
        questionBean.questionHead = "http://img.zcool.cn/community/01902d554c0125000001bf72a28724.jpg@1280w_1l_2o_100sh.jpg"
        questionBean.questionContent = "图片是红色的"
        questionBean.rightAnswer = "对"
        judgeView.setData(questionBean)
        judgeView.show(true)
    }

    private fun setCompletion(){
        val questionBean = QuestionBean()
        questionBean.questionHead = "按照《找春天》的课文内容填空"
        questionBean.questionContent = "小草从地下探出头来，那是春天的眉毛吧？#R##F##R#早开的野花#R##E##R#，那是春天的眼睛吧？#R##F##R#树木吐出#R##E##R#嫩芽，那是春天的音符吧？#R##F##R#解冻的小溪#R##E##R#，那是春天的琴声吧？"
        questionBean.rightAnswer = "一朵两朵#R#点点#R#丁丁冬冬"
        completeView.setData(questionBean)
        completeView.show(false)
    }

    private fun setWriteView(){
        val questionBean = QuestionBean()
        questionBean.id = 2
        questionBean.questionHead = "书写作业要求--写八个字出来"
        questionBean.questionContent = "这个写图片地址"
        questionBean.rightAnswer = ""
        writeView.setData(questionBean)
        writeView.show(true)
    }
}
