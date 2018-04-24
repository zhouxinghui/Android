package com.yzy.ebag.parents.ui.activity

import android.content.Context
import android.content.Intent
import com.yzy.ebag.parents.R
import ebag.core.base.BaseActivity

class RepresentActivity:BaseActivity(){
    override fun getLayoutId(): Int = R.layout.activity_represent

    override fun initViews() {

    }

    companion object {
        fun start(c:Context){
            c.startActivity(Intent(c,RepresentActivity::class.java))
        }
    }

}