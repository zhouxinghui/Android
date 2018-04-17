package com.yzy.ebag.teacher.module.homework

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.base.Constants
import com.yzy.ebag.teacher.bean.AssignClassBean
import com.yzy.ebag.teacher.bean.AssignGradeBean
import com.yzy.ebag.teacher.bean.AssignmentBean
import com.yzy.ebag.teacher.bean.TestPaperListBean
import ebag.core.base.mvp.MVPActivity
import ebag.core.util.loadImage
import ebag.core.xRecyclerView.adapter.RecyclerAdapter
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder
import ebag.mobile.bean.UnitBean
import kotlinx.android.synthetic.main.activity_assignment.*

class AssignmentActivity : MVPActivity(), AssignmentView{
    override fun getLayoutId(): Int {
        return R.layout.activity_assignment
    }

    companion object {
        fun jump(context: Context, workCategory: Int, title: String){
            context.startActivity(
                    Intent(context, AssignmentActivity::class.java)
                            .putExtra(Constants.ASSIGN_CATEGORY, workCategory)
                            .putExtra(Constants.ASSIGN_TITLE, title)
            )
        }
    }
    /**作业类型*/
    private var workCategory = 0
    private val assignmentPresenter by lazy { AssignmentPresenter(this,this) }
    /**试题adapter*/
    private val questionAdapter by lazy { QuestionsAdapter() }
    /**数据保存*/
    private val cacheMap by lazy { HashMap<String, Cache>() }
    /**当前年级code*/
    private var currentGradeCode = ""
    /**年级list*/
    private var gradeList : ArrayList<AssignGradeBean>? = null
    /**单元list*/
    private var unitList: ArrayList<UnitBean>? = null
    /**是否改变单元*/
    private var isUnitChange = false
    /** 1   系统试卷  2  我的试卷*/
    private var currentTestType = "1"
    /**是否是切换年级做的网络请求*/
    private var isGradeRequest = true
    /**难易度*/
    private var difficulty: String? = null
    override fun initViews() {
        workCategory = intent.getIntExtra(Constants.ASSIGN_CATEGORY, 0)
        titleBar.setTitle(intent.getStringExtra(Constants.ASSIGN_TITLE) ?: "")

        //题型
        val questionLayoutManager = GridLayoutManager(this,4)
        questionsRecycler.adapter = questionAdapter
        questionsRecycler.layoutManager = questionLayoutManager

        assignmentPresenter.loadBaseData(workCategory.toString())
        stateView.setOnRetryClickListener {
            assignmentPresenter.loadBaseData(workCategory.toString())
        }

        difficultyGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.unlimitedBtn ->{
                    difficulty = null
                }
                R.id.easyBtn ->{
                    difficulty = "1"
                }
                R.id.secondaryBtn ->{
                    difficulty = "2"
                }
                R.id.hardBtn ->{
                    difficulty = "3"
                }
            }
        }
    }

    override fun destroyPresenter() {
        assignmentPresenter.onDestroy()
    }

    override fun loadBaseStart() {
        stateView.showLoading()
    }

    override fun showBaseData(assignmentBean: AssignmentBean?) {
        stateView.showContent()
        gradeList = assignmentBean?.sendHomePageClazzInfoVos as ArrayList<AssignGradeBean>
        if (gradeList == null || gradeList!!.isEmpty()){
            stateView.showEmpty("未加入班级")
            return
        }
        val classList = assignmentBean.sendHomePageClazzInfoVos!![0].homeClazzInfoVos
        val firstClassId = assignmentBean.resultTaughtCoursesVo?.classId
        var tempClassBean: AssignClassBean? = null
        classList.forEach {
            if (it.classId == firstClassId) {
                tempClassBean = it
                return@forEach
            }
        }
        if (tempClassBean != null) {
            classList.remove(tempClassBean)
            classList.add(0, tempClassBean)
        }
        currentGradeCode = gradeList!![0].gradeCode
        val name =
        if (classList.isEmpty())
            "${gradeList!![0].gradeName}"
        else
            "${gradeList!![0].gradeName} ${classList[0].className}"
        clazzTv.text = name

        if (workCategory == Constants.ASSIGN_TEST_PAPER)
            assignmentPresenter.loadTestListData(currentTestType, currentGradeCode, null, assignmentBean.resultTaughtCoursesVo?.bookCode)
        gradeList!!.forEach {
            if(cacheMap[it.gradeCode] == null) {
                val cache = Cache()
                cacheMap[it.gradeCode] = cache
            }
        }

        showData(assignmentBean, true)
    }

    private fun showData(assignmentBean: AssignmentBean?, isFirst: Boolean = false) {
        val questionList = assignmentBean?.resultAdvertisementVos
        questionAdapter.datas = questionList

        if (!isUnitChange) {
            unitList = assignmentBean?.sendHomePageClazzInfoVos!![0].bookVersionOrUnitVos as ArrayList<UnitBean>
            cacheMap[currentGradeCode]!!.unitList = unitList as ArrayList<UnitBean>
        }

        cacheMap[currentGradeCode]!!.questionList = questionList as ArrayList<AssignmentBean.QuestionsBean>
        if (isGradeRequest) {
            val versionBean = assignmentBean.resultTaughtCoursesVo
            if (versionBean == null) {
                textBookVersion.text = ""
                return
            }
            setVersionTv(versionBean.bookVersionName,
                    versionBean.semeterName,
                    versionBean.bookName)
            setVersionCache(versionBean.bookVersionName,
                    versionBean.bookVersionId,
                    versionBean.semeterCode,
                    versionBean.semeterName,
                    versionBean.bookCode,
                    versionBean.bookName)
        }
    }

    private fun setVersionTv(version: String?, semester: String?, subName: String?){
        textBookVersion.text = getString(R.string.textbook_name,
                version,
                semester,
                subName)
    }
    private fun setVersionCache(versionName: String,
                                versionId: String,
                                semesterCode: String,
                                semesterName: String,
                                subCode: String,
                                subName: String){
        val cache = cacheMap[currentGradeCode]!!
        cache.versionName = versionName
        cache.versionId = versionId
        cache.semesterCode = semesterCode
        cache.semesterName = semesterName
        cache.subCode = subCode
        cache.subName = subName
        if (workCategory == Constants.ASSIGN_TEST_PAPER)
            assignmentPresenter.loadTestListData(currentTestType, currentGradeCode, null, cache.subCode)
    }

    override fun loadBaseError(t: Throwable) {
    }

    override fun loadUnitAndQuestionStart() {
    }

    override fun getUnitAndQuestion(assignmentBean: AssignmentBean?) {
    }

    override fun loadUnitAndQuestionError(t: Throwable) {
    }

    override fun loadTestListStart() {
    }

    override fun getTestList(testList: List<TestPaperListBean>?) {
    }

    override fun loadTestListError(t: Throwable) {
    }

    /**试题*/
    inner class QuestionsAdapter: RecyclerAdapter<AssignmentBean.QuestionsBean>(R.layout.item_assignment_questions){
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: AssignmentBean.QuestionsBean) {
            if(entity.questionList.size != 0) {
                setter.setVisible(R.id.point_id, true)
                setter.setText(R.id.point_id, entity.questionList.size.toString())
            }else{
                setter.setVisible(R.id.point_id, false)
            }
            setter.setText(R.id.question_name_id, entity.adverName)
            setter.getImageView(R.id.question_image_id).loadImage(entity.adverUrl)
        }
    }

    /**
     * 数据缓存类
     */
    inner class Cache{
        var classes = ArrayList<AssignClassBean>()
        var unitList = ArrayList<UnitBean>()
        var isTotal = true
        var questionList = ArrayList<AssignmentBean.QuestionsBean>()
        var currentUnitBean = UnitBean.UnitSubBean()
        var versionId = ""
        var versionName = ""

        var semesterCode = ""
        var semesterName = ""
        var subCode = ""
        var subName = ""
        fun clearQuestionSelected(){
            questionList.forEach { it.questionList.clear() }
        }
    }

}
