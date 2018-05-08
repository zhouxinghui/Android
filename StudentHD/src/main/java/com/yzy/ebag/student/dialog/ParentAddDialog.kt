package com.yzy.ebag.student.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import com.yzy.ebag.student.R
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.BaseFragmentDialog
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.T
import kotlinx.android.synthetic.main.dialog_parents_add.*

/**
 * @author caoyu
 * @date 2018/1/18
 * @description
 */
@SuppressLint("ValidFragment")
class ParentAddDialog(private val c:Context) : BaseFragmentDialog() {

    private lateinit var listener:SuccessListener

    companion object {
        fun newInstance(c: Context): ParentAddDialog {
            return ParentAddDialog(c)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun getBundle(bundle: Bundle?) {
    }

    override fun getLayoutRes(): Int {
        return R.layout.dialog_parents_add
    }

    override fun initView(view: View) {
        btnClose.setOnClickListener {
            dismiss()
        }


        tvConfirm.setOnClickListener {
            if (etCode.text.isEmpty()){
                T.show(activity,"请输入书包号")
                return@setOnClickListener
            }

            if (etRelation.text.isEmpty()){
                T.show(activity,"请输入你们的关系")
                return@setOnClickListener
            }

            StudentApi.bindParent(etCode.text.toString().trim(),etRelation.text.toString().trim(),object:RequestCallBack<String>(){

                override fun onSuccess(entity: String?) {
                    if (entity!!.contains("成功")) {
                        T.show(activity, "添加成功")
                        listener.refresh()
                        dismiss()
                    }else{
                        T.show(activity, entity)
                        dismiss()
                    }
                }

                override fun onError(exception: Throwable) {
                    exception.handleThrowable(c)
                    dismiss()
                }

            })
        }
    }

    fun setOnSuccessListener(listener:SuccessListener){
        this.listener = listener
    }

    interface SuccessListener{
        fun refresh()
    }
}