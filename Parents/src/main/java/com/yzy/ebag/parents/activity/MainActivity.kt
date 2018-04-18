package com.yzy.ebag.parents.activity

import android.support.v4.app.Fragment
import android.view.View
import android.widget.RadioGroup
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.adapter.MainPagerAdapter
import com.yzy.ebag.parents.fragment.ClazzFragment
import com.yzy.ebag.parents.fragment.MainFragment
import com.yzy.ebag.parents.fragment.PersonalFragment
import ebag.core.base.BaseActivity
import ebag.core.util.T
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), RadioGroup.OnCheckedChangeListener {

    private lateinit var pagerAdapter: MainPagerAdapter
    private val fragmentList: MutableList<Fragment> = mutableListOf(MainFragment.newInstance(), ClazzFragment.newInstance(), PersonalFragment.newInstance())

    private var mExitTime: Long = 0

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initViews() {

        pagerAdapter = MainPagerAdapter(fragmentList, supportFragmentManager)
        rb_main.performClick()
        viewpager.offscreenPageLimit = 3
        viewpager.adapter = pagerAdapter
        group.setOnCheckedChangeListener(this)

    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.rb_main -> {
                viewpager.setCurrentItem(0, false)
                setTilteVisiable(true)
                setTitle("学习中心")
            }

            R.id.rb_clazz -> {
                viewpager.setCurrentItem(1, false)
                setTilteVisiable(false)
                setTitle("班级")
            }

            R.id.rb_personal -> {
                viewpager.setCurrentItem(2, false)
                setTilteVisiable(false)
                setTitle("个人信息")
            }
        }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            T.show(this, "再按一次退出程序")
            mExitTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }

    private fun setTilteVisiable(flag: Boolean) {
        if (flag) {
            main_title_right.visibility = View.VISIBLE
            main_title_left.visibility = View.VISIBLE
            return
        }

        main_title_right.visibility = View.GONE
        main_title_left.visibility = View.GONE
    }

    private fun setTitle(str: String) {
        main_title_tv.text = str
    }
}
