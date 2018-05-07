package ebag.mobile.module.shop

import android.content.Context
import android.content.Intent
import ebag.core.base.BaseActivity
import ebag.mobile.R
import ebag.mobile.module.account.YBCenterActivity
import kotlinx.android.synthetic.main.activity_yb.*


class YBActivity: BaseActivity(){
    override fun getLayoutId(): Int = R.layout.activity_yb

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

    companion object {

        fun start(c:Context){
            c.startActivity(Intent(c,YBActivity::class.java))
        }
    }

}