package ebag.hd.activity

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.T
import ebag.hd.R
import ebag.hd.bean.ShopListBean
import ebag.hd.bean.WXPayBean
import ebag.hd.http.EBagApi
import kotlinx.android.synthetic.main.activity_shop_order_detail.*
import kotlin.concurrent.thread

/**
 * Created by fansan on 2018/3/15.
 */
class OrderDetailsActivity : BaseActivity() {
    private var count = 0
    private var address = false
    private var number = ""

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
            count += (dats[i].discountPrice.toInt() * dats[i].numbers)
            goods_list.addView(view)
        }


        tv_total_money.text = "¥ $count"
        tv_total_pay.text = "¥ $count"
        tv_should_pay.text = "¥ $count"

        cb_ali_pay.performClick()

        cb_ali_pay.setOnCheckedChangeListener { _, isChecked ->

            cb_wechat_pay.isChecked = !isChecked
        }
        2
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
                EBagApi.getPrepayid(number, "0.01", object : RequestCallBack<WXPayBean>() {
                    override fun onStart() {
                        super.onStart()
                        LoadingDialogUtil.showLoading(this@OrderDetailsActivity, "订单获取中...")
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
            } else {
                T.show(this, "还没有选择收货地址")
            }
        }
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


}