package com.yzy.ebag.teacher

import android.content.Intent
import android.support.v4.app.Fragment
import com.yzy.ebag.teacher.ui.activity.vnc.VNCSetActivity
import com.yzy.ebag.teacher.ui.fragment.FragmentClass
import com.yzy.ebag.teacher.ui.fragment.FragmentFirstPage
import com.yzy.ebag.teacher.ui.fragment.FragmentMine
import ebag.core.base.mvp.MVPActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MVPActivity() {
    private val fragmentArrays = arrayOf(FragmentFirstPage.newInstance(), FragmentClass.newInstance(), FragmentMine.newInstance())
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initViews() {
        changeFragment(0)
        shareScreen.setOnClickListener {
            startActivity(Intent(this, VNCSetActivity::class.java))
        }

        leftGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.firstPage -> {changeFragment(0)}
                R.id.clazz -> {changeFragment(1)}
                R.id.mine -> {changeFragment(2)}
            }
        }
    }

    private var tempFragment : Fragment? = null
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
}
