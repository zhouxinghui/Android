package com.yzy.ebag.student.activity.tools.practise

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.EditText
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.base.BaseListActivity
import com.yzy.ebag.student.bean.WordRecordBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.http.network.RequestCallBack
import ebag.core.util.DateUtil

/**
 * @author caoyu
 * @date 2018/1/23
 * @description
 */
class RecordActivity: BaseListActivity<WordRecordBean, WordRecordBean.ListBean>(){

    companion object {
        fun jump(context: Context, classId: String){
            context.startActivity(
                    Intent(context,RecordActivity::class.java)
                            .putExtra("classId", classId)
            )
        }
    }

    lateinit var edit: EditText
    private var classId = ""
    override fun loadConfig(intent: Intent) {
        /*val view = layoutInflater.inflate(R.layout.layout_record_search,null)
        edit = view.findViewById(R.id.editText)
        view.findViewById<View>(R.id.image).setOnClickListener { search() }
        edit.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                handled = true
                if(isEditEmpty()){
                    T.show(this,"请输入关键字")
                }else{
                    *//*隐藏软键盘*//*
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
        titleBar.addView(view,p)*/
        titleBar.setTitle("生字记录")
        classId = intent.getStringExtra("classId") ?: ""
    }

    /*private fun search(){
        if(isEditEmpty()){
            T.show(this,"请输入关键字")
            return
        }
    }

    private fun isEditEmpty(): Boolean{
        return edit.text.toString().isBlank()
    }*/

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
                recyclerView.layoutManager = GridLayoutManager(mContext,12)
            recyclerView.postDelayed({
                (recyclerView.adapter as ItemAdapter).setNewData(item?.wordUrl?.split(","))
            },20)

        }
    }

    inner class ItemAdapter: BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_activity_record_item){
        override fun convert(helper: BaseViewHolder, item: String?) {
            helper.setText(R.id.tvChar, item)
        }

    }
}