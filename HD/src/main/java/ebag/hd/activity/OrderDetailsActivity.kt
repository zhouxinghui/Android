package ebag.hd.activity

import android.content.Intent
import android.view.View
import android.widget.TextView
import ebag.core.base.BaseActivity
import ebag.hd.R
import ebag.hd.bean.ShopListBean
import kotlinx.android.synthetic.main.activity_shop_order_detail.*

/**
 * Created by fansan on 2018/3/15.
 */
class OrderDetailsActivity : BaseActivity() {
    private var count = 0

    override fun getLayoutId(): Int = R.layout.activity_shop_order_detail
    override fun initViews() {

        val dats = intent.getSerializableExtra("datas") as ArrayList<ShopListBean.ListBean>
        val number = intent.getStringExtra("number")
        tv_order_time.text = "订单编号:$number\n下单时间:${number.substring(0,4)}年${number[5]}月${number.substring(6,7)}日  ${number.substring(startIndex = 8, endIndex = 10)}:${number.substring(10,12)}"
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

            val intent = Intent(this,AddressListActivity::class.java)
            intent.putExtra("choose",true)
            startActivityForResult(intent,665)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 666){
            val result = data?.getStringExtra("result")
            val addressArr = result!!.split("/")
            tv_choose_address.visibility = View.GONE
            tv_name_phone.text = "${addressArr[0]}   ${addressArr[1]}"
            tv_adress.text = "${addressArr[2]}   ${addressArr[3]}"
        }
    }


}