package com.yzy.ebag.teacher.activity.clazz

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.base.BaseListActivity
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.PerformanceBean
import com.yzy.ebag.teacher.http.TeacherApi
import com.yzy.ebag.teacher.widget.PerformanceDialog
import ebag.core.http.network.RequestCallBack
import ebag.core.util.loadHead

/**
 * Created by YZY on 2018/3/8.
 */
class ClassPerformanceActivity : BaseListActivity<List<PerformanceBean>, PerformanceBean>() {
    companion object {
        fun jump(context: Context, classId: String){
            context.startActivity(
                    Intent(context, ClassPerformanceActivity::class.java)
                            .putExtra("classId", classId)
            )
        }
    }
    private var classId = ""
    override fun loadConfig(intent: Intent) {
        titleBar.setTitle(getString(R.string.class_expression))
        loadMoreEnabled(false)
        classId = intent.getStringExtra("classId")
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<PerformanceBean>>) {
        TeacherApi.classPerformance(classId, requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<PerformanceBean>?): List<PerformanceBean>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<PerformanceBean, BaseViewHolder> {
        return MyAdapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<PerformanceBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return GridLayoutManager(this, 5)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        PerformanceDialog(this).show()
    }

    inner class MyAdapter: BaseQuickAdapter<PerformanceBean, BaseViewHolder>(R.layout.item_performance){
        override fun convert(helper: BaseViewHolder, item: PerformanceBean) {
            val imageView = helper.getView<ImageView>(R.id.headImg)
            imageView.loadHead(item.headUrl)
            helper.setText(R.id.nameTv, item.name)
                    .setText(R.id.niceCount, item.teacherPraise.toString())
                    .setText(R.id.badCount, item.teacherCriticism.toString())
        }
    }
}