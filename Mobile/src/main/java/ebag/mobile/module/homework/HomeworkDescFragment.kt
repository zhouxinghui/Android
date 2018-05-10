package ebag.mobile.module.homework

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.base.BaseListFragment
import ebag.core.bean.QuestionBean
import ebag.core.http.network.RequestCallBack
import ebag.core.util.StringUtils
import ebag.core.util.VoicePlayerOnline
import ebag.core.xRecyclerView.adapter.OnItemChildClickListener
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder
import ebag.mobile.R
import ebag.mobile.widget.questions.base.BaseQuestionView

/**
 * Created by YZY on 2018/5/10.
 */
class HomeworkDescFragment: BaseListFragment<List<QuestionBean>, QuestionBean>() {
    companion object {
        fun newInstance(list: ArrayList<QuestionBean?>?): HomeworkDescFragment{
            val fragment = HomeworkDescFragment()
            val bundle = Bundle()
            bundle.putSerializable("list", list)
            fragment.arguments = bundle
            return fragment
        }
    }
    private lateinit var list: ArrayList<QuestionBean>
    private val mLayoutManager by lazy {
        LinearLayoutManager(mContext)
    }
    private val analyseDialog by lazy {
        QuestionAnalyseDialog(mContext)
    }
    override fun getBundle(bundle: Bundle?) {
        list = bundle?.getSerializable("list") as ArrayList<QuestionBean>
    }

    fun scrollTo(position: Int){
        mLayoutManager.scrollToPositionWithOffset(position, 0)
    }

    override fun loadConfig() {
        withFirstPageData(list)
        enableNetWork(false)
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<QuestionBean>>) {
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<QuestionBean>?): List<QuestionBean>? = parent

    override fun getAdapter(): BaseQuickAdapter<QuestionBean, BaseViewHolder> {
        val questionAdapter = QuestionAdapter()
        questionAdapter.onDoingListener = BaseQuestionView.OnDoingListener {
//            typeAdapter.notifyDataSetChanged()
        }

        questionAdapter.setOnItemChildClickListener(questionClickListener)

        questionAdapter.canDo = false
        questionAdapter.isShowAnalyseTv = true
        questionAdapter.showResult = true

        return questionAdapter
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        adapter as QuestionAdapter
        if (view?.id == R.id.analyseTv) {
            val questionBean = adapter.data[position]?.clone() as QuestionBean
            questionBean.answer = questionBean.rightAnswer
            analyseDialog.show(questionBean)
        }
        if(view?.id == R.id.recorder_play_id){
            recorderPlayer.playUrl(adapter.getItem(position)?.answer)
        }
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<QuestionBean, BaseViewHolder>): RecyclerView.LayoutManager? = mLayoutManager

    //-----------------------------------------语音播放相关
    private val recorderPlayer by lazy {VoicePlayerOnline(mContext)}

    private val questionClickListener: QuestionItemChildClickListener by lazy { QuestionItemChildClickListener() }
    private val voicePlayer: VoicePlayerOnline by lazy {
        val player = VoicePlayerOnline(mContext)
        player.setOnPlayChangeListener(object : VoicePlayerOnline.OnPlayChangeListener {
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
    private var anim: AnimationDrawable? = null
    private var progressBar: ProgressBar? = null
    private var tempUrl: String? = null

    inner class QuestionItemChildClickListener : OnItemChildClickListener {
        override fun onItemChildClick(holder: RecyclerViewHolder, view: View, position: Int) {
            if (view.id == R.id.play_id)
                voicePlaySetting(view)
        }
    }

    private fun voicePlaySetting(view: View) {
        var url: String = view.getTag(R.id.play_id) as String
        url = url.substring(3, url.length)
        if (StringUtils.isEmpty(url))
            return
        if (url != tempUrl) {
            if (anim != null) {
                anim!!.stop()
                anim!!.selectDrawable(0)
                progressBar!!.progress = 0
            }
            anim = view.getTag(R.id.image_id) as AnimationDrawable
            progressBar = view.getTag(R.id.progress_id) as ProgressBar
            voicePlayer.playUrl(url)
            anim!!.start()
            tempUrl = url
        } else {
            if (voicePlayer.isPlaying && !voicePlayer.isPause) {
                voicePlayer.pause()
                anim!!.stop()
            } else {
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
        voicePlayer.stop()
        super.onDestroy()
    }
}