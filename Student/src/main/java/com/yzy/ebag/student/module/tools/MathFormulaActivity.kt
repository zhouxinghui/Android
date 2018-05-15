package com.yzy.ebag.student.module.tools

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.FormulaTypeBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.BaseActivity
import ebag.core.http.network.MsgException
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import kotlinx.android.synthetic.main.activity_math_formula.*

/**
 * Created by YZY on 2018/5/15.
 */
class MathFormulaActivity: BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_math_formula

    private lateinit var fragments: Array<MathFormulaFragment?>
    private val request = object : RequestCallBack<ArrayList<FormulaTypeBean>>(){
        override fun onStart() {
            stateView.showLoading()
        }

        override fun onSuccess(entity: ArrayList<FormulaTypeBean>?) {
            if (entity == null || entity.isEmpty()){
                stateView.showEmpty()
            }else{
                stateView.showContent()
                fragments = arrayOfNulls(entity.size)
                viewPager.adapter = ViewPagerAdapter(entity)
                tabLayout.setupWithViewPager(viewPager)
            }
        }

        override fun onError(exception: Throwable) {
            if (exception is MsgException) {
                stateView.showError(exception.message.toString())
            }else{
                exception.handleThrowable(this@MathFormulaActivity)
                stateView.showError()
            }
        }
    }
    override fun initViews() {
        stateView.setOnRetryClickListener {
            StudentApi.formula("", 1, 1, request)
        }
        StudentApi.formula("", 1, 1, request)
    }

    private inner class ViewPagerAdapter(val entity: List<FormulaTypeBean>?): FragmentPagerAdapter(supportFragmentManager){
        override fun getItem(position: Int): Fragment? {
            if (fragments[position] == null){
                fragments[position] = MathFormulaFragment.newInstance(entity?.get(position))
                return fragments[position]
            }
            return null
        }

        override fun getCount(): Int = entity?.size ?: 0

        override fun getPageTitle(position: Int): CharSequence = entity?.get(position)?.formulaType ?: ""
    }
}