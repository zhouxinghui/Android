package com.yzy.ebag.teacher.ui.activity

import android.app.Activity
import android.content.Intent
import android.view.View
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.base.Constants
import com.yzy.ebag.teacher.bean.AssignClassBean
import com.yzy.ebag.teacher.bean.AssignUnitBean
import com.yzy.ebag.teacher.http.TeacherApi
import com.yzy.ebag.teacher.ui.fragment.PreviewFragment
import ebag.core.base.BaseActivity
import ebag.core.bean.QuestionBean
import ebag.core.bean.QuestionTypeUtils
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.hd.widget.TitleBar
import ebag.hd.widget.questions.*
import ebag.hd.widget.questions.base.BaseQuestionView
import kotlinx.android.synthetic.main.activity_question.*

/**
 * Created by YZY on 2018/2/2.
 */
class PreviewActivity: BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_question
    }
    private lateinit var previewList: ArrayList<QuestionBean>
    private var isPreview = true
    /**
     * 智能推送
     */
    private val smartPushRequest = object: RequestCallBack<List<QuestionBean>>(){
        override fun onStart() {
            stateView.showLoading()
        }
        override fun onSuccess(entity: List<QuestionBean>?) {
            if (entity == null || entity.isEmpty()){
                stateView.showEmpty()
            }else{
                previewList = entity as ArrayList<QuestionBean>
                setFragmentEvent(previewList)
                stateView.showContent()
            }
        }

        override fun onError(exception: Throwable) {
            stateView.showError()
            exception.handleThrowable(this@PreviewActivity)
        }

    }

    /**
     * 预览试卷
     */
    private val previewPaperRequest = object : RequestCallBack<List<QuestionBean>>(){
        override fun onStart() {
            stateView.showLoading()
        }
        override fun onSuccess(entity: List<QuestionBean>?) {
            if (entity == null || entity.isEmpty()){
                stateView.showEmpty()
            }else{
                previewList = entity as ArrayList<QuestionBean>
                setFragmentEvent(previewList)
                stateView.showContent()
            }
        }

        override fun onError(exception: Throwable) {
            stateView.showError()
            exception.handleThrowable(this@PreviewActivity)
        }

    }
    companion object {
        fun jump(activity: Activity,
                 isTest: Boolean,
                 classes: java.util.ArrayList<AssignClassBean>,
                 unitBean: AssignUnitBean.UnitSubBean,
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

    override fun initViews() {
        val isTest = intent.getBooleanExtra("isTest", false)
        val classes = intent.getSerializableExtra("classes") as java.util.ArrayList<AssignClassBean>
        val unitBean = intent.getSerializableExtra("unitBean") as AssignUnitBean.UnitSubBean
        val workType = intent.getIntExtra("workType", 0)
        val subCode = intent.getStringExtra("subCode")
        val bookVersionId = intent.getStringExtra("bookVersionId")
        isPreview = intent.getBooleanExtra("isPreview", false)
        if (isPreview) {
            val paperId = intent.getStringExtra("paperId")
            if (paperId != null){
                TeacherApi.previewTestPaper(paperId, previewPaperRequest)
            }else {
                previewList = intent.getSerializableExtra("previewList") as ArrayList<QuestionBean>
                setFragmentEvent(previewList)
            }
        }else{
            val count = intent.getIntExtra("count", 0)
            TeacherApi.smartPush(count, unitBean, null, workType.toString(), bookVersionId, smartPushRequest)
        }
        previewTv.text = "发布小组"
        publishTv.text = "发布班级"
        if (isTest){
            previewTv.visibility = View.GONE
            publishTv.visibility = View.GONE
        }
        previewTv.setOnClickListener {
            if (classes.isEmpty()){
                T.show(this, "未选择班级")
                return@setOnClickListener
            }
            PublishWorkActivity.jump(this, true, isTest, classes, unitBean, previewList, workType, subCode, bookVersionId)
        }
        publishTv.setOnClickListener {
            if (classes.isEmpty()){
                T.show(this, "未选择班级")
                return@setOnClickListener
            }
            PublishWorkActivity.jump(this, false, isTest, classes, unitBean, previewList, workType, subCode, bookVersionId)
        }

        titleBar.setOnTitleBarClickListener(object : TitleBar.OnTitleBarClickListener{
            override fun leftClick() {
                backEvent()
                finish()
            }
            override fun rightClick() {
            }
        })
    }
    private fun backEvent(){
        if (isPreview) {
            if (previewList.isEmpty())
                previewList = ArrayList()
            val intent = Intent()
            intent.putExtra("previewList", previewList)
            setResult(Constants.QUESTION_RESULT, intent)
        }
    }
    override fun onBackPressed() {
        backEvent()
        super.onBackPressed()
    }

    private fun setFragmentEvent(previewList: ArrayList<QuestionBean>){
        val fragment = PreviewFragment.newInstance(previewList)
        supportFragmentManager.beginTransaction().replace(R.id.questionLayout, fragment)
                .commitAllowingStateLoss()
        questionNumTv.text = "${previewList.size}题"
        fragment.onAnalyseClick = {
            when {
                it == null -> {
                    emptyAnalyseLayout.visibility = View.VISIBLE
                }
                StringUtils.isEmpty(it.rightAnswer) -> { //正确答案为空
                    emptyAnalyseLayout.visibility = View.VISIBLE
                    T.show(this, "暂无解析")
                }
                else -> {
                    showAnalyse(it)
                }
            }
        }
        fragment.onSelectClick = {
            questionNumTv.text = "${previewList.size}题"
        }
    }

    private fun showAnalyse(questionBean: QuestionBean){
        emptyAnalyseLayout.visibility = View.GONE
        rightAnswerLayout.removeAllViews()

        var questionView: BaseQuestionView? = null
        when(QuestionTypeUtils.getIntType(questionBean)){
            QuestionTypeUtils.QUESTIONS_CHOOSE_PIC_BY_WORD,
            QuestionTypeUtils.QUESTIONS_CHOOSE_WORD_BY_PIC,
            QuestionTypeUtils.QUESTIONS_JUDGE,
            QuestionTypeUtils.QUESTIONS_CHOICE,
            QuestionTypeUtils.QUESTIONS_CHOOSE_BY_VOICE->{
                questionView = ChoiceView(this)
            }
            QuestionTypeUtils.QUESTIONS_WRITE_WORD_BY_PIC,
            QuestionTypeUtils.QUESTIONS_COMPLETION_BY_VOICE,
            QuestionTypeUtils.QUESTIONS_COMPLETION->{
                questionView = CompleteView(this)
            }
            QuestionTypeUtils.QUESTIONS_EN_ORDER_SENTENCE->{
                questionView = SortHorizontalView(this)
            }
            QuestionTypeUtils.QUESTIONS_CN_ORDER_SENTENCE->{
                questionView = SortVerticalView(this)
            }
            QuestionTypeUtils.QUESTIONS_DRAW_LINE->{
                questionView = ConnectionView(this)
            }
            QuestionTypeUtils.QUESTIONS_CLASSIFICATION->{
                questionView = ClassificationView(this)
            }
            QuestionTypeUtils.QUESTIONS_READ_ALOUD, QuestionTypeUtils.QUESTIONS_FOLLOW_READ->{
                questionView = RecorderView(this)
            }
            QuestionTypeUtils.QUESTIONS_WRITE_COMPOSITION_BY_PIC,
            QuestionTypeUtils.QUESTIONS_CHINESE_SENTENCE,
            QuestionTypeUtils.QUESTIONS_CHINESE_WRITE_BY_VOICE,
            QuestionTypeUtils.QUESTION_MATH_APPLICATION->{
                questionView = SentenceView(this)
            }
            QuestionTypeUtils.QUESTION_MATH_VERTICAL->{
                questionView = MathVerticalView(this)
            }
            QuestionTypeUtils.QUESTION_MATH_EQUATION->{
                questionView = MathEquationView(this)
            }
            QuestionTypeUtils.QUESTION_MATH_FRACTION->{
                questionView = MathFractionView(this)
            }
            QuestionTypeUtils.QUESTIONS_CHINESE_READ_UNDERSTAND->{
                questionView = UnderstandView(this)
            }
            QuestionTypeUtils.QUESTIONS_WRITE->{
                questionView = WriteView(this)
            }
        }
        rightAnswerLayout.addView(questionView)

        questionView?.setData(questionBean)
        questionView?.show(false)
        analyseContentTv.text = if (StringUtils.isEmpty(questionBean.analytical)) "" else questionBean.analytical
    }
}