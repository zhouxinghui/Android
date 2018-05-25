package com.yzy.ebag.teacher.module.prepare

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseDialog
import ebag.core.http.network.MsgException
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.T
import ebag.mobile.bean.BaseClassesBean
import ebag.mobile.http.EBagApi
import kotlinx.android.synthetic.main.dialog_select_group.*

/**
 * Created by YZY on 2018/5/22.
 */
class DialogSelectClasses(mContext: Context): BaseDialog (mContext){
    override fun getLayoutRes(): Int = R.layout.dialog_select_group
    override fun setWidth(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.x250)
    }

    override fun setHeight(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.y550)
    }
    val request = object : RequestCallBack<List<BaseClassesBean>>(){
        override fun onStart() {
            stateView.showLoading()
        }

        override fun onSuccess(entity: List<BaseClassesBean>?) {
            if (entity == null || entity.isEmpty()){
                stateView.showEmpty()
                return
            }
            adapter.setNewData(entity)
            stateView.showContent()
        }

        override fun onError(exception: Throwable) {
            if (exception is MsgException){
                stateView.showError(exception.message.toString())
            }else{
                stateView.showError()
                exception.handleThrowable(mContext)
            }
        }
    }
    private val selectList = ArrayList<BaseClassesBean?>()
    private val adapter = MyAdapter()
    var onConfirmClick : ((groupList: ArrayList<BaseClassesBean?>) -> Unit)? = null
    init {
        title_tv.text = "选择班级"
        stateView.setBackgroundRes(R.drawable.normal_dialog_bg)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        confirmBtn.setOnClickListener {
            if (selectList.isEmpty()){
                T.show(mContext, "请选择班级")
                return@setOnClickListener
            }
            onConfirmClick?.invoke(selectList)
            dismiss()
        }
        EBagApi.getMyClasses(request)
        stateView.setOnRetryClickListener { EBagApi.getMyClasses(request) }
    }

    private inner class MyAdapter: BaseQuickAdapter<BaseClassesBean, BaseViewHolder>(R.layout.item_select_school){
        override fun convert(helper: BaseViewHolder, item: BaseClassesBean?) {
            val checkBox = helper.getView<CheckBox>(R.id.checkbox)
            checkBox.text = item?.className
            helper.itemView.setOnClickListener {
                if (selectList.contains(item))
                    selectList.remove(item)
                else
                    selectList.add(item)
            }
            checkBox.isChecked = selectList.contains(item)
        }
    }
}