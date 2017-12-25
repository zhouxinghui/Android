package com.yzy.ebag.student

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ebag.core.bean.QuestionBean
import ebag.core.util.T
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

        enable.setOnClickListener {
            if(completeView.isQuestionActive)
                T.show(this,"冻结操作")
            else
                T.show(this,"激活操作")
            completeView.questionActive(!choiceView.isQuestionActive)
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
        questionBean.answer = "4#R#7#R#4#R#6#R#1#R#7#R#8#R#3#R#8"
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

    private fun setChoiceView(){
        val questionBean = QuestionBean()
//        questionBean.questionType = "1"
//        questionBean.questionHead = "schoolbag"
//        questionBean.questionContent =
//                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit1_Classroom_blackboard1.png" +
//                        ";http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit1_Classroom_book1.png" +
//                        ";http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit1_Classroom_chair3.png" +
//                        ";http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit1_Classroom_schoolbag2.png"
//        questionBean.rightAnswer = "D"

//        questionBean.questionType = "dx"
//        questionBean.questionHead = "选择正确的项填空。\n" +
//                "Lily: How are you?\n" +
//                "Lilei: ______"
//        questionBean.questionContent =
//                "How do you do?;How about you?;Fine, thank you.;Good,how about you?"
//        questionBean.rightAnswer = "C"

        questionBean.questionType = "2"
        questionBean.questionHead = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit1_Classroom_blackboard3.png"
        questionBean.questionContent =
                "desk;blackboard;school;wall"
        questionBean.rightAnswer = "C"
        choiceView.setData(questionBean)
        choiceView.show(true)
    }
}
