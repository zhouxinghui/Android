package ebag.mobile.module.homework

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.bean.QuestionBean
import ebag.core.bean.QuestionTypeUtils
import ebag.core.bean.TypeQuestionBean
import ebag.core.util.StringUtils
import ebag.mobile.R

/**
 * Created by YZY on 2018/5/10.
 */
class QuestionTypeAdapter: BaseQuickAdapter<TypeQuestionBean, BaseViewHolder>(R.layout.item_question_type) {
    var onSubItemClickListener: ((parentPosition: Int, position: Int) -> Unit)? = null
    var showResult = false
    override fun convert(helper: BaseViewHolder, item: TypeQuestionBean?) {
        helper.setText(R.id.tv, QuestionTypeUtils.getTitle(item?.type ?: ""))
        val recyclerView = helper.getView<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(mContext, 5)
        val adapter = QuestionTypeSubAdapter()
        adapter.showResult = showResult
        recyclerView.adapter = adapter
        adapter.setNewData(item?.questionVos)
        adapter.setOnItemClickListener { _, _, position ->
            onSubItemClickListener?.invoke(helper.adapterPosition, position)
        }
    }

    private inner class QuestionTypeSubAdapter: BaseQuickAdapter<QuestionBean, BaseViewHolder>(R.layout.item_activity_overview_detail){
        var showResult = false
            set(value) {
                field = value
                notifyDataSetChanged()
            }
        override fun convert(helper: BaseViewHolder, item: QuestionBean?) {
            val tv = helper.getView<TextView>(R.id.tv)
            tv.text = "${helper.adapterPosition + 1}"
            if(showResult && !QuestionTypeUtils.isMarkType(QuestionTypeUtils.getIntType(item))){
                if(item?.isCorrect == true){
                    tv.setBackgroundResource(R.drawable.bac_overview_green)
                }else{
                    tv.setBackgroundResource(R.drawable.bac_overview_red)
                }
            }else{
                if(StringUtils.isEmpty(item?.answer)){
                    tv.setBackgroundResource(R.drawable.bac_overview_grey)
                }else{
                    tv.setBackgroundResource(R.drawable.bac_overview_blue)
                }
            }
        }
    }
}