package ebag.hd.homework

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import ebag.core.base.BaseActivity
import ebag.core.bean.QuestionBean
import ebag.core.bean.TypeQuestionBean
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.hd.IParticipateCallback
import ebag.hd.ITestAidlInterface
import ebag.hd.R
import ebag.hd.activity.ReportClassActivity
import ebag.hd.activity.ReportTestActivity
import ebag.hd.base.Constants
import ebag.hd.bean.request.CommitQuestionVo
import ebag.hd.bean.request.QuestionVo
import ebag.hd.http.EBagApi
import ebag.hd.service.AIDLTestService
import ebag.hd.widget.QuestionAnalyseDialog
import ebag.hd.widget.questions.base.BaseQuestionView
import kotlinx.android.synthetic.main.activity_do_homework.*

/**
 * @author caoyu
 * @date 2018/2/2
 * @description
 */
class DoHomeworkActivity: BaseActivity() {

    companion object {
        const val RESULT_CODE = 11
        fun jump(context: Context, homeworkId: String, type: String, studentId: String? = null, testTime: Int = 0){
            context.startActivity(
                    Intent(context , DoHomeworkActivity::class.java)
                            .putExtra("homeworkId", homeworkId)
                            .putExtra("type", type)
                            .putExtra("testTime", testTime)
                            .putExtra("studentId", studentId))
        }

        fun jumpForResult(context: Activity, homeworkId: String, type: String, requestCode: Int){
            context.startActivityForResult(
                    Intent(context , DoHomeworkActivity::class.java)
                            .putExtra("homeworkId", homeworkId)
                            .putExtra("type", type),
                    requestCode
            )
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_do_homework
    }

    private lateinit var homeworkId: String
    private lateinit var type: String
    private var testTime = 45
    private var studentId = ""
    private val mParticipateCallback = object : IParticipateCallback.Stub() {
        @Throws(RemoteException::class)
        override fun setText(text: String, time: Int) {
            runOnUiThread {
                titleBar.setRightText(text)
                if (time == 0) {
                    val dialog = AlertDialog.Builder(this@DoHomeworkActivity)
                            .setTitle("温馨提示：")
                            .setMessage("答题时间到，将自动交卷自动交卷")
                            .setCancelable(false)
                            .setPositiveButton("知道了", null)
                            .create()
                    dialog.show()
                    //重新设置点击方法  让点击button后对话框不消失
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

                    }
                }
            }
        }
    }
    private lateinit var mService: ITestAidlInterface
    private val mServiceConnection by lazy {
        object : ServiceConnection {
            override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
                mService = ITestAidlInterface.Stub.asInterface(iBinder)
                try {
                    mService.start(mParticipateCallback, testTime)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }

            }

            override fun onServiceDisconnected(componentName: ComponentName) {
//                mService = null
            }
        }
    }
    override fun initViews() {

        homeworkId = intent.getStringExtra("homeworkId") ?: ""
        type = intent.getStringExtra("type") ?: ""
        studentId = intent.getStringExtra("studentId") ?: ""
        testTime = intent.getIntExtra("testTime",0)

        if(type == Constants.KSSJ_TYPE){
            val intent = Intent(this, AIDLTestService::class.java)
            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)
        }

        initQuestion()

        // 设置标题
        when(type){
            Constants.KHZY_TYPE -> {
                commitBtn.text = "提交作业"
                titleBar.setTitle("课后作业")
            }
            Constants.STZY_TYPE -> {
                commitBtn.text = "提交作业"
                titleBar.setTitle("随堂作业")
            }
            Constants.KSSJ_TYPE -> {
                commitBtn.text = "提交试卷"
                titleBar.setTitle("考试试卷")
            }
            Constants.ERROR_TOPIC_TYPE -> {
                commitBtn.text = "纠正"
                titleBar.setTitle("错题纠正")
            }
            Constants.REPORT_TYPE ->{
                commitBtn.visibility = View.INVISIBLE
                titleBar.setTitle("作业报告")
            }
        }

        commitQuestionVo.homeWorkId = homeworkId
        // 点击提交按钮
        commitBtn.setOnClickListener {
            var hasDone = false
            var hasAllDone = true
            commitQuestionVo.clear()
            typeAdapter.data.forEach {
                if(it is TypeQuestionBean?) {
                    it?.questionVos?.forEach {
                        commitQuestionVo.add(QuestionVo(it.questionId, it.answer, it.type))
                        if (StringUtils.isEmpty(it.answer) && hasAllDone) {
                            hasAllDone = false
                        }
                        if (!StringUtils.isEmpty(it.answer) && !hasDone) {
                            hasDone = true
                        }
                    }
                }
            }

            // 没有做作业
            if(!hasDone){
                // 设置标题
                when(type){
                    Constants.KHZY_TYPE -> {
                        T.show(this, "作业要做哦！")
                    }
                    Constants.STZY_TYPE -> {
                        T.show(this, "作业要做哦！")
                    }
                    Constants.KSSJ_TYPE -> {
                        T.show(this, "不准交白卷哦！")
                    }
                    Constants.ERROR_TOPIC_TYPE -> {
                        T.show(this, "错题需要纠正哦！")
                    }
                }
            }else if(hasAllDone){ // 做了作业, 并且全部做完
                commit()
            }else{ // 做了作业, 没有全部做完
                commitTipDialog.show()
            }

        }

        stateView.setOnRetryClickListener {
            deatilRequest()
        }

        deatilRequest()

    }

    private val commitQuestionVo = CommitQuestionVo()

    private val questionAdapter = QuestionAdapter()
    private val typeAdapter = OverviewAdapter()
    private lateinit var typeQuestionList: List<TypeQuestionBean?>
    private var questionList: List<QuestionBean>? = null
    private val analyseDialog by lazy {
        QuestionAnalyseDialog(this)
    }
    private val questionManager = LinearLayoutManager(this)
    private fun initQuestion() {
        questionRecycler.layoutManager = questionManager
        questionRecycler.adapter = questionAdapter

        questionAdapter.onDoingListener = BaseQuestionView.OnDoingListener {
            typeAdapter.notifyDataSetChanged()
        }

        questionAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.analyseTv){
                analyseDialog.show(questionAdapter.data[position])
            }
        }
        when(type){
            Constants.REPORT_TYPE ->{
                questionAdapter.canDo = false
                questionAdapter.isShowAnalyseTv = true
            }
            Constants.ERROR_TOPIC_TYPE ->{
                questionAdapter.canDo = true
                questionAdapter.isShowAnalyseTv = true
            }
            else ->{
                questionAdapter.canDo = true
                questionAdapter.isShowAnalyseTv = false
            }
        }

        typeRecycler.layoutManager = GridLayoutManager(this, 5)
        typeAdapter.setSpanSizeLookup { gridLayoutManager, position ->
            if(typeAdapter.getItemViewType(position) == TypeQuestionBean.TYPE){
                gridLayoutManager.spanCount
            }else{
                1
            }
        }
        typeRecycler.adapter = typeAdapter

        typeAdapter.setOnItemClickListener { _, _, position ->
            if(typeAdapter.getItemViewType(position) == TypeQuestionBean.ITEM){
                val item = typeAdapter.getItem(position) as QuestionBean? ?: return@setOnItemClickListener

                val parentPosition = typeAdapter.getParentPosition(item)
                val ss = typeAdapter.getItem(parentPosition) as TypeQuestionBean?
                if(ss != null){
                    val sss = ss.questionVos
                    if(sss === questionList){
                        questionManager.scrollToPositionWithOffset(item.position, 0)
                    }else{
                        questionList = sss
                        questionAdapter.setNewData(questionList)
                        questionRecycler.postDelayed({
                            questionManager.scrollToPositionWithOffset(item.position, 0)
                        }, 100)

                    }
                }

            }

        }
    }

    private val commitTipDialog by lazy {

        // 设置标题
        val message = when(type){
            Constants.ERROR_TOPIC_TYPE -> {
                "题目没有完全纠正完，确定提交么？"
            }
            else ->{
                "题目没有完全做完，确定提交么？"
            }
        }

        AlertDialog.Builder(this)
                .setMessage(message)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定") { _, _ ->
                    commit()
                }
                .create()
    }

    private val commitCallback = object :RequestCallBack<String>(){

        override fun onStart() {
            LoadingDialogUtil.showLoading(this@DoHomeworkActivity, "提交中，请稍等...")
        }

        override fun onSuccess(entity: String?) {
            T.show(this@DoHomeworkActivity, "提交成功")
            when(type){
                Constants.STZY_TYPE -> {
                    ReportClassActivity.jump(this@DoHomeworkActivity, homeworkId)
                    finish()
                }
                Constants.KHZY_TYPE,
                Constants.KSSJ_TYPE -> {
                    ReportTestActivity.jump(this@DoHomeworkActivity, homeworkId)
                    finish()
                }
                Constants.ERROR_TOPIC_TYPE -> {
                    finish()
                    setResult(RESULT_CODE)
                }
            }
            LoadingDialogUtil.closeLoadingDialog()
        }

        override fun onError(exception: Throwable) {
            T.show(this@DoHomeworkActivity, "提交失败")
            LoadingDialogUtil.closeLoadingDialog()
        }

    }

    /**
     * 提交 作业
     */
    private fun commit(){
        when(type){
            Constants.KHZY_TYPE,
            Constants.STZY_TYPE,
            Constants.KSSJ_TYPE -> {
                EBagApi.commitHomework(commitQuestionVo, commitCallback)
            }
            Constants.ERROR_TOPIC_TYPE -> {
                EBagApi.errorCorrection(commitQuestionVo, commitCallback)
            }
        }

    }


    private val detailRequest = object :RequestCallBack<List<TypeQuestionBean>>(){
        override fun onStart() {
            stateView.showLoading()
        }
        override fun onSuccess(entity: List<TypeQuestionBean>?) {
            typeQuestionList = entity ?: ArrayList()
            if(typeQuestionList.isNotEmpty()){
                (0 until typeQuestionList.size).forEach {
                    typeQuestionList[it]?.initQuestionPosition(it)
                }
                stateView.showContent()
                typeAdapter.setNewData(typeQuestionList)
                typeAdapter.expandAll()
                questionList = typeQuestionList[0]?.questionVos
                questionAdapter.setNewData(questionList)
            }else{
                stateView.showEmpty()
            }
        }

        override fun onError(exception: Throwable) {
//            if(exception is MsgException)
//                stateView.showError(exception.message)
            stateView.showError()
            exception.handleThrowable(this@DoHomeworkActivity)
        }
    }

    private fun deatilRequest(){
        when(type){
        // 错题
            Constants.ERROR_TOPIC_TYPE -> {
                getErrorDetail()
            }

            else -> {
                getHomeworkDetail()
            }
        }
    }
    /**
     * 获取作业详情
     */
    private fun getHomeworkDetail(){
        EBagApi.getQuestions(homeworkId, type, studentId, detailRequest)
    }

    /**
     * 获取错题详情
     */
    private fun getErrorDetail(){
        EBagApi.getErrorDetail(homeworkId, detailRequest)
    }

}