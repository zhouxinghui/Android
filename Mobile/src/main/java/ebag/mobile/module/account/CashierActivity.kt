package ebag.mobile.module.account

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import com.alipay.sdk.app.PayTask
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.T
import ebag.mobile.R
import ebag.mobile.base.ActivityUtils
import ebag.mobile.bean.PayResult
import ebag.mobile.bean.WXPayBean
import ebag.mobile.http.EBagApi
import kotlinx.android.synthetic.main.activity_cashier.*

class CashierActivity : BaseActivity() {

    private lateinit var shouldPayCount: String
    private lateinit var number: String

    override fun getLayoutId(): Int = R.layout.activity_cashier


    @SuppressLint("HandlerLeak")
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            @Suppress("UNCHECKED_CAST")
            val result = PayResult(msg?.obj as Map<String, String>)
            val resultInfo = result.result
            val resultStatus = result.resultStatus
            if (TextUtils.equals(resultStatus, "9000")) {
                T.show(this@CashierActivity, "支付成功")
            } else {
                T.show(this@CashierActivity, "支付失败")
            }
            ActivityUtils.skipActivityAndFinishAll(this@CashierActivity, YBCenterActivity::class.java)
            Log.d("fansan", "alipay info = $resultInfo")

        }
    }

    override fun initViews() {
        ActivityUtils.addActivity(this)
        shouldPayCount = intent.getStringExtra("shouldpay")
        number = intent.getStringExtra("number")
        shouldpay.text = "${shouldPayCount}元"

        cb_ali_pay.isChecked = true

        cb_ali_pay.setOnCheckedChangeListener { buttonView, isChecked ->
            cb_wechat_pay.isChecked = !isChecked
        }

        cb_wechat_pay.setOnCheckedChangeListener { buttonView, isChecked ->
            cb_ali_pay.isChecked = !isChecked
        }

        pay_btn.setOnClickListener {

            when {
                cb_ali_pay.isChecked -> {
                    getAlipayInfo()
                }
                cb_wechat_pay.isChecked -> {
                    getWxPayInfo()
                }
            }

        }
    }

    private fun getAlipayInfo() {
        EBagApi.getAiliPrepayid(number, shouldPayCount, object : RequestCallBack<String>() {
            override fun onStart() {
                super.onStart()
                LoadingDialogUtil.showLoading(this@CashierActivity, "正在跳转支付页面...")
            }

            override fun onSuccess(entity: String?) {
                //WXPay(entity!!)
                val runnable = Runnable {
                    val paytask = PayTask(this@CashierActivity)
                    val result = paytask.payV2(entity, true)
                    val message = Message()
                    message.what = 1
                    message.obj = result
                    handler.sendMessage(message)
                }
                Thread(runnable).start()
                Log.d("fansan", "ali = $entity")
                LoadingDialogUtil.closeLoadingDialog()
            }

            override fun onError(exception: Throwable) {
                Log.d("fansan", "fail = " + exception.message)
                T.show(this@CashierActivity, exception.message.toString())
                LoadingDialogUtil.closeLoadingDialog()
                ActivityUtils.skipActivityAndFinishAll(this@CashierActivity, YBCenterActivity::class.java)
            }

        })
    }


    private fun getWxPayInfo() {
        EBagApi.getPrepayid(number, shouldPayCount, object : RequestCallBack<WXPayBean>() {
            override fun onStart() {
                super.onStart()
                LoadingDialogUtil.showLoading(this@CashierActivity, "正在跳转支付页面...")
            }

            override fun onSuccess(entity: WXPayBean?) {
                WXPay(entity!!)
                LoadingDialogUtil.closeLoadingDialog()
            }

            override fun onError(exception: Throwable) {
                Log.d("fansan", "fail = " + exception.message)
                T.show(this@CashierActivity, exception.message.toString())
                LoadingDialogUtil.closeLoadingDialog()
                ActivityUtils.skipActivityAndFinishAll(this@CashierActivity, YBCenterActivity::class.java)
            }

        })
    }

    private fun WXPay(bean: WXPayBean) {
        var wxKey: String = ""
        wxKey = if (packageName.contains("parents")) {
            "wx626a1c084ecd9ca9"
        } else {
            "wx4adbb68ec1c80484"
        }
        val wxapi = WXAPIFactory.createWXAPI(this, wxKey)
        wxapi.registerApp(wxKey)
        if (wxapi.isWXAppInstalled and wxapi.isWXAppSupportAPI) {
            val request = PayReq()
            request.appId = bean.appid
            request.partnerId = bean.partnerid
            request.prepayId = bean.prepayid
            request.packageValue = bean.package_
            request.nonceStr = bean.noncestr
            request.timeStamp = bean.timestamp.toString()
            request.sign = bean.sign
            wxapi.sendReq(request)
        } else {
            T.show(this@CashierActivity, "未安装微信客户端")
            ActivityUtils.skipActivityAndFinishAll(this@CashierActivity, YBCenterActivity::class.java)
        }
    }


    companion object {

        fun start(c: Context, shouldPay: String, number: String) {

            c.startActivity(Intent(c, CashierActivity::class.java).putExtra("shouldpay", shouldPay).putExtra("number", number))
        }
    }
}