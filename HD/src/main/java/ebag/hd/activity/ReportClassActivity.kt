package ebag.hd.activity

import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.base.BaseActivity
import ebag.hd.R
import kotlinx.android.synthetic.main.activity_report_class.*

/**
 * @author caoyu
 * @date 2018/1/24
 * @description
 */
class ReportClassActivity: BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_report_class
    }

    override fun initViews() {


        fillData()
    }

    private fun fillData(){

        titleView.setTitle("李毅敏")

        scoreRound.progress = 90
        var spannableString = SpannableString("总分\n90")
            spannableString.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.x34))
                    , 3, 3 + "90".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        scoreTv.text = spannableString

        heightRound.progress = 90
        spannableString = SpannableString("最高分\n90")
        spannableString.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.x34))
                , 4, 4 + "90".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        heightTv.text = spannableString

        errorRound.progress = 4
        spannableString = SpannableString("错题\n4")
        spannableString.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.x34))
                , 3, 3 + "4".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        errorTv.text = spannableString

        val adapter = Adapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val list = ArrayList<Result>()
        list.add(Result())
        list.add(Result())
        list.add(Result())
        list.add(Result())
        list.add(Result())
        list.add(Result())
        list.add(Result())
        list.add(Result())

        adapter.setNewData(list)
    }

    data class Result(
            val leixing: String = "选择题",
            val count: Int = 10,
            val errorCount: Int = 1,
            val score: Int = 90
    )

    inner class Adapter: BaseQuickAdapter<Result, BaseViewHolder>(R.layout.item_activity_report_class){

        override fun convert(helper: BaseViewHolder, item: Result?) {
            helper.setText(R.id.questionType, item?.leixing)
                    .setText(R.id.count, "${item?.count}")
                    .setText(R.id.errorCount, "${item?.errorCount}")
                    .setText(R.id.score, "${item?.score}")
                    .setBackgroundRes(R.id.layout,if(helper.adapterPosition % 2 == 0) R.color.light_blue else R.color.white)
        }

    }
}