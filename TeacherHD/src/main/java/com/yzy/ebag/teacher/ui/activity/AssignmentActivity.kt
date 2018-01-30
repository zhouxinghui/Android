package com.yzy.ebag.teacher.ui.activity

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.base.Constants
import com.yzy.ebag.teacher.bean.AssignClassBean
import com.yzy.ebag.teacher.bean.AssignGradeBean
import com.yzy.ebag.teacher.bean.AssignUnitBean
import com.yzy.ebag.teacher.bean.AssignmentBean
import com.yzy.ebag.teacher.ui.presenter.AssignmentPresenter
import com.yzy.ebag.teacher.ui.view.AssignmentView
import com.yzy.ebag.teacher.widget.ExchangeTextbookDialog
import ebag.core.base.mvp.MVPActivity
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.T
import ebag.core.util.loadImage
import ebag.core.xRecyclerView.adapter.RecyclerAdapter
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder
import kotlinx.android.synthetic.main.activity_assignment.*

class AssignmentActivity : MVPActivity(), AssignmentView{
    override fun getLayoutId(): Int {
        return R.layout.activity_assignment
    }
    private var workCategory = 0
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
            assignmentPresenter.loadUnitAndQuestion(workCategory.toString(), currentGradeCode, versionId)
        }
        dialog
    }
    private val gradeAdapter by lazy { GradeAdapter() }
    private val classAdapter by lazy { ClassAdapter() }
    private val questionAdapter by lazy { QuestionsAdapter() }
    private val unitAdapter by lazy { UnitAdapter(ArrayList()) }
    private val assignmentPresenter by lazy { AssignmentPresenter(this,this) }
    //数据保存
    private val cacheMap by lazy { HashMap<String, Cache>() }
    private var currentGradeCode = ""
    //因为切换单元和切换版本访问的是同一个接口，但是返回的版本数据是不会因为版本的改变而改变，而是返回固定年级的班级的所教课程，所以切换版本的时候自己本地变版本名称和版本id（服务器人员太懒），所以用一个boolean值控制，当为true的时候接口请求成功的时候就根据返回参数修改版本名称和版本id，为false的时候就手动修改（见showData方法）
    private var isGradeRequest = true
    override fun destroyPresenter() {
        assignmentPresenter.onDestroy()
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
        val bottomLayoutManager = GridLayoutManager(this, 5)
        bottomRecycler.adapter = bottomAdapter
        bottomRecycler.layoutManager = bottomLayoutManager
        if (workCategory == Constants.ASSIGN_TEST_PAPER) {
            bottomAdapter.datas = resources.getStringArray(R.array.bottom_test).asList()
        }else {
            bottomAdapter.datas = resources.getStringArray(R.array.bottom_work).asList()
            bottomLayoutManager.spanCount = 4
        }

        //单元
        unitRecycler.adapter = unitAdapter
        unitRecycler.layoutManager = LinearLayoutManager(this)

        gradeAdapter.setOnItemClickListener { holder, view, position ->
            gradeAdapter.selectPosition = position
            classAdapter.datas = gradeAdapter.datas[position].homeClazzInfoVos

            val cache = cacheMap[currentGradeCode]!!
            val unitList = cache.unitList
            val questionList = cache.questionList
            if (unitList.isEmpty()) {
                isGradeRequest = true
                assignmentPresenter.loadUnitAndQuestion(workCategory.toString(), currentGradeCode)
            }else {
                unitAdapter.setNewData(unitList as List<MultiItemEntity>)
                questionAdapter.datas = questionList
                setVersionTv(cache.versionName, cache.semesterName, cache.subName)
            }
        }

        classAdapter.setOnItemClickListener { holder, view, position ->
            val cache = cacheMap[currentGradeCode]
            val classes = cache?.classes
            if (classes!!.contains(classAdapter.datas[position])){
                classes.remove(classAdapter.datas[position])
            }else{
                classes.add(classAdapter.datas[position])
            }
            classAdapter.notifyDataSetChanged()
        }

        bottomAdapter.setOnItemClickListener { holder, view, position ->
            val tag: Int = view.tag as Int
            when (tag) {
                testImg[0] -> {//系统试卷
                    toast("系统试卷")
                }
                testImg[1] -> {//组卷
                    toast("组卷")
                }
                testImg[2] -> {//我的试卷
                    toast("我的试卷")
                }
                testImg[3] -> {//发布小组
                    toast("发布小组")
                }
                testImg[4] -> {//发布班级
                    toast("发布班级")
                }
                workImg[0] -> {//智能推送
                    toast("智能推送")
                }
                workImg[1] -> {//自定义
                    toast("自定义")
                }
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
            }
        }

        textBookVersion.setOnClickListener {
            exchangeDialog.show(cacheMap[currentGradeCode]!!.classes)
        }
        assignmentPresenter.loadBaseData(workCategory.toString())
        stateView.setOnRetryClickListener {
            assignmentPresenter.loadBaseData(workCategory.toString())
        }
    }

    override fun loadBaseStart() {
        stateView.showLoading()
    }

    override fun showBaseData(assignmentBean: AssignmentBean?) {
        stateView.showContent()
        val gradeList = assignmentBean?.sendHomePageClazzInfoVos
        gradeAdapter.datas = gradeList
        classAdapter.datas = assignmentBean?.sendHomePageClazzInfoVos!![0].homeClazzInfoVos
        gradeAdapter.selectPosition = 0

        gradeList?.forEach {
            if(cacheMap[it.gradeCode] == null) {
                val cache = Cache()
                cacheMap[it.gradeCode] = cache
            }
        }

        showData(assignmentBean)
    }

    override fun loadBaseError(t: Throwable) {
        stateView.showError()
        t.handleThrowable(this)
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

    private fun showData(assignmentBean: AssignmentBean?){
        val questionList = assignmentBean?.resultAdvertisementVos
        questionAdapter.datas = questionList
        val unitList = assignmentBean?.sendHomePageClazzInfoVos!![0].bookVersionOrUnitVos
        unitList.forEach {
            val subList = it.resultBookUnitOrCatalogVos
            if (subList.isEmpty()){
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
        cacheMap[currentGradeCode]!!.questionList = questionList as ArrayList<AssignmentBean.QuestionsBean>
        if (isGradeRequest) {
            val versionBean = assignmentBean.resultTaughtCoursesVo
            setVersionTv(versionBean?.bookVersionName,
                    versionBean?.semeterName,
                    versionBean?.bookName)
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
        cacheMap[currentGradeCode]!!.versionName = versionName
        cacheMap[currentGradeCode]!!.versionId = versionId
        cacheMap[currentGradeCode]!!.semesterCode = semesterCode
        cacheMap[currentGradeCode]!!.semesterName = semesterName
        cacheMap[currentGradeCode]!!.subCode = subCode
        cacheMap[currentGradeCode]!!.subName = subName
    }
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
    inner class ClassAdapter: RecyclerAdapter<AssignClassBean>(R.layout.item_assignment_class){
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: AssignClassBean) {
            val textView: TextView = setter.getTextView(R.id.Class)
            textView.text = entity.className

            val cache = cacheMap[currentGradeCode]
            val classes = cache?.classes
            textView.isSelected = classes!!.contains(entity)
        }
    }
    inner class QuestionsAdapter: RecyclerAdapter<AssignmentBean.QuestionsBean>(R.layout.item_assignment_questions){
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: AssignmentBean.QuestionsBean?) {
            setter.setText(R.id.point_id, "14")
            setter.setText(R.id.question_name_id, entity!!.adverName)
            setter.getImageView(R.id.question_image_id).loadImage(entity.adverUrl)
        }
    }

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

    val workImg = intArrayOf(R.drawable.icon_smart_push, R.drawable.icon_custom, R.drawable.icon_assign_group, R.drawable.icon_assign_class)
    val testImg = intArrayOf(R.drawable.icon_system_test_paper, R.drawable.icon_compose_paper, R.drawable.icon_my_test_paper, R.drawable.icon_assign_group, R.drawable.icon_assign_class)
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
     * 数据缓存类
     */
    inner class Cache{
        var classes = ArrayList<AssignClassBean>()
        var unitList = ArrayList<AssignUnitBean>()
        var questionList = ArrayList<AssignmentBean.QuestionsBean>()
        var currentUnitBean = AssignUnitBean.UnitSubBean()
        var versionId = ""
        var versionName = ""

        var semesterCode = ""
        var semesterName = ""
        var subCode = ""
        var subName = ""
    }
}
