package ebag.mobile.module.homework

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Message
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.base.BaseListFragment
import ebag.core.bean.QuestionBean
import ebag.core.http.network.RequestCallBack
import ebag.core.util.*
import ebag.core.xRecyclerView.adapter.OnItemChildClickListener
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder
import ebag.mobile.R
import ebag.mobile.base.Constants
import ebag.mobile.bean.UserEntity
import ebag.mobile.widget.questions.base.BaseQuestionView
import java.io.File

/**
 * Created by YZY on 2018/5/10.
 */
class HomeworkDescFragment: BaseListFragment<List<QuestionBean>, QuestionBean>() {
    companion object {
        fun newInstance(list: ArrayList<QuestionBean?>?, workType: String, isReport: Boolean = true, homeworkId: String = ""): HomeworkDescFragment{
            val fragment = HomeworkDescFragment()
            val bundle = Bundle()
            bundle.putSerializable("list", list)
            bundle.putSerializable("workType", workType)
            bundle.putSerializable("isReport", isReport)
            bundle.putSerializable("homeworkId", homeworkId)
            fragment.arguments = bundle
            return fragment
        }
    }
    private lateinit var list: ArrayList<QuestionBean>
    private var workType = "1"
    private var isReport = true
    private lateinit var homeworkId: String
    private val mLayoutManager by lazy {
        LinearLayoutManager(mContext)
    }
    private val analyseDialog by lazy {
        QuestionAnalyseDialog(mContext)
    }
    var onDoingListener : (() -> Unit)? = null
    override fun getBundle(bundle: Bundle?) {
        list = bundle?.getSerializable("list") as ArrayList<QuestionBean>
        workType = bundle.getString("workType", "1")
        homeworkId = bundle.getString("homeworkId", "")
        isReport = bundle.getBoolean("isReport", true)
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
            onDoingListener?.invoke()
        }

        questionAdapter.setOnItemChildClickListener(questionClickListener)

        if (isReport) {
            questionAdapter.canDo = false
            questionAdapter.isShowAnalyseTv = true
            questionAdapter.showResult = true
        }else{
            when(workType){
                Constants.ERROR_TOPIC_TYPE ->{
                    questionAdapter.canDo = true
                    questionAdapter.isShowAnalyseTv = true
                    questionAdapter.showResult = false
                }
                else ->{
                    questionAdapter.canDo = true
                    questionAdapter.isShowAnalyseTv = false
                }
            }
        }

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
        recorderClick(view, position, adapter)
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

    //----------------------------------录音相关
    private var recorderAnim : AnimationDrawable? = null
    private val recorderUtil by lazy {
        val recorderUtil = RecorderUtil(mContext)
        recorderUtil.setFinalFileName(FileUtil.getRecorderPath() + "questionRecorder.amr")
        recorderUtil
    }
    private var playBtn: TextView? = null
    private var uploadBtn: TextView? = null
    private var tempPosition: Int = -1
    private lateinit var currentQuestionBean: QuestionBean
    private lateinit var cloudFileName: String
    private val alertDialog by lazy {
        AlertDialog.Builder(mContext)
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
    private val dialogNew by lazy {
        AlertDialog.Builder(mContext)
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
        val uid: String = SerializableUtils.getSerializable<UserEntity>(Constants.STUDENT_USER_ENTITY).uid
        cloudFileName = "$uid$homeworkId${currentQuestionBean.questionId}.amr"
        recorderAnim!!.stop()
        recorderAnim!!.selectDrawable(0)
        recorderUtil.finishRecord()
        //上传录音
        OSSUploadUtils.getInstance().uploadFileToOss(
                mContext,
                File(FileUtil.getRecorderPath() + "questionRecorder.amr"),
                "personal/questionRecorder",
                cloudFileName,
                mHandler)
        LoadingDialogUtil.showLoading(mContext, "上传录音...")
    }

    private val mHandler = MyHandler(this)
    class MyHandler(fragment: HomeworkDescFragment): HandlerUtil<HomeworkDescFragment>(fragment){
        override fun handleMessage(fragment: HomeworkDescFragment?, msg: Message?) {
            when {
                msg!!.what == ebag.core.util.Constants.UPLOAD_SUCCESS ->{//上传文件成功
                    fragment!!.playBtn!!.visibility = View.VISIBLE
                    fragment.uploadBtn!!.visibility = View.INVISIBLE
                    LoadingDialogUtil.closeLoadingDialog()
                    T.show(fragment.mContext, "上传成功")
                    fragment.currentQuestionBean.answer = "${ebag.core.util.Constants.OSS_BASE_URL}/personal/questionRecorder/${fragment.cloudFileName}"
                    fragment.onDoingListener?.invoke()
                }
                msg.what == ebag.core.util.Constants.UPLOAD_FAIL ->{//上传文件失败
                    LoadingDialogUtil.closeLoadingDialog()
                    T.show(fragment!!.mContext, "录音上传失败，请点击上传按钮重试")
                }
            }
        }
    }
    private fun recorderClick(view: View?, position: Int, adapter: QuestionAdapter){
        currentQuestionBean = adapter.data[position]
        when (view?.id){
            R.id.recorder_id -> {
                //点了其他item的录音按钮并且前面已经有item处于录音状态了
                if(tempPosition != position && recorderAnim != null){
                    //有正在录音的item
                    if (recorderUtil.isRecording){
                        //暂停录音
                        recorderAnim!!.stop()
                        recorderUtil.pauseRecord()
                        alertDialog.show()
                    }else{
                        //前面的item录音文件未上传
                        if(uploadBtn!!.visibility == View.VISIBLE) {
                            alertDialog.show()
                            //前面的item录音文件已经上传，直接开始录音
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
                        //已经录音了，点击录音按钮提示用户是否重新录音
                        if(uploadBtn!!.visibility == View.INVISIBLE)
                            dialogNew.show()
                        //已经暂停录音，点击录音按钮继续录音
                        else{
                            recorderUtil.startRecord()
                            recorderAnim!!.start()
                        }
                    }
                }
            }
            R.id.recorder_upload_id -> {
                uploadFile()
            }
            R.id.recorder_play_id -> {
                recorderUtil.playRecord(currentQuestionBean.answer)
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