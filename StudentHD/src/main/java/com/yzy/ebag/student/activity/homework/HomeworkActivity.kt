package com.yzy.ebag.student.activity.homework

import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.base.BaseListTabActivity
import com.yzy.ebag.student.base.Constants
import com.yzy.ebag.student.bean.response.SubjectBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.http.network.RequestCallBack


/**
 * Created by unicho on 2018/1/9.
 */
class HomeworkActivity : BaseListTabActivity<ArrayList<SubjectBean>,SubjectBean>() {

    private var type = "1"
    private var classId = ""
    private var i = 1

    override fun loadConfig() {
        setLeftWidth(resources.getDimensionPixelSize(R.dimen.x180))
        type = intent.getStringExtra("type") ?: ""
        classId = intent.getStringExtra("classId") ?: ""
        when(type){
            Constants.KHZY_TYPE -> {
                setTitleContent(R.string.main_khzy)
            }

            Constants.STZY_TYPE -> {
                setTitleContent(R.string.main_stzy)
            }

            else -> {
                setTitleContent(R.string.main_kssj)
                enableNetWork(false)
            }
        }
    }

    override fun requestData(requestCallBack: RequestCallBack<ArrayList<SubjectBean>>) {
        StudentApi.subjectWorkList(type, classId, "", 1, 10, requestCallBack)
    }

    override fun parentToList(parent: ArrayList<SubjectBean>?): List<SubjectBean>? {
        val bean = SubjectBean()
        bean.itemType = 1
        parent?.add(0, bean)
        return parent
    }

    override fun getLeftAdapter(): BaseQuickAdapter<SubjectBean, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager? {
        return null
    }

    override fun getFragment(pagerIndex: Int, adapter: BaseQuickAdapter<SubjectBean, BaseViewHolder>): Fragment {
        return HomeworkListFragment.newInstance(
                type,classId,
                adapter.getItem(pagerIndex + 1)?.subCode ?: "",
                adapter.getItem(pagerIndex + 1)?.homeWorkInfoVos
        )
    }

    override fun getViewPagerSize(adapter: BaseQuickAdapter<SubjectBean, BaseViewHolder>): Int {
        return adapter.data.size - 1
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int) {
        if(position != 0 && (adapter as Adapter).selectedPosition != position){
            setCurrentItem(position-1)
            adapter.selectedPosition = position
        }
    }

    private class Adapter: BaseMultiItemQuickAdapter<SubjectBean,BaseViewHolder>(null){

        init {
            addItemType(0, R.layout.activity_homework_subject_item)
            addItemType(1, R.layout.activity_homework_subject_item_header)
        }

        var selectedPosition = 1
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun convert(helper: BaseViewHolder, entity: SubjectBean?) {
            if(helper.itemViewType == 0){
                helper.setText(R.id.text,entity?.subject ?: "")
                helper.setBackgroundRes(
                        R.id.dot,
                        // null 或  已完成
                        // 0 未完成 1 已完成
                        if(entity?.homeWorkComplete != "0")
                            R.drawable.homework_subject_dot_done_selector
                        else
                            R.drawable.homework_subject_dot_undo_selector
                )

                helper.getView<TextView>(R.id.text)?.isSelected = helper.adapterPosition == selectedPosition
                helper.getView<View>(R.id.dot)?.isSelected = helper.adapterPosition == selectedPosition
            }
        }
    }
}
