package com.yzy.ebag.student.activity.location

import android.content.Intent
import com.yzy.ebag.student.R
import ebag.core.base.BaseActivity
import kotlinx.android.synthetic.main.activity_location.*

/**
 * Created by fansan on 2018/3/26.
 */
class LocationActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_location

    override fun initViews() {

        title_bar.setOnRightClickListener {

            startActivityForResult(Intent(this, UplodaLocationActivity::class.java),999)
        }

        stateview.showEmpty()

    }

}