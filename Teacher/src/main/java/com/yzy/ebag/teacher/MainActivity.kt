package com.yzy.ebag.teacher

import android.text.Html
import ebag.core.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initViews() {
        text.text = Html.fromHtml("<font color='green'><font color='red'>T</font>he<font color='red'>y</font> <font color='red'>us</font>e<font color='red'>d</font> the wo<font color='red'>od</font> t<font color='red'>o</font> m<font color='red'>ak</font>e m<font color='red'>a</font>t<font color='red'>c</font>h<font color='red'>es.</font> </font>")
    }
}
