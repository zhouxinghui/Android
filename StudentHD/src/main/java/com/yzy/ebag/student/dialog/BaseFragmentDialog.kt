package com.yzy.ebag.student.dialog

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ebag.hd.R

/**
 * @author 曹宇
 * @date 2018/1/16
 * @description
 */
open abstract class BaseFragmentDialog: AppCompatDialogFragment() {

    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBundle(arguments)
        mContext = activity
        setStyle(AppCompatDialogFragment.STYLE_NO_TITLE, R.style.NoBackgroundDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutRes(), container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view!!)

    }

    /**获取数据源*/
    abstract fun getBundle(bundle: Bundle?)
    abstract fun getLayoutRes(): Int
    abstract fun initView(view: View)

}