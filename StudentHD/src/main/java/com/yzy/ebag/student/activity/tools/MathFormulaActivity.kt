package com.yzy.ebag.student.activity.tools

import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.TestFragment
import com.yzy.ebag.student.base.BaseListTabActivity
import ebag.core.http.network.RequestCallBack

/**
 * Created by unicho on 2018/1/13.
 */
class MathFormulaActivity: BaseListTabActivity<String,String>() {
    override fun loadConfig() {
        val list = arrayListOf("几何公式","数学定律","数量关系","单位换算","特殊问题")
        withTabData(list)
    }

    override fun requestData(requestCallBack: RequestCallBack<String>) {
    }

    override fun parentToList(parent: String?): List<String>? {
        return null
    }

    override fun getLeftAdapter(): BaseQuickAdapter<String, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager? {
        return null
    }

    override fun getFragment(index: Int, item: String?): Fragment {
        return TestFragment.newInstance()
    }

    override fun leftItemClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int) {
        (adapter as Adapter).selectedPosition = position
    }

    private class Adapter: BaseQuickAdapter<String,BaseViewHolder>(R.layout.activity_homework_subject_item){

        var selectedPosition = 0
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun convert(helper: BaseViewHolder?, entity: String?) {
            helper?.setText(R.id.text,entity ?: "")
            helper?.getView<TextView>(R.id.text)?.isSelected = helper?.adapterPosition == selectedPosition
            helper?.getView<View>(R.id.dot)?.isSelected = helper?.adapterPosition == selectedPosition
        }
    }
}