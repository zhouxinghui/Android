package com.yzy.ebag.student.activity.growth

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.Achievement
import com.yzy.ebag.student.bean.HomeworkBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.LazyFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.util.SPUtils
import ebag.core.widget.FoldChartView
import ebag.hd.base.Constants
import kotlinx.android.synthetic.main.fragment_achievement.*

/**
 * @author caoyu
 * @date 2018/2/1
 * @description
 */
class AchievementFragment : LazyFragment() {

    private lateinit var adapter:Adapter
    private lateinit var chartView: FoldChartView
    companion object {
        fun newInstance(gradeId: String, type: Int): AchievementFragment {
            val fragment = AchievementFragment()
            val bundle = Bundle()
            bundle.putString("gradeId", gradeId)
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
        /*chartView.setXAxis(
                arrayOf("3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "1", "2").asList(),
                "月份")*/

        recyclerView.layoutManager = LinearLayoutManager(mContext)
        adapter = Adapter()
        recyclerView.adapter = adapter
        request()
//        chartView.setOnValueItemClickListener { lineIndex, position ->
//            T.show(mContext,"第${lineIndex+1}条线的第${position + 1}个的Value")
//        }


        adapter.setOnItemClickListener { _, _, position ->
            AchievementDetailActivity.jump(mContext, adapter.getItem(position))
        }

        stateview.setOnRetryClickListener {
            request()
        }
    }

    private fun initChartView(){
        chartView.setYAxis(
            arrayOf("0", "20", "40", "60", "80", "100").asList(),
            "分数")
        chartView.setTextSize(resources.getDimensionPixelSize(R.dimen.x24), resources.getDimensionPixelSize(R.dimen.x20))
        chartView.setFullSize(100)
        //        curveChartView.setValueTextSize(resources.getDimensionPixelSize(R.dimen.x20))
        //        curveChartView.addPoints(intArrayOf(48, 73, 89, 72, 82, 68, 68, 83, 73, 90).asList(), null)
        chartView.setValueBackground(
                resources.getDrawable(R.drawable.achievement_icon_selector),
                resources.getDimensionPixelSize(R.dimen.x49),
                resources.getDimensionPixelSize(R.dimen.x59)
        )
    }

    private fun request() {
        StudentApi.examSocre(SPUtils.get(activity, Constants.CLASS_ID, "") as String, type.toString(), "student", object : RequestCallBack<List<HomeworkBean>>() {
            override fun onStart() {
                stateview.showLoading()
            }

            override fun onSuccess(entity: List<HomeworkBean>?) {

                val list = ArrayList<Achievement>()
                if (entity!!.isEmpty()) {
                    stateview.showEmpty()
                } else {
                    val intList = arrayListOf<Int>()
                    entity.forEach {
                        var date = it.resultDateTime.split(" ")[0]
                        list.add(Achievement(date.substring(0,date.lastIndexOf("-")), "", it.avgScore.toDouble().toInt(), ""))
                        intList.add(it.avgScore.toDouble().toInt())
//                        intList.add(0)
                    }

                    val time = list[0].date
                    var month = time.substring(time.length - 1, time.length).toInt()
                    val monthList = ArrayList<String>()
                    for (i in 0 .. 11){
                        monthList.add(month.toString())
                        if (month == 12){
                            month = 0
                        }
                        month ++
                    }
                    layout.removeAllViews()
                    chartView = FoldChartView(mContext)
                    chartView.setXAxis(monthList, "月份")
                    initChartView()

                    chartView.addPoints(intList
                            , null, null, null, null,
                            resources.getDimensionPixelSize(R.dimen.x20), null, null, null)
                    layout.addView(chartView)
                    adapter.setNewData(list)

                    chartView.show()
                    stateview.showContent()
                }
            }

            override fun onError(exception: Throwable) {
//                exception.handleThrowable(activity)
                stateview.showError((exception.message))
            }

        })
    }


    inner class Adapter : BaseQuickAdapter<Achievement, BaseViewHolder>(R.layout.item_fragment_achievement) {

        override fun convert(helper: BaseViewHolder, item: Achievement?) {
            helper.setText(R.id.date, item?.date)
                    .setText(R.id.time, item?.time)
                    .setText(R.id.score, "${item?.score}分")
                    .setText(R.id.summary, item?.summary)
        }

    }
}