package com.yzy.ebag.student.module.personal

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.LeaningProgressBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import kotlinx.android.synthetic.main.activity_coursedetail.*

/**
 *  Created by fansan on 2018/5/24 17:51
 */

class CourseDetailActivity : BaseActivity() {

    companion object {
        fun jump(context: Context, gradeId: String, gradeCode: String) {
            context.startActivity(
                    Intent(context, CourseDetailActivity::class.java)
                            .putExtra("gradeId", gradeId)
                            .putExtra("gradeCode", gradeCode)
            )
        }
    }

    private lateinit var gradeId: String
    private lateinit var gradeCode: String

    override fun getLayoutId(): Int = R.layout.activity_coursedetail

    override fun initViews() {

        gradeId = intent.getStringExtra("gradeId") ?: ""
        gradeCode = intent.getStringExtra("gradeCode") ?: ""
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.addItemDecoration(ebag.core.xRecyclerView.manager.DividerItemDecoration(DividerItemDecoration.VERTICAL, 1, Color.parseColor("#e0e0e0")))
        val adapter = Adapter()
        recyclerview.adapter = adapter

        val list = ArrayList<Course>()

        request(list, adapter)

        state_view.setOnRetryClickListener {

            request(list, adapter)
        }
    }

    private fun request(list: ArrayList<Course>, adapter: Adapter) {
        StudentApi.learningProcess(gradeCode, object : RequestCallBack<List<LeaningProgressBean>>() {

            override fun onStart() {
                state_view.showLoading()
            }

            override fun onSuccess(entity: List<LeaningProgressBean>?) {
                if (entity!!.isNotEmpty()) {
                    entity.forEach {
                        list.add(Course("就读学校", it.schoolName))
                        list.add(Course("就读班级", it.learningProcessClassDtos[0].className))
                        list.add(Course("班  主  任", it.learningProcessClassDtos[0].headmaster))
                        it.learningProcessClassDtos[0].teacherDtos.forEach {
                            list.add(Course(it.curriculum, it.teacherName))
                        }
                    }


                    adapter.setNewData(list)
                    state_view.showContent()
                } else {
                    state_view.showError("暂无数据")
                }
            }

            override fun onError(exception: Throwable) {
                state_view.showError()
                exception.handleThrowable(this@CourseDetailActivity)
            }

        })
    }

    inner class Adapter : BaseQuickAdapter<Course, BaseViewHolder>(R.layout.item_coursedetail) {

        override fun convert(helper: BaseViewHolder, item: Course?) {

            when (helper.layoutPosition) {
                0 -> {
                    helper.setImageResource(R.id.image, R.drawable.icon_presonal_center_school)
                    helper.setText(R.id.label, "${item?.tipName}:${item?.name}")
                }
                1 -> {
                    helper.setImageResource(R.id.image, R.drawable.icon_presonal_center_class)
                    helper.setText(R.id.label, "${item?.tipName}:${item?.name}")
                }
                else -> {
                    helper.setText(R.id.label, "${item?.tipName}老师:${item?.name}")
                    helper.setImageResource(R.id.image, R.drawable.icon_presonal_center_teacher)
                }
            }


        }

    }

    data class Course(
            val tipName: String? = "",
            val name: String? = ""
    )

}