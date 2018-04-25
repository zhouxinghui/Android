package com.yzy.ebag.teacher.module.tools

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.LetterRecordBaseBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.util.DateUtil
import java.util.*

/**
 * Created by unicho on 2018/3/13.
 */
class LetterRecordFragment: BaseListFragment<List<LetterRecordBaseBean>, LetterRecordBaseBean>() {
    private var unitCode = ""
    private var classId = ""
    companion object {
        fun newInstance(): LetterRecordFragment{
            val fragment = LetterRecordFragment()
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

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<LetterRecordBaseBean>>) {
        TeacherApi.getLetterRecord(unitCode, classId, requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<LetterRecordBaseBean>?): List<LetterRecordBaseBean>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<LetterRecordBaseBean, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<LetterRecordBaseBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return GridLayoutManager(mContext, 2)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        adapter as Adapter
        LetterRecordListActivity.jump(unitCode, adapter.data[position].createDate, classId, mContext)
    }
    inner class Adapter: BaseQuickAdapter<LetterRecordBaseBean, BaseViewHolder>(R.layout.item_tools_record_history){
        override fun convert(helper: BaseViewHolder, item: LetterRecordBaseBean?) {
            helper.setText(R.id.tvTime, DateUtil.getFormatDateTime(Date(item?.createDate ?: 0), "yyyy年MM月dd日"))
                    .setText(R.id.tvNumber, "${item?.timeLength}人生字练习")
        }

    }
}