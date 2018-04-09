package com.yzy.ebag.student.dialog

import android.os.Bundle
import android.view.View
import com.yzy.ebag.student.R
import ebag.hd.base.BaseFragmentDialog

/**
 * Created by fansan on 2018/4/8.
 */

class ClazzmateInfoDIalog:BaseFragmentDialog(){


    companion object {
        fun newInstance(): ClazzmateInfoDIalog {
            return ClazzmateInfoDIalog()
        }
    }

    override fun getBundle(bundle: Bundle?) {

    }

    override fun getLayoutRes(): Int = R.layout.dialog_classmateinfo

    override fun initView(view: View) {

    }

}