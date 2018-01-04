package com.yzy.ebag.student

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ProgressBar
import ebag.core.bean.QuestionBean
import ebag.core.util.StringUtils
import ebag.core.util.VoicePlayerOnline
import ebag.core.xRecyclerView.adapter.OnItemChildClickListener
import ebag.core.xRecyclerView.adapter.RecyclerAdapter
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder
import ebag.hd.widget.questions.ChoiceView
import kotlinx.android.synthetic.main.activity_demo.*
import java.util.*

class DemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        val questionList : ArrayList<QuestionBean> = ArrayList()
        val questionBean1 = QuestionBean()
        questionBean1.questionType = "8"
        questionBean1.questionHead = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/Lesson/hj/yy/3/1/5262/5273/Module 4 Unit 11.mp3"
        questionBean1.questionContent = "desk;blackboard;school;wall"
        val questionBean2 = QuestionBean()
        questionBean2.questionType = "8"
        questionBean2.questionHead = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/Lesson/hj/yy/3/1/5260/5266/Module 2 Unit 4.mp3"
        questionBean2.questionContent = "desk;blackboard;school;wall"
        val questionBean3 = QuestionBean()
        questionBean3.questionType = "8"
        questionBean3.questionHead = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/Lesson/rj/yw/2/1/1010/5427/一封信.mp3"
        questionBean3.questionContent = "desk;blackboard;school;wall"
        questionList.add(questionBean1)
        questionList.add(questionBean2)
        questionList.add(questionBean3)

        val adapter = MyAdapter()
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        adapter.datas = questionList
        click = MyOnItemChildClickListener()
        voicePlayer = VoicePlayerOnline(this)

        voicePlayer!!.setOnPlayChangeListener(object : VoicePlayerOnline.OnPlayChangeListener{
            override fun onProgressChange(progress: Int) {
                progressBar!!.progress = progress
            }
            override fun onCompletePlay() {
                tempUrl = null
                anim!!.stop()
                anim!!.selectDrawable(0)
            }
        })
    }

    private var click : MyOnItemChildClickListener? = null
    private var voicePlayer : VoicePlayerOnline? = null
    private var anim : AnimationDrawable? = null
    private var progressBar : ProgressBar? = null
    private var tempUrl: String? = null
    inner class MyAdapter : RecyclerAdapter<QuestionBean> (R.layout.item_demo){
        override fun fillData(setter: RecyclerViewHolder?, position: Int, entity: QuestionBean?) {
            val choiceView = setter!!.getView<ChoiceView>(R.id.choiceView)
            choiceView.setData(entity)
            choiceView.show(true)
            choiceView.setOnItemChildClickListener(click)
        }
    }

    inner class MyOnItemChildClickListener : OnItemChildClickListener {
        override fun onItemChildClick(holder: RecyclerViewHolder?, view: View?, position: Int) {
            var url : String = view?.getTag(R.id.play_id) as String
            url = url.substring(3, url.length)
            if (StringUtils.isEmpty(url))
                return
            if (url != tempUrl){
                if(anim != null) {
                    anim!!.stop()
                    anim!!.selectDrawable(0)
                    progressBar!!.progress = 0
                }
                anim = view.getTag(R.id.image_id) as AnimationDrawable
                progressBar = view.getTag(R.id.progress_id) as ProgressBar
                voicePlayer!!.playUrl(url)
                anim!!.start()
                tempUrl = url
            }else{
                if (voicePlayer!!.isPlaying && !voicePlayer!!.isPause){
                    voicePlayer!!.pause()
                    anim!!.stop()
                }else{
                    voicePlayer!!.play()
                    anim!!.start()
                }
            }
        }
    }
}
