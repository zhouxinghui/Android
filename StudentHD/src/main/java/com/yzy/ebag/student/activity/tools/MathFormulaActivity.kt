package com.yzy.ebag.student.activity.tools

import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.FormulaTypeBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.http.network.RequestCallBack
import ebag.hd.base.BaseListTabActivity

/**
 * Created by caoyu on 2018/1/13.
 */
class MathFormulaActivity: BaseListTabActivity<ArrayList<FormulaTypeBean>, FormulaTypeBean>() {

    override fun loadConfig() {
        setLeftWidth(resources.getDimensionPixelSize(R.dimen.x200))
//        val list = arrayListOf("几何公式","数学定律","数量关系","单位换算","特殊问题")
//        withTabData(list)
    }

    override fun requestData(requestCallBack: RequestCallBack<ArrayList<FormulaTypeBean>>) {
        StudentApi.formula("", 1, 10, requestCallBack)
    }

    override fun parentToList(parent: ArrayList<FormulaTypeBean>?): List<FormulaTypeBean>? {
        return parent
    }

    override fun getLeftAdapter(): BaseQuickAdapter<FormulaTypeBean, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<FormulaTypeBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    override fun getFragment(pagerIndex: Int, adapter: BaseQuickAdapter<FormulaTypeBean, BaseViewHolder>): Fragment {
        return MathFormulaFragment.newInstance(adapter.getItem(pagerIndex))
    }

    override fun getViewPagerSize(adapter: BaseQuickAdapter<FormulaTypeBean, BaseViewHolder>): Int {
        return adapter.itemCount
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        setCurrentItem(position)
        (adapter as Adapter).selectedPosition = position
    }

    private class Adapter: BaseQuickAdapter<FormulaTypeBean,BaseViewHolder>(R.layout.item_activity_homework_subject){

        var selectedPosition = 0
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun convert(helper: BaseViewHolder?, entity: FormulaTypeBean?) {
            helper?.setText(R.id.text,entity?.formulaType ?: "XXX")
            helper?.getView<TextView>(R.id.text)?.isSelected = helper?.adapterPosition == selectedPosition
            helper?.getView<View>(R.id.dot)?.isSelected = helper?.adapterPosition == selectedPosition
        }
    }
}