package com.yzy.ebag.parents.activity

import android.widget.RadioGroup
import com.yzy.ebag.parents.R
import ebag.core.base.BaseActivity
import ebag.core.util.T
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : BaseActivity(),RadioGroup.OnCheckedChangeListener {

    private var mExitTime: Long = 0

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initViews() {

        rb_main.performClick()

    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {

    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            T.show(this,"再按一次退出程序")
            mExitTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }
}
