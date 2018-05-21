package com.yzy.ebag.teacher.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import ebag.core.util.SPUtils
import ebag.core.util.T
import ebag.mobile.base.ActivityUtils
import ebag.mobile.base.Constants
import ebag.mobile.module.account.YBCenterActivity
import ebag.mobile.module.shop.ShopOrderActivity


/**
 * Created by fansan on 2018/3/20.
 */
class WXPayEntryActivity : Activity(), IWXAPIEventHandler {

    private lateinit var api: IWXAPI
    private var flag = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = WXAPIFactory.createWXAPI(this, "wx4adbb68ec1c80484")
        api.handleIntent(intent, this)
        flag = (SPUtils.get(this, Constants.WXPAY_FLAG, -1) as Int)
        ActivityUtils.addActivity(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        api.handleIntent(intent, this)
    }


    override fun onResp(p0: BaseResp?) {
        when (p0?.errCode) {
            0 -> {
                T.show(this, "支付成功")
                if (flag == 1) {
                    ActivityUtils.skipActivityAndFinishAll(this@WXPayEntryActivity, YBCenterActivity::class.java)
                } else if (flag == 2) {
                    ActivityUtils.skipActivityAndFinishAll(this@WXPayEntryActivity, ShopOrderActivity::class.java)
                }

            }
            -1 -> {
                T.show(this, "支付失败 : " + p0.errStr)
                if (flag == 1) {
                    ActivityUtils.skipActivityAndFinishAll(this@WXPayEntryActivity, YBCenterActivity::class.java)
                } else if (flag == 2) {
                    ActivityUtils.skipActivityAndFinishAll(this@WXPayEntryActivity, ShopOrderActivity::class.java)
                }
            }
            -2 -> {
                T.show(this, "支付未完成")
                if (flag == 1) {
                    ActivityUtils.skipActivityAndFinishAll(this@WXPayEntryActivity, YBCenterActivity::class.java)
                } else if (flag == 2) {
                    ActivityUtils.skipActivityAndFinishAll(this@WXPayEntryActivity, ShopOrderActivity::class.java)
                }
            }
        }
    }

    override fun onReq(p0: BaseReq?) {

    }

}