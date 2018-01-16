package com.yzy.ebag.teacher.ui.activity

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.base.BaseListActivity
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.MyCourseBean
import ebag.core.http.network.RequestCallBack
import ebag.core.util.T
import ebag.core.util.loadImage

class MyCourseActivity : BaseListActivity<List<MyCourseBean>,MyCourseBean>() {
    private val adapter by lazy { MyAdapter() }
    override fun loadConfig(intent: Intent) {
        loadMoreEnabled(false)
        refreshEnabled(false)
        titleBar.setTitle(R.string.course_teaching)
        titleBar.setRightText(resources.getString(R.string.add), {
            T.show(this, "添加课程")
        })
        val list = ArrayList<MyCourseBean>()
        list.add(MyCourseBean("人教版", "2010-10-24", "上学期", "语文", "三年级"))
        list.add(MyCourseBean("人教版", "2010-10-24", "上学期", "英语", "三年级"))
        list.add(MyCourseBean("人教版", "2010-10-24", "上学期", "数学", "三年级"))
        list.add(MyCourseBean("人教版", "2010-10-24", "上学期", "生物", "三年级"))
        list.add(MyCourseBean("人教版", "2010-10-24", "上学期", "化学", "三年级"))
        list.add(MyCourseBean("人教版", "2010-10-24", "上学期", "历史", "三年级"))
        list.add(MyCourseBean("人教版", "2010-10-24", "上学期", "社会", "三年级"))
        withFirstPageData(list)
    }



    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<MyCourseBean>>) {

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
            imageView.loadImage(item.image)
            helper.setText(R.id.tvEdition,item.edition)
                    .setText(R.id.tvTime,"[添加时间:${item.time}]")
                    .setText(R.id.tvSemester,item.item)
                    .setText(R.id.tvSubject,item.subject)
                    .setText(R.id.tvClass,item.classX)
        }
    }
}
