package com.yzy.ebag.student.activity.tools.practise

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.base.BaseListActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.util.DateUtil
import ebag.core.util.T

/**
 * @author caoyu
 * @date 2018/1/23
 * @description
 */
class RecordActivity: BaseListActivity<List<RecordActivity.Record>, RecordActivity.Record>(){

    companion object {
        fun jump(context: Context){
            context.startActivity(Intent(context,RecordActivity::class.java))
        }
    }

    lateinit var edit: EditText
    override fun loadConfig(intent: Intent) {

        val view = layoutInflater.inflate(R.layout.layout_record_search,null)
        edit = view.findViewById(R.id.editText)
        view.findViewById<View>(R.id.image).setOnClickListener { search() }
        edit.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                handled = true
                if(isEditEmpty()){
                    T.show(this,"请输入关键字")
                }else{
                    /*隐藏软键盘*/
                    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                }
            }
            handled
        }

        val p = RelativeLayout.LayoutParams(resources.getDimensionPixelOffset(R.dimen.x465)
                ,resources.getDimensionPixelOffset(R.dimen.x48))
        p.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE)
        titleBar.addView(view,p)
        val list = ArrayList<Record>()
        list.add(Record())
        list.add(Record())
        list.add(Record())
        list.add(Record())
        list.add(Record())
        list.add(Record())
        list.add(Record())
        list.add(Record())
        list.add(Record())
        list.add(Record())

        withFirstPageData(list)
    }

    private fun search(){
        if(isEditEmpty()){
            T.show(this,"请输入关键字")
            return
        }
    }

    private fun isEditEmpty(): Boolean{
        return edit.text.toString().isBlank()
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<RecordActivity.Record>>) {
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<RecordActivity.Record>?): List<Record>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<Record, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<Record, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    inner class Adapter: BaseQuickAdapter<Record, BaseViewHolder>(R.layout.item_activity_record){

        override fun convert(helper: BaseViewHolder, item: Record?) {
            helper.setText(R.id.tvContent,"默写生字：${item?.letters ?: ""}")
                    .setText(R.id.tvEdition, item?.content)
                    .setText(R.id.tvTime, DateUtil.getDateTime(item?.time ?: 0, "yyyy-MM-dd HH:mm"))
            val recyclerView = helper.getView<RecyclerView>(R.id.recyclerView)
            recyclerView.isNestedScrollingEnabled = false
            if(recyclerView.adapter == null)
                recyclerView.adapter = ItemAdapter()
            if(recyclerView.layoutManager == null)
                recyclerView.layoutManager = GridLayoutManager(mContext,12)
            recyclerView.postDelayed({
                (recyclerView.adapter as ItemAdapter).setNewData(item?.letters?.split(","))
            },20)

        }
    }

    inner class ItemAdapter: BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_activity_record_item){
        override fun convert(helper: BaseViewHolder, item: String?) {
            helper.setText(R.id.tvChar, item)
        }

    }

    data class Record(
            val letters: String = "画,花,话,化,画,花,话,化,画,花,话,化",
            val content: String = "三年级-北师大-上册-第一单元-观潮",
            val time: Long = 1515996933000
    )
}