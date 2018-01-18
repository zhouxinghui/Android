package com.yzy.ebag.student

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import ebag.core.base.BaseFragment

/**
 * Created by unicho on 2018/1/14.
 */
class TestFragment:BaseFragment() {

    companion object {
        fun newInstance(): Fragment {
            return TestFragment()
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.test_child
    }

    override fun getBundle(bundle: Bundle?) {
    }

    override fun initViews(rootView: View) {
    }
}