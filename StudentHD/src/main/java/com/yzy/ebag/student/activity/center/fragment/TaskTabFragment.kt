package com.yzy.ebag.student.activity.center.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.yzy.ebag.student.R
import ebag.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_task_tab.*

/**
 * @author caoyu
 * @date 2018/1/17
 * @description
 */
class TaskTabFragment: BaseFragment() {

    companion object {
        fun newInstance(classId: String): TaskTabFragment{
            val fragment = TaskTabFragment()
            val bundle =Bundle()
            bundle.putString("classId",classId)
            fragment.arguments = bundle
            return fragment
        }
    }

    lateinit var classId: String
    override fun getLayoutRes(): Int {
        return R.layout.fragment_task_tab
    }

    override fun getBundle(bundle: Bundle?) {
        classId = bundle?.getString("classId") ?: ""
    }

    override fun initViews(rootView: View) {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbLabour -> {//劳动
                    viewPager.setCurrentItem(0, false)
                }
                R.id.rbStudy -> {//学习
                    viewPager.setCurrentItem(1,false)
                }
            }
        }
        viewPager.adapter = SectionsPagerAdapter(childFragmentManager, arrayOfNulls(2))
        viewPager.offscreenPageLimit = 1
        viewPager.setCurrentItem(0,false)

    }

    inner class SectionsPagerAdapter(fm: FragmentManager, private val fragments: Array<Fragment?>) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            if (fragments[position] == null) {
                fragments[position] = showFragment(position)
            }
            return fragments[position]!!
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }

    private fun showFragment(index: Int): Fragment =
        when(index){
            0 -> LabourFragment.newInstance(classId)
            1 -> StudyFragment.newInstance(classId)
            else -> LabourFragment.newInstance(classId)
        }

}