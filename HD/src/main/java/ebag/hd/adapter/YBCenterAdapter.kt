package ebag.hd.adapter

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.util.DateUtil
import ebag.hd.R
import ebag.hd.mvp.model.YBCenterModel

/**
 * Created by fansan on 2018/3/13.
 */
class YBCenterAdapter(layout: Int, data: MutableList<YBCenterModel>): BaseQuickAdapter<YBCenterModel, BaseViewHolder>(layout,data) {

    override fun convert(helper: BaseViewHolder?, item: YBCenterModel?) {
        helper!!.setText(R.id.item_ybcenter_title,item!!.accountType).setText(R.id.item_ybcenter_details,item.remake)
        val time = DateUtil.getDateTime(item.createDate!!)
        helper.setText(R.id.item_ybcenter_time,time)
        if (item.type == "1"){
            helper.setTextColor(R.id.item_ybcenter_quantity,Color.parseColor("#7970FF"))
            helper.setText(R.id.item_ybcenter_quantity,"+${item.money}")
        }else{
            helper.setTextColor(R.id.item_ybcenter_quantity,Color.parseColor("#FF6565"))
            helper.setText(R.id.item_ybcenter_quantity,"-${item.money}")
        }

    }
}