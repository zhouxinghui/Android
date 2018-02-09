package com.yzy.ebag.teacher

import com.yzy.ebag.teacher.ui.fragment.custom.ChoiceFragment
import ebag.core.base.BaseActivity

class TestXmlActivity : BaseActivity() {
    override fun initViews() {
        supportFragmentManager.beginTransaction().replace(R.id.contentLayout, ChoiceFragment.newInstance()).commitAllowingStateLoss()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_test_xml
    }
}
