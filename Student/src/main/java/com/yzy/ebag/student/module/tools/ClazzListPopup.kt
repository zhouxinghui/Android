package com.yzy.ebag.student.module.tools

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.WindowManager
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import ebag.core.base.BasePopupWindow
import ebag.core.http.network.RequestCallBack
import ebag.core.widget.empty.StateView
import ebag.mobile.bean.BaseClassesBean
import ebag.mobile.http.EBagApi

/**
 * Created by YZY on 2018/4/24.
 */
class ClazzListPopup(private val mContext: Context, queryType: String): BasePopupWindow(mContext) {
    override fun getLayoutRes(): Int = R.layout.popup_clazz_list
    override fun setWidth(): Int = WindowManager.LayoutParams.MATCH_PARENT
    override fun setHeight(): Int = contentView.resources.getDimensionPixelSize(R.dimen.y500)

    private val classesRequest = object : RequestCallBack<List<BaseClassesBean>>(){
        override fun onStart() {
            stateView.showLoading()
        }

        override fun onSuccess(entity: List<BaseClassesBean>?) {
            if (entity == null || entity.isEmpty()){
                stateView.showEmpty("暂无班级信息")
                return
            }
            stateView.showContent()
            adapter.setNewData(entity)
        }

        override fun onError(exception: Throwable) {
            stateView.showError(exception.message.toString())
        }
    }
    private val stateView = contentView.findViewById<StateView>(R.id.stateView)
    private val adapter = MyAdapter()
    var onClassSelectListener : ((classBean: BaseClassesBean) -> Unit)? = null
    init {
        stateView.setBackgroundRes(R.color.white)
        val recyclerView = contentView.findViewById<RecyclerView>(R.id.clazzRecycler)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener { _, _, position ->
            onClassSelectListener?.invoke(adapter.data[position])
            adapter.selectPosition = position
            dismiss()
        }

        EBagApi.getMyClasses(classesRequest)
    }

    inner class MyAdapter: BaseQuickAdapter<BaseClassesBean, BaseViewHolder>(R.layout.item_assign_popup){
        var selectPosition = -1
            set(value) {
                field = value
                notifyDataSetChanged()
            }
        override fun convert(helper: BaseViewHolder, item: BaseClassesBean?) {
            val textView = helper.getView<TextView>(R.id.tv)
            textView.text = item?.className
            textView.isSelected = selectPosition == helper.adapterPosition
        }
    }
}