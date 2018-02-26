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
        setMathFractionView()
        setMathEquationView()
        setMathEquationView2()
        setMathVerticalView2()
        setMathVerticalView()
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
            mathVerticalView.showResult()
            val answer = mathFractionView.answer
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

    private fun setMathFractionView(){
        val questionBean = QuestionBean()
        questionBean.type = "15"
        questionBean.minType = "19"
        questionBean.title = "竖式计算"
        questionBean.content = "#1,6#:-:#1,7#:=:#*,*#;#4,5#:-:#2,3#:=:#*,*#;#1,8#:+:#1,6#:=:#*,*#;"
        questionBean.rightAnswer = "1,42;2,15;7,24"
//        questionBean.answer = "4,2,2,8,3,1,2,1,2"
        mathFractionView.setData(questionBean)
        mathFractionView.show(true)
    }

    private fun setMathEquationView(){
        val questionBean = QuestionBean()
        questionBean.type = "15"
        questionBean.minType = "18"
        questionBean.title = "简便计算"
        questionBean.content = "*(70,－,46),÷,8,＝,#C#"
        questionBean.rightAnswer = "24,3"
//        questionBean.answer = "4,2,2,8,3,1,2,1,2"
        mathEquationView.setData(questionBean)
        mathEquationView.show(true)
    }

    private fun setMathEquationView2(){
        val questionBean = QuestionBean()
        questionBean.type = "15"
        questionBean.minType = "18"
        questionBean.title = "直接写出得数"
        questionBean.content = "#45,－,(13,＋,12),＝,#C#"
        questionBean.rightAnswer = "25,20"
//        questionBean.answer = "4,2,2,8,3,1,2,1,2"
        mathEquationView2.setData(questionBean)
        mathEquationView2.show(true)
    }

    private fun setMathVerticalView2(){
        val questionBean = QuestionBean()
        questionBean.type = "15"
        questionBean.minType = "17"
        questionBean.title = "82×16=(    )"
        questionBean.content = "#G#,#C#,#C#,#C#,#F#,#H#,#F#,3,3,5,3,#F#,#G#,#C#,#G#,#G#," +
                "#F#,#E#,#F#,#G#,#G#,#C#,#G#,#F#,#G#,#G#,#C#,#G#,#F#,#E#,#F#,#G#,#G#,#C#,#C#," +
                "#F#,#G#,#G#,#C#,#C#,#F#,#E#,#F#,#G#,#G#,#G#,#C#"
        questionBean.rightAnswer = "1,1,7,3,5,3,2,3,2,1,2"
//        questionBean.answer = "4,2,2,8,3,1,2,1,2"
        mathVerticalView2.setData(questionBean)
        mathVerticalView2.show(true)
    }

    private fun setMathVerticalView(){
        val questionBean = QuestionBean()
        questionBean.type = "15"
        questionBean.minType = "21"
        questionBean.title = "82×16=(    )"
        questionBean.content = "#G#,#G#,8,2,#F#,×,#F#,#G#,#G#,1,6,#F#,#E#,#F#,#G#,#C#,#C#,#C#,#F#,#G#,#C#,#C#,#G#,#F#,#E#,#F#,#C#,#C#,#C#,#C#"
        questionBean.rightAnswer = "4,9,2,8,2,1,3,1,2"
//        questionBean.answer = "4,2,2,8,3,1,2,1,2"
        mathVerticalView.setData(questionBean)
        mathVerticalView.show(true)
    }

    private fun setSentenceView(){
        val questionBean = QuestionBean()
        questionBean.type = "19"
        questionBean.title = "用现代文翻译句子"
        questionBean.content = "孔子东游，见两小儿辩斗，问其故。"
        questionBean.answer = "孔子往东边游学"
        sentenceView.setData(questionBean)
        sentenceView.show(true)
    }

    private fun setConnectionView(){
        val questionBean = QuestionBean()
        questionBean.title = "给图片和单词连线"
        questionBean.content = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/blackboard.jpg," +
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/chair.jpg," +
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/desk.jpg," +
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/schoolbag.jpg;" +
                "blackboard,desk,chair,schoolbag"
        questionBean.rightAnswer = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/blackboard.jpg,blackboard;" +
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/chair.jpg,chair;" +
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/desk.jpg,desk;" +
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/schoolbag.jpg,schoolbag"
//        questionBean.answer = "blackboard,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/blackboard.jpg;" +
//                "desk,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/chair.jpg;" +
//                "chair,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/desk.jpg;" +
//                "schoolbag,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/schoolbag.jpg"
        connectionView.setData(questionBean)
        connectionView.show(true)
    }
    private fun setClassificationView(){
        val questionBean = QuestionBean()
        questionBean.title = "给下列单词归类"
        questionBean.content = "on,classroom,she,elephant,he,under,bird,blackboard;介词,学校物品,代词,动物"
        questionBean.rightAnswer = "介词#R#on,under;学校物品#R#classroom,blackboard;代词#R#she,he;动物#R#elephant,bird"
        questionBean.answer = ""
        classificationView.setData(questionBean)
        classificationView.show(true)
    }

    private fun setEnSort(){
        val questionBean = QuestionBean()
        questionBean.title = "将下列各词连成一句话"
        questionBean.content = "your#R#in#R#ruler#R#Put#R#pencil box#R#the#R#."
        questionBean.rightAnswer = "Put,your,ruler,in,the,pencil box,."
        questionBean.answer = "Put,ruler,your,in,the,pencil box,."
        enSortView.setData(questionBean)
        enSortView.show(active)
    }

    private fun setJudge(){
        val questionBean = QuestionBean()
        questionBean.type = "pd"
        questionBean.title = "http://img.zcool.cn/community/01902d554c0125000001bf72a28724.jpg@1280w_1l_2o_100sh.jpg"
        questionBean.content = "图片是红色的"
        questionBean.rightAnswer = "对"
        questionBean.answer = "错"
        judgeView.setData(questionBean)
        judgeView.show(active)
    }

    private fun setCompletion(){
        val questionBean = QuestionBean()
        questionBean.type = "4"
        questionBean.title = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/sx/tuxing6.png"
        questionBean.content = "12－8＝(#R#*#R#)        " +
                "13－6＝(#R#*#R#)#R##F##R#13－8＝(#R#*#R#)        " +
                "15－9＝(#R#*#R#)#R##F##R#9 + 4＝(#R#*#R#)        " +
                "12－5＝(#R#*#R#)#R##F##R#11－3＝(#R#*#R#)       " +
                "17－9＝(#R#*#R#)#R##F##R#15－7＝(#R#*#R#)"
        questionBean.rightAnswer = "电视机#R#台灯#R#桌子#R#房间#R#在......后面#R#在......旁边"
        questionBean.answer = "电视机#R#台灯#R#桌子#R#房间#R#在......后面#R#在......旁边"
        completeView.setData(questionBean)
        completeView.show(true)
    }

    private fun setWriteView(){
        val questionBean = QuestionBean()
        questionBean.questionId = "2"
        questionBean.title = "书写作业要求--写八个字出来"
        questionBean.content = "这个写图片地址"
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

        questionBean.type = "8"
        questionBean.title = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/Lesson/hj/yy/3/1/5262/5273/Module 4 Unit 11.mp3"
        questionBean.content =
                "desk;blackboard;school;wall"
        questionBean.rightAnswer = "C"
        questionBean.answer = "C"
        choiceView.setData(questionBean)
        choiceView.show(active)
    }

    private fun setSortView(){
        val questionBean = QuestionBean()
        questionBean.title = "把下列句子排成一段通顺的话。"
        questionBean.content = "与大象一起跳舞最难忘。#R#象是泰国的国宝。#R#在泰国，人与象之间没有距离，#R#在泰国，到处遇到大象很正常，#R#大象聪明而有灵气，"
        questionBean.rightAnswer = "5,2,3,1,4"
        questionBean.answer = "3,2,4,1,5"
        sortView.setData(questionBean)
        sortView.show(active)
    }
}
