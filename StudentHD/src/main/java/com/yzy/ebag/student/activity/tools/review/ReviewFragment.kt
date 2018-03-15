package com.yzy.ebag.student.activity.tools.review

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack

/**
 * Created by unicho on 2018/3/9.
 */
class ReviewFragment : BaseListFragment<ArrayList<String>, String>(){
    private var unitCode = ""
    companion object {
        fun newInstance(): ReviewFragment{
            val fragment = ReviewFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun getBundle(bundle: Bundle?) {
    }

    override fun loadConfig() {
    }

    /**
     * 数据更新
     */
    fun update(unitCode: String?){
        if(this.unitCode != unitCode){
            this.unitCode = unitCode ?: ""
            cancelRequest()
            onRetryClick()
        }
    }

    override fun isPagerFragment(): Boolean {
        return false
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<ArrayList<String>>) {
    }

    override fun parentToList(isFirstPage: Boolean, parent: ArrayList<String>?): List<String>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<String, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<String, BaseViewHolder>): RecyclerView.LayoutManager? {
        return GridLayoutManager(mContext, 2)
    }

    inner class Adapter: BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_review){
        override fun convert(helper: BaseViewHolder?, item: String?) {

        }
    }
}