package com.yzy.ebag.student.activity.growth

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
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

        val fragments = ArrayList<Fragment>()
        val titleList = ArrayList<String>()
        fragments.add(AchievementFragment.newInstance(gradeId, 1))
        fragments.add(AchievementFragment.newInstance(gradeId, 2))
        fragments.add(AchievementFragment.newInstance(gradeId, 3))
        titleList.add("随堂作业")
        titleList.add("课后作业")
        titleList.add("考试试卷")

        val adapter = ViewPagerAdapter(
                supportFragmentManager, fragments, titleList)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 2
        tabLayout.setupWithViewPager(viewPager)
    }

    inner class ViewPagerAdapter(fm: FragmentManager, private val fragments: ArrayList<Fragment>, private val titleList: ArrayList<String>): FragmentPagerAdapter(fm){
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
}