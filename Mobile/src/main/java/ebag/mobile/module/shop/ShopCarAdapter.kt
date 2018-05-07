package ebag.mobile.module.shop

import android.content.Context
import android.graphics.Paint
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.util.loadImage
import ebag.mobile.R
import ebag.mobile.bean.ShopListBean

/**
 * Created by fansan on 2018/3/16.
 */
class ShopCarAdapter(context:Context,data:MutableList<ShopListBean.ListBean>,onCheckedChange:ShopCarActivity.OnCheckChange):BaseQuickAdapter<ShopListBean.ListBean,BaseViewHolder>(R.layout.item_shop_car_recyclerview,data){

    private val onCheckedChange:ShopCarActivity.OnCheckChange by lazy { onCheckedChange }

    override fun convert(helper: BaseViewHolder, item: ShopListBean.ListBean?) {
        helper.getView<CheckBox>(R.id.cb_selector).isChecked =item!!.isChecked
        helper.setText(R.id.goods_name,item.shoppingName).setText(R.id.goods_yun_price,"云币:${item.ysbMoney}").setText(R.id.goods_price,"¥ ${item.discountPrice}").setText(R.id.goods_old_price,"¥ ${item.price}").setText(R.id.tv_num,item.numbers.toString())
        helper.getView<TextView>(R.id.goods_old_price).paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        helper.getView<ImageView>(R.id.goods_img).loadImage(item.shopUrl)
        helper.getView<CheckBox>(R.id.cb_selector).setOnCheckedChangeListener { _, isChecked ->
            onCheckedChange.getPosition(helper.layoutPosition,isChecked)
        }

        helper.addOnClickListener(R.id.add)
        helper.addOnClickListener(R.id.lower)
    }



}