package com.yzy.ebag.student

import android.app.ProgressDialog
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Message
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import ebag.core.bean.QuestionBean
import ebag.core.util.*
import ebag.core.xRecyclerView.adapter.OnItemChildClickListener
import ebag.core.xRecyclerView.adapter.RecyclerAdapter
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder
import ebag.hd.widget.questions.RecorderView
import kotlinx.android.synthetic.main.activity_demo.*
import java.io.File
import java.util.*

class DemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        val questionList : ArrayList<QuestionBean> = ArrayList()
        val questionBean1 = QuestionBean()
        questionBean1.questionType = "14"
        questionBean1.questionHead = "听录音跟读句子"
        questionBean1.questionContent = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/Lesson/hj/yy/3/1/5262/5273/Module 4 Unit 11.mp3"
        val questionBean2 = QuestionBean()
        questionBean2.questionType = "14"
        questionBean2.questionHead = "听录音跟读句子"
        questionBean2.questionContent = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/Lesson/hj/yy/3/1/5260/5266/Module 2 Unit 4.mp3"
        val questionBean3 = QuestionBean()
        questionBean3.questionType = "14"
        questionBean3.questionHead = "听录音跟读句子"
        questionBean3.questionContent = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/Lesson/rj/yw/2/1/1010/5427/一封信.mp3"
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

        recorderUtil.setFinalFileName(FileUtil.getRecorderPath() + File.separator + "1.amr")
        adapter.setOnItemChildClickListener { holder, view, position ->
            when {
                view.id == R.id.recorder_id -> {
                    //点了其他item的录音按钮并且前面已经有item处于录音状态了
                    if(tempPosition != position && recorderAnim != null){
                        //有正在录音的item
                        if (recorderUtil.isRecording){
                            //暂停录音
                            recorderAnim!!.stop()
                            recorderUtil.pauseRecord()
                            alertDialog.show()
                        }else{
                            L.e(uploadBtn!!.visibility)
                            if(uploadBtn!!.visibility == View.VISIBLE) {
                                alertDialog.show()
                            }else{
                                startNewRecorder(view, position)
                            }
                        }
                        //第一次点击item的录音按钮
                    }else if (tempPosition != position && recorderAnim == null){
                        startNewRecorder(view, position)
                        //点击同一个item
                    }else if (tempPosition == position){
                        if(recorderUtil.isRecording){
                            recorderUtil.pauseRecord()
                            recorderAnim!!.stop()
                        }else{
                            if(uploadBtn!!.visibility == View.INVISIBLE)
                                dialogNew.show()
                            else{
                                recorderUtil.startRecord()
                                recorderAnim!!.start()
                            }
                        }
                    }
                }
                view.id == R.id.recorder_upload_id -> {
                    uploadFile()
                }
                view.id == R.id.recorder_play_id -> {
                    recorderUtil.playRecord("http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com"
                            + "/personal/" + "test2018/" + "1.amr")
                }
            }
        }
    }

    private var recorderAnim : AnimationDrawable? = null
    private val recorderUtil : RecorderUtil = RecorderUtil(this)
    private var playBtn: TextView? = null
    private var uploadBtn: TextView? = null
    private var tempPosition: Int = -1
    private val alertDialog by lazy {
        AlertDialog.Builder(this)
            .setMessage("你有录音正在进行中！")
            .setCancelable(false)
            .setNegativeButton("继续录音", { dialog, which ->
                recorderAnim!!.start()
                recorderUtil.startRecord()
                dialog.dismiss()
            }).setPositiveButton("上传录音", {dialog, which ->
                uploadFile()
            })
            .create() }
    private val uploadDialog by lazy {
        ProgressDialog(this)
    }
    private val dialogNew by lazy {
        AlertDialog.Builder(this)
                .setMessage("录音已存在，是否重新录制？")
                .setCancelable(false)
                .setNegativeButton("取消", { dialog, which ->
                    dialog.dismiss()
                }).setPositiveButton("重新录音", {dialog, which ->
                    recorderUtil.startRecord()
                    recorderAnim!!.start()
                    uploadBtn!!.visibility = View.VISIBLE
                    playBtn!!.visibility = View.VISIBLE
                })
                .create()
    }

    private fun startNewRecorder(view: View, position: Int){
        recorderAnim = view.background as AnimationDrawable?
        recorderAnim!!.start()
        recorderUtil.startRecord()
        tempPosition = position
        uploadBtn = view.getTag(R.id.recorder_upload_id) as TextView
        playBtn = view.getTag(R.id.recorder_play_id) as TextView
        uploadBtn!!.visibility = View.VISIBLE
        playBtn!!.visibility = View.INVISIBLE
    }

    private fun uploadFile(){
        recorderAnim!!.stop()
        recorderAnim!!.selectDrawable(0)
        recorderUtil.finishRecord()
        //上传录音
        OSSUploadUtils.getInstance().uploadFileToOss(
                this,
                File(FileUtil.getRecorderPath() + File.separator + "1.amr"),
                "personal/test2018",
                "1.amr",
                mHandler)
        uploadDialog.setMessage("正在上传录音...")
        uploadDialog.setCanceledOnTouchOutside(false)
        uploadDialog.show()

    }

    private var click : MyOnItemChildClickListener? = null
    private var voicePlayer : VoicePlayerOnline? = null
    private var anim : AnimationDrawable? = null
    private var progressBar : ProgressBar? = null
    private var tempUrl: String? = null

    inner class MyAdapter : RecyclerAdapter<QuestionBean> (R.layout.item_demo){
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: QuestionBean?) {
            val recorderView = setter.getView<RecorderView>(R.id.recorderView)
            recorderView.setData(entity)
            recorderView.show(true)
            recorderView.setOnItemChildClickListener(click)

            val recorderBtn = setter.getImageView(R.id.recorder_id)
            val playBtn = setter.getTextView(R.id.recorder_play_id)
            val uploadBtn = setter.getTextView(R.id.recorder_upload_id)
            recorderBtn.setTag(R.id.recorder_upload_id, uploadBtn)
            recorderBtn.setTag(R.id.recorder_play_id, playBtn)
            setter.addClickListener(R.id.recorder_id)
            setter.addClickListener(R.id.recorder_upload_id)
            setter.addClickListener(R.id.recorder_play_id)
        }
    }

    inner class MyOnItemChildClickListener : OnItemChildClickListener {
        override fun onItemChildClick(holder: RecyclerViewHolder, view: View?, position: Int) {
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

    private val mHandler = MyHandler(this)
    class MyHandler(activity: DemoActivity): HandlerUtil<DemoActivity>(activity){
        override fun handleMessage(activity: DemoActivity?, msg: Message?) {
            when {
                msg!!.what == Constants.UPLOAD_SUCCESS ->{//上传文件成功
                    activity!!.playBtn!!.visibility = View.VISIBLE
                    activity.uploadBtn!!.visibility = View.INVISIBLE
                    activity.uploadDialog.dismiss()
                    T.show(activity, "上传成功")
                }
                msg.what == Constants.UPLOAD_FAIL ->{//上传文件失败
                    activity!!.uploadDialog.dismiss()
                    T.show(activity, "录音上传失败，请点击上传按钮重试")
                }
            }
        }
    }
}
