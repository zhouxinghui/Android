package ebag.hd.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.alipay.sdk.app.PayTask
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.T
import ebag.core.util.loadImage
import ebag.hd.R
import ebag.hd.bean.*
import ebag.hd.http.EBagApi
import ebag.hd.util.ActivityUtils
import kotlinx.android.synthetic.main.activity_shop_order_detail.*

/**
 * Created by fansan on 2018/3/15.
 */
class OrderDetailsActivity : BaseActivity() {
    private var count = 0
    private var address = false
    private var number = ""
    private var mList: ArrayList<SaveOrderPBean.ListBean> = arrayListOf()
    private var which = 0
    private var ybCount = 0
    private var isStudent = false
    private var freight = ""
    private var addressStr = ""
    @SuppressLint("HandlerLeak")
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            @Suppress("UNCHECKED_CAST")
            val result = PayResult(msg?.obj as Map<String, String>)
            val resultInfo = result.result
            val resultStatus = result.resultStatus
            if (TextUtils.equals(resultStatus, "9000")) {
                T.show(this@OrderDetailsActivity, "支付成功")
            } else {
                T.show(this@OrderDetailsActivity, "支付失败")
            }
            ActivityUtils.skipActivityAndFinishAll(this@OrderDetailsActivity, ShopOrderActivity::class.java)
            Log.d("fansan", "alipay info = $resultInfo")

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityUtils.addActivity(this)
    }

    override fun getLayoutId(): Int = R.layout.activity_shop_order_detail
    override fun initViews() {

        @Suppress("UNCHECKED_CAST")
        val dats = intent.getSerializableExtra("datas") as ArrayList<ShopListBean.ListBean>
        number = intent.getStringExtra("number")
        freight = intent.getStringExtra("freight")
        if (intent.hasExtra("which")) {
            which = intent.getIntExtra("which", 0)
        }
        if (intent.hasExtra("addressStr")) {
            addressStr = intent.getStringExtra("addressStr")
        }
        val packageName = packageName
        tv_order_time.text = "订单编号:$number\n下单时间:${number.substring(0, 4)}年${number[5]}月${number.substring(6, 8)}日  ${number.substring(startIndex = 8, endIndex = 10)}:${number.substring(10, 12)}"
        for (i in dats.indices) {
            val view = View.inflate(this, R.layout.item_shop_order_detail, null)
            view.findViewById<TextView>(R.id.goods_name).text = dats[i].shoppingName
            view.findViewById<TextView>(R.id.goods_yun_price).text = dats[i].ysbMoney
            view.findViewById<TextView>(R.id.tv_price).text = "¥ ${dats[i].discountPrice}"
            view.findViewById<TextView>(R.id.tv_num).text = "x${dats[i].numbers}"
            view.findViewById<ImageView>(R.id.goods_img).loadImage(dats[i].shopUrl)
            if (dats[i].imgUrls != null && dats[i].imgUrls.isNotEmpty()) {
                if (i == 0) {
                    if (dats[i].imgUrls.size != 0)
                        view.findViewById<ImageView>(R.id.goods_img).loadImage(dats[0].imgUrls[0])
                }
            }
            count += (dats[i].discountPrice.toInt() * dats[i].numbers)
            val ybmoney = dats[i].ysbMoney ?: "0"
            ybCount += (ybmoney.toInt() * dats[i].numbers)
            mList.add(SaveOrderPBean.ListBean(dats[i].id.toString(), dats[i].numbers.toString(), freight, dats[i].ysbMoney))
            goods_list.addView(view)
        }

        if (addressStr.isNotEmpty() && addressStr.isNotBlank()) {
            val addList = addressStr.split("  ")
            tv_name_phone.text = "${addList[0]}  ${addList[1]}"
            tv_adress.text = "${addList[2]}  ${addList[3]}"
            address = true
        } else {
            queryAddress()
        }

        if (packageName.contains("student")) {
            isStudent = true
            cb_ali_pay.visibility = View.GONE
            cb_wechat_pay.visibility = View.GONE
            cb_yb_pay.isChecked = true
            cb_yb_pay.isEnabled = false
            tv_total_money.text = "Y币 $ybCount"
        } else {
            tv_total_money.text = "¥ $count"
        }


        cb_yb_pay.text = "Y币 $ybCount"
        tv_yunfei.text = "¥ $freight"
        tv_total_pay.text = "¥ $count"
        try {
            tv_should_pay.text = "¥ ${count + freight.toInt()}"
        } catch (e: Exception) {
            tv_should_pay.text = "¥ $count"
        }


        cb_ali_pay.performClick()

        cb_ali_pay.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                cb_ali_pay.isEnabled = false
                cb_wechat_pay.isEnabled = true
                cb_yb_pay.isEnabled = true
                cb_wechat_pay.isChecked = false
                cb_yb_pay.isChecked = false
                tv_total_money.text = "¥ $count"
            }
        }

        cb_wechat_pay.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                cb_ali_pay.isEnabled = true
                cb_wechat_pay.isEnabled = false
                cb_yb_pay.isEnabled = true
                cb_ali_pay.isChecked = false
                cb_yb_pay.isChecked = false
                tv_total_money.text = "¥ $count"
            }
        }

        cb_yb_pay.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                cb_ali_pay.isEnabled = true
                cb_wechat_pay.isEnabled = true
                cb_yb_pay.isEnabled = false
                cb_wechat_pay.isChecked = false
                cb_ali_pay.isChecked = false
                tv_total_money.text = "Y币 $ybCount"
            }
        }

        rl_adress.setOnClickListener {

            val intent = Intent(this, AddressListActivity::class.java)
            intent.putExtra("choose", true)
            startActivityForResult(intent, 665)
        }

        btn_pay.setOnClickListener {

            if (address) {
                if (which == 1) {
                    if (isStudent) {
                        ybPay(number, ybCount.toString())
                    } else {
                        when {
                            cb_ali_pay.isChecked -> getAlipayInfo()
                            cb_yb_pay.isChecked -> ybPay(number, ybCount.toString())
                            else -> getWxPayInfo()
                        }
                    }
                } else {
                    EBagApi.saveOrder("${tv_name_phone.text}  ${tv_adress.text}", count.toString(), count.toString(), mList, number, "", object : RequestCallBack<String>() {

                        override fun onStart() {
                            super.onStart()
                            LoadingDialogUtil.showLoading(this@OrderDetailsActivity, "正在生成订单..请稍后")
                        }

                        override fun onSuccess(entity: String?) {
                            LoadingDialogUtil.closeLoadingDialog()
                            if (isStudent) {
                                ybPay(number, ybCount.toString())
                            } else {
                                when {
                                    cb_ali_pay.isChecked -> getAlipayInfo()
                                    cb_yb_pay.isChecked -> ybPay(number, ybCount.toString())
                                    else -> getWxPayInfo()
                                }
                            }
                        }

                        override fun onError(exception: Throwable) {
                            exception.handleThrowable(this@OrderDetailsActivity)
                            LoadingDialogUtil.closeLoadingDialog()
                        }

                    })
                }
            } else {
                T.show(this, "还没有选择收货地址")
            }
        }

    }

    private fun getWxPayInfo() {
        EBagApi.getPrepayid(number, tv_should_pay.text.toString(), object : RequestCallBack<WXPayBean>() {
            override fun onStart() {
                super.onStart()
                LoadingDialogUtil.showLoading(this@OrderDetailsActivity, "正在跳转支付页面...")
            }

            override fun onSuccess(entity: WXPayBean?) {
                WXPay(entity!!)
                LoadingDialogUtil.closeLoadingDialog()
            }

            override fun onError(exception: Throwable) {
                Log.d("fansan", "fail = " + exception.message)
                T.show(this@OrderDetailsActivity, exception.message.toString())
                LoadingDialogUtil.closeLoadingDialog()
            }

        })
    }

    private fun ybPay(oid: String, allPrice: String) {

        EBagApi.ybPay(oid, allPrice, object : RequestCallBack<String>() {
            override fun onStart() {
                super.onStart()
                LoadingDialogUtil.showLoading(this@OrderDetailsActivity, "正在支付...")
            }

            override fun onSuccess(entity: String?) {
                Log.d("fansan", "")
                T.show(this@OrderDetailsActivity, "支付成功")
                ActivityUtils.skipActivityAndFinishAll(this@OrderDetailsActivity, ShopOrderActivity::class.java)
            }

            override fun onError(exception: Throwable) {
                exception.handleThrowable(this@OrderDetailsActivity)
                ActivityUtils.skipActivityAndFinishAll(this@OrderDetailsActivity, ShopOrderActivity::class.java)
            }

        })
    }

    private fun queryAddress() {
        EBagApi.queryAddress(object : RequestCallBack<MutableList<AddressListBean>>() {
            override fun onSuccess(entity: MutableList<AddressListBean>?) {
                if (entity!!.isNotEmpty()) {
                    entity.forEach {
                        if (it.type == "0") {
                            tv_choose_address.visibility = View.GONE
                            tv_name_phone.text = "${it.consignee}  ${it.phone}"
                            tv_adress.text = "${it.preAddress}  ${it.address}"
                            address = true
                        }
                    }
                }
            }

            override fun onError(exception: Throwable) {

            }

        })
    }


    private fun getAlipayInfo() {
        EBagApi.getAiliPrepayid(number, tv_should_pay.text.toString(), object : RequestCallBack<String>() {
            override fun onStart() {
                super.onStart()
                LoadingDialogUtil.showLoading(this@OrderDetailsActivity, "正在跳转支付页面...")
            }

            override fun onSuccess(entity: String?) {
                //WXPay(entity!!)
                val runnable = Runnable {
                    val paytask = PayTask(this@OrderDetailsActivity)
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
                T.show(this@OrderDetailsActivity, exception.message.toString())
                LoadingDialogUtil.closeLoadingDialog()
            }

        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 666) {
            val result = data?.getStringExtra("result")
            val addressArr = result!!.split("/")
            tv_choose_address.visibility = View.GONE
            tv_name_phone.text = "${addressArr[0]}   ${addressArr[1]}"
            tv_adress.text = "${addressArr[2]}   ${addressArr[3]}"
            address = true
        }
    }

    private fun WXPay(bean: WXPayBean) {
        val wxapi = WXAPIFactory.createWXAPI(this, "wx4adbb68ec1c80484")
        wxapi.registerApp("wx4adbb68ec1c80484")
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
            T.show(this@OrderDetailsActivity, "未安装微信客户端")
        }
    }

    /*companion object {
        private var mList:ArrayList<SaveOrderPBean.ListBean> = arrayListOf()
        private var mBean = SaveOrderPBean("","","",mList,"")
        fun getDetailsBean():SaveOrderPBean{
            return mBean
        }
    }*/


}