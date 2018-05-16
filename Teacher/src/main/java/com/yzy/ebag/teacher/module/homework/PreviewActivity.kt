package com.yzy.ebag.teacher.module.homework

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.base.Constants
import com.yzy.ebag.teacher.bean.AssignClassBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.bean.QuestionBean
import ebag.core.http.network.RequestCallBack
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.core.util.VoicePlayerOnline
import ebag.core.xRecyclerView.adapter.OnItemChildClickListener
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder
import ebag.mobile.base.BaseListActivity
import ebag.mobile.bean.UnitBean
import ebag.mobile.module.homework.QuestionAnalyseDialog

/**
 * Created by YZY on 2018/4/19.
 */
class PreviewActivity: BaseListActivity<List<QuestionBean>, QuestionBean>() {
    companion object {
        fun jump(activity: Activity,
                 isTest: Boolean,
                 classes: java.util.ArrayList<AssignClassBean>,
                 unitBean: UnitBean.UnitSubBean?,
                 previewList: ArrayList<QuestionBean>,
                 workType: Int,
                 subCode: String,
                 bookVersionId: String,
                 isPreview: Boolean,
                 paperId: String? = null,
                 count: Int = 0
        ){
            activity.startActivityForResult(
                    Intent(activity, PreviewActivity::class.java)
                            .putExtra("isTest", isTest)
                            .putExtra("classes", classes)
                            .putExtra("unitBean", unitBean)
                            .putExtra("previewList", previewList)
                            .putExtra("workType", workType)
                            .putExtra("subCode", subCode)
                            .putExtra("bookVersionId", bookVersionId)
                            .putExtra("isPreview", isPreview)
                            .putExtra("paperId", paperId)
                            .putExtra("count", count)
                    , Constants.PREVIEW_REQUEST)
        }
    }
    private val analyseDialog by lazy { QuestionAnalyseDialog(this) }
    private var isPreview = true
    private var previewList = ArrayList<QuestionBean>()
    private lateinit var unitBean: UnitBean.UnitSubBean
    private var workType = 0
    private lateinit var bookVersionId: String
    private lateinit var classes: ArrayList<AssignClassBean>
    private var isTest = false
    private var paperId: String? = null
    private var subCode = ""
    private lateinit var questionNumTv: TextView
    private val previewPopup by lazy {
        val popup = PreviewPopup(this)
        popup.onAssignClick = {
            when(it){
                1 ->{   // 发布小组
                    when {
                        classes.isEmpty() -> T.show(this, "未选择班级")
                        previewList.isEmpty() -> T.show(this, "没有可发布的试题")
                        else -> PublishWorkActivity.jump(this, true, isTest, classes, unitBean, previewList, workType, subCode, bookVersionId)
                    }
                }
                2 ->{   //发布班级
                    when {
                        classes.isEmpty() -> T.show(this, "未选择班级")
                        previewList.isEmpty() -> T.show(this, "没有可发布的试题")
                        else -> PublishWorkActivity.jump(this, false, isTest, classes, unitBean, previewList, workType, subCode, bookVersionId)
                    }
                }
            }
        }
        popup
    }
    override fun loadConfig(intent: Intent) {
        loadMoreEnabled(false)
        isTest = intent.getBooleanExtra("isTest", false)
        classes = intent.getSerializableExtra("classes") as java.util.ArrayList<AssignClassBean>
        unitBean = intent.getSerializableExtra("unitBean") as UnitBean.UnitSubBean
        workType = intent.getIntExtra("workType", 0)
        subCode = intent.getStringExtra("subCode") ?: ""
        bookVersionId = intent.getStringExtra("bookVersionId") ?: ""
        isPreview = intent.getBooleanExtra("isPreview", false)
        paperId = intent.getStringExtra("paperId")

        val rightImage = ImageView(this)
        rightImage.setImageResource(R.drawable.more)
        rightImage.setPadding(resources.getDimension(ebag.mobile.R.dimen.x8).toInt(), 0, resources.getDimension(ebag.mobile.R.dimen.x8).toInt(), 0)
        val previewParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, resources.getDimensionPixelSize(R.dimen.title_bar_height))
        previewParams.addRule(RelativeLayout.ALIGN_PARENT_END)
        rightImage.layoutParams = previewParams
        titleBar.addView(rightImage)
        rightImage.setOnClickListener {
            previewPopup.showAsDropDown(rightImage)
        }

        if (isTest){
            rightImage.visibility = View.GONE
        }

        questionNumTv = TextView(this)
        questionNumTv.setTextColor(resources.getColor(R.color.white))
        questionNumTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.tv_normal))
        questionNumTv.gravity = Gravity.CENTER
        val numParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, resources.getDimensionPixelSize(R.dimen.title_bar_height) -1)
        numParams.addRule(RelativeLayout.ALIGN_PARENT_END)
        numParams.marginEnd = resources.getDimensionPixelSize(R.dimen.x40)
        questionNumTv.layoutParams = numParams
        titleBar.addView(questionNumTv)
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<QuestionBean>>) {
        if (isPreview) {
            if (paperId != null){
                TeacherApi.previewTestPaper(paperId!!, requestCallBack)
            }else {
                previewList = intent.getSerializableExtra("previewList") as ArrayList<QuestionBean>
                questionNumTv.text = "+${previewList.size}题"
                withFirstPageData(previewList, false)
                (mAdapter as QuestionAdapter).previewList = previewList
            }
        }else{
            val count = intent.getIntExtra("count", 0)
            TeacherApi.smartPush(count, unitBean, null, workType.toString(), bookVersionId, requestCallBack)
        }
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<QuestionBean>?): List<QuestionBean>?{
        if (parent != null && !parent.isEmpty())
            previewList = parent as ArrayList<QuestionBean>
        questionNumTv.text = "+${previewList.size}题"
        (mAdapter as QuestionAdapter).previewList = previewList
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<QuestionBean, BaseViewHolder> {
        val questionAdapter = QuestionAdapter(true)
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
                questionBean!!.isChoose = false
                if (adapter.previewList.contains(questionBean))
                    adapter.remove(position)
                questionNumTv.text = "+${previewList.size}题"

                adapter.notifyDataSetChanged()
            }
            R.id.analyseTv ->{
                adapter.selectItem = position
                val questionBean = adapter.getItem(position)?.clone() as QuestionBean
                questionBean.answer = questionBean.rightAnswer
                analyseDialog.show(questionBean)
            }
        }
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<QuestionBean, BaseViewHolder>): RecyclerView.LayoutManager? = null

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