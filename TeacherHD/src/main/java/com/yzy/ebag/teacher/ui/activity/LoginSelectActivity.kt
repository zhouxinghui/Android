package com.yzy.ebag.teacher.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.umeng.socialize.utils.Log

import com.yzy.ebag.teacher.R
import ebag.core.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login_select.*

class LoginSelectActivity : BaseActivity(), View.OnClickListener {
    private var uid: String? = null
    private var threeparty: String? = null
    private var unionid: String? = null
    private var access_token: String? = null
    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btn_binding -> {
                startActivity(Intent(this, BindingActivity::class.java)
                        .putExtra("name", "b")
                        .putExtra("threeparty", threeparty)
                        .putExtra("unionid", unionid)
                        .putExtra("uid",uid)
                        .putExtra("access_token", access_token))
            }
            R.id.btn_create -> {
                startActivity(Intent(this, BindingActivity::class.java)
                        .putExtra("name", "x")
                        .putExtra("threeparty", threeparty)
                        .putExtra("unionid", unionid)
                        .putExtra("access_token", access_token)
                )
            }

        }
    }

    override fun initViews() {
        uid = intent.getStringExtra("uid")
        threeparty = intent.getStringExtra("name")
        unionid = intent.getStringExtra("unionid")
        access_token = intent.getStringExtra("access_token")
        Log.d("wy1","$uid,$threeparty,$unionid,$access_token")
        btn_create.setOnClickListener(this)
        btn_binding.setOnClickListener(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login_select
    }

}
