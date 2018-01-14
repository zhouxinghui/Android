package ebag.hd.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by YZY on 2018/1/14.
 */
class UnitAdapter(list: ArrayList<MultiItemEntity>): BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(list){

    override fun convert(helper: BaseViewHolder?, item: MultiItemEntity) {

    }

}