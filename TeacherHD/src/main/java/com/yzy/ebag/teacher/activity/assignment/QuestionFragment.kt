package com.yzy.ebag.teacher.activity.assignment

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.AssignUnitBean
import com.yzy.ebag.teacher.http.TeacherApi
import com.yzy.ebag.teacher.widget.QuestionFeedbackDialog
import ebag.core.base.BaseListFragment
import ebag.core.bean.QuestionBean
import ebag.core.http.network.RequestCallBack
import ebag.core.util.StringUtils
import ebag.core.util.VoicePlayerOnline
import ebag.core.xRecyclerView.adapter.OnItemChildClickListener
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder

/**
 * Created by YZY on 2018/1/31.
 */
class QuestionFragment: BaseListFragment<List<QuestionBean>, QuestionBean>() {
    private lateinit var unitBean: AssignUnitBean.UnitSubBean
    private var difficulty: String? = null
    private lateinit var type: String
    private var gradeCode = ""
    private var semeterCode = ""
    private var course = ""
    private var bookVersionId = ""
    private lateinit var previewList: ArrayList<QuestionBean>
    private var isPreview = false
    private val feedbackDialog by lazy { QuestionFeedbackDialog(mContext) }
    companion object {
        fun newInstance(
                previewList: ArrayList<QuestionBean>,
                unitBean: AssignUnitBean.UnitSubBean,
                difficulty: String?,
                type: String,
                gradeCode: String,
                semeterCode: String,
                course: String,
                bookVersionId: String): QuestionFragment {
            val fragment = QuestionFragment()
            val bundle = Bundle()
            bundle.putSerializable("previewList", previewList)
            bundle.putSerializable("unitBean", unitBean)
            bundle.putString("difficulty", difficulty)
            bundle.putString("type", type)
            bundle.putString("gradeCode", gradeCode)
            bundle.putString("semeterCode", semeterCode)
            bundle.putString("course", course)
            bundle.putString("bookVersionId", bookVersionId)
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun getBundle(bundle: Bundle?) {
        unitBean = bundle?.getSerializable("unitBean") as AssignUnitBean.UnitSubBean
        difficulty = bundle.getString("difficulty")
        type = bundle.getString("type")
        gradeCode = bundle.getString("gradeCode")
        semeterCode = bundle.getString("semeterCode")
        course = bundle.getString("course")
        bookVersionId = bundle.getString("bookVersionId")
        previewList = bundle.getSerializable("previewList") as ArrayList<QuestionBean>
    }

    override fun isPagerFragment(): Boolean {
        return false
    }
    override fun loadConfig() {
        (mAdapter as QuestionAdapter).previewList = this.previewList
    }

    fun showPreview(list: ArrayList<QuestionBean>){
        isPreview = true
        withFirstPageData(list)
    }
    fun showSelect(list: ArrayList<QuestionBean>){
        isPreview = false
        withFirstPageData(list, true)
    }
    fun getData(): ArrayList<QuestionBean>{
        return mAdapter!!.data as ArrayList<QuestionBean>
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        adapter as QuestionAdapter
        when(view?.id){
            R.id.feedBackTv ->{
                feedbackDialog.show(adapter.data[position].questionId)
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
                onSelectClick?.invoke(questionBean)

                adapter.notifyDataSetChanged()
            }
            R.id.analyseTv ->{
                adapter.selectItem = position
                val questionBean = adapter.getItem(position)?.clone() as QuestionBean
                questionBean.answer = questionBean.rightAnswer
                onAnalyseClick?.invoke(questionBean)
            }
        }
    }
    var onAnalyseClick : ((questionBean: QuestionBean?) -> Unit)? = null
    var onSelectClick: ((questionBean: QuestionBean) -> Unit)? = null

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<QuestionBean>>) {
        TeacherApi.searchQuestion(unitBean, difficulty, type, gradeCode, semeterCode, course, bookVersionId, page, requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<QuestionBean>?): List<QuestionBean>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<QuestionBean, BaseViewHolder> {
        val questionAdapter = QuestionAdapter(false)
        questionAdapter.setOnItemChildClickListener(questionClickListener)
        return questionAdapter
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<QuestionBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    //-----------------------------------------语音播放相关
    private val questionClickListener : QuestionItemChildClickListener by lazy { QuestionItemChildClickListener() }
    private val voicePlayer : VoicePlayerOnline by lazy {
        val player = VoicePlayerOnline(mContext)
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