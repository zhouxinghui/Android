package com.yzy.ebag.teacher.ui.activity

import android.support.v4.app.Fragment
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.ui.fragment.CorrectingFragment
import ebag.core.base.BaseActivity
import ebag.hd.base.Constants
import kotlinx.android.synthetic.main.activity_correcting.*

class CorrectingActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_correcting
    }

    override fun initViews() {
        backBtn.setOnClickListener { finish() }
        changeFragment(0)

        titleGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.clazzWork ->{
                    changeFragment(0)
                }
                R.id.afterWork ->{
                    changeFragment(1)
                }
                R.id.testPaper ->{
                    changeFragment(2)
                }
            }
        }
    }

    private var tempFragment : Fragment? = null
    private val fragmentArrays = arrayOf(CorrectingFragment.newInstance(Constants.STZY_TYPE), CorrectingFragment.newInstance(Constants.KHZY_TYPE), CorrectingFragment.newInstance(Constants.KSSJ_TYPE))
    /**
     * 显示指定的Fragment
     *
     * @param index 索引
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
                fragmentTransaction.hide(tempFragment).add(R.id.contentLayout, fragment)
            }
        } else {
            fragmentTransaction.add(R.id.contentLayout, fragment)
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

}
