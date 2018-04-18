package com.yzy.ebag.teacher.module.homework

import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.WindowManager
import android.widget.TextView
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.base.Constants
import com.yzy.ebag.teacher.bean.AssignClassBean
import com.yzy.ebag.teacher.bean.AssignGradeBean
import com.yzy.ebag.teacher.bean.AssignmentBean
import com.yzy.ebag.teacher.bean.TestPaperListBean
import ebag.core.base.BasePopupWindow
import ebag.core.base.mvp.MVPActivity
import ebag.core.http.network.MsgException
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.T
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

    /**切换版本*/
    private val exchangeVersionPopup by lazy {
        val popup = ExchangeVersionPopup(this)
        popup.onConfirmClick = {versionName,
                                versionId,
                                semesterCode,
                                semesterName,
                                subCode,
                                subName ->
            setVersionTv(versionName,
                    semesterName,
                    subName)
            setVersionCache(versionName,
                    versionId,
                    semesterCode,
                    semesterName,
                    subCode,
                    subName)
            isGradeRequest = false
            assignmentPresenter.loadDataByVersion(workCategory.toString(), versionId, subCode)
            isUnitChange = false
        }
        popup
    }

    private val unitPopup by lazy {
        val popup = UnitPopupWindow(this)
        popup.onConfirmClick = {name, unitBean ->
            val cache = cacheMap[currentGradeCode]
            cache?.currentUnitBean = unitBean ?: UnitBean.UnitSubBean()
            cache?.isTotal = unitBean == null
            if(workCategory == Constants.ASSIGN_TEST_PAPER)
                assignmentPresenter.loadTestListData(currentTestType, currentGradeCode,
                        if (cache!!.currentUnitBean?.unitCode == null) null else cache.currentUnitBean?.unitCode, cache.subCode)
            else {
                assignmentPresenter.loadDataByVersion(workCategory.toString(), cache?.versionId, cache?.subCode, unitBean?.unitCode)
                isUnitChange = true
            }
            cache?.currentUnitName = name
            unitTv.text = name
        }
        popup
    }

    /**试题adapter*/
    private val questionAdapter by lazy { QuestionsAdapter() }

    /**年级adapter*/
    private val gradeAdapter by lazy { GradeAdapter() }

    private val gradePopup by lazy { GradePopupWindow() }

    /**班级adapter*/
    private val classAdapter by lazy { ClassAdapter() }

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
    private val createClassDialog by lazy {
        val dialog = AlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage("你暂未加入班级，你可以联系对应班级班主任添加任课老师，也可自己创建班级")
                .setNegativeButton("取消", null)
                .setPositiveButton("创建班级", {dialog, which ->
//                    CreateClassActivity.jump(this)
                    finish()
                })
                .create()
        dialog
    }
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

        questionAdapter.setOnItemClickListener { holder, view, position ->
            val cache = cacheMap[currentGradeCode]!!
            //TODO 页面跳转
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

        clazzBtn.setOnClickListener {
            gradePopup.showAsDropDown(clazzBtn)
        }
        textbookBtn.setOnClickListener {
            val list = cacheMap[currentGradeCode]!!.classes
            if (list.isEmpty()){
                T.show(this, "请选择班级")
                return@setOnClickListener
            }
            exchangeVersionPopup.setData(list)
            exchangeVersionPopup.showAsDropDown(textbookBtn)
        }
        unitBtn.setOnClickListener {
            unitPopup.setData(cacheMap[currentGradeCode]!!.unitList)
            unitPopup.showAsDropDown(unitBtn)
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
        gradeAdapter.datas = gradeList
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
        classAdapter.datas = classList
        gradeAdapter.selectPosition = 0
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
            unitList!!.forEach {
                val subList = it.resultBookUnitOrCatalogVos
                if (subList.isEmpty()) {
                    val subBean = UnitBean.UnitSubBean()
                    subBean.id = it.id
                    subBean.code = it.code
                    subBean.name = it.name
                    subBean.bookVersionId = it.bookVersionId
                    subBean.pid = it.pid
                    subBean.unitCode = it.unitCode
                    subBean.isUnit = true
                    it.resultBookUnitOrCatalogVos.add(subBean)
                }
            }
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
        if (isFirst)
            classAdapter.selectPosition = 0
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
        stateView.showError()
        if(t is MsgException && t.code == "2001"){
            createClassDialog.show()
        }else {
            t.handleThrowable(this)
        }
    }

    override fun loadUnitAndQuestionStart() {
        LoadingDialogUtil.showLoading(this)
    }

    override fun getUnitAndQuestion(assignmentBean: AssignmentBean?) {
        LoadingDialogUtil.closeLoadingDialog()
        if (assignmentBean != null){
            showData(assignmentBean)
        }else{
            T.show(this, "暂无数据")
        }
    }

    override fun loadUnitAndQuestionError(t: Throwable) {
        LoadingDialogUtil.closeLoadingDialog()
        t.handleThrowable(this)
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

    inner class GradePopupWindow: BasePopupWindow(this){
        override fun getLayoutRes(): Int {
            return R.layout.popup_assgin_grade
        }

        override fun setWidth(): Int {
            return WindowManager.LayoutParams.MATCH_PARENT
        }

        override fun setHeight(): Int {
            return resources.getDimensionPixelSize(R.dimen.y400)
        }
        init {
            val gradeRecycler = contentView.findViewById<RecyclerView>(R.id.gradeRecycler)
            val classRecycler = contentView.findViewById<RecyclerView>(R.id.classRecycler)
            val confirmBtn = contentView.findViewById<TextView>(R.id.confirmBtn)
            gradeRecycler.layoutManager = LinearLayoutManager(this@AssignmentActivity)
            classRecycler.layoutManager = LinearLayoutManager(this@AssignmentActivity)
            gradeRecycler.adapter = gradeAdapter
            classRecycler.adapter = classAdapter

            gradeAdapter.setOnItemClickListener { holder, view, position ->
                gradeAdapter.selectPosition = position
                classAdapter.datas = gradeAdapter.datas[position].homeClazzInfoVos

                val cache = cacheMap[currentGradeCode]!!
                val unitList = cache.unitList
                val questionList = cache.questionList
                if (unitList.isEmpty()) {
                    isGradeRequest = true
                    assignmentPresenter.loadUnitAndQuestion(workCategory.toString(), currentGradeCode)
                    cache.isTotal = true
                    unitTv.text = "全部"
                    if (workCategory == Constants.ASSIGN_TEST_PAPER)
                        assignmentPresenter.loadTestListData(currentTestType, currentGradeCode, null, cache.subCode)
                }else {
                    questionAdapter.datas = questionList
                    setVersionTv(cache.versionName, cache.semesterName, cache.subName)
                    unitTv.text = cache.currentUnitName
                    if (workCategory == Constants.ASSIGN_TEST_PAPER)
                        assignmentPresenter.loadTestListData(currentTestType, currentGradeCode, null, cache.subCode)
                }
            }

            classAdapter.setOnItemClickListener { holder, view, position ->
                classAdapter.selectPosition = position
            }

            confirmBtn.setOnClickListener {
                dismiss()
            }
            setOnDismissListener {
                setGradeTv()
            }
        }
        private fun setGradeTv(){
            val sb = StringBuilder()
            val cache = cacheMap[currentGradeCode]!!
            cache.classes.forEach {
                sb.append("${it.className}、")
            }
            var clazzName = ""
            if(sb.isNotEmpty())
                clazzName = sb.deleteCharAt(sb.lastIndexOf("、")).toString()
            clazzTv.text = "${gradeAdapter.datas[gradeAdapter.selectPosition].gradeName} $clazzName"
        }
    }

    /**年级*/
    inner class GradeAdapter : RecyclerAdapter<AssignGradeBean>(R.layout.item_assign_popup){
        var selectPosition = -1
            set(value) {
                field = value
                currentGradeCode = datas[selectPosition].gradeCode
                notifyDataSetChanged()
            }
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: AssignGradeBean?) {
            val textView: TextView = setter.getTextView(R.id.tv)
            textView.text = entity!!.gradeName
            textView.isSelected = selectPosition != -1 && selectPosition == position
        }
    }

    /**班级*/
    inner class ClassAdapter: RecyclerAdapter<AssignClassBean>(R.layout.item_assign_popup){
        var selectPosition = -1
            set(value) {
                field = value
                val cache = cacheMap[currentGradeCode]
                val classes = cache?.classes
                if (classes!!.contains(classAdapter.datas[selectPosition])){
                    classes.remove(classAdapter.datas[selectPosition])
                }else{
                    classes.add(classAdapter.datas[selectPosition])
                }
                notifyDataSetChanged()
            }
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: AssignClassBean?) {
            val textView: TextView = setter.getTextView(R.id.tv)
            textView.text = entity?.className

            val cache = cacheMap[currentGradeCode]
            val classes = cache?.classes
            textView.isSelected = classes!!.contains(entity)
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
        var currentUnitBean: UnitBean.UnitSubBean? = UnitBean.UnitSubBean()
        var currentUnitName: String? = ""
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
