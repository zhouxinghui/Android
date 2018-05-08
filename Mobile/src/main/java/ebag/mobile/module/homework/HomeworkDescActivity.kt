package ebag.mobile.module.homework

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import ebag.core.base.BaseActivity
import ebag.core.base.BaseDialog
import ebag.core.bean.QuestionBean
import ebag.core.bean.QuestionTypeUtils
import ebag.core.bean.TypeQuestionBean
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.SerializableUtils
import ebag.core.util.StringUtils
import ebag.core.util.VoicePlayerOnline
import ebag.core.xRecyclerView.adapter.OnItemChildClickListener
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder
import ebag.mobile.R
import ebag.mobile.base.Constants
import ebag.mobile.bean.MyChildrenBean
import ebag.mobile.http.EBagApi
import ebag.mobile.widget.questions.base.BaseQuestionView
import kotlinx.android.synthetic.main.activity_homework_desc.*

/**
 * Created by YZY on 2018/4/28.
 */
class HomeworkDescActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_homework_desc

    companion object {
        fun jump(context: Context, homeworkId: String, workType: String, studentId: String? = null, testTime: Int = 0) {
            context.startActivity(
                    Intent(context, HomeworkDescActivity::class.java)
                            .putExtra("homeworkId", homeworkId)
                            .putExtra("testTime", testTime)
                            .putExtra("studentId", studentId)
                            .putExtra("workType", workType)
            )
        }
    }

    private var isError = false
    private lateinit var homeworkId: String
    private var workType = ""
    private var studentId = ""
    private val questionAdapter = QuestionAdapter()
    private val typeAdapter = OverviewAdapter()
    private var questionList: List<QuestionBean>? = null
    private lateinit var typeQuestionList: List<TypeQuestionBean?>
    private val questionManager = LinearLayoutManager(this)
    private val analyseDialog by lazy {
        QuestionAnalyseDialog(this)
    }
    private val typeDialog by lazy {
        TypeDialog()
    }
    private val detailRequest = object : RequestCallBack<List<TypeQuestionBean>>() {
        override fun onStart() {
            stateView.showLoading()
        }

        override fun onSuccess(entity: List<TypeQuestionBean>?) {
            typeQuestionList = entity ?: ArrayList()
            if (typeQuestionList.isNotEmpty()) {
                titleBar.setTitle(QuestionTypeUtils.getTitle(typeQuestionList[0]?.type))
                (0 until typeQuestionList.size).forEach {
                    typeQuestionList[it]?.initQuestionPosition(it)
                }
                stateView.showContent()
                typeAdapter.setNewData(typeQuestionList)
                typeAdapter.expandAll()
                questionList = typeQuestionList[0]?.questionVos
                questionAdapter.setNewData(questionList)
            } else {
                stateView.showEmpty()
            }
        }

        override fun onError(exception: Throwable) {
//            if(exception is MsgException)
//                stateView.showError(exception.message)
            stateView.showError()
            exception.handleThrowable(this@HomeworkDescActivity)
        }
    }

    override fun initViews() {
        homeworkId = intent.getStringExtra("homeworkId") ?: ""
        workType = intent.getStringExtra("workType") ?: ""
        studentId = intent.getStringExtra("studentId") ?: ""
        if (intent.hasExtra("error")) {
            isError = intent.getBooleanExtra("error", false)
        }

        questionRecycler.layoutManager = questionManager
        questionRecycler.adapter = questionAdapter
        questionRecycler.isNestedScrollingEnabled = true

        /*val footerView = layoutInflater.inflate(R.layout.homework_desc_foot_view, null) as FrameLayout
        val exchangeTv = footerView.findViewById<TextView>(R.id.tv)
        exchangeTv.setOnClickListener {
            typeDialog.show()
        }
        questionAdapter.addFooterView(footerView)*/
        titleBar.setOnRightClickListener {
            typeDialog.show()
        }

        questionAdapter.onDoingListener = BaseQuestionView.OnDoingListener {
            typeAdapter.notifyDataSetChanged()
        }

        questionAdapter.setOnItemChildClickListener(questionClickListener)
        questionAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.analyseTv) {
                val questionBean = questionAdapter.data[position]?.clone() as QuestionBean
                questionBean.answer = questionBean.rightAnswer
                analyseDialog.show(questionBean)
            }
        }

        questionAdapter.canDo = false
        questionAdapter.isShowAnalyseTv = true
        questionAdapter.showResult = true

        typeAdapter.setSpanSizeLookup { gridLayoutManager, position ->
            if (typeAdapter.getItemViewType(position) == TypeQuestionBean.TYPE) {
                gridLayoutManager.spanCount
            } else {
                1
            }
        }

        typeAdapter.setOnItemClickListener { _, _, position ->
            if (typeAdapter.getItemViewType(position) == TypeQuestionBean.ITEM) {
                typeDialog.dismiss()

                val item = typeAdapter.getItem(position) as QuestionBean?
                        ?: return@setOnItemClickListener

                val parentPosition = typeAdapter.getParentPosition(item)
                val ss = typeAdapter.getItem(parentPosition) as TypeQuestionBean?
                titleBar.setTitle(QuestionTypeUtils.getTitle(ss?.type))
                if (ss != null) {
                    val sss = ss.questionVos
                    if (sss === questionList) {
                        questionManager.scrollToPositionWithOffset(item.position, 0)
                    } else {
                        questionList = sss
                        questionAdapter.setNewData(questionList)
                        questionRecycler.postDelayed({
                            questionManager.scrollToPositionWithOffset(item.position, 0)
                        }, 100)
                    }
                }
            }
        }

        stateView.setOnRetryClickListener {
            request()
        }

        request()

    }

    private fun request() {

        if (isError) {
            EBagApi.getErrorDetail(homeworkId, (SerializableUtils.getSerializable(Constants.CHILD_USER_ENTITY) as MyChildrenBean).uid, detailRequest)
        } else {
            EBagApi.getQuestions(homeworkId, workType, studentId, detailRequest)
        }
    }

    private inner class TypeDialog : BaseDialog(this) {
        override fun getLayoutRes(): Int = R.layout.recycler_view_layout

        override fun setWidth(): Int = WindowManager.LayoutParams.MATCH_PARENT
        override fun setHeight(): Int = resources.getDimensionPixelSize(R.dimen.y700)
        override fun getGravity(): Int = Gravity.BOTTOM

        init {
            val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
            recyclerView.layoutManager = GridLayoutManager(this@HomeworkDescActivity, 5)
            recyclerView.adapter = typeAdapter
        }
    }

    //-----------------------------------------语音播放相关
    private val questionClickListener: QuestionItemChildClickListener by lazy { QuestionItemChildClickListener() }
    private val voicePlayer: VoicePlayerOnline by lazy {
        val player = VoicePlayerOnline(this)
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