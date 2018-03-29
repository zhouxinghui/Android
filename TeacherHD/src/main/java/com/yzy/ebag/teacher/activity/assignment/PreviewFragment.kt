package com.yzy.ebag.teacher.activity.assignment

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
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
class PreviewFragment : BaseListFragment<List<QuestionBean>, QuestionBean>() {
    private lateinit var previewList: ArrayList<QuestionBean>
    private val feedbackDialog by lazy { QuestionFeedbackDialog(mContext) }
    companion object {
        fun newInstance(previewList: ArrayList<QuestionBean>): PreviewFragment {
            val fragment = PreviewFragment()
            val bundle = Bundle()
            bundle.putSerializable("previewList", previewList)
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun getBundle(bundle: Bundle?) {
        previewList = bundle?.getSerializable("previewList") as ArrayList<QuestionBean>
    }

    override fun loadConfig() {
        (mAdapter as QuestionAdapter).previewList = previewList
        withFirstPageData(previewList)
    }
    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        adapter as QuestionAdapter
        when(view?.id){
            R.id.feedBackTv ->{
                feedbackDialog.show()
            }
            R.id.selectTv ->{
                val questionBean = adapter.getItem(position)
                questionBean!!.isChoose = false
                if (adapter.previewList.contains(questionBean))
                    adapter.remove(position)
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
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<QuestionBean>?): List<QuestionBean>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<QuestionBean, BaseViewHolder> {
        val questionAdapter = QuestionAdapter(true)
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