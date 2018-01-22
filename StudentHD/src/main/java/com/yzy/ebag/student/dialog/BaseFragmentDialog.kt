package com.yzy.ebag.student.dialog

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import ebag.hd.R

/**
 * 写这个类
 * @author caoyu
 * @date 2018/1/16
 * @description
 */
abstract class BaseFragmentDialog: AppCompatDialogFragment() {

    lateinit var mContext: Context
    private var rootView : View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBundle(arguments)
        mContext = activity
        //添加这一行
        setStyle(AppCompatDialogFragment.STYLE_NO_TITLE, R.style.NoBackgroundDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(rootView == null){
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            rootView = inflater.inflate(getLayoutRes(), container, false)
        }else{
            val view = rootView!!.parent
            if(view != null)
                (view as ViewGroup).removeAllViewsInLayout()
        }
        return rootView
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