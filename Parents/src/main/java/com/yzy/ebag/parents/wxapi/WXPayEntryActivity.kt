package com.yzy.ebag.parents.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import ebag.core.util.T
import ebag.mobile.base.ActivityUtils
import ebag.mobile.module.account.YBCenterActivity


/**
 * Created by fansan on 2018/3/20.
 */
class WXPayEntryActivity : Activity(), IWXAPIEventHandler {

    private lateinit var api: IWXAPI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = WXAPIFactory.createWXAPI(this, "wx626a1c084ecd9ca9")
        api.handleIntent(intent, this)
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
                ActivityUtils.skipActivityAndFinishAll(this@WXPayEntryActivity, YBCenterActivity::class.java)

            }
            -1 -> {
                T.show(this, "支付失败 : " + p0.errStr)
                ActivityUtils.skipActivityAndFinishAll(this@WXPayEntryActivity, YBCenterActivity::class.java)
            }
            -2 -> {
                T.show(this, "支付未完成")
                ActivityUtils.skipActivityAndFinishAll(this@WXPayEntryActivity, YBCenterActivity::class.java)
            }
        }
    }

    override fun onReq(p0: BaseReq?) {

    }

}