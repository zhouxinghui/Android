package com.yzy.ebag.student.activity

import android.content.Intent
import android.view.View
import com.yzy.ebag.student.R
import com.yzy.ebag.student.activity.account.LoginActivity
import ebag.core.base.App
import ebag.core.base.BaseActivity
import ebag.core.util.SerializableUtils
import ebag.hd.base.Constants
import kotlinx.android.synthetic.main.activity_setting.*

/**
 * Created by caoyu on 2018/1/9.
 */
class SettingActivity: BaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.logoutBtn -> {
                App.deleteToken()
                SerializableUtils.deleteSerializable(ebag.hd.base.Constants.STUDENT_USER_ENTITY)
                startActivity(
                        Intent(this, LoginActivity::class.java)
                                .putExtra(Constants.KEY_TO_MAIN,true)
                )
            }

            R.id.themeBtn -> {

            }

            R.id.updateBtn -> {

            }

            R.id.announceBtn -> {

            }

            R.id.feedbackBtn -> {

            }

            R.id.aboutBtn -> {

            }

            R.id.setBtn -> {

            }

            R.id.desktopBtn -> {

            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }

    override fun initViews() {
        logoutBtn.setOnClickListener(this)
        themeBtn.setOnClickListener(this)
        updateBtn.setOnClickListener(this)
        announceBtn.setOnClickListener(this)
        feedbackBtn.setOnClickListener(this)
        aboutBtn.setOnClickListener(this)
        setBtn.setOnClickListener(this)
        desktopBtn.setOnClickListener(this)

    }
}