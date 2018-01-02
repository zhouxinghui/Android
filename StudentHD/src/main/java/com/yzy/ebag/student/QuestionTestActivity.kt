package com.yzy.ebag.student

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ebag.core.bean.QuestionBean
import ebag.core.util.L
import ebag.core.util.T
import kotlinx.android.synthetic.main.activity_question_test.*

class QuestionTestActivity : AppCompatActivity() {

    private var active = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_test)
        setSentenceView()
        setJudge()
        setChoiceView()
        setCompletion()
        setWriteView()
        setChoiceView()
        setClassificationView()
        setSortView()
        setEnSort()
        setConnectionView()

        showResult.setOnClickListener {
            classificationView.showResult()
            judgeView.showResult()
            choiceView.showResult()
            completeView.showResult()
            writeView.showResult()
            sortView.showResult()
            enSortView.showResult()
            sentenceView.showResult()
            connectionView.showResult()
            val answer = connectionView.answer
            L.e(answer)
        }

        enable.setOnClickListener {
            if(completeView.isQuestionActive)
                T.show(this,"冻结操作")
            else
                T.show(this,"激活操作")
            completeView.questionActive(!choiceView.isQuestionActive)
            classificationView.questionActive(!classificationView.isQuestionActive)
        }
//        enable.setOnClickListener {
//            active = !active
//            if(active)
//                T.show(this,"激活操作")
//            else
//                T.show(this,"冻结操作")
//            judgeView.questionActive(active)
//            completeView.questionActive(active)
//            completeView.questionActive(active)
//            writeView.questionActive(active)
//        }
    }

    private fun setSentenceView(){
        val questionBean = QuestionBean()
        questionBean.questionHead = "用现代文翻译句子"
        questionBean.questionContent = "孔子东游，见两小儿辩斗，问其故。"
        questionBean.answer = "孔子往东边游学"
        sentenceView.setData(questionBean)
        sentenceView.show(true)
    }

    private fun setConnectionView(){
        val questionBean = QuestionBean()
        questionBean.questionHead = "给下列单词归类"
        questionBean.questionContent = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/blackboard.jpg,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/chair.jpg,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/desk.jpg,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/schoolbag.jpg;blackboard,desk,chair,schoolbag"
        questionBean.rightAnswer = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/blackboard.jpg,blackboard;http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/chair.jpg,chair;http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/desk.jpg,desk;http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/schoolbag.jpg,schoolbag"
        questionBean.answer = "blackboard,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/blackboard.jpg;desk,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/chair.jpg;chair,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/desk.jpg;schoolbag,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/schoolbag.jpg"
        connectionView.setData(questionBean)
        connectionView.show(true)
    }
    private fun setClassificationView(){
        val questionBean = QuestionBean()
        questionBean.questionHead = "给下列单词归类"
        questionBean.questionContent = "on,classroom,she,elephant,he,under,bird,blackboard;介词,学校物品,代词,动物"
        questionBean.rightAnswer = "介词#R#on,under;学校物品#R#classroom,blackboard;代词#R#she,he;动物#R#elephant,bird"
        questionBean.answer = ""
        classificationView.setData(questionBean)
        classificationView.show(true)
    }

    private fun setEnSort(){
        val questionBean = QuestionBean()
        questionBean.questionHead = "将下列各词连成一句话"
        questionBean.questionContent = "your#R#in#R#ruler#R#Put#R#pencil box#R#the#R#."
        questionBean.rightAnswer = "Put,your,ruler,in,the,pencil box,."
        questionBean.answer = "Put,ruler,your,in,the,pencil box,."
        enSortView.setData(questionBean)
        enSortView.show(active)
    }

    private fun setJudge(){
        val questionBean = QuestionBean()
        questionBean.questionType = "pd"
        questionBean.questionHead = "http://img.zcool.cn/community/01902d554c0125000001bf72a28724.jpg@1280w_1l_2o_100sh.jpg"
        questionBean.questionContent = "图片是红色的"
        questionBean.rightAnswer = "对"
        questionBean.answer = "错"
        judgeView.setData(questionBean)
        judgeView.show(active)
    }

    private fun setCompletion(){
        val questionBean = QuestionBean()
        questionBean.questionHead = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/sx/tuxing6.png"
        questionBean.questionContent = "12－8＝(#R#*#R#)        13－6＝(#R#*#R#)#R##F##R#13－8＝(#R#*#R#)        15－9＝(#R#*#R#)#R##F##R#9 + 4＝(#R#*#R#)        12－5＝(#R#*#R#)#R##F##R#11－3＝(#R#*#R#)       17－9＝(#R#*#R#)#R##F##R#15－7＝(#R#*#R#)"
        questionBean.rightAnswer = "4#R#7#R#5#R#6#R#13#R#7#R#8#R#8#R#8"
        questionBean.answer = "4#R#7#R#4#R#6#R#1#R#7#R#8#R#3#R#8"
        completeView.setData(questionBean)
        completeView.show(active)
        completeView.show(true)
    }

    private fun setWriteView(){
        val questionBean = QuestionBean()
        questionBean.id = 2
        questionBean.questionHead = "书写作业要求--写八个字出来"
        questionBean.questionContent = "这个写图片地址"
        questionBean.rightAnswer = ""
        writeView.setData(questionBean)
        writeView.show(active)
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
        questionBean.answer = "C"
        choiceView.setData(questionBean)
        choiceView.show(active)
    }

    private fun setSortView(){
        val questionBean = QuestionBean()
        questionBean.questionHead = "把下列句子排成一段通顺的话。"
        questionBean.questionContent = "与大象一起跳舞最难忘。#R#象是泰国的国宝。#R#在泰国，人与象之间没有距离，#R#在泰国，到处遇到大象很正常，#R#大象聪明而有灵气，"
        questionBean.rightAnswer = "5,2,3,1,4"
        questionBean.answer = "3,2,4,1,5"
        sortView.setData(questionBean)
        sortView.show(active)
    }
}
