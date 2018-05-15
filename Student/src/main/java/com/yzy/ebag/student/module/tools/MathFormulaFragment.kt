package com.yzy.ebag.student.module.tools

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.FormulaTypeBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack

/**
 * Created by caoyu on 2018/1/14.
 */
class MathFormulaFragment: BaseListFragment<ArrayList<FormulaTypeBean>, FormulaTypeBean.FormulaBean>() {

    private var formulaType: FormulaTypeBean? = null
//    private var spanSize = 1

    companion object {
        fun newInstance(formulaTypeBean: FormulaTypeBean?): MathFormulaFragment{
            val fragment = MathFormulaFragment()
            if(formulaTypeBean != null){
                val bundle = Bundle()
                bundle.putParcelable("formulaType",formulaTypeBean)
                fragment.arguments = bundle
            }
            return fragment
        }
    }

    override fun getBundle(bundle: Bundle?) {
        formulaType = bundle?.getParcelable("formulaType")
//        if("几何公式" == formulaType?.formulaType)
//            spanSize = 2
    }

    override fun loadConfig() {
        setPadding(resources.getDimensionPixelSize(R.dimen.x5),
                resources.getDimensionPixelSize(R.dimen.x5),
                resources.getDimensionPixelSize(R.dimen.x5), 0)
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<ArrayList<FormulaTypeBean>>) {
        StudentApi.formula(formulaType?.id ?: "", page, getPageSize(), requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: ArrayList<FormulaTypeBean>?): List<FormulaTypeBean.FormulaBean>? {
        return if(parent != null && parent.isNotEmpty()){
            parent[0].formulaDetailDtos
        }else{
            null
        }
    }

    override fun getAdapter(): BaseQuickAdapter<FormulaTypeBean.FormulaBean, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<FormulaTypeBean.FormulaBean, BaseViewHolder>): RecyclerView.LayoutManager? = null

    private inner class Adapter: BaseQuickAdapter<FormulaTypeBean.FormulaBean,BaseViewHolder>(R.layout.item_fragment_math_formula_list){

        override fun convert(helper: BaseViewHolder, item: FormulaTypeBean.FormulaBean?) {
            val spannableString = SpannableString("${item?.formulaTitle}\n${item?.handleContent}")
            spannableString.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.tv_big)), 0, item?.formulaTitle?.length ?: 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            helper.setText(R.id.tvText, spannableString)
//            helper.setText(R.id.tvText, Html.fromHtml("<font><big>${item?.title}</big></font><br/>${item?.content}"))
        }
    }

}