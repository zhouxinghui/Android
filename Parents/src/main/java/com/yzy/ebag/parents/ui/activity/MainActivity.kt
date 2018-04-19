package com.yzy.ebag.parents.ui.activity

import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.View
import android.widget.RadioGroup
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.ui.adapter.MainPagerAdapter
import com.yzy.ebag.parents.ui.fragment.ClazzFragment
import com.yzy.ebag.parents.ui.fragment.MainFragment
import com.yzy.ebag.parents.ui.fragment.PersonalFragment
import com.yzy.ebag.parents.ui.widget.SharePupopwindow
import ebag.core.base.BaseActivity
import ebag.core.util.T
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), RadioGroup.OnCheckedChangeListener, View.OnClickListener {

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
        main_title_right.setOnClickListener(this)
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

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.main_title_right -> {
                val p = SharePupopwindow(this, object : ShareClickCallback {
                    override fun wxClick() {
                       /* if (UMShareAPI.get(this@MainActivity).isInstall(this@MainActivity, SHARE_MEDIA.WEIXIN)) {

                        } else {
                            T.show(this@MainActivity, "请先安装微信客户端")
                        }*/
                    }

                    override fun weiboClick() {

                    }

                    override fun momentClick() {
                        /*if (UMShareAPI.get(this@MainActivity).isInstall(this@MainActivity, SHARE_MEDIA.WEIXIN)) {

                        } else {
                            T.show(this@MainActivity, "请先安装微信客户端")
                        }*/
                    }

                    override fun qqClick() {
                        /*if (UMShareAPI.get(this@MainActivity).isInstall(this@MainActivity, SHARE_MEDIA.QQ)) {

                        } else {
                            T.show(this@MainActivity, "请先安装QQ客户端")
                        }*/
                    }

                })
                p.showAtLocation(rootLayout, Gravity.BOTTOM, 0, 0)
            }
        }
    }

    interface ShareClickCallback {

        fun wxClick()
        fun weiboClick()
        fun momentClick()
        fun qqClick()
    }

}
