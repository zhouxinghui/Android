package com.yzy.ebag.teacher

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.yzy.ebag.teacher.module.home.ClassFragment
import com.yzy.ebag.teacher.module.home.FirstPageFragment
import com.yzy.ebag.teacher.module.home.MineFragment
import ebag.core.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initViews() {
        titleBar.hiddenTitleLeftButton()

        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        bottomNavigationBar.setTabSelectedListener(object : BottomNavigationBar.SimpleOnTabSelectedListener() {
            override fun onTabSelected(position: Int) {
                viewPager.setCurrentItem(position, false)
            }
        })
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING)
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE)
        bottomNavigationBar.addItem(BottomNavigationItem(R.drawable.teacher_logo, R.string.first_page).setActiveColorResource(R.color.white))
                .addItem(BottomNavigationItem(R.drawable.teacher_logo, R.string.clazz).setActiveColorResource(R.color.white))
                .addItem(BottomNavigationItem(R.drawable.teacher_logo, R.string.mine).setActiveColorResource(R.color.white))
                .setFirstSelectedPosition(0)
                .initialise() //所有的设置需在调用该方法前完成
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                bottomNavigationBar.selectTab(position)
                when(position){
                    0 ->{
                        titleBar.setTitle("学习中心")
                    }
                    1 ->{
                        titleBar.setTitle("班级")
                    }
                    2 ->{
                        titleBar.setTitle("我的")
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    private inner class ViewPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager){
        val fragments = arrayOf(FirstPageFragment.newInstance(), ClassFragment.newInstance(), MineFragment.newInstance())
        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

    }
}
