package com.yzy.ebag.teacher.ui.activity

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.GradeBean
import ebag.core.base.mvp.MVPActivity
import ebag.core.util.loadImage
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
        val gradeLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        gradeRecycler.adapter = gradeAdapter
        gradeRecycler.layoutManager = gradeLayoutManager

        val classAdapter = ClassAdapter()
        val classLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        classRecycler.adapter = classAdapter
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

        val questionAdapter = QuestionsAdapter()
        val questionLayoutManager = GridLayoutManager(this,5)
        questionsRecycler.adapter = questionAdapter
        questionsRecycler.layoutManager = questionLayoutManager
        questionAdapter.datas = list
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
            if(selectPosition != -1 && selectPosition == position && !textView.isSelected)
                textView.isSelected = true
            if (selectPosition != -1 && selectPosition == position && textView.isSelected)
                textView.isSelected = false
        }
    }
    inner class QuestionsAdapter: RecyclerAdapter<GradeBean>(R.layout.item_assignment_questions){
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: GradeBean?) {
            setter.setText(R.id.point_id, "14")
            setter.setText(R.id.question_name_id, "看图写单词")
            setter.getImageView(R.id.question_image_id).loadImage("http://img.taopic.com/uploads/allimg/140116/267869-1401160T23259.jpg")
        }
    }
    val workImgs = intArrayOf(R.drawable.icon_smart_push, R.drawable.icon_custom, R.drawable.icon_assign_group, R.drawable.icon_assign_class)
    val testImgs = intArrayOf(R.drawable.icon_system_test_paper, R.drawable.icon_compose_paper, R.drawable.icon_my_test_paper, R.drawable.icon_assign_group, R.drawable.icon_assign_class)
    inner class BottomAdapter: RecyclerAdapter<String>(R.layout.item_assignment_bottom){
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: String?) {

        }

    }
}
