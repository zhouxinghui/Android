package com.yzy.ebag.teacher.module.correcting

import android.content.res.Resources
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.TypedValue
import android.widget.LinearLayout
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseActivity
import kotlinx.android.synthetic.main.activity_correcting.*
import java.lang.reflect.Field

class CorrectingActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_correcting

    override fun initViews() {
        val fragments = arrayListOf(CorrectingFragment.newInstance(), CorrectingFragment.newInstance(), CorrectingFragment.newInstance())
        val titleList = arrayListOf("随堂作业", "课后作业", "考试试卷")

        val adapter = ViewPagerAdapter(
                supportFragmentManager, fragments, titleList)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 2
        tabLayout.post{setIndicator(tabLayout, 5, 5)}
        tabLayout.setupWithViewPager(viewPager)
    }

    inner class ViewPagerAdapter(fm: FragmentManager, private val fragments: List<Fragment>, private val titleList: List<String>): FragmentPagerAdapter(fm){
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
