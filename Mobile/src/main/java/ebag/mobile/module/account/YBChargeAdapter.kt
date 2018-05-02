package ebag.mobile.module.account

import android.content.Context
import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.mobile.R

class YBChargeAdapter(datas: MutableList<ChargeBean>, private val c: Context) : BaseQuickAdapter<ChargeBean, BaseViewHolder>(R.layout.item_ybcharge,datas) {

    override fun convert(helper: BaseViewHolder, item: ChargeBean?) {

        helper.setText(R.id.label, item?.yb)
                .setText(R.id.money, item?.money)

        if (item!!.isSelected) {
            helper.setBackgroundRes(R.id.layout, R.drawable.ybcharge_bcg)
            helper.setTextColor(R.id.label, c.resources.getColor(R.color.colorPrimary))
            helper.setTextColor(R.id.money, c.resources.getColor(R.color.colorPrimary))
        } else {
            helper.setBackgroundRes(R.id.layout, R.drawable.ybcharge_bcg_normal)
            helper.setTextColor(R.id.label, Color.BLACK)
            helper.setTextColor(R.id.money, Color.parseColor("#9b9a9b"))
        }
    }

}