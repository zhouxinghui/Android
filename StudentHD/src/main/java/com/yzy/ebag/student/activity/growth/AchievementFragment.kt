package com.yzy.ebag.student.activity.growth

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.Achievement
import ebag.core.base.LazyFragment
import kotlinx.android.synthetic.main.fragment_achievement.*

/**
 * @author caoyu
 * @date 2018/2/1
 * @description
 */
class AchievementFragment: LazyFragment() {

    companion object {
        fun newInstance(gradeId: String, type: Int): AchievementFragment{
            val fragment = AchievementFragment()
            val bundle = Bundle()
            bundle.putString("gradeId",gradeId)
            bundle.putInt("type", type)
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun lazyLoad() {

    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_achievement
    }

    private var type: Int = 0
    private lateinit var gradeId: String

    override fun getBundle(bundle: Bundle?) {
        gradeId = bundle?.getString("gradeId") ?: ""
        type = bundle?.getInt("type") ?: 0

    }

    override fun initViews(rootView: View) {

        chartView.setXAxis(
                arrayOf("9", "10", "11", "12", "1", "3", "4", "5", "6", "7").asList(),
                "月份")
        chartView.setYAxis(
                arrayOf("0", "20", "40", "60", "80", "100").asList(),
                "分数")
        chartView.setTextSize(resources.getDimensionPixelSize(R.dimen.x24),resources.getDimensionPixelSize(R.dimen.x20))
        chartView.setFullSize(100)
//        curveChartView.setValueTextSize(resources.getDimensionPixelSize(R.dimen.x20))

        chartView.addPoints(intArrayOf(58, 83, 99, 82, 92, 78, 78, 93, 83, 100).asList()
                , null, null, null, null,
                resources.getDimensionPixelSize(R.dimen.x20),null,null,null)
//        curveChartView.addPoints(intArrayOf(48, 73, 89, 72, 82, 68, 68, 83, 73, 90).asList(), null)
        chartView.setValueBackground(
                resources.getDrawable(R.drawable.achievement_icon_selector),
                resources.getDimensionPixelSize(R.dimen.x49),
                resources.getDimensionPixelSize(R.dimen.x59)
        )
        chartView.show()

//        chartView.setOnValueItemClickListener { lineIndex, position ->
//            T.show(mContext,"第${lineIndex+1}条线的第${position + 1}个的Value")
//        }

        recyclerView.layoutManager = LinearLayoutManager(mContext)
        val adapter = Adapter()
        recyclerView.adapter = adapter
        val list = ArrayList<Achievement>()
        list.add(Achievement("2017年12月13日","19:22",88, ""))
        list.add(Achievement("2017年2月13日","9:22",100, "哈哈哈哈 "))
        list.add(Achievement("2017年2月13日","9:22",7, "哈哈哈哈 "))
        list.add(Achievement("2017年2月13日","9:22",50, "哈哈哈哈 "))
        list.add(Achievement("2017年2月13日","9:22",100, "哈哈哈哈 "))
        list.add(Achievement("2017年2月13日","9:22",100, "哈哈哈哈 "))
        list.add(Achievement("2017年2月13日","9:22",100, "哈哈哈哈 "))
        adapter.setNewData(list)

        adapter.setOnItemClickListener { _, _, position ->
            AchievementDetailActivity.jump(mContext, adapter.getItem(position))
        }
    }

    inner class Adapter: BaseQuickAdapter<Achievement, BaseViewHolder>(R.layout.item_fragment_achievement){

        override fun convert(helper: BaseViewHolder, item: Achievement?) {
            helper.setText(R.id.date, item?.date)
                    .setText(R.id.time, item?.time)
                    .setText(R.id.score, "${item?.score}分")
                    .setText(R.id.summary, item?.summary)
        }

    }
}