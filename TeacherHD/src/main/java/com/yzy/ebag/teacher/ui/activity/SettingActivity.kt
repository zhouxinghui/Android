package com.yzy.ebag.teacher.ui.activity

import android.content.Intent
import android.view.View
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.ui.activity.account.LoginActivity
import ebag.core.base.App
import ebag.core.base.BaseActivity
import ebag.core.util.AppManager
import ebag.core.util.T
import ebag.hd.base.Constants
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity(), View.OnClickListener{
    override fun getLayoutId(): Int { return R.layout.activity_setting }

    override fun initViews() {
        versionUpdate.setOnClickListener(this)
        officeNotice.setOnClickListener(this)
        userFeedback.setOnClickListener(this)
        aboutUs.setOnClickListener(this)
        exitLogin.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.versionUpdate ->{
                T.show(this, "版本更新")
            }
            R.id.officeNotice ->{
                T.show(this, "官方公告")
            }
            R.id.userFeedback ->{
                T.show(this, "用户反馈")
            }
            R.id.aboutUs ->{
                startActivity(Intent(this, AboutUsActivity::class.java))
            }
            R.id.exitLogin ->{
                App.deleteToken()
                startActivity(Intent(this, LoginActivity::class.java).putExtra(Constants.KEY_TO_MAIN, true))
                AppManager.finishAllActivity()
            }
        }
    }
}
