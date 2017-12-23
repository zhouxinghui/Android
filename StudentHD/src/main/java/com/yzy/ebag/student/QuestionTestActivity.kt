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
        showResult.setOnClickListener {
            judgeView.showResult()
        }
    }

    private fun setJudge(){
        val questionBean = QuestionBean()
        questionBean.questionHead = "判断题"
        questionBean.questionContent = "http://img.zcool.cn/community/01902d554c0125000001bf72a28724.jpg@1280w_1l_2o_100sh.jpg#R#下面的图片是红色的"
        questionBean.rightAnswer = "对"
        judgeView.setData(questionBean)
        judgeView.show(true)
    }
}
