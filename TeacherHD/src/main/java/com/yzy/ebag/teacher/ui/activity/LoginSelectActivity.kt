package com.yzy.ebag.teacher.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import com.yzy.ebag.teacher.R
import ebag.core.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login_select.*

class LoginSelectActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btn_create -> {
                startActivity(Intent(this, BindingActivity::class.java))
            }
        }
    }

    override fun initViews() {
        btn_create.setOnClickListener(this)
        btn_binding.setOnClickListener(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login_select
    }

}
