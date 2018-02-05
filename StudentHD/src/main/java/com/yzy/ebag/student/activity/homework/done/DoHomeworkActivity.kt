package com.yzy.ebag.student.activity.homework.done

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.yzy.ebag.student.R
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.BaseActivity
import ebag.core.bean.QuestionBean
import ebag.core.bean.TypeQuestionBean
import ebag.core.http.network.MsgException
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import kotlinx.android.synthetic.main.activity_do_homework.*

/**
 * @author caoyu
 * @date 2018/2/2
 * @description
 */
class DoHomeworkActivity: BaseActivity() {

    companion object {
        fun jump(context: Context, homeworkId: String, type: String){
            context.startActivity(
                    Intent(context , DoHomeworkActivity::class.java)
                            .putExtra("homeworkId", homeworkId)
                            .putExtra("type", type)
            )
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_do_homework
    }

    private lateinit var homeworkId: String
    private lateinit var type: String
    override fun initViews() {

        homeworkId = intent.getStringExtra("homeworkId") ?: ""
        type = intent.getStringExtra("type") ?: ""

        initQuestion()

        when(type){
            com.yzy.ebag.student.base.Constants.KHZY_TYPE -> {
                titleBar.setTitle("课后作业")
            }
            com.yzy.ebag.student.base.Constants.STZY_TYPE -> {
                titleBar.setTitle("随堂作业")
            }
            com.yzy.ebag.student.base.Constants.KSSJ_TYPE -> {
                titleBar.setTitle("考试试卷")
            }
        }

        StudentApi.getQuestions(homeworkId, object :RequestCallBack<List<TypeQuestionBean>>(){
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
                if(exception is MsgException)
                    stateView.showError(exception.message)
                exception.handleThrowable(this@DoHomeworkActivity)
            }
        })
    }

    private val questionAdapter = QuestionAdapter()
    private val typeAdapter = OverviewAdapter()
    private lateinit var typeQuestionList: List<TypeQuestionBean?>
    private var questionList: List<QuestionBean>? = null

    private val questionManager = LinearLayoutManager(this)
    private fun initQuestion() {
        questionRecycler.layoutManager = questionManager
        questionRecycler.adapter = questionAdapter

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
}