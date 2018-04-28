package ebag.mobile.module.account

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.util.DateUtil
import ebag.mobile.R
import ebag.mobile.bean.YBCurrentBean

class YBCenterAdapter(datas: MutableList<YBCurrentBean.MoneyDetailsBean>) : BaseQuickAdapter<YBCurrentBean.MoneyDetailsBean, BaseViewHolder>(R.layout.item_ybcenter,datas) {

    override fun convert(helper: BaseViewHolder, item: YBCurrentBean.MoneyDetailsBean?) {
        helper.setText(R.id.label, item?.remark)

        val time = DateUtil.getDateTime(item?.createDate!!)
        helper.setText(R.id.date, time)
        if (item.type == "1") {
            helper.setTextColor(R.id.content, Color.parseColor("#fdc62e"))
            helper.setText(R.id.content, "+${item.money}")
        } else {
            helper.setTextColor(R.id.content, Color.parseColor("#dddddd"))
            helper.setText(R.id.content, "-${item.money}")
        }

    }

}