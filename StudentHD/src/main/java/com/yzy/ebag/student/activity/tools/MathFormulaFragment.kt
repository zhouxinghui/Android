package com.yzy.ebag.student.activity.tools

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack

/**
 * Created by unicho on 2018/1/14.
 */
class MathFormulaFragment: BaseListFragment<String, MathFormulaFragment.Formula>() {

    private lateinit var formulaCode: String
    private var spanSize = 1

    companion object {
        fun newInstance(formulaCode: String): Fragment{
            val fragment = MathFormulaFragment()
            val bundle = Bundle()
            bundle.putString("formulaCode",formulaCode)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getBundle(bundle: Bundle) {
        formulaCode = bundle.getString("formulaCode","")
        if("几何公式" == formulaCode)
            spanSize = 2
    }

    override fun loadConfig() {
        setPadding(resources.getDimensionPixelSize(R.dimen.x5),
                resources.getDimensionPixelSize(R.dimen.x7),
                resources.getDimensionPixelSize(R.dimen.x5), 0)
        var list = ArrayList<Formula>()
        list.add(Formula("喝茶问题","(:＋差 ÷2)大数 \n(:(差 ÷2)小数"))
        list.add(Formula("喝茶问题","(:＋差 ÷2)大数 \n(:(差 ÷2)小数"))
        list.add(Formula("喝茶问题","(:＋差 ÷2)大数 \n(:(差 ÷2)小数"))
        list.add(Formula("喝茶问题","(:＋差 ÷2)大数 \n(:(差 ÷2)小数"))
        list.add(Formula("喝茶问题","(:＋差 ÷2)大数 \n(:(差 ÷2)小数"))
        list.add(Formula("喝茶问题","(:＋差 ÷2)大数 \n(:(差 ÷2)小数"))
        list.add(Formula("喝茶问题","(:＋差 ÷2)大数 \n(:(差 ÷2)小数"))
        withFirstPageData(list)
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<String>) {
    }

    override fun parentToList(isFirstPage: Boolean, parent: String?): List<Formula>? {
        return null
    }

    override fun getAdapter(): BaseQuickAdapter<Formula, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<Formula, BaseViewHolder>): RecyclerView.LayoutManager? {
        return GridLayoutManager(mContext,spanSize)
    }

    private inner class Adapter: BaseQuickAdapter<Formula,BaseViewHolder>(R.layout.fragment_math_formula_list_item){

        override fun convert(helper: BaseViewHolder, item: Formula?) {
            val spannableString = SpannableString("${item?.title}\n${item?.content}")
            spannableString.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.x28)), 0, item?.title?.length ?: 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            helper.setText(R.id.tvText, spannableString)
//            helper.setText(R.id.tvText, Html.fromHtml("<font><big>${item?.title}</big></font><br/>${item?.content}"))
        }
    }

    data class Formula(
            val title: String = "",
            val content: String = ""
            )
}