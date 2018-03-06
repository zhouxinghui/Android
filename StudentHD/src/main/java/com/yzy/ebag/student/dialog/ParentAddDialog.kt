package com.yzy.ebag.student.dialog

import android.os.Bundle
import android.view.View
import com.yzy.ebag.student.R
import ebag.hd.base.BaseFragmentDialog
import kotlinx.android.synthetic.main.dialog_class_add.*

/**
 * @author caoyu
 * @date 2018/1/18
 * @description
 */
class ParentAddDialog : BaseFragmentDialog() {

    companion object {
        fun newInstance(): ParentAddDialog {
            return ParentAddDialog()
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
    }
}