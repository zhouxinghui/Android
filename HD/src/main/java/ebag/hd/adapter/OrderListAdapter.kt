package ebag.hd.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by fansan on 2018/3/15.
 */
class OrderListAdapter(layout:Int,data:MutableList<String>):BaseQuickAdapter<String,BaseViewHolder>(layout,data) {

    override fun convert(helper: BaseViewHolder?, item: String?) {

    }
}