package ebag.mobile.module.shop

import android.text.InputFilter
import android.text.InputType
import android.widget.EditText
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.mobile.R

/**
 * Created by fansan on 2018/3/14.
 */
class EditAddressAdapter(layout: Int, data: MutableList<EditAddressModel>) : BaseQuickAdapter<EditAddressModel, BaseViewHolder>(layout, data) {


    override fun convert(helper: BaseViewHolder, item: EditAddressModel?) {

        helper?.setText(R.id.editaddress_label, item?.label)?.setText(R.id.editaddress_edittext, item?.value)?.setText(R.id.editaddress_area, item?.value)
        when (helper?.adapterPosition) {
            0 -> helper.getView<EditText>(R.id.editaddress_edittext)?.hint = "请收入收货人名称"
            1 -> {
                helper.getView<EditText>(R.id.editaddress_edittext)?.hint = "请收入收货人电话"
                helper.getView<EditText>(R.id.editaddress_edittext).inputType = InputType.TYPE_CLASS_NUMBER
            }
            2 -> {
                helper.setGone(R.id.editaddress_edittext, true)
                helper.setVisible(R.id.editaddress_area, true)
            }
            3 -> {
                helper.getView<EditText>(R.id.editaddress_edittext)?.hint = "街道、门牌号等"
                helper.getView<EditText>(R.id.editaddress_edittext)?.maxLines = 2
                helper.getView<EditText>(R.id.editaddress_edittext)?.filters = arrayOf(InputFilter.LengthFilter(100))
            }
        }
        helper.addOnClickListener(R.id.editaddress_area)

    }

}