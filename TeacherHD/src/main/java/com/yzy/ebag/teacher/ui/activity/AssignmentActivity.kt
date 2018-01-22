package com.yzy.ebag.teacher.ui.activity

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.base.Constants
import com.yzy.ebag.teacher.bean.AssignmentBean
import com.yzy.ebag.teacher.bean.BookVersionOrUnitVosBean
import com.yzy.ebag.teacher.ui.presenter.AssignmentPresenter
import com.yzy.ebag.teacher.ui.view.AssignmentView
import com.yzy.ebag.teacher.widget.ExchangeTextbookDialog
import ebag.core.base.mvp.MVPActivity
import ebag.core.http.network.handleThrowable
import ebag.core.util.loadImage
import ebag.core.xRecyclerView.adapter.RecyclerAdapter
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder
import kotlinx.android.synthetic.main.activity_assignment.*

class AssignmentActivity : MVPActivity(), AssignmentView{
    private var workCategory = 0
    private val exchangeDialog by lazy { ExchangeTextbookDialog(this) }
    private val gradeAdapter by lazy { GradeAdapter() }
    private val classAdapter by lazy { ClassAdapter() }
    private val questionAdapter by lazy { QuestionsAdapter() }
    private val unitAdapter by lazy { UnitAdapter(ArrayList()) }
    private val assignmentPresenter by lazy { AssignmentPresenter(this,this) }
    override fun destroyPresenter() {
        assignmentPresenter.onDestroy()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_assignment
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
            gradeAdapter.selectItem(position)
        }

        classAdapter.setOnItemClickListener { holder, view, position ->
            classAdapter.selectItem(position)
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
            if(item is BookVersionOrUnitVosBean) {
                if (item.isExpanded) {
                    adapter.collapse(position)
                } else {
                    adapter.expand(position)
                }
            }else{
                item as BookVersionOrUnitVosBean.ResultBookUnitOrCatalogVosBean
                unitAdapter.selectSub = item
            }
        }

        textBookVersion.setOnClickListener {
            exchangeDialog.show()
        }
        assignmentPresenter.loadBaseData(workCategory.toString())
        stateView.setOnRetryClickListener {
            assignmentPresenter.loadBaseData(workCategory.toString())
        }
    }

    override fun loadStart() {
        stateView.showLoading()
    }

    override fun showBaseData(assignmentBean: AssignmentBean?) {
        stateView.showContent()
        gradeAdapter.datas = assignmentBean?.sendHomePageClazzInfoVos
        classAdapter.datas = assignmentBean?.sendHomePageClazzInfoVos!![0].homeClazzInfoVos
        questionAdapter.datas = assignmentBean.resultAdvertisementVos
        unitAdapter.setNewData(assignmentBean.sendHomePageClazzInfoVos[0].bookVersionOrUnitVos as List<MultiItemEntity>)
        textBookVersion.text = getString(R.string.textbook_name,
                        assignmentBean.resultTaughtCoursesVo?.bookVersionName,
                        assignmentBean.resultTaughtCoursesVo?.semeterName,
                        assignmentBean.resultTaughtCoursesVo?.bookName)
    }

    override fun loadError(t: Throwable) {
        stateView.showError()
        t.handleThrowable(this)
    }

    inner class GradeAdapter : RecyclerAdapter<AssignmentBean.SendHomePageClazzInfoVosBean>(R.layout.item_assignment_grade){
        private var selectPosition = -1
        fun selectItem(selectPosition: Int){
            this.selectPosition = selectPosition
            notifyDataSetChanged()
        }
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: AssignmentBean.SendHomePageClazzInfoVosBean?) {
            val textView: TextView = setter.getTextView(R.id.grade)
            textView.text = entity!!.gradeName
            textView.isSelected = selectPosition != -1 && selectPosition == position
        }
    }
    inner class ClassAdapter: RecyclerAdapter<AssignmentBean.SendHomePageClazzInfoVosBean.HomeClazzInfoVosBean>(R.layout.item_assignment_class){
        private var selectPosition = -1
        fun selectItem(selectPosition: Int){
            this.selectPosition = selectPosition
            notifyDataSetChanged()
        }
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: AssignmentBean.SendHomePageClazzInfoVosBean.HomeClazzInfoVosBean?) {
            val textView: TextView = setter.getTextView(R.id.Class)
            textView.text = entity?.className
            if(selectPosition != -1 && selectPosition == position && !textView.isSelected)
                textView.isSelected = true
        }
    }
    inner class QuestionsAdapter: RecyclerAdapter<AssignmentBean.ResultAdvertisementVosBean>(R.layout.item_assignment_questions){
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: AssignmentBean.ResultAdvertisementVosBean?) {
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
        var selectSub: BookVersionOrUnitVosBean.ResultBookUnitOrCatalogVosBean? = null
            set(value) {
                field = value
                notifyDataSetChanged()
            }
        override fun convert(helper: BaseViewHolder?, item: MultiItemEntity) {
            val tv = helper!!.getView<TextView>(ebag.hd.R.id.text)
            when(helper.itemViewType){
                Constants.LEVEL_ONE ->{
                    item as BookVersionOrUnitVosBean
                    tv.text = item.name
                    tv.isSelected = item.isExpanded
                }
                Constants.LEVEL_TWO ->{
                    item as BookVersionOrUnitVosBean.ResultBookUnitOrCatalogVosBean
                    tv.text = item.name
                    tv.isSelected = selectSub == item
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
}
