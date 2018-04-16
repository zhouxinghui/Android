package com.yzy.ebag.teacher

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ebag.core.bean.QuestionBean
import kotlinx.android.synthetic.main.activity_question_test.*

class QuestionTestActivity : AppCompatActivity() {
    private val active = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_test)

        setChoiceView()
        setClassificationView()
        setCompletion()
        setConnectionView()
        setMathEquationView()
        setMathEquationView2()
        setMathFractionView()
    }

    private fun setChoiceView(){
        val questionBean = QuestionBean()

        /*questionBean.type = "1"
        questionBean.title = "schoolbag"
        questionBean.content =
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit1_Classroom_blackboard1.png" +
                        ";http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit1_Classroom_book1.png" +
                        ";http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit1_Classroom_chair3.png" +
                        ";http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit1_Classroom_schoolbag2.png"
        questionBean.rightAnswer = "D"*/

        questionBean.type = "dx"
        questionBean.title = "选择正确的项填空。\n" +
                "Lily: How are you?\n" +
                "Lilei: ______"
        questionBean.content =
                "How do you do?;How about you?;Fine, thank you.;Good,how about you?"
        questionBean.rightAnswer = "C"

        /*questionBean.type = "8"
        questionBean.title = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/Lesson/hj/yy/3/1/5262/5273/Module 4 Unit 11.mp3"
        questionBean.content =
                "desk;blackboard;school;wall"
        questionBean.rightAnswer = "C"
        questionBean.answer = "C"*/
        choiceView.setData(questionBean)
        choiceView.show(active)
    }

    private fun setClassificationView(){
        val questionBean = QuestionBean()
        questionBean.title = "将下列图片分类"
        questionBean.content = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit3_clothes_red.png,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit3_clothes_blue.png,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit3_clothes_dress7.png,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit3_clothes_pants.png,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit3_clothes_T-shirt3.png,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit3_clothes_green.png,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit3_clothes_yellow.png,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit3_clothes_scarf.png;颜色,衣服"
        questionBean.rightAnswer = "颜色#R#http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit3_clothes_red.png,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit3_clothes_blue.png,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit3_clothes_green.png,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit3_clothes_yellow.png;衣服#R#http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit3_clothes_dress7.png,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit3_clothes_pants.png,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit3_clothes_T-shirt3.png,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/yy/Unit3_clothes_scarf.png"
        questionBean.answer = ""
/*
        questionBean.title = "给下列单词归类"
        questionBean.content = "on,classroom,she,elephant,he,under,bird,blackboard,blackboard,blackboard,blackboard,blackboard;介词,学校物品,代词,动物"
        questionBean.rightAnswer = "介词#R#on,under;学校物品#R#classroom,blackboard;代词#R#she,he;动物#R#elephant,bird"
        questionBean.answer = ""
*/
        classificationView.setData(questionBean)
        classificationView.show(active)
    }

    private fun setCompletion(){
        val questionBean = QuestionBean()
        questionBean.type = "3"
        questionBean.title = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/1/sx/tuxing6.png"
        questionBean.content = "要算15-9，先把15分成#R##A##R#和5，再用其中的10-9＝#R##A##R#，最后用#R##A##R#+5＝#R##A##R#。"
        questionBean.rightAnswer = "电视机#R#台灯#R#桌子#R#房间#R#在......后面#R#在......旁边"
        questionBean.answer = "电视机#R#台灯#R#桌子#R#房间#R#在......后面#R#在......旁边"
        completeView.setData(questionBean)
        completeView.show(true)
    }

    private fun setConnectionView(){
        val questionBean = QuestionBean()
        questionBean.title = "给图片和单词连线"
        questionBean.content = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/blackboard.jpg," +
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/chair.jpg," +
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/desk.jpg," +
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/question/questionImg/schoolbag.jpg;" +
                "chair,blackboard,desk,schoolbag"
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
}
