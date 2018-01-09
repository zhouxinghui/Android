package com.yzy.ebag.teacher.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.GradeBean
import ebag.core.base.mvp.MVPActivity
import ebag.core.xRecyclerView.adapter.RecyclerAdapter
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder
import kotlinx.android.synthetic.main.activity_assignment.*

class AssignmentActivity : MVPActivity() {
    override fun destroyPresenter() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_assignment
    }

    override fun initViews() {
        val gradeAdapter = GradeAdapter()
        val classAdapter = ClassAdapter()
        gradeRecycler.adapter = gradeAdapter
        classRecycler.adapter = classAdapter
        val gradeLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val classLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        gradeRecycler.layoutManager = gradeLayoutManager
        classRecycler.layoutManager = classLayoutManager
        val list = ArrayList<GradeBean>()
        for (i in 0..9){
            val bean = GradeBean()
            list.add(bean)
        }
        gradeAdapter.datas = list
        classAdapter.datas = list
        gradeAdapter.setOnItemClickListener { holder, view, position ->
            gradeAdapter.selectItem(position)
        }
        classAdapter.setOnItemClickListener { holder, view, position ->
            classAdapter.selectItem(position)
        }
    }

    inner class GradeAdapter : RecyclerAdapter<GradeBean>(R.layout.item_assignment_grade){
        private var selectPosition = -1
        fun selectItem(selectPosition: Int){
            this.selectPosition = selectPosition
            notifyDataSetChanged()
        }
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: GradeBean?) {
            val textView: TextView = setter.getTextView(R.id.grade)
            textView.text = "一年级"
            textView.isSelected = selectPosition != -1 && selectPosition == position
        }
    }
    inner class ClassAdapter: RecyclerAdapter<GradeBean>(R.layout.item_assignment_class){
        private var selectPosition = -1
        fun selectItem(selectPosition: Int){
            this.selectPosition = selectPosition
            notifyDataSetChanged()
        }
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: GradeBean?) {
            val textView: TextView = setter.getTextView(R.id.Class)
            textView.text = "一班"
            textView.isSelected = selectPosition != -1 && selectPosition == position
        }
    }

}
