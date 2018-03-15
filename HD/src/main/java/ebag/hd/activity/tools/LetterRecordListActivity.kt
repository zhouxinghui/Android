package ebag.hd.activity.tools

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.base.BaseListActivity
import ebag.core.http.network.RequestCallBack
import ebag.hd.R
import ebag.hd.bean.response.LetterRecordBean

/**
 * Created by unicho on 2018/3/13.
 */
class LetterRecordListActivity: BaseListActivity<ArrayList<LetterRecordBean>, LetterRecordBean>() {
    override fun loadConfig(intent: Intent) {
        setPageTitle("生字默写")
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<ArrayList<LetterRecordBean>>) {
    }

    override fun parentToList(isFirstPage: Boolean, parent: ArrayList<LetterRecordBean>?): List<LetterRecordBean>? {
        // 这里需要添加一个题目内容
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<LetterRecordBean, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<LetterRecordBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    inner class Adapter: BaseMultiItemQuickAdapter<LetterRecordBean, BaseViewHolder>(null){
        val question_type = 1
        val answer_type = 2
        init {
            addItemType(question_type, R.layout.item_record_question_tip)
            addItemType(answer_type, R.layout.item_record_answer_tip)
        }

        override fun convert(helper: BaseViewHolder, item: LetterRecordBean?) {
            if(helper.itemViewType == question_type){

            }else{
                helper.setText(R.id.tvContent,"默写生字：${item?.letters ?: ""}")
                        .setText(R.id.tvEdition, item?.content)
//                        .setText(R.id.tvTime, DateUtil.getDateTime(item?.time ?: 0, "yyyy-MM-dd HH:mm"))
                val recyclerView = helper.getView<RecyclerView>(R.id.recyclerView)
                recyclerView.isNestedScrollingEnabled = false
                if(recyclerView.adapter == null)
                    recyclerView.adapter = ItemAdapter()
                if(recyclerView.layoutManager == null)
                    recyclerView.layoutManager = GridLayoutManager(mContext,12)
                recyclerView.postDelayed({
                    (recyclerView.adapter as ItemAdapter).setNewData(item?.letters?.split(","))
                },20)
            }
        }

    }

    inner class ItemAdapter: BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_record_question_image){
        override fun convert(helper: BaseViewHolder, item: String?) {
            helper.setText(R.id.tvChar, item)
        }

    }
}