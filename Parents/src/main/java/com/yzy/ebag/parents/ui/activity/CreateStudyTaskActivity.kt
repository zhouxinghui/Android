package com.yzy.ebag.parents.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.bean.StudentSubjectBean
import com.yzy.ebag.parents.mvp.CreateStudyTaskContract
import com.yzy.ebag.parents.mvp.presenter.CreateStudyTaskPersenter
import com.yzy.ebag.parents.ui.adapter.CreateTaskAdapter
import ebag.core.base.BaseActivity
import ebag.mobile.bean.MyChildrenBean
import kotlinx.android.synthetic.main.activity_createstudytask.*

class CreateStudyTaskActivity : BaseActivity(), CreateStudyTaskContract.CreateStudyTaskView {


    private var datas: ArrayList<MyChildrenBean> = arrayListOf()
    private var subjectDatas: ArrayList<StudentSubjectBean> = arrayListOf()
    private lateinit var mAdapter: CreateTaskAdapter
    private lateinit var mPersenter: CreateStudyTaskContract.Persenter
    private lateinit var mSubjectAdapter: SubjectChooseAdapter
    private var selectedUid = ""


    override fun getLayoutId(): Int = R.layout.activity_createstudytask

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
                mAdapter.notifyItemRangeChanged(0, datas.size)
                mPersenter.querySubject(datas[position].classId)
            }
        }

        subject_type_recycler.layoutManager = GridLayoutManager(this, 4)
        mSubjectAdapter = SubjectChooseAdapter(subjectDatas)
        subject_type_recycler.adapter = mSubjectAdapter

        mSubjectAdapter.setOnItemClickListener { _, _, position ->

            mSubjectAdapter.selectedPosition = position
            mSubjectAdapter.notifyDataSetChanged()
        }
    }


    override fun showSubject(datas: List<StudentSubjectBean>) {

        val bookList: ArrayList<String> = arrayListOf()
        val newList: ArrayList<StudentSubjectBean> = arrayListOf()

        /*val yyList = datas.filter {
            it.bookName == "语文"
        }
*/
        datas.forEach {
            if (!bookList.contains(it.bookName)){
                bookList.add(it.bookName)
                newList.add(it)
            }
        }


        subjectDatas.clear()
        mSubjectAdapter.bookList.clear()
        subjectDatas.addAll(newList)
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
        var bookList: ArrayList<String> = arrayListOf()
        var selectedPosition: Int = 0

        override fun convert(helper: BaseViewHolder, item: StudentSubjectBean?) {
            helper.getView<TextView>(R.id.subject_tv).isSelected = helper.layoutPosition == selectedPosition
            if (!bookList.contains(item!!.bookName)) {
                bookList.add(item.bookName)
                helper.setText(R.id.subject_tv, item.bookName)
            }

        }

    }

}