package com.yzy.ebag.student.activity.center.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.yzy.ebag.student.R
import ebag.core.base.BaseFragment
import ebag.core.util.T
import ebag.hd.activity.AddressListActivity
import ebag.hd.activity.YBCenterActivity
import kotlinx.android.synthetic.main.fragment_yb_center.*

/**
 * @author caoyu
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
            startActivity(Intent(activity,AddressListActivity::class.java))
        }

        btnGift.setOnClickListener {
            T.show(mContext,"暂未开放")
        }

        btnOrder.setOnClickListener {
            T.show(mContext,"暂未开放")
        }

        btnCenter.setOnClickListener {
            startActivity(Intent(activity,YBCenterActivity::class.java))
        }
    }
}