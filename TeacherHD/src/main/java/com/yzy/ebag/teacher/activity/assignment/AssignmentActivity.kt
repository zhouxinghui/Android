package com.yzy.ebag.teacher.activity.assignment

import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.activity.clazz.CreateClassActivity
import com.yzy.ebag.teacher.base.Constants
import com.yzy.ebag.teacher.bean.*
import com.yzy.ebag.teacher.ui.presenter.AssignmentPresenter
import com.yzy.ebag.teacher.ui.view.AssignmentView
import com.yzy.ebag.teacher.widget.ExchangeTextbookDialog
import com.yzy.ebag.teacher.widget.OrganizePaperDialog
import com.yzy.ebag.teacher.widget.SmartPushDialog
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
import kotlinx.android.synthetic.main.activity_assignment.*

class AssignmentActivity : MVPActivity(), AssignmentView{
    override fun getLayoutId(): Int = R.layout.activity_assignment

    /**
     * 作业类型
     */
    private var workCategory = 0
    /**
     * 切换版本对话框
     */
    private val exchangeDialog by lazy {
        val dialog = ExchangeTextbookDialog(this)
        dialog.onConfirmClick = {versionName,
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
            assignmentPresenter.loadDataByVersion(workCategory.toString(), versionId, subCode, difficulty)
            isUnitChange = false
        }
        dialog
    }
    /**
     * 年级
     */
    private val gradeAdapter by lazy { GradeAdapter() }
    /**
     * 班级
     */
    private val classAdapter by lazy { ClassAdapter() }
    /**
     * 试题
     */
    private val questionAdapter by lazy { QuestionsAdapter() }
    /**
     * 单元
     */
    private val unitAdapter by lazy { UnitAdapter(ArrayList()) }
    /**
     * 试卷列表
     */
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
    /**
     * 组卷（填写试卷名）对话框
     */
    private val organizePaperDialog by lazy {
        val dialog = OrganizePaperDialog(this)
        dialog.onOrganizeSuccess = {
            cacheMap[currentGradeCode]!!.clearQuestionSelected()
            questionAdapter.notifyDataSetChanged()
        }
        dialog
    }
    /**
     * 智能推送（填写试题数量）对话框
     */
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
    private val assignmentPresenter by lazy { AssignmentPresenter(this,this) }
    //数据保存
    private val cacheMap by lazy { HashMap<String, Cache>() }
    private var currentGradeCode = ""
    //因为切换单元和切换版本访问的是同一个接口，但是返回的版本数据是不会因为版本的改变而改变，而是返回固定年级的班级的所教课程，所以切换版本的时候自己本地变版本名称和版本id（服务器人员太懒），所以用一个boolean值控制，当为true的时候接口请求成功的时候就根据返回参数修改版本名称和版本id，为false的时候就手动修改（见showData方法）
    private var isGradeRequest = true
    private var difficulty: String? = null
    /**
     * 1   系统试卷  2  我的试卷
     */
    private var currentTestType = "1"
    private var isOrganizeTest = false
    private var isSaveTest = false
    private var currentPaperId: String? = null
    private var currentPaperName: String? = null
    private var isUnitChange = false
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
    override fun destroyPresenter() {
        assignmentPresenter.onDestroy()
    }

    /**
     *
     * 重写此方法，加上setIntent(intent);否则在onResume里面得不到intent
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
        titleBar.setTitle(intent.getStringExtra(Constants.ASSIGN_TITLE))
        //年级
        val gradeLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        gradeRecycler.adapter = gradeAdapter
        gradeRecycler.layoutManager = gradeLayoutManager

        //班级
        val classLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        classRecycler.adapter = classAdapter
        classRecycler.layoutManager = classLayoutManager

        //题型
        val questionLayoutManager = GridLayoutManager(this,5)
        questionsRecycler.adapter = questionAdapter
        questionsRecycler.layoutManager = questionLayoutManager

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

        //单元
        unitRecycler.adapter = unitAdapter
        unitRecycler.layoutManager = LinearLayoutManager(this)

        //试卷
        if (workCategory == Constants.ASSIGN_TEST_PAPER){
            questionsRecycler.visibility = View.GONE

            supportFragmentManager.beginTransaction().replace(R.id.testPaperLayout, testFragment).commitAllowingStateLoss()
            testPaperLayout.visibility = View.VISIBLE
        }
        totalUnitTv.isSelected = true
        totalUnitTv.setOnClickListener {
            if (!totalUnitTv.isSelected){
                val cache = cacheMap[currentGradeCode]!!
                cache.currentUnitBean = AssignUnitBean.UnitSubBean()
                unitAdapter.selectSub = cache.currentUnitBean
                totalUnitTv.isSelected = true
                cacheMap[currentGradeCode]!!.isTotal = true
                if(workCategory == Constants.ASSIGN_TEST_PAPER)
                    testFragment.requestData(currentTestType, currentGradeCode,
                            if (cache.currentUnitBean.unitCode == null) null else cache.currentUnitBean.unitCode, cache.subCode)
                assignmentPresenter.loadDataByVersion(
                        workCategory.toString(),
                        cache.versionId, cache.subCode,
                        difficulty,
                        if (cache.currentUnitBean.unitCode == null) null else cache.currentUnitBean.unitCode)
                isUnitChange = true
            }
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
            val cache = cacheMap[currentGradeCode]
            assignmentPresenter.loadDataByVersion(
                    workCategory.toString(),
                    cache?.versionId, cache?.subCode,
                    difficulty,
                    if (cache?.currentUnitBean?.unitCode == null) null else cache.currentUnitBean.unitCode)
        }

        questionAdapter.setOnItemClickListener { holder, view, position ->
            /*val unitBean = cacheMap[currentGradeCode]!!.currentUnitBean
            if (unitBean.id == 0){
                T.show(this, "请选择单元")
                return@setOnItemClickListener
            }*/
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

        gradeAdapter.setOnItemClickListener { holder, view, position ->
            gradeAdapter.selectPosition = position
            classAdapter.datas = gradeAdapter.datas[position].homeClazzInfoVos

            val cache = cacheMap[currentGradeCode]!!
            val unitList = cache.unitList
            val questionList = cache.questionList
            if (unitList.isEmpty()) {
                isGradeRequest = true
                assignmentPresenter.loadUnitAndQuestion(workCategory.toString(), currentGradeCode)
                totalUnitTv.isSelected = true
                cache.isTotal = true
                if (workCategory == Constants.ASSIGN_TEST_PAPER)
                    testFragment.requestData(currentTestType, currentGradeCode, null, cache.subCode)
            }else {
                unitAdapter.setNewData(unitList as List<MultiItemEntity>)
                questionAdapter.datas = questionList
                setVersionTv(cache.versionName, cache.semesterName, cache.subName)
                totalUnitTv.isSelected = cache.isTotal
                if (workCategory == Constants.ASSIGN_TEST_PAPER)
                    testFragment.requestData(currentTestType, currentGradeCode, null, cache.subCode)
            }
        }

        classAdapter.setOnItemClickListener { holder, view, position ->
            classAdapter.selectPosition = position
        }

        bottomAdapter.setOnItemClickListener { holder, view, position ->
            val tag: Int = view.tag as Int
            when (tag) {
                testImg[0] -> {//系统试卷
                    titleBar.setTitle(getString(R.string.assign_system_test_paper))
                    isOrganizeTest = false

                    isSaveTest = false
                    bottomAdapter.datas[1] = "组卷"
                    bottomAdapter.notifyDataSetChanged()

                    testPaperLayout.visibility = View.VISIBLE
                    questionsRecycler.visibility = View.GONE
                    currentTestType = "1"
                    val cache = cacheMap[currentGradeCode]!!
                    testFragment.requestData(currentTestType, currentGradeCode,
                            if (cache.currentUnitBean.unitCode == null) null else cache.currentUnitBean.unitCode, cache.subCode)
                }
                testImg[1] -> {//组卷
                    isOrganizeTest = true
                    if(isSaveTest){//保存试卷
                        val cache = cacheMap[currentGradeCode]!!
                        val questionList = getPreviewList()
                        val unitId = cache.currentUnitBean.unitCode
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
                    titleBar.setTitle(getString(R.string.assign_my_test_paper))
                    isOrganizeTest = false

                    isSaveTest = false
                    bottomAdapter.datas[1] = "组卷"
                    bottomAdapter.notifyDataSetChanged()

                    testPaperLayout.visibility = View.VISIBLE
                    questionsRecycler.visibility = View.GONE
                    currentTestType = "2"
                    val cache = cacheMap[currentGradeCode]!!
                    testFragment.requestData(currentTestType, currentGradeCode,
                            if (cache.currentUnitBean.unitCode == null) null else cache.currentUnitBean.unitCode, cache.subCode)
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
        unitAdapter.setOnItemClickListener { adapter, view, position ->
            val item = adapter.getItem(position)
            if(item is AssignUnitBean) {
                if (item.isExpanded) {
                    adapter.collapse(position)
                } else {
                    adapter.expand(position)
                }
            }else{
                item as AssignUnitBean.UnitSubBean
                val cache = cacheMap[currentGradeCode]
                cache?.currentUnitBean = item
                unitAdapter.selectSub = item
                totalUnitTv.isSelected = false
                cache?.isTotal = false
                if(workCategory == Constants.ASSIGN_TEST_PAPER)
                    testFragment.requestData(currentTestType, currentGradeCode,
                            item.unitCode, cache!!.subCode)
                assignmentPresenter.loadDataByVersion(workCategory.toString(), cache?.versionId, cache?.subCode, difficulty, item.unitCode)
                isUnitChange = true
            }
        }
        //切换版本
        textBookVersion.setOnClickListener {
            exchangeDialog.show(cacheMap[currentGradeCode]!!.classes)
        }
        //预览
        titleBar.setOnRightClickListener {
            if (!isOrganizeTest && workCategory == Constants.ASSIGN_TEST_PAPER){
                if(currentPaperId == null){
                    T.show(this@AssignmentActivity, "请选择你需要预览的试卷")
                    return@setOnRightClickListener
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
                    return@setOnRightClickListener
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
        }

        assignmentPresenter.loadBaseData(workCategory.toString())
        stateView.setOnRetryClickListener {
            assignmentPresenter.loadBaseData(workCategory.toString())
        }
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

    override fun loadBaseStart() {
        stateView.showLoading()
    }

    override fun showBaseData(assignmentBean: AssignmentBean?) {
        stateView.showContent()
        val gradeList = assignmentBean?.sendHomePageClazzInfoVos
        if (gradeList == null || gradeList.isEmpty()){
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
        if (workCategory == Constants.ASSIGN_TEST_PAPER)
            testFragment.requestData(currentTestType, currentGradeCode, null, assignmentBean.resultTaughtCoursesVo?.bookCode)
        gradeList.forEach {
            if(cacheMap[it.gradeCode] == null) {
                val cache = Cache()
                cacheMap[it.gradeCode] = cache
            }
        }

        showData(assignmentBean, true)
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
        if (!isOrganizeTest)
            emptyTestTv.visibility = View.VISIBLE
        t.handleThrowable(this)
    }

    private fun showData(assignmentBean: AssignmentBean?, isFirst: Boolean = false){
        val questionList = assignmentBean?.resultAdvertisementVos
        questionAdapter.datas = questionList

        if (!isUnitChange) {
            val unitList = assignmentBean?.sendHomePageClazzInfoVos!![0].bookVersionOrUnitVos
            unitList.forEach {
                val subList = it.resultBookUnitOrCatalogVos
                if (subList.isEmpty()) {
                    val subBean = AssignUnitBean.UnitSubBean()
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
            unitAdapter.setNewData(unitList as List<MultiItemEntity>)
            cacheMap[currentGradeCode]!!.unitList = unitList as ArrayList<AssignUnitBean>
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

    /**
     * 年级
     */
    inner class GradeAdapter : RecyclerAdapter<AssignGradeBean>(R.layout.item_assignment_grade){
        var selectPosition = -1
        set(value) {
            field = value
            currentGradeCode = datas[selectPosition].gradeCode
            notifyDataSetChanged()
        }
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: AssignGradeBean?) {
            val textView: TextView = setter.getTextView(R.id.grade)
            textView.text = entity!!.gradeName
            textView.isSelected = selectPosition != -1 && selectPosition == position
        }
    }

    /**
     * 班级
     */
    inner class ClassAdapter: RecyclerAdapter<AssignClassBean>(R.layout.item_assignment_class){
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
            val textView: TextView = setter.getTextView(R.id.Class)
            textView.text = entity?.className

            val cache = cacheMap[currentGradeCode]
            val classes = cache?.classes
            textView.isSelected = classes!!.contains(entity)
        }
    }

    /**
     * 试题
     */
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
     * 单元
     */
    inner class UnitAdapter(list: ArrayList<MultiItemEntity>): BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(list){
        init {
            addItemType(1, ebag.hd.R.layout.unit_group_item)
            addItemType(2, ebag.hd.R.layout.unit_sub_item)
        }
        var selectSub: AssignUnitBean.UnitSubBean? = null
            set(value) {
                field = value
                notifyDataSetChanged()
            }
        override fun convert(helper: BaseViewHolder?, item: MultiItemEntity) {
            val tv = helper!!.getView<TextView>(ebag.hd.R.id.text)
            when(helper.itemViewType){
                Constants.LEVEL_ONE ->{
                    item as AssignUnitBean
                    tv.text = item.name
                    tv.isSelected = item.isExpanded
                }
                Constants.LEVEL_TWO ->{
                    item as AssignUnitBean.UnitSubBean
                    tv.text = item.name
                    tv.isSelected = selectSub == item
                    tv.isSelected = item == cacheMap[currentGradeCode]!!.currentUnitBean
                }
            }
        }
    }

//    val workImg = intArrayOf(R.drawable.icon_smart_push, R.drawable.icon_custom, R.drawable.icon_assign_group, R.drawable.icon_assign_class)
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

    /**
     * 是否可以发布作业了
     */
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

    /**
     * 当前选的所有试题
     */
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

    /**
     * 数据缓存类
     */
    inner class Cache{
        var classes = ArrayList<AssignClassBean>()
        var unitList = ArrayList<AssignUnitBean>()
        var isTotal = true
        var questionList = ArrayList<AssignmentBean.QuestionsBean>()
        var currentUnitBean = AssignUnitBean.UnitSubBean()
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
