package com.yzy.ebag.student.module.personal

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.yzy.ebag.student.R
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.BaseFragmentDialog
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.T
import kotlinx.android.synthetic.main.dialog_class_add.*

/**
 * @author caoyu
 * @date 2018/1/18
 * @description
 */
class ClassJoinDialog: BaseFragmentDialog() {

    companion object {
        fun newInstance(): ClassJoinDialog{
            return ClassJoinDialog()
        }
    }

    override fun getBundle(bundle: Bundle?) {
    }

    override fun getLayoutRes(): Int {
        return R.layout.dialog_class_add
    }

    var successListener: (() -> Unit)? = null

    override fun initView(view: View) {
        tvConfirm.isEnabled = false

        etCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                tvConfirm.isEnabled = !s.isNullOrBlank()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        tvConfirm.setOnClickListener {
            StudentApi.joinClass(etCode.text.toString(), object : RequestCallBack<String>(){

                override fun onStart() {
                    LoadingDialogUtil.showLoading(mContext)
                }

                override fun onSuccess(entity: String?) {
                    T.show(mContext, "加入班级成功")
                    if(successListener != null)
                        successListener!!.invoke()
                    LoadingDialogUtil.closeLoadingDialog()
                }

                override fun onError(exception: Throwable) {
                    exception.handleThrowable(mContext)
                    LoadingDialogUtil.closeLoadingDialog()
                }

            })
        }

    }
}