package com.yzy.ebag.student

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ebag.core.bean.QuestionBean
import kotlinx.android.synthetic.main.activity_question_test.*

class QuestionTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_test)
        setChoiceView()
        showResult.setOnClickListener {
            choiceView.showResult()
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
        questionBean.questionHead = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/sx/tuxing6.png"
        questionBean.questionContent = "12－8＝(#R#*#R#)        13－6＝(#R#*#R#)#R##F##R#13－8＝(#R#*#R#)        15－9＝(#R#*#R#)#R##F##R#9 + 4＝(#R#*#R#)        12－5＝(#R#*#R#)#R##F##R#11－3＝(#R#*#R#)       17－9＝(#R#*#R#)#R##F##R#15－7＝(#R#*#R#)"
        questionBean.rightAnswer = "4#R#7#R#5#R#6#R#13#R#7#R#8#R#8#R#8"
        completeView.setData(questionBean)
        completeView.show(true)
    }

    private fun setChoiceView(){
        val questionBean = QuestionBean()
        questionBean.questionType = "1"
        questionBean.questionHead = "schoolbag"
        questionBean.questionContent =
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit1_Classroom_blackboard1.png" +
                        ";http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit1_Classroom_book1.png" +
                        ";http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit1_Classroom_chair3.png" +
                        ";http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit1_Classroom_schoolbag2.png"
        questionBean.rightAnswer = "D"
        choiceView.setData(questionBean)
        choiceView.show(true)
    }
}
