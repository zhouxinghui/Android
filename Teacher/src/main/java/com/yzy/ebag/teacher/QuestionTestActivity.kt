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
        setMathVerticalView2()
        setMathVerticalView()
        setSentenceView()
        setEnSort()
        setSortView()
        setUnderstandView()
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
        questionBean.type = "16"
        questionBean.title = "用现代文翻译句子"
        questionBean.content = "孔子东游，见两小儿辩斗，问其故。"
        questionBean.answer = "孔子往东边游学"
        sentenceView.setData(questionBean)
        sentenceView.show(true)
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

    private fun setSortView(){
        val questionBean = QuestionBean()
        questionBean.title = "把下列句子排成一段通顺的话。"
        questionBean.content = "与大象一起跳舞最难忘。#R#象是泰国的国宝。#R#在泰国，人与象之间没有距离，#R#在泰国，到处遇到大象很正常，#R#大象聪明而有灵气，"
        questionBean.rightAnswer = "5,2,3,1,4"
        questionBean.answer = "3,2,4,1,5"
        sortView.setData(questionBean)
        sortView.show(active)
    }

    private fun setUnderstandView(){
        val questionBean = QuestionBean()
        questionBean.title = "阅读乐园。\n自己是自己的镜子"
        questionBean.content = "爱因斯坦16岁那年，由于整日同一群调皮贪（tān）玩的孩子在一起，致（zhì）使自己几门功课不及格。关心他的父亲给他讲了这样一个故事： #R##F##R#　　“有两只猫在屋顶上玩耍。一不小心，一只猫抱着另一只猫掉到了烟囱（cōng）里。当两只猫从烟囱里爬出来时，一只猫的脸上沾（zhān）满了烟灰，而另一只猫的脸上却干干净净。干净的猫看见满脸黑灰的猫，以为自己的脸也又脏又丑，便快步跑到河边洗了脸。而黑脸猫看见干净的猫，以为自己的脸也是干净的。结果，吓得其他的猫都四下躲避（bì），以为见到了妖（yāo）怪。” #R##F##R#　　“爱因斯坦，谁也不能成为你的镜子，只有自己才是自己的镜子。拿别人做自己的镜子，天才也许会照成傻瓜。” #R##F##R#　　爱因斯坦听后，羞愧（kuì）地放下钓竿，回到了自己的小屋里。 #R##F##R#　　从此，爱因斯坦时常用自己作为镜子来审视和映照自己，终于映照出了他人生的璀璨（cuǐcàn）光芒。 #R##F##R#1．短文共有#R##E##R#个自然段。 #R##F##R#2．请你为加粗词语选择一个意思相近的词语。 #R##F##R#（1）爱因斯坦整日同一群【调皮】贪玩的孩子在一起。#R##E##R# #R##F##R#A．顽皮#R##F##R#B．好玩 #R##F##R#（2）爱因斯坦听后，【羞愧】地放下钓竿，回到了自己的小屋里。#R##E##R# #R##F##R#A．惭愧#R##F##R#B．害羞 #R##F##R#3．父亲讲这个故事是为了教育爱因斯坦#R##E##R#。#R##F##R#A．不要去钓鱼 #R##F##R#B．不要和贪玩的孩子在一起 #R##F##R#C．不要拿别人做自己的镜子，只有自己才是自己的镜子"
        questionBean.rightAnswer = "5,2,3,1,4"
        understandView.setData(questionBean)
        understandView.show(active)
    }

}
