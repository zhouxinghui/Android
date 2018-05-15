package com.yzy.ebag.student.module.personal

import android.content.Context
import com.yzy.ebag.student.R
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.BaseDialog
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.T
import kotlinx.android.synthetic.main.dialog_parents_add.*

/**
 * Created by YZY on 2018/5/15.
 */
class ParentAddDialog(mContext: Context): BaseDialog(mContext) {
    override fun getLayoutRes(): Int = R.layout.dialog_parents_add

    var listener : (() -> Unit)? = null
    private val request = object: RequestCallBack<String>(){
        override fun onSuccess(entity: String?) {
            if (entity!!.contains("成功")) {
                T.show(mContext, "添加成功")
                listener?.invoke()
                dismiss()
            }else{
                T.show(mContext, entity)
                dismiss()
            }
        }

        override fun onError(exception: Throwable) {
            exception.handleThrowable(mContext)
            dismiss()
        }

    }
    init {
        tvConfirm.setOnClickListener {
            if (etCode.text.isEmpty()){
                T.show(mContext,"请输入书包号")
                return@setOnClickListener
            }

            if (etRelation.text.isEmpty()){
                T.show(mContext,"请输入你们的关系")
                return@setOnClickListener
            }

            StudentApi.bindParent(etCode.text.toString().trim(),etRelation.text.toString().trim(),request)
        }
    }
}