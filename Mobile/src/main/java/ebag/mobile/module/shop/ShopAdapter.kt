package ebag.mobile.module.shop

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.util.loadImage
import ebag.mobile.R
import ebag.mobile.bean.ShopListBean

/**
 * Created by fansan on 2018/3/16.
 */
class ShopAdapter(context:Context,layout:Int,data:MutableList<ShopListBean.ListBean>):BaseQuickAdapter<ShopListBean.ListBean,BaseViewHolder>(layout,data){

    private val context:Context by lazy { context }

    override fun convert(helper: BaseViewHolder, item: ShopListBean.ListBean?) {
            helper.setText(R.id.goods_name,item?.shoppingName).setText(R.id.rmb_price,item?.discountPrice).setText(R.id.yb_price,item?.ysbMoney).setText(R.id.sold_num,item?.numbers.toString())
            //Glide.with(context).load(R.drawable.ic_launcher).into(helper.getView(R.id.goods_img))
            helper.getView<ImageView>(R.id.goods_img).loadImage(item!!.imgUrls[0])
        helper.addOnClickListener(R.id.add_goods)
    }

}