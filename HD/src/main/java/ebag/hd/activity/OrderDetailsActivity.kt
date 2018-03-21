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
import ebag.hd.bean.PayResult
import ebag.hd.bean.SaveOrderPBean
import ebag.hd.bean.ShopListBean
import ebag.hd.bean.WXPayBean
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
    private var addresID:String = ""
    private var mList:ArrayList<SaveOrderPBean.ListBean> = arrayListOf()
    @SuppressLint("HandlerLeak")
    private val handler = object:Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            val result = PayResult(msg?.obj as Map<String,String>)
            val resultInfo = result.result
            val resultStatus = result.resultStatus
            if (TextUtils.equals(resultStatus,"9000")){
                T.show(this@OrderDetailsActivity,"支付成功")
            }else{
                T.show(this@OrderDetailsActivity,"支付失败")
                ActivityUtils.skipActivityAndFinishAll(this@OrderDetailsActivity,ShopOrderActivity::class.java)
            }
            Log.d("fansan", "alipay info = $resultInfo")

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityUtils.addActivity(this)
    }
    override fun getLayoutId(): Int = R.layout.activity_shop_order_detail

    override fun initViews() {

        val dats = intent.getSerializableExtra("datas") as ArrayList<ShopListBean.ListBean>
        number = intent.getStringExtra("number")
        tv_order_time.text = "订单编号:$number\n下单时间:${number.substring(0, 4)}年${number[5]}月${number.substring(6, 7)}日  ${number.substring(startIndex = 8, endIndex = 10)}:${number.substring(10, 12)}"
        for (i in dats.indices) {
            val view = View.inflate(this, R.layout.item_shop_order_detail, null)
            view.findViewById<TextView>(R.id.goods_name).text = dats[i].shoppingName
            view.findViewById<TextView>(R.id.goods_yun_price).text = dats[i].ysbMoney
            view.findViewById<TextView>(R.id.tv_price).text = "¥ ${dats[i].discountPrice}"
            view.findViewById<TextView>(R.id.tv_num).text = "x${dats[i].numbers}"
            if (i == 0){
                view.findViewById<ImageView>(R.id.goods_img).loadImage(dats[0].imgUrls[0])
            }
            count += (dats[i].discountPrice.toInt() * dats[i].numbers)
           /* mBean.price = dats[i].price
            mBean.allPrice = count.toString()*/
            mList.add(SaveOrderPBean.ListBean(dats[i].id.toString(), dats[i].numbers.toString()))
            /*mBean.oid = number*/
            goods_list.addView(view)
        }


        tv_total_money.text = "¥ $count"
        tv_total_pay.text = "¥ $count"
        tv_should_pay.text = "¥ $count"

        cb_ali_pay.performClick()

        cb_ali_pay.setOnCheckedChangeListener { _, isChecked ->

            cb_wechat_pay.isChecked = !isChecked
        }

        cb_wechat_pay.setOnCheckedChangeListener { _, isChecked ->
            cb_ali_pay.isChecked = !isChecked
        }

        tv_choose_address.setOnClickListener {

            val intent = Intent(this, AddressListActivity::class.java)
            intent.putExtra("choose", true)
            startActivityForResult(intent, 665)
        }

        btn_pay.setOnClickListener {

            if (address) {

                EBagApi.saveOrder(addresID,count.toString(),count.toString(),mList,number,object:RequestCallBack<String>(){

                    override fun onStart() {
                        super.onStart()
                        LoadingDialogUtil.showLoading(this@OrderDetailsActivity,"正在生成订单..请稍后")
                    }

                    override fun onSuccess(entity: String?) {
                        LoadingDialogUtil.closeLoadingDialog()
                        if (cb_ali_pay.isChecked) {
                            getAlipayInfo()
                        }else{
                            getWxPayInfo()
                        }
                    }

                    override fun onError(exception: Throwable) {
                        exception.handleThrowable(this@OrderDetailsActivity)
                        LoadingDialogUtil.closeLoadingDialog()
                    }

                })
            } else {
                T.show(this, "还没有选择收货地址")
            }
        }
    }

    private fun getWxPayInfo() {
        EBagApi.getPrepayid(number, "0.01", object : RequestCallBack<WXPayBean>() {
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


    private fun getAlipayInfo() {
        EBagApi.getAiliPrepayid(number, "0.01", object : RequestCallBack<String>() {
            override fun onStart() {
                super.onStart()
                LoadingDialogUtil.showLoading(this@OrderDetailsActivity, "正在跳转支付页面...")
            }

            override fun onSuccess(entity: String?) {
                //WXPay(entity!!)
                val runnable = Runnable {
                    val paytask = PayTask(this@OrderDetailsActivity)
                    val result = paytask.payV2(entity,true)
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
            addresID = addressArr[4]
            address = true
        }
    }

    private fun WXPay(bean: WXPayBean) {
        val wxapi = WXAPIFactory.createWXAPI(this, "wx4adbb68ec1c80484")
        wxapi.registerApp("wx4adbb68ec1c80484")
        val request = PayReq()
        request.appId = bean.appid
        request.partnerId = bean.partnerid
        request.prepayId = bean.prepayid
        request.packageValue = bean.package_
        request.nonceStr = bean.noncestr
        request.timeStamp = bean.timestamp.toString()
        request.sign = bean.sign
        wxapi.sendReq(request)
    }

    /*companion object {
        private var mList:ArrayList<SaveOrderPBean.ListBean> = arrayListOf()
        private var mBean = SaveOrderPBean("","","",mList,"")
        fun getDetailsBean():SaveOrderPBean{
            return mBean
        }
    }*/


}