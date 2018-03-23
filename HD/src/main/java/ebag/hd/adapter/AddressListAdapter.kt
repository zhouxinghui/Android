package ebag.hd.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.hd.R
import ebag.hd.activity.AddressListActivity
import ebag.hd.bean.AddressListBean

/**
 * Created by fansan on 2018/3/14.
 */
class AddressListAdapter(private val context: Context, layout: Int, datas: MutableList<AddressListBean>, private val onChecked: AddressListActivity.onChecked) : BaseQuickAdapter<AddressListBean, BaseViewHolder>(layout, datas) {


    override fun convert(helper: BaseViewHolder?, item: AddressListBean?) {
        helper?.setText(R.id.myaddress_item_username, item?.consignee)?.setText(R.id.myaddress_item_phone, item?.phone)
                ?.setText(R.id.myaddress_item__area, item?.preAddress + item?.address)

        helper?.addOnClickListener(R.id.item_address_edit)
        helper?.addOnClickListener(R.id.item_address_delete)
        if (item?.type == "0") {
            helper?.setChecked(R.id.myaddress_defult_toggle, true)
            helper?.getView<CheckBox>(R.id.myaddress_defult_toggle)?.text = "默认地址"
            helper?.getView<CheckBox>(R.id.myaddress_defult_toggle)?.isEnabled = false
        }

        helper?.getView<CheckBox>(R.id.myaddress_defult_toggle)?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AlertDialog.Builder(context).setMessage("是否设为默认收货地址").setNegativeButton("否") { _, _ ->
                    helper.getView<CheckBox>(R.id.myaddress_defult_toggle).isChecked = false
                }.setNeutralButton("是") { _, _ ->
                    onChecked.setDefault(helper.layoutPosition)
                }.show()
            }
        }


    }


}