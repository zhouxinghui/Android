package com.yzy.ebag.teacher.activity.home

import android.content.Intent
import android.support.v4.app.Fragment
import android.view.KeyEvent
import cn.jpush.android.api.JPushInterface
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.activity.vnc.VNCSetActivity
import ebag.core.base.mvp.MVPActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : MVPActivity() {
    private var exitTime: Long = 0
    private val fragmentArrays = arrayOf(FragmentFirstPage.newInstance(), FragmentClass.newInstance(), FragmentMine.newInstance())
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initViews() {
        JPushInterface.init(applicationContext)
        changeFragment(0)
        shareScreen.setOnClickListener {
            startActivity(Intent(this, VNCSetActivity::class.java))
        }

        leftGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.firstPage -> {
                    changeFragment(0)
                }
                R.id.clazz -> {
                    changeFragment(1)
                }
                R.id.mine -> {
                    changeFragment(2)
                }
            }
        }
    }

    private var tempFragment: Fragment? = null
    /**
     * 显示指定的Fragment
     *
     * @param index
     */
    private fun changeFragment(index: Int) {
        val fragment = fragmentArrays[index]
        //v4中Fragment: 获取Fragment事务管理
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        //true: 表示当前的Fragment已经添加到FragmentManager中， false:则反
        if (tempFragment != null) {
            if (fragment.isAdded) {
                fragmentTransaction.hide(tempFragment).show(fragment)
            } else {
                //hide上一个Fragment，add：当前Fragment
                fragmentTransaction.hide(tempFragment).add(R.id.replaceLayout, fragment)
            }
        } else {
            fragmentTransaction.add(R.id.replaceLayout, fragment)
        }

        tempFragment = fragment
        if (!isDestroy) {
            /**
             * 警告：你只能在activity处于可保存状态的状态时，比如running中，onPause()方法和onStop()方法中提交事务，否则会引发异常。
             * 这是因为fragment的状态会丢失。如果要在可能丢失状态的情况下提交事务，请使用commitAllowingStateLoss()。
             */
            fragmentTransaction.commitAllowingStateLoss()   //提交
        }
    }

    override fun destroyPresenter() {

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode === KeyEvent.KEYCODE_BACK && event!!.action === KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                toast("再按一次退出程序")
                exitTime = System.currentTimeMillis();
            } else {
                finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
