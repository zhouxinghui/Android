package com.yzy.ebag.student.activity.homework

import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
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
class HomeworkActivity : BaseListTabActivity<List<SubjectBean>,SubjectBean>() {

    private var type = "1"
    private var classId = ""

    override fun loadConfig() {
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
        showTipTag("科目")
    }

    override fun requestData(requestCallBack: RequestCallBack<List<SubjectBean>>) {
        StudentApi.subjectWorkList(type, classId, "", 1, 10, requestCallBack)
    }

    override fun parentToList(parent: List<SubjectBean>?): List<SubjectBean>? {
        return parent
    }

    override fun getLeftAdapter(): BaseQuickAdapter<SubjectBean, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager? {
        return null
    }

    override fun getFragment(index: Int, item: SubjectBean?): Fragment {
        return HomeworkListFragment.newInstance(
                type,classId,
                item?.subCode ?: "",
                item?.homeWorkInfoVos
        )
    }

    override fun leftItemClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int) {
        (adapter as Adapter).selectedPosition = position
    }

    private class Adapter: BaseQuickAdapter<SubjectBean,BaseViewHolder>(R.layout.activity_homework_subject_item){

        var selectedPosition = 0
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun convert(helper: BaseViewHolder?, entity: SubjectBean?) {
            helper?.setText(R.id.text,entity?.subject ?: "")
            helper?.setBackgroundRes(
                    R.id.dot,
                    if(entity?.homeWorkComplete == "0")
                        R.drawable.homework_subject_dot_undo_selector
                    else
                        R.drawable.homework_subject_dot_done_selector
                )

            helper?.getView<TextView>(R.id.text)?.isSelected = helper?.adapterPosition == selectedPosition
            helper?.getView<View>(R.id.dot)?.isSelected = helper?.adapterPosition == selectedPosition
        }
    }
}
