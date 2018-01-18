package com.yzy.ebag.student.activity.center.fragment

import android.os.Bundle
import android.view.View
import com.yzy.ebag.student.R
import ebag.core.base.BaseFragment
import ebag.core.util.T
import kotlinx.android.synthetic.main.fragment_yb_center.*

/**
 * @author 曹宇
 * @date 2018/1/18
 * @description
 */
class YbCenterFragment: BaseFragment() {

    companion object {
        fun newInstance(): YbCenterFragment{
            return YbCenterFragment()
        }
    }
    override fun getLayoutRes(): Int {
        return R.layout.fragment_yb_center
    }

    override fun getBundle(bundle: Bundle?) {
    }

    override fun initViews(rootView: View) {
        btnAddress.setOnClickListener {
            T.show(mContext,"暂未开放")
        }

        btnGift.setOnClickListener {
            T.show(mContext,"暂未开放")
        }

        btnOrder.setOnClickListener {
            T.show(mContext,"暂未开放")
        }

        btnCenter.setOnClickListener {
            T.show(mContext,"暂未开放")
        }
    }
}