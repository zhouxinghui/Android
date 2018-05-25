package com.yzy.ebag.student.activity.growth

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.widget.FrameLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.LeaningProgressBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import kotlinx.android.synthetic.main.activity_course_detail.*

/**
 * 学习历程
 * @author caoyu
 * @date 2018/1/28
 * @description
 */
class CourseDetailActivity : BaseActivity() {

    companion object {
        fun jump(context: Context, gradeId: String,gradeCode:String) {
            context.startActivity(
                    Intent(context, CourseDetailActivity::class.java)
                            .putExtra("gradeId", gradeId)
                            .putExtra("gradeCode",gradeCode)
            )
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_course_detail
    }

    private lateinit var gradeId: String
    private lateinit var gradeCode: String
    private lateinit var manager: GridLayoutManager
    private var random = 0
    override fun initViews() {


        gradeId = intent.getStringExtra("gradeId") ?: ""
        gradeCode = intent.getStringExtra("gradeCode") ?: ""
        titleView.setRightText("切换布局") {
            val params = recyclerView.layoutParams as FrameLayout.LayoutParams
            when ((++random) % 3) {

                0 -> {
                    bg.setBackgroundResource(R.drawable.course_primary_school_bg)
                    params.width = resources.getDimensionPixelSize(R.dimen.x580)
                    params.height = resources.getDimensionPixelSize(R.dimen.x460)
                    params.topMargin = resources.getDimensionPixelSize(R.dimen.x180)
                    params.rightMargin = resources.getDimensionPixelSize(R.dimen.x130)
                    manager.spanCount = 1
                }

                1 -> {
                    bg.setBackgroundResource(R.drawable.course_secondary_school_bg)
                    params.width = resources.getDimensionPixelSize(R.dimen.x720)
                    params.height = resources.getDimensionPixelSize(R.dimen.x320)
                    params.topMargin = resources.getDimensionPixelSize(R.dimen.x220)
                    params.rightMargin = resources.getDimensionPixelSize(R.dimen.x100)
                    manager.spanCount = 3
                }

                2 -> {
                    bg.setBackgroundResource(R.drawable.course_high_school_bg)
                    params.width = resources.getDimensionPixelSize(R.dimen.x720)
                    params.height = resources.getDimensionPixelSize(R.dimen.x320)
                    params.topMargin = resources.getDimensionPixelSize(R.dimen.x210)
                    params.rightMargin = resources.getDimensionPixelSize(R.dimen.x240)
                    manager.spanCount = 3
                }
            }
            recyclerView.layoutParams = params
        }

        val adapter = Adapter()
        recyclerView.adapter = adapter
        manager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = manager

        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position < 3)
                    manager.spanCount
                else
                    1
            }
        }

        val list = ArrayList<Course>()

        request(list, adapter)

        state_view.setOnRetryClickListener {

            request(list, adapter)
        }
    }

    private fun request(list: ArrayList<Course>, adapter: Adapter) {
        StudentApi.learningProcess(gradeCode,object : RequestCallBack<List<LeaningProgressBean>>() {

            override fun onStart() {
                state_view.showLoading()
            }

            override fun onSuccess(entity: List<LeaningProgressBean>?) {
                if (entity!!.isNotEmpty()){
                entity.forEach {
                    list.add(Course("就读学校", it.schoolName))
                    list.add(Course("就读班级", it.learningProcessClassDtos[0].className))
                    list.add(Course("班  主  任", it.learningProcessClassDtos[0].headmaster))
                    it.learningProcessClassDtos[0].teacherDtos.forEach {
                        list.add(Course(it.curriculum, it.teacherName))
                    }
                }


                adapter.setNewData(list)
                state_view.showContent()}else{
                    state_view.showError("暂无数据")
                }
            }

            override fun onError(exception: Throwable) {
                state_view.showError()
                exception.handleThrowable(this@CourseDetailActivity)
            }

        })
    }

    data class Course(
            val tipName: String? = "班主任",
            val name: String? = "样某某"
    )

    inner class Adapter : BaseQuickAdapter<Course, BaseViewHolder>(R.layout.item_activity_course) {
        override fun convert(helper: BaseViewHolder, item: Course?) {
            helper.setText(R.id.text, "${item?.tipName}：${item?.name}")
            if (helper.adapterPosition == 2) {
                helper.getView<TextView>(R.id.text).setPadding(
                        0, mContext.resources.getDimensionPixelSize(R.dimen.x10),
                        0, mContext.resources.getDimensionPixelSize(R.dimen.x10))
            } else {
                helper.getView<TextView>(R.id.text).setPadding(
                        0, mContext.resources.getDimensionPixelSize(R.dimen.x7),
                        0, mContext.resources.getDimensionPixelSize(R.dimen.x7))
            }
        }

    }
}