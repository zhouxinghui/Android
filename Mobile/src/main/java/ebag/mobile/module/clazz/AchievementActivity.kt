package ebag.mobile.module.clazz

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import ebag.core.base.BaseActivity
import ebag.mobile.R
import kotlinx.android.synthetic.main.activity_achievement.*

/**
 * @author caoyu
 * @date 2018/2/1
 * @description 成绩统计
 */
class AchievementActivity : BaseActivity() {

    companion object {
        fun jump(context: Context, gradeCode: String) {
            context.startActivity(
                    Intent(context, AchievementActivity::class.java)
                            .putExtra("gradeCode", gradeCode)
            )
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_achievement
    }

    lateinit var gradeCode: String

    override fun initViews() {
        gradeCode = intent.getStringExtra("gradeCode") ?: ""

        val fragments = ArrayList<Fragment>()
        val titleList = ArrayList<String>()
        fragments.add(AchievementFragment.newInstance(1, gradeCode))
        fragments.add(AchievementFragment.newInstance(2, gradeCode))
        fragments.add(AchievementFragment.newInstance(4, gradeCode))
        titleList.add("随堂作业")
        titleList.add("课后作业")
        titleList.add("考试试卷")

        val adapter = ViewPagerAdapter(
                supportFragmentManager, fragments, titleList)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 2
        tabLayout.setupWithViewPager(viewPager)
    }

    inner class ViewPagerAdapter(fm: FragmentManager, private val fragments: ArrayList<Fragment>, private val titleList: ArrayList<String>) : FragmentPagerAdapter(fm) {
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