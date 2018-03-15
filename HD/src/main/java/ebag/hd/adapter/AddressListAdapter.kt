package ebag.hd.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.hd.R
import ebag.hd.bean.AddressListBean

/**
 * Created by fansan on 2018/3/14.
 */
class AddressListAdapter(layout:Int,datas:MutableList<AddressListBean>):BaseQuickAdapter<AddressListBean,BaseViewHolder>(layout,datas){


    override fun convert(helper: BaseViewHolder?, item: AddressListBean?) {
        helper?.setText(R.id.myaddress_item_username,item?.consignee)?.setText(R.id.myaddress_item_phone,item?.phone)
                ?.setText(R.id.myaddress_item__area,item?.preAddress + item?.address)

        helper?.addOnClickListener(R.id.item_address_edit)
        helper?.addOnClickListener(R.id.item_address_delete)

    }


}