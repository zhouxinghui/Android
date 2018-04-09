package ebag.hd.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.util.loadHead
import ebag.hd.R
import ebag.hd.bean.ClassMemberBean

/**
 * Created by fansan on 2018/4/2.
 */
class ClazzmateAdapter(data: List<ClassMemberBean.StudentsBean>) : BaseQuickAdapter<ClassMemberBean.StudentsBean, BaseViewHolder>(R.layout.item_clazzmate, data) {
    override fun convert(helper: BaseViewHolder, item: ClassMemberBean.StudentsBean?) {

        helper.setText(R.id.clazz_name, item?.name)
        helper.getView<ImageView>(R.id.clazz_head).loadHead(item?.headUrl)

    }

}