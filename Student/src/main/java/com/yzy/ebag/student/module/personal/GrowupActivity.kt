package com.yzy.ebag.student.module.personal

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import com.yzy.ebag.student.R
import ebag.core.base.BaseActivity
import kotlinx.android.synthetic.main.activity_growth_enter_tab.*

/**
 *  Created by fansan on 2018/5/24 9:51
 */
 
class GrowupActivity:BaseActivity(){

    override fun getLayoutId(): Int = R.layout.activity_growth_enter_tab

    override fun initViews() {

        viewPager.adapter = SectionsPagerAdapter(supportFragmentManager, arrayOfNulls(3))
        viewPager.offscreenPageLimit = 1

        viewPager.setCurrentItem(0,false)
        previousBtn.visibility = View.INVISIBLE

        nextBtn.setOnClickListener {
            viewPager.setCurrentItem(viewPager.currentItem + 1,false)
        }

        previousBtn.setOnClickListener {
            viewPager.setCurrentItem(viewPager.currentItem - 1,false)
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if(position == 0){
                    previousBtn.visibility = View.INVISIBLE
                }else{
                    previousBtn.visibility = View.VISIBLE
                }

                if(position == 2){
                    nextBtn.visibility = View.INVISIBLE
                }else{
                    nextBtn.visibility = View.VISIBLE
                }
            }

        })
    }

    inner class SectionsPagerAdapter(fm: FragmentManager, private val fragments: Array<Fragment?>) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            if (fragments[position] == null) {
                fragments[position] = GrowthEnterFragment.newInstance(position)
            }
            return fragments[position]!!
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }

    companion object {
        fun start(c:Context){
            c.startActivity(Intent(c,GrowupActivity::class.java))
        }
    }

}