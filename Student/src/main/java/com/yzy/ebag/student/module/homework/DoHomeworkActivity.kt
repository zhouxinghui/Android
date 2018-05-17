package com.yzy.ebag.student.module.homework

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.WindowManager
import com.yzy.ebag.student.IParticipateCallback
import com.yzy.ebag.student.ITestAidlInterface
import com.yzy.ebag.student.R
import com.yzy.ebag.student.http.StudentApi
import com.yzy.ebag.student.service.AIDLTestService
import ebag.core.base.BaseActivity
import ebag.core.base.BaseDialog
import ebag.core.bean.QuestionBean
import ebag.core.bean.QuestionTypeUtils
import ebag.core.bean.TypeQuestionBean
import ebag.core.http.network.MsgException
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.mobile.base.Constants
import ebag.mobile.bean.request.CommitQuestionVo
import ebag.mobile.bean.request.QuestionVo
import ebag.mobile.http.EBagApi
import ebag.mobile.module.homework.HomeworkDescFragment
import ebag.mobile.module.homework.QuestionTypeAdapter
import ebag.mobile.module.homework.WorkReportActivity
import kotlinx.android.synthetic.main.activity_do_homework.*

/**
 * Created by YZY on 2018/4/28.
 */
class DoHomeworkActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_do_homework

    companion object {
        fun jump(context: Context, homeworkId: String, workType: String, testTime: Int = 0) {
            context.startActivity(
                    Intent(context, DoHomeworkActivity::class.java)
                            .putExtra("homeworkId", homeworkId)
                            .putExtra("testTime", testTime)
                            .putExtra("workType", workType)
            )
        }
    }

    private lateinit var homeworkId: String
    private var workType = ""
    private val typeAdapter by lazy {
        val adapter = QuestionTypeAdapter()
        adapter.showResult = false
        adapter
    }
    private lateinit var typeQuestionList: List<TypeQuestionBean?>
    private var fragments: Array<HomeworkDescFragment?>? = null

    private val typeDialog by lazy {
        TypeDialog()
    }
    /*考试倒计时*/
    private var testTime = 45
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
    /*考试倒计时*/

    /**试题请求*/
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
                fragments = arrayOfNulls(typeQuestionList.size)
                viewPager.adapter = ViewPagerAdapter(fragments)
                viewPager.offscreenPageLimit = 2
            } else {
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
    /**保存需要提交的试题*/
    private val commitQuestionVo = CommitQuestionVo()
    override fun initViews() {
        homeworkId = intent.getStringExtra("homeworkId") ?: ""
        workType = intent.getStringExtra("workType") ?: ""
        testTime = intent.getIntExtra("testTime", 0)

        checkQuestion.setOnClickListener {
            typeDialog.show()
        }

        stateView.setOnRetryClickListener {
            request()
        }
        typeAdapter.onSubItemClickListener = {parentPosition, position ->
            viewPager.setCurrentItem(parentPosition, false)
            fragments!![parentPosition]?.scrollTo(position)
            typeDialog.dismiss()
        }
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                titleBar.setTitle(QuestionTypeUtils.getTitle(typeQuestionList[position]?.type))
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        request()

        if(workType == Constants.KSSJ_TYPE){
            val intent = Intent(this, AIDLTestService::class.java)
            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)
            titleBar.hiddenTitleLeftButton()
        }

        commitTv.setOnClickListener {
            val status = getRequestData()
            when(status){
                1 -> {// 没有做作业
                    // 设置标题
                    when(workType){
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
    /**提交作业的请求*/
    private val commitCallback = object :RequestCallBack<String>(){
        override fun onStart() {
            LoadingDialogUtil.showLoading(this@DoHomeworkActivity, "提交中，请稍等...")
        }

        override fun onSuccess(entity: String?) {
            T.show(this@DoHomeworkActivity, "提交成功")
            when(workType){
                Constants.ERROR_TOPIC_TYPE -> {
                    finish()
                }
                else ->{
                    WorkReportActivity.jump(this@DoHomeworkActivity, homeworkId, workType)
                    finish()
                }
            }
            LoadingDialogUtil.closeLoadingDialog()
        }

        override fun onError(exception: Throwable) {
            LoadingDialogUtil.closeLoadingDialog()
            if(exception is MsgException && exception.code == "2003"){
                T.show(this@DoHomeworkActivity, "你还有未作答正确的试题，请检查并纠正后重新提交")
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
        when(workType){
            Constants.KHZY_TYPE,
            Constants.STZY_TYPE,
            Constants.KSSJ_TYPE -> {
                StudentApi.commitHomework(commitQuestionVo, commitCallback)
            }
            Constants.ERROR_TOPIC_TYPE -> {
                StudentApi.errorCorrection(commitQuestionVo, commitCallback)
            }
        }

    }

    private val commitTipDialog by lazy {
        // 设置标题
        val message = when(workType){
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

    private fun request() {
        if (workType == Constants.ERROR_TOPIC_TYPE) {
            EBagApi.getErrorDetail(homeworkId, null, detailRequest)
            commitTv.text = "纠正"
        } else {
            EBagApi.getQuestions(homeworkId, workType, null, detailRequest)
            commitTv.text = "提交"
        }
    }

    private inner class TypeDialog : BaseDialog(this) {
        override fun getLayoutRes(): Int = R.layout.recycler_view_layout

        override fun setWidth(): Int = WindowManager.LayoutParams.MATCH_PARENT
        override fun setHeight(): Int = resources.getDimensionPixelSize(R.dimen.y700)
        override fun getGravity(): Int = Gravity.BOTTOM

        init {
            val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this@DoHomeworkActivity)
            recyclerView.adapter = typeAdapter
        }
    }

    private inner class ViewPagerAdapter(val fragments: Array<HomeworkDescFragment?>?): FragmentPagerAdapter(supportFragmentManager){
        override fun getItem(position: Int): Fragment {
            if (fragments!![position] == null){
                fragments[position] = HomeworkDescFragment.newInstance(
                        typeQuestionList[position]?.questionVos as ArrayList<QuestionBean?>,
                        workType,
                        false,
                        homeworkId)
                fragments[position]!!.onDoingListener = {
                    typeAdapter.notifyDataSetChanged()
                }
                return fragments[position]!!
            }
            return HomeworkDescFragment.newInstance(ArrayList(), workType, false)
        }

        override fun getCount(): Int = fragments?.size ?: 0
    }

    override fun onDestroy() {
        if (workType == Constants.KSSJ_TYPE) {
            if (mService != null) {
                try {
                    mService!!.unregisterParticipateCallback(mParticipateCallback)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }

            }
            unbindService(mServiceConnection)
        }
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (workType == Constants.KSSJ_TYPE){
            T.show(this, "考试中不能直接点击返回键退出")
        }else {
            super.onBackPressed()
        }
    }
}