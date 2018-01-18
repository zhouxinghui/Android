package com.yzy.ebag.student.activity.center.fragment

import android.os.Bundle
import android.view.View
import com.yzy.ebag.student.R
import ebag.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_parents_info.*

/**
 * @author 曹宇
 * @date 2018/1/18
 * @description
 */
class ParentInfoFragment: BaseFragment() {

    companion object {
        fun newInstance(): ParentInfoFragment{
            return ParentInfoFragment()
        }
    }

    var backClick: ((view:View) -> Unit)? = null

    override fun getLayoutRes(): Int {
        return R.layout.fragment_parents_info
    }

    override fun getBundle(bundle: Bundle?) {
    }

    override fun initViews(rootView: View) {
        btnBack.setOnClickListener{
            backClick?.invoke(it)
        }
    }

}