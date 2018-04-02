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
import com.yzy.ebag.student.bean.ErrorTopicBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.http.network.RequestCallBack
import ebag.core.util.SPUtils
import ebag.hd.base.BaseListTabActivity

/**
 * @author caoyu
 * @date 2018/1/20
 * @description
 */
class ErrorTopicActivity: BaseListTabActivity<ArrayList<ErrorTopicBean>, ErrorTopicBean>() {

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

    }

    override fun requestData(requestCallBack: RequestCallBack<ArrayList<ErrorTopicBean>>) {
        StudentApi.errorTopic(classId, "", 1, 10, requestCallBack)
    }

    override fun parentToList(parent: ArrayList<ErrorTopicBean>?): List<ErrorTopicBean>? {
        if (parent!!.isNotEmpty()) {
            val bean = ErrorTopicBean()
            bean.itemType = 1
            parent?.add(0, bean)
            SPUtils.put(this, "subCode", parent!![1].subCode)
            return parent
        }

        return null
    }

    override fun getLeftAdapter(): BaseQuickAdapter<ErrorTopicBean, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<ErrorTopicBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    override fun getFragment(pagerIndex: Int, adapter: BaseQuickAdapter<ErrorTopicBean, BaseViewHolder>): Fragment {
        return TopicListFragment.newInstance(
                classId,
                adapter.getItem(pagerIndex + 1)?.subCode ?: "",
                adapter.getItem(pagerIndex + 1)?.errorHomeWorkVos
        )
    }

    override fun getViewPagerSize(adapter: BaseQuickAdapter<ErrorTopicBean, BaseViewHolder>): Int {
        return adapter.data.size - 1
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        if(position != 0 && (adapter as Adapter).selectedPosition != position){
            setCurrentItem(position-1)
            adapter.selectedPosition = position
        }
    }

    private class Adapter: BaseMultiItemQuickAdapter<ErrorTopicBean, BaseViewHolder>(null){

        init {
            addItemType(0, R.layout.item_activity_homework_subject)
            addItemType(1, R.layout.item_activity_homework_subject_header)
        }

        var selectedPosition = 1
            set(value) {
                field = value
                SPUtils.put(mContext, "subCode", data[selectedPosition].subCode)
                notifyDataSetChanged()
            }

        override fun convert(helper: BaseViewHolder, entity: ErrorTopicBean?) {
            if(helper.itemViewType == 0){
                helper.setText(R.id.text,entity?.subject ?: "")
                helper.setBackgroundRes(
                        R.id.dot,
                        // null 或  已完成
                        // 0 未纠正 1 已纠正
                        if(entity?.errorState != "0")
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