package com.yzy.ebag.student.module.personal

import android.content.Context
import android.content.Intent
import android.view.View
import com.yzy.ebag.student.R
import com.yzy.ebag.student.module.account.LoginActivity
import ebag.core.base.App
import ebag.core.base.BaseActivity
import ebag.core.util.AppManager
import ebag.mobile.base.Constants
import ebag.mobile.checkUpdate
import ebag.mobile.module.AboutUsActivity
import ebag.mobile.module.OfficialAnnounceActivity
import ebag.mobile.widget.UserFeedbackDialog
import kotlinx.android.synthetic.main.activity_setting.*

/**
 * Created by YZY on 2018/5/15.
 */
class SettingActivity: BaseActivity(), View.OnClickListener {
    override fun getLayoutId(): Int = R.layout.activity_setting

    companion object {
        fun jump(context: Context){
            context.startActivity(Intent(context, SettingActivity::class.java))
        }
    }
    private val feedbackDialog by lazy { UserFeedbackDialog(this) }
    override fun initViews() {
        val  packageInfo = packageManager.getPackageInfo(this.packageName, 0)
        versionName.text =  "当前版本："+packageInfo.versionName
        logoutBtn.setOnClickListener(this)
        updateBtn.setOnClickListener(this)
        announceBtn.setOnClickListener(this)
        feedbackBtn.setOnClickListener(this)
        aboutBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.logoutBtn -> {
                App.deleteToken()
                startActivity(
                        Intent(this, LoginActivity::class.java)
                                .putExtra(Constants.KEY_TO_MAIN,true)
                )
                AppManager.finishAllActivity()
            }

            R.id.updateBtn -> {
                checkUpdate(Constants.UPDATE_STUDENT)
            }

            R.id.announceBtn -> {
                OfficialAnnounceActivity.jump(this, "student")
            }

            R.id.feedbackBtn -> {
                feedbackDialog.show()
            }

            R.id.aboutBtn -> {
                startActivity(Intent(this, AboutUsActivity::class.java))
            }
        }
    }
}