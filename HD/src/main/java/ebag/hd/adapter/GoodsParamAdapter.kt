package ebag.hd.adapter

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.hd.R
import ebag.hd.bean.GoodsDetailsBean

/**
 * Created by fansan on 2018/3/19.
 */
class GoodsParamAdapter(context: Context, data: MutableList<GoodsDetailsBean.ProductParametersVoBean.ResultGruopVOSBean>) : BaseQuickAdapter<GoodsDetailsBean.ProductParametersVoBean.ResultGruopVOSBean, BaseViewHolder>(R.layout.item_goodsparam, data) {

    private val context: Context by lazy { context }

    override fun convert(helper: BaseViewHolder, item: GoodsDetailsBean.ProductParametersVoBean.ResultGruopVOSBean?) {
        helper.setText(R.id.goodsparam_title, item!!.groupName)
        val layout = helper.getView<LinearLayout>(R.id.goodsparam_layout)
        if (item.productParameterVos.size != 0) {
            for (i in item.productParameterVos.indices) {
                val view = View.inflate(context, R.layout.goodparamdetailsview, null)
                view.findViewById<TextView>(R.id.goodsparam_label).text = item.productParameterVos[i].parameterName
                view.findViewById<TextView>(R.id.goodsparam_content).text = item.productParameterVos[i].parameterValue
                layout.addView(view)
            }
        }
    }

}