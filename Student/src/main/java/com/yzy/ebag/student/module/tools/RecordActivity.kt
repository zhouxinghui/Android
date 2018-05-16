package com.yzy.ebag.student.module.tools

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.EditText
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.WordRecordBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.http.network.RequestCallBack
import ebag.core.util.DateUtil
import ebag.core.util.loadImage
import ebag.mobile.base.BaseListActivity

class RecordActivity: BaseListActivity<WordRecordBean, WordRecordBean.ListBean>(){

    companion object {
        fun jump(context: Context, classId: String){
            context.startActivity(
                    Intent(context,RecordActivity::class.java)
                            .putExtra("classId", classId)
            )
        }
    }

    private var classId = ""
    override fun loadConfig(intent: Intent) {
        titleBar.setTitle("生字记录")
        classId = intent.getStringExtra("classId") ?: ""
    }

    override fun getPageSize(): Int {
        return 10
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<WordRecordBean>) {
        StudentApi.wordRecord(classId, getPageSize(), page, requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: WordRecordBean?): List<WordRecordBean.ListBean>? {
        return parent?.list
    }

    override fun getAdapter(): BaseQuickAdapter<WordRecordBean.ListBean, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<WordRecordBean.ListBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    inner class Adapter: BaseQuickAdapter<WordRecordBean.ListBean, BaseViewHolder>(R.layout.item_activity_record){

        override fun convert(helper: BaseViewHolder, item: WordRecordBean.ListBean?) {
            helper.setText(R.id.tvContent,"默写生字：${item?.words ?: ""}")
                    .setText(R.id.tvEdition, "${item?.gradeName}-${item?.versionName}-${item?.semesterName}-${item?.unitName}-${item?.name}")
                    .setText(R.id.tvTime, DateUtil.getDateTime(item?.createDate ?: 0, "yyyy-MM-dd HH:mm"))
            helper.getView<EditText>(R.id.scoreEdit).setText(item?.score ?: "")
            val recyclerView = helper.getView<RecyclerView>(R.id.recyclerView)
            recyclerView.isNestedScrollingEnabled = false
            if(recyclerView.adapter == null)
                recyclerView.adapter = ItemAdapter()
            if(recyclerView.layoutManager == null)
                recyclerView.layoutManager = GridLayoutManager(mContext,4)
            recyclerView.postDelayed({
                (recyclerView.adapter as ItemAdapter).setNewData(item?.wordUrl?.split(","))
            },20)

        }
    }

    inner class ItemAdapter: BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_activity_record_item){
        override fun convert(helper: BaseViewHolder, item: String?) {
            //helper.setText(R.id.tvChar, item)
            helper.getView<ImageView>(R.id.iv_char).loadImage(item)
        }

    }
}