package com.yzy.ebag.parents.ui.widget

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.bean.JoinClazzBean
import com.yzy.ebag.parents.http.ParentsAPI
import ebag.core.base.BaseFragmentDialog
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.SerializableUtils
import ebag.core.util.T
import ebag.mobile.base.Constants
import ebag.mobile.bean.MyChildrenBean
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

    var successListener: ((clazzName:String,clazzId:String) -> Unit)? = null

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
            ParentsAPI.joinClazz(etCode.text.toString(),(SerializableUtils.getSerializable(Constants.CHILD_USER_ENTITY) as MyChildrenBean).uid, object : RequestCallBack<JoinClazzBean>(){

                override fun onStart() {
                    LoadingDialogUtil.showLoading(mContext)
                }

                override fun onSuccess(entity: JoinClazzBean?) {
                    T.show(mContext, "加入班级成功")
                    if(successListener != null)
                        successListener!!.invoke(entity!!.className,entity.classId)
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