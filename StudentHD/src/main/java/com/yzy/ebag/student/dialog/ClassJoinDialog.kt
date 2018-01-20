package com.yzy.ebag.student.dialog

import android.os.Bundle
import android.view.View
import com.yzy.ebag.student.R
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun getBundle(bundle: Bundle?) {
    }

    override fun getLayoutRes(): Int {
        return R.layout.dialog_class_add
    }

    override fun initView(view: View) {
        btnClose.setOnClickListener {
            dismiss()
        }
    }
}