package com.yzy.ebag.teacher.module.homework

import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.base.Constants
import com.yzy.ebag.teacher.bean.AssignClassBean
import com.yzy.ebag.teacher.bean.AssignGradeBean
import com.yzy.ebag.teacher.bean.AssignmentBean
import com.yzy.ebag.teacher.bean.TestPaperListBean
import com.yzy.ebag.teacher.module.clazz.CreateClassActivity
import ebag.core.base.BasePopupWindow
import ebag.core.base.mvp.MVPActivity
import ebag.core.bean.QuestionBean
import ebag.core.http.network.MsgException
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.core.util.loadImage
import ebag.core.xRecyclerView.adapter.RecyclerAdapter
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder
import ebag.mobile.bean.UnitBean
import kotlinx.android.synthetic.main.activity_assignment.*

class AssignmentActivity : MVPActivity(), AssignmentView{
    override fun getLayoutId(): Int = R.layout.activity_assignment

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

    /**单元popupWindow*/
    private val unitPopup by lazy {
        val popup = UnitPopupWindow(this)
        popup.onConfirmClick = {name, unitBean ->
            val cache = cacheMap[currentGradeCode]
            cache?.currentUnitBean = unitBean ?: UnitBean.UnitSubBean()
            cache?.isTotal = unitBean == null
            if(workCategory == Constants.ASSIGN_TEST_PAPER)
                testFragment.requestData(currentTestType, currentGradeCode,
                        if (cache!!.currentUnitBean?.unitCode == null) null else cache.currentUnitBean?.unitCode, cache.subCode)
            assignmentPresenter.loadDataByVersion(workCategory.toString(), cache?.versionId, cache?.subCode,
                    if (cache!!.currentUnitBean?.unitCode == null) null else cache.currentUnitBean?.unitCode)
            isUnitChange = true
            cache?.currentUnitName = name
            unitTv.text = name
        }
        popup
    }

    /**试题adapter*/
    private val questionAdapter by lazy { QuestionsAdapter() }

    /**试卷列表adapter*/
    private val testFragment by lazy {
        val fragment = TestPaperFragment.newInstance()
        fragment.onItemClickListener = {adapter, position ->
            adapter.selectPosition = position
            currentPaperId = adapter.data[position].testPaperId
            currentPaperName = adapter.data[position].testPaperName
        }
        fragment.onTestDataReceive = {
            if(it == null || it.isEmpty()){
                if (!isOrganizeTest)
                    emptyTestTv.visibility = View.VISIBLE
            }else{
                emptyTestTv.visibility = View.GONE
                currentPaperId = ""
                currentPaperName = ""
            }
        }
        fragment
    }

    private var currentPaperId: String? = null
    private var currentPaperName: String? = null
    private var isOrganizeTest = false
    private var isSaveTest = false

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

    /**组卷（填写试卷名）对话框*/
    private val organizePaperDialog by lazy {
        val dialog = OrganizePaperDialog(this)
        dialog.onOrganizeSuccess = {
            cacheMap[currentGradeCode]!!.clearQuestionSelected()
            questionAdapter.notifyDataSetChanged()
        }
        dialog
    }

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
                    CreateClassActivity.jump(this)
                    finish()
                })
                .create()
        dialog
    }

    /**智能推送（填写试题数量）对话框*/
    private val smartPushDialog by lazy {
        val dialog = SmartPushDialog(this)
        dialog.onConfirmClickListener = {
            dialog.dismiss()
            PreviewActivity.jump(this@AssignmentActivity,
                    workCategory == Constants.ASSIGN_TEST_PAPER,
                    cacheMap[currentGradeCode]!!.classes,
                    cacheMap[currentGradeCode]!!.currentUnitBean,
                    getPreviewList(),
                    workCategory,
                    cacheMap[currentGradeCode]!!.subCode,
                    cacheMap[currentGradeCode]!!.versionId,
                    false,
                    null,
                    it
            )
        }
        dialog
    }

    /**重写此方法，加上setIntent(intent);否则在onResume里面得不到intent
     * @param intent intent
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val isClearQuestion = intent.getBooleanExtra("clearQuestion", false)
        if (cacheMap[currentGradeCode] != null && isClearQuestion){
            cacheMap[currentGradeCode]!!.clearQuestionSelected()
            questionAdapter.notifyDataSetChanged()
        }
    }

    override fun initViews() {
        workCategory = intent.getIntExtra(Constants.ASSIGN_CATEGORY, 0)
        titleBar.setTitle(intent.getStringExtra(Constants.ASSIGN_TITLE) ?: "")

        //题型
        val questionLayoutManager = GridLayoutManager(this,4)
        questionsRecycler.adapter = questionAdapter
        questionsRecycler.layoutManager = questionLayoutManager

        //试卷
        if (workCategory == Constants.ASSIGN_TEST_PAPER){
            questionsRecycler.visibility = View.GONE

            supportFragmentManager.beginTransaction().replace(R.id.testPaperLayout, testFragment).commitAllowingStateLoss()
            testPaperLayout.visibility = View.VISIBLE
        }

        //底部导航
        workCategory = intent.getIntExtra(Constants.ASSIGN_CATEGORY, 0)
        val bottomAdapter = BottomAdapter()
        val bottomLayoutManager = GridLayoutManager(this, 4)
        bottomRecycler.adapter = bottomAdapter
        bottomRecycler.layoutManager = bottomLayoutManager
        if (workCategory == Constants.ASSIGN_TEST_PAPER) {
            bottomAdapter.datas = resources.getStringArray(R.array.bottom_test).asList()
            bottomLayoutManager.spanCount = 4
        }else {
            bottomLayoutManager.spanCount = 3
            bottomAdapter.datas = resources.getStringArray(R.array.bottom_work).asList()
        }
        bottomAdapter.setOnItemClickListener { holder, view, position ->
            val tag: Int = view.tag as Int
            when (tag) {
                testImg[0] -> {//系统试卷
                    titleBar.setTitle("系统试卷")
                    isOrganizeTest = false

                    isSaveTest = false
                    bottomAdapter.datas[1] = "组卷"
                    bottomAdapter.notifyDataSetChanged()

                    testPaperLayout.visibility = View.VISIBLE
                    questionsRecycler.visibility = View.GONE
                    currentTestType = "1"
                    val cache = cacheMap[currentGradeCode]!!
                    testFragment.requestData(currentTestType, currentGradeCode,
                            if (cache.currentUnitBean?.unitCode == null) null else cache.currentUnitBean?.unitCode, cache.subCode)
                }
                testImg[1] -> {//组卷
                    isOrganizeTest = true
                    if(isSaveTest){//保存试卷
                        val cache = cacheMap[currentGradeCode]!!
                        val questionList = getPreviewList()
                        val unitId = cache.currentUnitBean?.unitCode
                        if (questionList.isEmpty()){
                            T.show(this, "你还没有选题")
                        }else {
                            organizePaperDialog.show(currentGradeCode, unitId, questionList, cache.subCode)
                        }
                    }else{//组卷
                        isSaveTest = true
                        bottomAdapter.datas[1] = "保存试卷"
                        bottomAdapter.notifyDataSetChanged()
                    }
                    if (emptyTestTv.visibility == View.VISIBLE)
                        emptyTestTv.visibility = View.GONE
                    testPaperLayout.visibility = View.GONE
                    questionsRecycler.visibility = View.VISIBLE
                }
                testImg[2] -> {//我的试卷
                    titleBar.setTitle("我的试卷")
                    isOrganizeTest = false

                    isSaveTest = false
                    bottomAdapter.datas[1] = "组卷"
                    bottomAdapter.notifyDataSetChanged()

                    testPaperLayout.visibility = View.VISIBLE
                    questionsRecycler.visibility = View.GONE
                    currentTestType = "2"
                    val cache = cacheMap[currentGradeCode]!!
                    testFragment.requestData(currentTestType, currentGradeCode,
                            if (cache.currentUnitBean?.unitCode == null) null else cache.currentUnitBean?.unitCode, cache.subCode)
                }
                workImg[1] -> {//发布小组
                    if(cacheMap[currentGradeCode]!!.classes.size > 1){
                        T.show(this, "发布小组不能多选班级")
                        return@setOnItemClickListener
                    }
                    if (!isReadyToAssign())
                        return@setOnItemClickListener
                    jumpToPublish(true)
                }
                workImg[2] -> {//发布班级\发布试卷
                    if (!isReadyToAssign())
                        return@setOnItemClickListener
                    jumpToPublish(false)
                }
                workImg[0] -> {//智能推送
                    smartPushDialog.show()
                }
            /*workImg[1] -> {//自定义
                toast("自定义")
            }*/
            }
        }

        assignmentPresenter.loadBaseData(workCategory.toString())
        stateView.setOnRetryClickListener {
            assignmentPresenter.loadBaseData(workCategory.toString())
        }

        questionAdapter.setOnItemClickListener { holder, view, position ->
            val cache = cacheMap[currentGradeCode]!!
            QuestionActivity.jump(
                    this,
                    questionAdapter.datas[position].questionList,
                    cache.currentUnitBean,
                    difficulty,
                    questionAdapter.datas[position].adverCode,
                    currentGradeCode,
                    cache.semesterCode,
                    cache.subCode,
                    cache.versionId
            )
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
        titleBar.setRightText("预览", {
            if (!isOrganizeTest && workCategory == Constants.ASSIGN_TEST_PAPER){
                if(currentPaperId == null){
                    T.show(this@AssignmentActivity, "请选择你需要预览的试卷")
                    return@setRightText
                }
                PreviewActivity.jump(this@AssignmentActivity,
                        workCategory == Constants.ASSIGN_TEST_PAPER,
                        cacheMap[currentGradeCode]!!.classes,
                        cacheMap[currentGradeCode]!!.currentUnitBean,
                        getPreviewList(),
                        workCategory,
                        cacheMap[currentGradeCode]!!.subCode,
                        cacheMap[currentGradeCode]!!.versionId,
                        true,
                        currentPaperId
                )
            }else{
                if(getPreviewList().isEmpty()){
                    T.show(this@AssignmentActivity, "你还没有选题")
                    return@setRightText
                }
                PreviewActivity.jump(this@AssignmentActivity,
                        workCategory == Constants.ASSIGN_TEST_PAPER,
                        cacheMap[currentGradeCode]!!.classes,
                        cacheMap[currentGradeCode]!!.currentUnitBean,
                        getPreviewList(),
                        workCategory,
                        cacheMap[currentGradeCode]!!.subCode,
                        cacheMap[currentGradeCode]!!.versionId,
                        true
                )
            }
        })
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
            testFragment.requestData(currentTestType, currentGradeCode, null, assignmentBean.resultTaughtCoursesVo?.bookCode)
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
            testFragment.requestData(currentTestType, currentGradeCode, null, cache.subCode)
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

    private fun jumpToPublish(isGroup: Boolean){
        PublishWorkActivity.jump(
                this,
                isGroup,
                workCategory == Constants.ASSIGN_TEST_PAPER,
                cacheMap[currentGradeCode]!!.classes,
                cacheMap[currentGradeCode]!!.currentUnitBean,
                getPreviewList(),
                workCategory,
                cacheMap[currentGradeCode]!!.subCode,
                cacheMap[currentGradeCode]!!.versionId,
                currentPaperId,
                currentPaperName
        )
    }

    /**是否可以发布作业了*/
    private fun isReadyToAssign(): Boolean{
        if(cacheMap[currentGradeCode]!!.classes.size == 0){
            T.show(this, "请选择班级")
            return false
        }
        if (workCategory == Constants.ASSIGN_TEST_PAPER){
            if (StringUtils.isEmpty(currentPaperId)){
                T.show(this, "请选择你要发布的试卷")
                return false
            }
        }else{
            if (getPreviewList().isEmpty()){
                T.show(this, "你还没有选题")
                return false
            }
        }
        return true
    }

    override fun getTestList(testList: List<TestPaperListBean>?) {

    }

    override fun loadTestListError(t: Throwable) {
        if (!isOrganizeTest)
            emptyTestTv.visibility = View.VISIBLE
        t.handleThrowable(this)
    }

    /**当前选的所有试题*/
    private fun getPreviewList(): ArrayList<QuestionBean>{
        val previewList = ArrayList<QuestionBean>()
        if (cacheMap[currentGradeCode] == null)
            return previewList
        cacheMap[currentGradeCode]!!.questionList.forEach {
            previewList.addAll(it.questionList)
        }
        return previewList
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null)
            return
        if (requestCode == Constants.QUESTION_REQUEST && resultCode == Constants.QUESTION_RESULT) {
            val previewList = data.getSerializableExtra("previewList") as ArrayList<QuestionBean>
            val type = data.getStringExtra("type")
            cacheMap[currentGradeCode]!!.questionList.forEach {
                if (it.adverCode == type) {
                    it.questionList.clear()
                    it.questionList.addAll(previewList)
                    return@forEach
                }
            }
            questionAdapter.notifyDataSetChanged()
        }
        if (requestCode == Constants.PREVIEW_REQUEST && resultCode == Constants.QUESTION_RESULT) {
            val previewList = data.getSerializableExtra("previewList") as ArrayList<QuestionBean>
            cacheMap[currentGradeCode]!!.questionList.forEach {
                val type = it.adverCode
                it.questionList.clear()
                it.questionList.addAll( previewList.filter {it.type == type})
            }
            questionAdapter.notifyDataSetChanged()
        }
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

    /**试卷*/
    inner class TestAdapter: BaseQuickAdapter<TestPaperListBean, BaseViewHolder>(R.layout.item_assignment_test){
        var selectPosition = -1
            set(value) {
                field = value
                notifyDataSetChanged()
            }
        override fun convert(helper: BaseViewHolder, item: TestPaperListBean?) {
            helper.setText(R.id.tv_id, item?.testPaperName)
            helper.getView<TextView>(R.id.tv_id).isSelected = selectPosition == helper.adapterPosition
        }
    }

    /**底部按钮*/
    val workImg = intArrayOf(R.drawable.icon_smart_push, R.drawable.icon_assign_group, R.drawable.icon_assign_class)
    val testImg = intArrayOf(R.drawable.icon_system_test_paper, R.drawable.icon_compose_paper, R.drawable.icon_my_test_paper, R.drawable.icon_assign_class)
    inner class BottomAdapter: RecyclerAdapter<String>(R.layout.item_assignment_bottom){
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: String?) {
            val textView: TextView = setter.getTextView(R.id.tv_id)
            textView.text = entity
            if (workCategory == Constants.ASSIGN_TEST_PAPER) {
                setter.setImageResource(R.id.img_id, testImg[position])
                val convertView = setter.convertView
                convertView.tag = testImg[position]
            }else {
                setter.setImageResource(R.id.img_id, workImg[position])
                val convertView = setter.convertView
                convertView.tag = workImg[position]
            }
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
                        testFragment.requestData(currentTestType, currentGradeCode, null, cache.subCode)
                }else {
                    questionAdapter.datas = questionList
                    setVersionTv(cache.versionName, cache.semesterName, cache.subName)
                    unitTv.text = cache.currentUnitName
                    if (workCategory == Constants.ASSIGN_TEST_PAPER)
                        testFragment.requestData(currentTestType, currentGradeCode, null, cache.subCode)
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
