package com.yzy.ebag.teacher.ui.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.base.BaseListActivity
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.MyCourseBean
import com.yzy.ebag.teacher.http.TeacherApi
import com.yzy.ebag.teacher.widget.ExchangeTextbookDialog
import ebag.core.http.network.RequestCallBack
import ebag.core.util.loadImage

class MyCourseActivity : BaseListActivity<List<MyCourseBean>,MyCourseBean>() {
    private val adapter by lazy { MyAdapter() }
    private lateinit var classId: String
    private val addCourseDialog by lazy {
        val dialog = ExchangeTextbookDialog(this)
        dialog.onAddCourseSuccess = {
            onRetryClick()
        }
        dialog
    }
    companion object {
        fun jump(activity: Activity, classId: String){
            activity.startActivity(Intent(activity, MyCourseActivity::class.java).putExtra("classId", classId))
        }
    }
    override fun loadConfig(intent: Intent) {
        loadMoreEnabled(false)
        refreshEnabled(false)
        titleBar.setTitle(R.string.course_teaching)
        titleBar.setRightText(resources.getString(R.string.add), {
            addCourseDialog.show(classId)
        })
        classId = intent.getStringExtra("classId")
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<MyCourseBean>>) {
        TeacherApi.searchCourse(classId, requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<MyCourseBean>?): List<MyCourseBean>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<MyCourseBean, BaseViewHolder> {
        return adapter
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<MyCourseBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return GridLayoutManager(this, 3)
    }

    inner class MyAdapter: BaseQuickAdapter<MyCourseBean, BaseViewHolder>(R.layout.item_my_course){
        override fun convert(helper: BaseViewHolder, item: MyCourseBean) {
            val imageView = helper.getView<ImageView>(R.id.ivBook)
            imageView.loadImage("")
            helper.setText(R.id.tvEdition,item.bookVersionName)
                    .setText(R.id.tvTime,"[添加时间:无返回字段]")
                    .setText(R.id.tvSemester,item.semeterName)
                    .setText(R.id.tvSubject,item.bookName)
                    .setText(R.id.tvClass,item.gradeName)
        }
    }
}
