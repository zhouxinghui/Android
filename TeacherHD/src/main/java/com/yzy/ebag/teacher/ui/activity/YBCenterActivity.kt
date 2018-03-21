package com.yzy.ebag.teacher.ui.activity

import android.content.Intent
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseActivity
import ebag.hd.activity.AddressListActivity
import ebag.hd.activity.ShopActivity
import ebag.hd.activity.ShopOrderActivity
import ebag.hd.activity.YBCenterActivity
import kotlinx.android.synthetic.main.fragment_yb_center.*

/**
 * Created by fansan on 2018/3/20.
 */
class YBCenterActivity:BaseActivity(){
    override fun getLayoutId(): Int = R.layout.fragment_yb_center

    override fun initViews() {

        btnAddress.setOnClickListener {
            startActivity(Intent(this, AddressListActivity::class.java))
        }

        btnGift.setOnClickListener {
            startActivity(Intent(this, ShopActivity::class.java))
        }

        btnOrder.setOnClickListener {
            startActivity(Intent(this, ShopOrderActivity::class.java))
        }

        btnCenter.setOnClickListener {
            startActivity(Intent(this, YBCenterActivity::class.java))
        }
    }

}