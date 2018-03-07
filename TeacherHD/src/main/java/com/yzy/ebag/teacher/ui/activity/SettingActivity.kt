package com.yzy.ebag.teacher.ui.activity

import android.content.Intent
import android.view.View
import android.widget.Toast
import com.umeng.socialize.UMAuthListener
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.utils.Log
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.ui.activity.account.LoginActivity
import ebag.core.base.App
import ebag.core.base.BaseActivity
import ebag.core.util.AppManager
import ebag.core.util.T
import ebag.hd.base.Constants
import ebag.hd.util.checkUpdate
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
                checkUpdate(Constants.UPDATE_TEACHER)
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
                Toast.makeText(this@SettingActivity, "取消授权成功", Toast.LENGTH_SHORT)
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
