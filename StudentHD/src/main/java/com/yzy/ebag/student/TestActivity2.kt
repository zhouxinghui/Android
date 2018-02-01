package com.yzy.ebag.student

import android.view.KeyEvent
import ebag.core.base.BaseActivity
import ebag.core.util.T
import kotlinx.android.synthetic.main.test1_activity.*

/**
 * @author caoyu
 * @date 2018/1/29
 * @description
 */
class TestActivity2:BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.test1_activity
    }

    override fun initViews() {
        btn.setOnClickListener {
            curveChartView.show()
        }
        init()
    }



    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun init(){
        curveChartView.setXAxis(
                arrayOf("9", "10", "11", "12", "1", "3", "4", "5", "6", "7").asList(),
                "月份")
        curveChartView.setYAxis(
                arrayOf("0", "20", "40", "60", "80", "100").asList(),
                "分数")
        curveChartView.setTextSize(resources.getDimensionPixelSize(R.dimen.x24),resources.getDimensionPixelSize(R.dimen.x20))
        curveChartView.setFullSize(100)
//        curveChartView.setValueTextSize(resources.getDimensionPixelSize(R.dimen.x20))

        curveChartView.addPoints(intArrayOf(58, 83, 99, 82, 92, 78, 78, 93, 83, 100).asList()
                , null, null, null)
//        curveChartView.addPoints(intArrayOf(48, 73, 89, 72, 82, 68, 68, 83, 73, 90).asList(), null)
//        curveChartView.setValueBackground(
//                resources.getDrawable(R.drawable.achievement_icon),
//                resources.getDimensionPixelSize(R.dimen.x48),
//                resources.getDimensionPixelSize(R.dimen.x58)
//        )
        curveChartView.show()

        curveChartView.setOnValueItemClickListener { lineIndex, position ->
            T.show(this,"第${lineIndex+1}条线的第${position + 1}个的Value")
        }
    }

//    private fun initCurveChart2() {
//        val xLabel = arrayOf("9", "10", "11", "12", "1", "3", "4", "5", "6", "7")
//        val yLabel = arrayOf("0", "20", "40", "60", "80", "100")
//        val data1 = intArrayOf(58, 83, 99, 82, 92, 78, 78, 93, 83, 100)
//        val data2 = intArrayOf(48, 73, 89, 72, 82, 68, 68, 83, 73, 90)
//        val data = ArrayList<IntArray>()
//        val color = ArrayList<Int>()
//        data.add(data1)
//        color.add(R.color.color26)
//        data.add(data2)
//        color.add(R.color.color14)
//        customCurveChart2.addView(CustomCurveChart(this, xLabel, yLabel, data, color, true))
//    }

}