package com.yzy.ebag.teacher.module.homework

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.base.Constants
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.bean.QuestionBean
import ebag.core.http.network.RequestCallBack
import ebag.core.util.StringUtils
import ebag.core.util.VoicePlayerOnline
import ebag.core.xRecyclerView.adapter.OnItemChildClickListener
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder
import ebag.mobile.base.BaseListActivity
import ebag.mobile.bean.UnitBean

/**
 * Created by YZY on 2018/4/18.
 */
class QuestionActivity: BaseListActivity<List<QuestionBean>, QuestionBean>() {
    private lateinit var unitBean: UnitBean.UnitSubBean
    private var difficulty: String? = null
    private lateinit var type: String
    private var gradeCode = ""
    private var semeterCode = ""
    private var course = ""
    private var bookVersionId = ""
    private lateinit var previewList: ArrayList<QuestionBean>
    private var isPreview = false
    companion object {
        fun jump(
                activity: Activity,
                previewList: ArrayList<QuestionBean>,
                unitBean: UnitBean.UnitSubBean?,
                difficulty: String?,
                type: String,
                gradeCode: String,
                semeterCode: String,
                course: String,
                bookVersionId: String){
            activity.startActivityForResult(
                    Intent(activity, QuestionActivity::class.java)
                            .putExtra("previewList", previewList)
                            .putExtra("unitBean", unitBean)
                            .putExtra("difficulty", difficulty)
                            .putExtra("type", type)
                            .putExtra("gradeCode", gradeCode)
                            .putExtra("semeterCode", semeterCode)
                            .putExtra("course", course)
                            .putExtra("bookVersionId", bookVersionId)
                    , Constants.QUESTION_REQUEST)
        }
    }
    override fun loadConfig(intent: Intent) {
        unitBean = intent.getSerializableExtra("unitBean") as UnitBean.UnitSubBean
        difficulty = intent.getStringExtra("difficulty")
        type = intent.getStringExtra("type")
        gradeCode = intent.getStringExtra("gradeCode")
        semeterCode = intent.getStringExtra("semeterCode")
        course = intent.getStringExtra("course")
        bookVersionId = intent.getStringExtra("bookVersionId")
        previewList = intent.getSerializableExtra("previewList") as ArrayList<QuestionBean>
        (mAdapter as QuestionAdapter).previewList = previewList

        titleBar.setOnLeftClickListener {
            backEvent()
            finish()
        }
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<QuestionBean>>) {
        TeacherApi.searchQuestion(unitBean, difficulty, type, gradeCode, semeterCode, course, bookVersionId, page, requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<QuestionBean>?): List<QuestionBean>? = parent

    override fun getAdapter(): BaseQuickAdapter<QuestionBean, BaseViewHolder> {
        val questionAdapter = QuestionAdapter(false)
        questionAdapter.setOnItemChildClickListener(questionClickListener)
        return questionAdapter
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        adapter as QuestionAdapter
        when(view.id){
            R.id.feedBackTv ->{
//                feedbackDialog.show(adapter.data[position].questionId)
            }
            R.id.selectTv ->{
                val questionBean = adapter.getItem(position)
                if (isPreview){
                    questionBean!!.isChoose = false
                    if (adapter.previewList.contains(questionBean))
                        adapter.remove(position)
                }else {
                    if (questionBean!!.isChoose) {
                        if (adapter.previewList.contains(questionBean))
                            adapter.previewList.remove(questionBean)
                        questionBean.isChoose = false
                    } else {
                        adapter.previewList.add(questionBean)
                        questionBean.isChoose = true
                    }
                }
//                onSelectClick?.invoke(questionBean)

                adapter.notifyDataSetChanged()
            }
            R.id.analyseTv ->{
                adapter.selectItem = position
                val questionBean = adapter.getItem(position)?.clone() as QuestionBean
                questionBean.answer = questionBean.rightAnswer
//                onAnalyseClick?.invoke(questionBean)
            }
        }
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<QuestionBean, BaseViewHolder>): RecyclerView.LayoutManager? = null

    override fun onBackPressed() {
        backEvent()
        super.onBackPressed()
    }

    private fun backEvent(){
        val intent = Intent()
        intent.putExtra("previewList", previewList)
        intent.putExtra("type", type)
        setResult(Constants.QUESTION_RESULT, intent)
    }

    //-----------------------------------------语音播放相关
    private val questionClickListener : QuestionItemChildClickListener by lazy { QuestionItemChildClickListener() }
    private val voicePlayer : VoicePlayerOnline by lazy {
        val player = VoicePlayerOnline(this)
        player.setOnPlayChangeListener(object : VoicePlayerOnline.OnPlayChangeListener{
            override fun onProgressChange(progress: Int) {
                progressBar!!.progress = progress
            }
            override fun onCompletePlay() {
                tempUrl = null
                anim!!.stop()
                anim!!.selectDrawable(0)
                progressBar!!.progress = 0
            }
        })
        player
    }
    private var anim : AnimationDrawable? = null
    private var progressBar : ProgressBar? = null
    private var tempUrl: String? = null

    inner class QuestionItemChildClickListener : OnItemChildClickListener {
        override fun onItemChildClick(holder: RecyclerViewHolder, view: View, position: Int) {
            if (view.id == R.id.play_id)
                voicePlaySetting(view)
        }
    }
    private fun voicePlaySetting(view: View){
        var url : String = view.getTag(R.id.play_id) as String
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
            voicePlayer.playUrl(url)
            anim!!.start()
            tempUrl = url
        }else{
            if (voicePlayer.isPlaying && !voicePlayer.isPause){
                voicePlayer.pause()
                anim!!.stop()
            }else{
                voicePlayer.play()
                anim!!.start()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (voicePlayer.isPlaying && !voicePlayer.isPause) {
            voicePlayer.pause()
            anim!!.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        voicePlayer.stop()
    }
}