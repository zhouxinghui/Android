package com.yzy.ebag.student.activity.tools

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack

/**
 * Created by unicho on 2018/1/14.
 */
class MathFormulaFragment: BaseListFragment<String,String>() {
    override fun getBundle(bundle: Bundle) {
    }

    override fun loadConfig() {
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<String>) {
    }

    override fun parentToList(isFirstPage: Boolean, parent: String?): List<String>? {
        return null
    }

    override fun getAdapter(): BaseQuickAdapter<String, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager? {
        return null
    }

    private class Adapter: BaseQuickAdapter<String,BaseViewHolder>(R.layout.fragment_math_formula_list_item){
        override fun convert(helper: BaseViewHolder?, item: String?) {
        }

    }
}