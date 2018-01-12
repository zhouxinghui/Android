package com.yzy.ebag.student.activity.homework

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.SpannableString
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.base.BaseListFragment
import ebag.core.http.network.RequestCallBack



/**
 * Created by unicho on 2018/1/8.
 */
class HomeworkListFragment : BaseListFragment<WorkList>(){

    private lateinit var subject: String
    companion object {
        fun newInstance(subject: String): HomeworkListFragment{
            val fragment = HomeworkListFragment()
            val bundle = Bundle()
            bundle.putString("subject",subject)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getBundle(bundle: Bundle) {
        subject = bundle.getString("subject")
    }

    override fun loadConfig() {
        loadMoreEnabled(false)
        refreshEnabled(false)
        onlyView(true)
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<WorkList>>) {
    }

    override fun getAdapter(): BaseQuickAdapter<WorkList,BaseViewHolder> {
        val adapter = HomeWorkListAdapter()
        adapter.setNewData(listOf(
                WorkList(28,21,"${subject}第一条","认真仔细","2017-12-25 12:50"),
                WorkList(28,23,"${subject}第二条","认真仔细","2017-12-25 12:50"),
                WorkList(28,24,"${subject}第三条","认真仔细","2017-12-25 12:50"),
                WorkList(28,28,"${subject}第四条","认真仔细","2017-12-25 12:50"),
                WorkList(8,8,"${subject}第五条","认真仔细","2017-12-25 12:50"),
                WorkList(28,21,"${subject}第六条","认真仔细","2017-12-25 12:50"),
                WorkList(28,21,"${subject}第七条","认真仔细","2017-12-25 12:50"),
                WorkList(28,21,"${subject}第八条","认真仔细","2017-12-25 12:50"),
                WorkList(28,21,"${subject}第九条","认真仔细","2017-12-25 12:50"),
                WorkList(28,21,"${subject}第十条","认真仔细","2017-12-25 12:50"),
                WorkList(28,28,"${subject}第十一条","认真仔细","2017-12-25 12:50"),
                WorkList(28,21,"${subject}第十二条","认真仔细","2017-12-25 12:50"),
                WorkList(21,21,"${subject}第十三条","认真仔细","2017-12-25 12:50"),
                WorkList(28,21,"${subject}第十四条","认真仔细","2017-12-25 12:50"),
                WorkList(28,21,"${subject}第十五条","认真仔细","2017-12-25 12:50")
        ))
        return adapter
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager? {
        return null
    }

    inner class HomeWorkListAdapter: BaseQuickAdapter<WorkList,BaseViewHolder>(R.layout.fragment_homework_list_item){

        override fun convert(helper: BaseViewHolder, item: WorkList?) {
            val spannableString = SpannableString("完成： ${item?.doneCount}/${item?.totalCount}")
//            spannableString.setSpan(ForegroundColorSpan(resources.getColor(R.color.color_homework_selected)), 4, 4 + "${entity.doneCount}".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            setter.setText(R.id.tvCount, spannableString)
            helper.setText(R.id.tvCount, Html.fromHtml("完成： <font color='#FF7800'>${item?.doneCount}</font>/${item?.totalCount}"))
                    .setText(R.id.tvContent,"内容： ${item?.content}")
                    .setText(R.id.tvRequire,"要求： ${item?.require}")
                    .setText(R.id.tvTime,"截止时间： ${item?.time}")
                    .setText(R.id.tvStatus,if(item?.totalCount == item?.doneCount) "已完成" else "未完成")
            helper.getView<View>(R.id.tvStatus).isSelected = item?.totalCount == item?.doneCount
        }
    }


}

data class WorkList(
        val totalCount: Int = 28,
        val doneCount: Int = 28,
        val content: String = "",
        val require: String = "",
        val time: String = "2017-12-25 12:50"
)