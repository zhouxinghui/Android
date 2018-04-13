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
}
