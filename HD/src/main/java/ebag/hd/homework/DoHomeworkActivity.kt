package ebag.hd.homework

import android.annotation.TargetApi
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Rect
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.IBinder
import android.os.Message
import android.os.RemoteException
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.util.DisplayMetrics
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import ebag.core.base.BaseActivity
import ebag.core.bean.QuestionBean
import ebag.core.bean.TypeQuestionBean
import ebag.core.http.network.MsgException
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.*
import ebag.core.xRecyclerView.adapter.OnItemChildClickListener
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder
import ebag.hd.IParticipateCallback
import ebag.hd.ITestAidlInterface
import ebag.hd.R
import ebag.hd.activity.ReportClassActivity
import ebag.hd.activity.ReportTestActivity
import ebag.hd.base.Constants
import ebag.hd.bean.request.CommitQuestionVo
import ebag.hd.bean.request.QuestionVo
import ebag.hd.bean.response.UserEntity
import ebag.hd.http.EBagApi
import ebag.hd.service.AIDLTestService
import ebag.hd.widget.QuestionAnalyseDialog
import ebag.hd.widget.keyboard.KeyBoardView
import ebag.hd.widget.questions.base.BaseQuestionView
import kotlinx.android.synthetic.main.activity_do_homework.*
import java.io.File
import java.lang.reflect.Method

/**
 * @author caoyu
 * @date 2018/2/2
 * @description
 */
class DoHomeworkActivity: BaseActivity() {

    companion object {
        const val RESULT_CODE = 11
        fun jump(context: Context, homeworkId: String, type: String, workType: String, studentId: String? = null, testTime: Int = 0){
            context.startActivity(
                    Intent(context , DoHomeworkActivity::class.java)
                            .putExtra("homeworkId", homeworkId)
                            .putExtra("type", type)
                            .putExtra("testTime", testTime)
                            .putExtra("studentId", studentId)
                            .putExtra("workType", workType)
            )
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
    private lateinit var uid: String
    private lateinit var type: String
    private var workType = ""
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
                        getRequestData()
                        commit()
                    }
                }
            }
        }
    }
    private var mService: ITestAidlInterface? = null
    private val mServiceConnection by lazy {
        object : ServiceConnection {
            override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
                mService = ITestAidlInterface.Stub.asInterface(iBinder)
                try {
                    mService!!.start(mParticipateCallback, testTime)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }

            }

            override fun onServiceDisconnected(componentName: ComponentName) {
                mService = null
            }
        }
    }
    override fun initViews() {
        val userEntity: UserEntity = SerializableUtils.getSerializable(Constants.STUDENT_USER_ENTITY)
        uid = userEntity.uid
        homeworkId = intent.getStringExtra("homeworkId") ?: ""
        type = intent.getStringExtra("type") ?: ""
        workType = intent.getStringExtra("workType") ?: ""
        studentId = intent.getStringExtra("studentId") ?: ""
        testTime = intent.getIntExtra("testTime",0)

        if(type == Constants.KSSJ_TYPE){
            val intent = Intent(this, AIDLTestService::class.java)
            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)
            titleBar.hiddenTitleLeftButton()
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

            val status = getRequestData()
            when(status){
                1 -> {// 没有做作业
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
                }
                2 -> {// 做了作业, 并且全部做完
                    commit()
                }
                3 -> {// 做了作业, 没有全部做完
                    commitTipDialog.show()
                }
            }
        }

        stateView.setOnRetryClickListener {
            deatilRequest()
        }

        deatilRequest()

        hiddenImage.setOnClickListener {
            linearlayou_keyboard.visibility = View.GONE
        }

        //获取屏幕高度
        val screenHeight = this.windowManager.defaultDisplay.height
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 4
        typeRecycler.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            KeyBoardView.isboolean = isSoftShowing()

            if (oldBottom != 0 && bottom != 0 && oldBottom - bottom > keyHeight) {
                key_board.setSystemInput(true)
            } else if (oldBottom != 0 && bottom != 0 && bottom - oldBottom > keyHeight) {
                key_board.setSystemInput(false)
            }
        }
    }
    private var keyHeight = 0
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun getSoftButtonsBarHeight(): Int {
        val metrics = DisplayMetrics()
        //这个方法获取可能不是真实屏幕的高度
        windowManager.defaultDisplay.getMetrics(metrics)
        val usableHeight = metrics.heightPixels
        //获取当前屏幕的真实高度
        windowManager.defaultDisplay.getRealMetrics(metrics)
        val realHeight = metrics.heightPixels
        return if (realHeight > usableHeight) {
            realHeight - usableHeight
        } else {
            0
        }
    }
    private fun isSoftShowing(): Boolean {
        //获取当前屏幕内容的高度
        val screenHeight = window.decorView.height
        //获取View可见区域的bottom
        val rect = Rect()
        window.decorView.getWindowVisibleDisplayFrame(rect)

        return screenHeight - rect.bottom - getSoftButtonsBarHeight() != 0
    }

    fun bindKeyBoard(editText: EditText, keyboardType: Int) {
        if (key_board != null) {
            hideSystemSofeKeyboard(editText)
            key_board_special_numeric_left.bindEditText(editText, KeyBoardView.m_special_numeric, linearlayou_keyboard, nestedScrollView_1, key_board_special_numeric_left)
            key_board_special_numeric_left.showKeyboard(KeyBoardView.m_special_numeric)
            key_board.bindEditText(editText, if (keyboardType != KeyBoardView.number_keyboard && isEnglish()) KeyBoardView.eng_keyboard else keyboardType, linearlayou_keyboard, nestedScrollView_1, key_board_special_numeric_left)
        }
    }

    fun isEnglish(): Boolean{
        return true
    }

    private fun getRequestData() : Int{
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
        return if(!hasDone) 1 else if(hasAllDone) 2 else 3
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
        questionRecycler.isNestedScrollingEnabled = true

        questionAdapter.onDoingListener = BaseQuestionView.OnDoingListener {
            typeAdapter.notifyDataSetChanged()
        }

        questionAdapter.setOnItemChildClickListener(questionClickListener)

        questionAdapter.setOnItemChildClickListener { adapter, view, position ->
            adapter as QuestionAdapter
            if (view.id == R.id.analyseTv){
                analyseDialog.show(questionAdapter.data[position])
            }
            recorderClick(view, position, adapter)
        }
        when(type){
            Constants.REPORT_TYPE ->{
                questionAdapter.canDo = false
                questionAdapter.isShowAnalyseTv = true
                questionAdapter.showResult = true
            }
            Constants.ERROR_TOPIC_TYPE ->{
                questionAdapter.canDo = true
                questionAdapter.isShowAnalyseTv = true
                typeAdapter.showResult = true
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
                    ReportClassActivity.jump(this@DoHomeworkActivity, homeworkId, workType)
                    finish()
                }
                Constants.KHZY_TYPE,
                Constants.KSSJ_TYPE -> {
                    ReportTestActivity.jump(this@DoHomeworkActivity, homeworkId, workType)
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
            LoadingDialogUtil.closeLoadingDialog()
            if(exception is MsgException && exception.code == "2003"){
                T.show(this@DoHomeworkActivity, "你还有未作答正确的试题，请检查并纠正后重新提交")
                typeAdapter.wrongIds.clear()
                typeAdapter.wrongIds.addAll(exception.message.toString().split(","))
                typeAdapter.notifyDataSetChanged()
            }else{
                exception.handleThrowable(this@DoHomeworkActivity)
            }
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

    override fun onDestroy() {
        if (type == Constants.KSSJ_TYPE) {
            if (mService != null) {
                try {
                    mService!!.unregisterParticipateCallback(mParticipateCallback)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }

            }
            unbindService(mServiceConnection)
        }
        voicePlayer.stop()
        super.onDestroy()
    }
    /**
     * 获取作业详情
     */
    private fun getHomeworkDetail(){
        EBagApi.getQuestions(homeworkId, workType, studentId, detailRequest)
    }

    /**
     * 获取错题详情
     */
    private fun getErrorDetail(){
        EBagApi.getErrorDetail(homeworkId, detailRequest)
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

    override fun onBackPressed() {
        if (type == Constants.KSSJ_TYPE){
            T.show(this, "考试中不能直接点击返回键退出")
        }else {
            super.onBackPressed()
        }
    }
    //----------------------------------录音相关
    private var recorderAnim : AnimationDrawable? = null
    private val recorderUtil by lazy {
        val recorderUtil = RecorderUtil(this)
        recorderUtil.setFinalFileName(FileUtil.getRecorderPath() + "questionRecorder.amr")
        recorderUtil
    }
    private var playBtn: TextView? = null
    private var uploadBtn: TextView? = null
    private var tempPosition: Int = -1
    private lateinit var currentQuestionBean: QuestionBean
    private lateinit var cloudFileName: String
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
        cloudFileName = "$uid$homeworkId${currentQuestionBean.questionId}.amr"
        recorderAnim!!.stop()
        recorderAnim!!.selectDrawable(0)
        recorderUtil.finishRecord()
        //上传录音
        OSSUploadUtils.getInstance().uploadFileToOss(
                this,
                File(FileUtil.getRecorderPath() + "questionRecorder.amr"),
                "personal/questionRecorder",
                cloudFileName,
                mHandler)
        LoadingDialogUtil.showLoading(this, "上传录音...")
    }

    private val mHandler = MyHandler(this)
    class MyHandler(activity: DoHomeworkActivity): HandlerUtil<DoHomeworkActivity>(activity){
        override fun handleMessage(activity: DoHomeworkActivity?, msg: Message?) {
            when {
                msg!!.what == ebag.core.util.Constants.UPLOAD_SUCCESS ->{//上传文件成功
                    activity!!.playBtn!!.visibility = View.VISIBLE
                    activity.uploadBtn!!.visibility = View.INVISIBLE
                    LoadingDialogUtil.closeLoadingDialog()
                    T.show(activity, "上传成功")
                    activity.currentQuestionBean.answer = "${ebag.core.util.Constants.OSS_BASE_URL}/personal/questionRecorder/${activity.cloudFileName}"
                    activity.typeAdapter.notifyDataSetChanged()
                }
                msg.what == ebag.core.util.Constants.UPLOAD_FAIL ->{//上传文件失败
                    LoadingDialogUtil.closeLoadingDialog()
                    T.show(activity!!, "录音上传失败，请点击上传按钮重试")
                }
            }
        }
    }
    private fun recorderClick(view: View, position: Int, adapter: QuestionAdapter){
        currentQuestionBean = adapter.data[position]
        when (view.id){
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

    /**
     * 隐藏系统键盘
     *
     * @param editText
     */
    private fun hideSystemSofeKeyboard(editText: EditText) {
        if (Build.VERSION.SDK_INT >= 11) {
            try {
                val cls = EditText::class.java
                val setShowSoftInputOnFocus: Method
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", Boolean::class.javaPrimitiveType)
                setShowSoftInputOnFocus.isAccessible = true
                setShowSoftInputOnFocus.invoke(editText, false)

            } catch (e: SecurityException) {
                e.printStackTrace()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            editText.inputType = InputType.TYPE_NULL
        }
    }
}