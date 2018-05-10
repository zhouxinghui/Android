package com.yzy.ebag.teacher.module.personal

import android.content.Intent
import android.view.View
import com.umeng.socialize.UMAuthListener
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.utils.Log
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.module.account.LoginActivity
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
 * Created by YZY on 2018/4/26.
 */
class SettingActivity: BaseActivity(), View.OnClickListener {
    override fun getLayoutId(): Int = R.layout.activity_setting

    private val feedbackDialog by lazy { UserFeedbackDialog(this) }
    override fun initViews() {
        val  packageInfo = packageManager.getPackageInfo(this.packageName, 0)
        versionName.text =  "当前版本：${packageInfo.versionName}"
        versionUpdate.setOnClickListener(this)
        officeNotice.setOnClickListener(this)
        userFeedback.setOnClickListener(this)
        aboutUs.setOnClickListener(this)
        exitLogin.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.versionUpdate ->{
                checkUpdate(Constants.UPDATE_TEACHER)
            }
            R.id.officeNotice ->{
                OfficialAnnounceActivity.jump(this, "teacher")
            }
            R.id.userFeedback ->{
                feedbackDialog.show()
            }
            R.id.aboutUs ->{
                startActivity(Intent(this, AboutUsActivity::class.java))
            }
            R.id.exitLogin ->{
                App.deleteToken()
                startActivity(Intent(this, LoginActivity::class.java).putExtra(Constants.KEY_TO_MAIN, true))
                AppManager.finishAllActivity()
                cancelThreeParty(SHARE_MEDIA.QQ)
                cancelThreeParty(SHARE_MEDIA.WEIXIN)
                cancelThreeParty(SHARE_MEDIA.SINA)
            }
        }
    }

    fun  cancelThreeParty(share_media: SHARE_MEDIA) {
        UMShareAPI.get(this).deleteOauth(this, share_media, object : UMAuthListener {
            override fun onComplete(p0: SHARE_MEDIA?, p1: Int, p2: MutableMap<String, String>?) {
                Log.d("取消授权成功")
            }

            override fun onCancel(p0: SHARE_MEDIA?, p1: Int) {
            }

            override fun onError(p0: SHARE_MEDIA?, p1: Int, p2: Throwable?) {
            }

            override fun onStart(p0: SHARE_MEDIA?) {
            }
        })
    }
}