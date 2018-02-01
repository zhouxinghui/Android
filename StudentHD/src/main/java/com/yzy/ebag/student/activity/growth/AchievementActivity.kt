package com.yzy.ebag.student.activity.growth

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.yzy.ebag.student.R
import ebag.core.base.BaseActivity
import kotlinx.android.synthetic.main.activity_achievement.*

/**
 * @author caoyu
 * @date 2018/2/1
 * @description 成绩统计
 */
class AchievementActivity : BaseActivity(){

    companion object {
        fun jump(context: Context, gradeId: String){
            context.startActivity(
                    Intent(context,AchievementActivity::class.java)
                            .putExtra("gradeId", gradeId)
            )
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_achievement
    }

    lateinit var gradeId: String

    override fun initViews() {
        gradeId = intent.getStringExtra("gradeId") ?: ""

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.rbSt -> {//随堂
                    viewPager.setCurrentItem(0,false)
                }
                R.id.rbKh -> {// 课后
                    viewPager.setCurrentItem(1,false)
                }
                R.id.rbKs -> {// 考试
                    viewPager.setCurrentItem(2,false)
                }
            }
        }

        viewPager.adapter = SectionsPagerAdapter(supportFragmentManager, arrayOfNulls(3))
        viewPager.offscreenPageLimit = 1
        viewPager.setCurrentItem(0,false)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when(position){
                    0 -> {
                        radioGroup.check(R.id.rbSt)
                    }
                    1 -> {
                        radioGroup.check(R.id.rbKh)
                    }
                    2 -> {
                        radioGroup.check(R.id.rbKs)
                    }
                }
            }

        })
    }

    inner class SectionsPagerAdapter(fm: FragmentManager, private val fragments: Array<Fragment?>) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            if (fragments[position] == null) {
                fragments[position] = AchievementFragment.newInstance(gradeId, position)
            }
            return fragments[position]!!
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }
}