package com.yzy.ebag.parents.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.view.Gravity
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.bean.StudentSubjectBean
import com.yzy.ebag.parents.http.ParentsAPI
import com.yzy.ebag.parents.mvp.CreateStudyTaskContract
import com.yzy.ebag.parents.mvp.presenter.CreateStudyTaskPersenter
import com.yzy.ebag.parents.ui.adapter.CreateTaskAdapter
import com.yzy.ebag.parents.ui.widget.ChooseUnitDialog
import com.yzy.ebag.parents.ui.widget.UnitPopupWindow
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.T
import ebag.mobile.base.ActivityUtils
import ebag.mobile.bean.MyChildrenBean
import ebag.mobile.bean.UnitBean
import kotlinx.android.synthetic.main.activity_createstudytask.*

class CreateStudyTaskActivity : BaseActivity(), CreateStudyTaskContract.CreateStudyTaskView {


    private var datas: ArrayList<MyChildrenBean> = arrayListOf()
    private var subjectDatas: ArrayList<StudentSubjectBean> = arrayListOf()
    private lateinit var mAdapter: CreateTaskAdapter
    private lateinit var mPersenter: CreateStudyTaskContract.Persenter
    private lateinit var mSubjectAdapter: SubjectChooseAdapter
    private var selectedUid = ""
    private var selectedBookid = ""
    private var questionCount = 10
    private var subCode = ""
    private var unitBean: UnitBean.UnitSubBean? = null
    override fun getLayoutId(): Int = R.layout.activity_createstudytask

    private val chooseUnitDialog by lazy {
        val dialog = ChooseUnitDialog(this)
        dialog.onSelectedListener = { bookId, bookName ->
            selectedBookid = bookId
            unit__id.text = bookName
            unit_tv_id.text = ""
            dialog.dismiss()
        }
        dialog
    }

    private val unitPopupWindow by lazy {
        val pop = UnitPopupWindow(this)
        pop.onConfirmClick = { name, bean ->
            unit_tv_id.text = name
            unitBean = bean ?: UnitBean.UnitSubBean()
        }
        pop
    }

    override fun initViews() {
        ActivityUtils.addActivity(this)
        mPersenter = CreateStudyTaskPersenter(this)
        mAdapter = CreateTaskAdapter(datas)
        recyclerview.layoutManager = GridLayoutManager(this, 3)
        recyclerview.adapter = mAdapter
        mPersenter.queryChild()

        mAdapter.setOnItemClickListener { adapter, _, position ->

            if (!(adapter.getItem(position) as MyChildrenBean).isSelected) {
                datas.forEachIndexed { index, myChildrenBean ->
                    myChildrenBean.isSelected = index == position
                }
                selectedUid = datas[position].uid
                selectedBookid = ""
                unit__id.text = ""
                unit_tv_id.text = ""
                mAdapter.notifyItemRangeChanged(0, datas.size)
                if ((adapter.getItem(position) as MyChildrenBean).classId.isEmpty()) {
                    unit__id.text = "小孩未加入班级"
                    unit_tv_id.text = "小孩未加入班级"
                    unit__id.isClickable = false
                    unit_tv_id.isClickable = false
                    subjectDatas.clear()
                    mSubjectAdapter.notifyDataSetChanged()
                } else {
                    unit__id.text = ""
                    unit_tv_id.text = ""
                    unit__id.isClickable = true
                    unit_tv_id.isClickable = true
                    mPersenter.querySubject(datas[position].classId)
                }
            }
        }

        subject_type_recycler.layoutManager = GridLayoutManager(this, 4)
        mSubjectAdapter = SubjectChooseAdapter(subjectDatas)
        subject_type_recycler.adapter = mSubjectAdapter

        mSubjectAdapter.setOnItemClickListener { _, _, position ->

            mSubjectAdapter.selectedPosition = position
            chooseUnitDialog.updateList(subjectDatas[position].subjectList)
            unit__id.text = ""
            unit_tv_id.text = ""
            subCode = subjectDatas[position].subCode
            mSubjectAdapter.notifyDataSetChanged()
        }

        unit__id.setOnClickListener {

            chooseUnitDialog.show()
        }

        unit_tv_id.setOnClickListener {

            if (selectedBookid.isNotEmpty())

                ParentsAPI.getBookUnit(selectedBookid, object : RequestCallBack<java.util.ArrayList<UnitBean>>() {

                    override fun onStart() {
                        super.onStart()
                        LoadingDialogUtil.showLoading(this@CreateStudyTaskActivity, "正在加载...")
                    }

                    override fun onSuccess(entity: java.util.ArrayList<UnitBean>?) {
                        unitPopupWindow.setData(entity)
                        unitPopupWindow.showAtLocation(window.decorView, Gravity.CENTER, 0, 0)
                        LoadingDialogUtil.closeLoadingDialog()
                    }

                    override fun onError(exception: Throwable) {
                        exception.handleThrowable(this@CreateStudyTaskActivity)
                        LoadingDialogUtil.closeLoadingDialog()
                    }

                })

        }

        comfirm_btn.setOnClickListener {

            when {
                !unit__id.isClickable -> T.show(this@CreateStudyTaskActivity, "小孩未加入班级")
                selectedUid.isEmpty() -> T.show(this@CreateStudyTaskActivity, "还没有选择小孩")
                selectedBookid.isEmpty() -> T.show(this@CreateStudyTaskActivity, "还没有选择教材")
                (unitBean == null) && unit_tv_id.text.isEmpty() -> T.show(this@CreateStudyTaskActivity, "还没有选择章节")
                else -> PreviewActivity.jump(this@CreateStudyTaskActivity, false, arrayListOf(), unitBean , arrayListOf(), 1, subCode, selectedBookid, false, "", questionCount,selectedUid)
            }
        }


        question_count_group.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.count_a -> questionCount = 10
                R.id.count_b -> questionCount = 20
                R.id.count_c -> questionCount = 30
                R.id.count_d -> questionCount = 40
            }
        }
    }


    override fun showSubject(datas: List<StudentSubjectBean>) {

        subjectDatas.clear()
        subjectDatas.addAll(datas)
        chooseUnitDialog.updateList(datas[mSubjectAdapter.selectedPosition].subjectList)
        subCode = subjectDatas[0].subCode
        mSubjectAdapter.notifyDataSetChanged()
    }

    override fun showSubjectEmpty() {

    }

    override fun <T> showUnitDialog(datas: List<T>) {

    }

    override fun refreshUnitText(str: String) {

    }

    override fun showLoading() {

    }

    override fun showEmpty() {

    }

    override fun <T> showContents(data: List<T>) {

        datas.addAll(data as ArrayList<MyChildrenBean>)
        mAdapter.notifyDataSetChanged()
        selectedUid = data[0].uid
        if (data[0].classId.isEmpty()) {
            unit__id.text = "小孩未加入班级"
            unit_tv_id.text = "小孩未加入班级"
            unit__id.isClickable = false
            unit_tv_id.isClickable = false
        } else {
            mPersenter.querySubject(data[0].classId)
        }

    }

    override fun <T> showMoreComplete(data: List<T>) {

    }

    override fun loadmoreEnd() {

    }

    override fun loadmoreFail() {

    }


    companion object {

        fun start(c: Context) {
            c.startActivity(Intent(c, CreateStudyTaskActivity::class.java))
        }
    }


    inner class SubjectChooseAdapter(datas: List<StudentSubjectBean>) : BaseQuickAdapter<StudentSubjectBean, BaseViewHolder>(R.layout.item_subject_choice, datas) {
        var selectedPosition: Int = 0

        override fun convert(helper: BaseViewHolder, item: StudentSubjectBean?) {
            helper.getView<TextView>(R.id.subject_tv).isSelected = helper.layoutPosition == selectedPosition
            helper.setText(R.id.subject_tv, item?.subName)

        }

    }

    override fun showError(e: Throwable?) {
        super.showError(e)
    }

}