package com.yzy.ebag.teacher.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import ebag.core.util.T

/**
 * Created by fansan on 2018/3/20.
 */
class WXPayEntryActivity : Activity(), IWXAPIEventHandler {

    private lateinit var  api: IWXAPI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = WXAPIFactory.createWXAPI(this,"wx4adbb68ec1c80484")
        api.handleIntent(intent,this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        api.handleIntent(intent,this)
    }


    override fun onResp(p0: BaseResp?) {
        T.show(this,"code = " + p0?.errCode)
        Log.d("WXPAY", "onPayFinish, code = " + p0?.errCode)
        finish()
    }

    override fun onReq(p0: BaseReq?) {

    }

}