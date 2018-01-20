package com.yzy.ebag.student.activity.homework

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.TestFragment
import com.yzy.ebag.student.base.BaseListTabActivity
import com.yzy.ebag.student.bean.SubjectBean
import ebag.core.http.network.RequestCallBack

/**
 * @author caoyu
 * @date 2018/1/20
 * @description
 */
class ErrorTopicActivity: BaseListTabActivity<ArrayList<SubjectBean>, SubjectBean>() {

    companion object {
        fun jump(content: Context, classId: String){
            content.startActivity(
                    Intent(content,ErrorTopicActivity::class.java)
                            .putExtra("classId",classId)
            )
        }
    }

    private var classId = ""
    override fun loadConfig() {
        classId = intent.getStringExtra("classId") ?: ""

        setLeftWidth(resources.getDimensionPixelSize(R.dimen.x180))
        setTitleContent(R.string.main_error_topic)

        val list = ArrayList<SubjectBean>()
        var subjectBean = SubjectBean()
        subjectBean.itemType = 1
        list.add(subjectBean)

        subjectBean = SubjectBean()
        subjectBean.subCode = "yw"
        subjectBean.subject = "语文"
        subjectBean.homeWorkComplete = "1"
        list.add(subjectBean)

        subjectBean = SubjectBean()
        subjectBean.subCode = "sx"
        subjectBean.subject = "数学"
        subjectBean.homeWorkComplete = "1"
        list.add(subjectBean)

        subjectBean = SubjectBean()
        subjectBean.subCode = "yy"
        subjectBean.subject = "英语"
        subjectBean.homeWorkComplete = "0"
        list.add(subjectBean)

        withTabData(list)

    }

    override fun requestData(requestCallBack: RequestCallBack<ArrayList<SubjectBean>>) {
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

    override fun getLayoutManager(adapter: BaseQuickAdapter<SubjectBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    override fun getFragment(pagerIndex: Int, adapter: BaseQuickAdapter<SubjectBean, BaseViewHolder>): Fragment {
        return TopicListFragment.newInstance(
                classId,
                adapter.getItem(pagerIndex + 1)?.subCode ?: "",
                adapter.getItem(pagerIndex + 1)?.homeWorkInfoVos
        )
    }

    override fun getViewPagerSize(adapter: BaseQuickAdapter<SubjectBean, BaseViewHolder>): Int {
        return adapter.data.size - 1
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        if(position != 0 && (adapter as Adapter).selectedPosition != position){
            setCurrentItem(position-1)
            adapter.selectedPosition = position
        }
    }

    private class Adapter: BaseMultiItemQuickAdapter<SubjectBean, BaseViewHolder>(null){

        init {
            addItemType(0, R.layout.item_activity_homework_subject)
            addItemType(1, R.layout.item_activity_homework_subject_header)
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