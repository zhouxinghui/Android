package com.yzy.ebag.parents.ui.fragment

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.ui.activity.ReadRecordListActivity
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.util.DateUtil
import ebag.mobile.bean.ReadRecordBaseBean
import ebag.mobile.http.EBagApi
import java.util.*

/**
 * Created by unicho on 2018/3/13.
 */
class ReadRecordFragment : BaseListFragment<List<ReadRecordBaseBean>, ReadRecordBaseBean>() {
    private var unitCode = ""
    private var classId = ""
    companion object {
        fun newInstance(): ReadRecordFragment{
            val fragment = ReadRecordFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun getBundle(bundle: Bundle?) {
    }

    override fun loadConfig() {
        loadMoreEnabled(false)
    }
    /**
     * 数据更新
     */
    fun update(unitCode: String?, classId: String?){
        if(this.unitCode != unitCode || this.classId != classId){
            this.unitCode = unitCode ?: ""
            this.classId = classId ?: ""
            cancelRequest()
            onRetryClick()
        }
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<ReadRecordBaseBean>>) {
        EBagApi.getReadRecord(unitCode, classId, requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<ReadRecordBaseBean>?): List<ReadRecordBaseBean>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<ReadRecordBaseBean, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<ReadRecordBaseBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return GridLayoutManager(mContext, 2)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        adapter as Adapter
        ReadRecordListActivity.jump(unitCode, classId, adapter.data[position].dateTime, mContext)
    }

    inner class Adapter: BaseQuickAdapter<ReadRecordBaseBean, BaseViewHolder>(R.layout.item_tools_record_history){
        override fun convert(helper: BaseViewHolder, item: ReadRecordBaseBean?) {
            helper.setText(R.id.tvTime, DateUtil.getFormatDateTime(Date(item?.dateTime ?: 0), "yyyy年MM月dd日"))
                    .setText(R.id.tvNumber, "${item?.studentCount}人口语练习")
        }
    }
}