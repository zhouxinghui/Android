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
        pop.onConfirmClick = { name, unitBean ->
            unit_tv_id.text = name
        }
        pop
    }

    override fun initViews() {
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
                unit__id.text = ""
                unit_tv_id.text = ""
                mAdapter.notifyItemRangeChanged(0, datas.size)
                mPersenter.querySubject(datas[position].classId)
            }
        }

        subject_type_recycler.layoutManager = GridLayoutManager(this, 4)
        mSubjectAdapter = SubjectChooseAdapter(subjectDatas)
        subject_type_recycler.adapter = mSubjectAdapter

        mSubjectAdapter.setOnItemClickListener { _, _, position ->

            mSubjectAdapter.selectedPosition = position
            chooseUnitDialog.updateList(subjectDatas[position].subjectList)
            unit__id.text = ""
            mSubjectAdapter.notifyDataSetChanged()
        }

        unit__id.setOnClickListener {

            chooseUnitDialog.show()
        }

        unit_tv_id.setOnClickListener {

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
    }


    override fun showSubject(datas: List<StudentSubjectBean>) {

        subjectDatas.clear()
        subjectDatas.addAll(datas)
        chooseUnitDialog.updateList(datas[mSubjectAdapter.selectedPosition].subjectList)
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
        mPersenter.querySubject(data[0].classId)
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

}