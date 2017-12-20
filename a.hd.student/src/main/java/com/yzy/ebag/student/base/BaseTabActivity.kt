package com.yzy.ebag.student.base

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.yzy.ebag.student.R
import ebag.core.base.BaseActivity
import kotlinx.android.synthetic.main.activity_base_tabstrip.*

/**
 * Created by unicho on 2017/11/29.
 */
abstract class BaseTabActivity : BaseActivity(){

    private var startPage: Int = 0
    override fun getLayoutId(): Int {
        return R.layout.activity_base_tabstrip
    }

    override fun initViews() {
        init()
        val mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        tabsContent.adapter = mSectionsPagerAdapter
        for (i in 0 until getTabTitle().size) {
            mTabs.addTab(mTabs.newTab().setText(getTabTitle()[i]))
        }
        mTabs.setupWithViewPager(tabsContent)
        if (onPageChangeListener != null) {
            mTabs.addOnTabSelectedListener(onPageChangeListener!!)
        }
        setViewPagerCurrentItem(startPage)
    }

    /**
     * 设置起始tab
     * @param currentPosition
     */
    protected fun setViewPagerCurrentItem(currentPosition: Int) {
        tabsContent?.currentItem = currentPosition
    }

    fun getViewPagerCurrentItem(): Int {
        return tabsContent.currentItem
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        private var fragments = arrayOfNulls<Fragment>(getTabTitle().size)

        override fun getItem(position: Int): Fragment {
            var fragment: Fragment? = fragments[position]
            if (fragment == null) {
                fragment = getFragment(position)
                fragments[position] = fragment
            }
            return fragment
        }

        override fun getCount(): Int {
            return getTabTitle().size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return getTabTitle()[position]
        }
    }

    /**
     * @Description: 获取TabTitle 文字数组
     * @return
     */
    protected abstract fun getTabTitle(): Array<String>

    /**
     * @Description: 获取TabTitle 文字数组
     * @return
     */
    protected abstract fun init()

    /**
     * @Description: 获取点击或滑动显示的Fragment
     * @param position
     * @return
     */
    protected abstract fun getFragment(position: Int): Fragment

    private var onPageChangeListener: TabLayout.OnTabSelectedListener? = null

    fun setOnPageChangeListener(onPageChangeListener: TabLayout.OnTabSelectedListener) {
        this.onPageChangeListener = onPageChangeListener
    }

    fun setStartPage(startPage: Int) {
        this.startPage = startPage
    }
}