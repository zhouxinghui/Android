package com.yzy.ebag.student.activity.growth

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import com.yzy.ebag.student.R
import ebag.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_growth_enter_tab.*

/**
 * @author caoyu
 * @date 2018/1/27
 * @description
 */
class GrowthEnterTabFragment : BaseFragment() {

    companion object {
        fun newInstance(): GrowthEnterTabFragment {
            return GrowthEnterTabFragment()
        }
    }
    override fun getLayoutRes(): Int {
        return R.layout.fragment_growth_enter_tab
    }

    override fun getBundle(bundle: Bundle?) {

    }

    override fun initViews(rootView: View) {
        viewPager.adapter = SectionsPagerAdapter(childFragmentManager, arrayOfNulls(3))
        viewPager.offscreenPageLimit = 1

        viewPager.setCurrentItem(0,false)
        previousBtn.visibility = View.INVISIBLE

        nextBtn.setOnClickListener {
            viewPager.setCurrentItem(viewPager.currentItem + 1,false)
        }

        previousBtn.setOnClickListener {
            viewPager.setCurrentItem(viewPager.currentItem - 1,false)
        }

        viewPager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
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
}