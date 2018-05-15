package com.yzy.ebag.student.module.mission

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.util.TypedValue
import android.widget.LinearLayout
import com.yzy.ebag.student.R
import ebag.core.base.BaseActivity
import kotlinx.android.synthetic.main.activity_my_mission.*
import java.lang.reflect.Field

/**
 * Created by YZY on 2018/5/14.
 */
class MyMissionActivity: BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_my_mission
    companion object {
        fun jump(context: Context, classId: String){
            context.startActivity(
                    Intent(context, MyMissionActivity::class.java)
                            .putExtra("classId", classId)
            )
        }
    }
    override fun initViews() {
        val fragments = arrayListOf(
                LabourMissionFragment.newInstance(),
                StudyMissionFragment.newInstance())
        val titleList = arrayListOf("劳动任务", "学习任务")

        val adapter = ViewPagerAdapter(
                supportFragmentManager, fragments, titleList)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 2
        tabLayout.post{setIndicator(tabLayout, 5, 5)}
        tabLayout.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
            override fun onPageSelected(position: Int) {

            }

        })
    }

    private inner class ViewPagerAdapter(fm: FragmentManager, private val fragments: List<Fragment>, private val titleList: List<String>): FragmentPagerAdapter(fm){
        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return titleList[position]
        }
    }

    private fun setIndicator(tab: TabLayout, leftDip: Int, rightDip: Int) {
        val tabLayout = tab.javaClass
        val tabStrip: Field?
        tabStrip = tabLayout.getDeclaredField("mTabStrip")
        tabStrip.isAccessible = true
        val llTab: LinearLayout?
        llTab = tabStrip.get(tab) as LinearLayout
        val left = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip.toFloat(), Resources.getSystem().displayMetrics).toInt()
        val right = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip.toFloat(), Resources.getSystem().displayMetrics).toInt()
        for (i in 0 until llTab.childCount) {
            val child = llTab.getChildAt(i)
            child.setPadding(0, 0, 0, 0)
            val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            params.leftMargin = left
            params.rightMargin = right
            child.layoutParams = params
            child.invalidate()
        }

    }
}