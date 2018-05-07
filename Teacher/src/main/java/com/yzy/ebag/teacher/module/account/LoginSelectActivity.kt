package com.yzy.ebag.teacher.module.account

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.umeng.socialize.UMAuthListener
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.utils.Log
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login_select.*

class LoginSelectActivity : BaseActivity(), View.OnClickListener {
    companion object {
        var mActivity : Activity? = null
    }
    private var uid: String? = null
    private var accessToken: String? = null
    private var name :String? = null
    private var iconurl :String? = null
    private var gender :String? = null
    private var shareMedia :String? = null
    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btn_binding -> {
                startActivity(Intent(this, BindingActivity::class.java)
                        .putExtra("BX", "b")
                        .putExtra("name", name)
                        .putExtra("iconurl", iconurl)
                        .putExtra("gender",gender)
                        .putExtra("shareMedia",shareMedia)
                        .putExtra("accessToken", accessToken)
                        .putExtra("uid",uid)
                        .putExtra("titleText","绑定账号")
                )
            }
            R.id.btn_create -> {
                startActivity(Intent(this, BindingActivity::class.java)
                        .putExtra("uid",uid)
                        .putExtra("BX", "x")
                        .putExtra("accessToken", accessToken)
                        .putExtra("name", name)
                        .putExtra("iconurl", iconurl)
                        .putExtra("gender",gender)
                        .putExtra("shareMedia",shareMedia)
                        .putExtra("titleText","创建账号")
                )
            }
            R.id.titleBar -> {
                cancelThreeParty(judge(shareMedia!!))
                finish()
            }

        }
    }

    fun judge(threeparty: String): SHARE_MEDIA {
        if ("QQ".equals(threeparty, true)) {
            iv_thirdly.setImageResource(R.drawable.icon_third_party_login_qq)
            return SHARE_MEDIA.QQ
        } else if ("sina".equals(threeparty, true)) {
            iv_thirdly.setImageResource(R.drawable.icon_third_party_login_micro_blog)
            return SHARE_MEDIA.SINA
        } else {
            iv_thirdly.setImageResource(R.drawable.icon_third_party_login_weinxin)
            return SHARE_MEDIA.WEIXIN
        }
    }

    override fun initViews() {
        mActivity = this
        uid = intent.getStringExtra("uid")
        accessToken = intent.getStringExtra("accessToken")
        name = intent.getStringExtra("name")
        iconurl = intent.getStringExtra("iconurl")
        gender = intent.getStringExtra("gender")
        shareMedia = intent.getStringExtra("share_media")
        if ("QQ".equals(shareMedia, true)) {
            iv_thirdly.setImageResource(R.drawable.icon_third_party_login_qq)
        } else if ("sina".equals(shareMedia, true)) {
            iv_thirdly.setImageResource(R.drawable.icon_third_party_login_micro_blog)
        } else {
            iv_thirdly.setImageResource(R.drawable.icon_third_party_login_weinxin)

        }

        btn_create.setOnClickListener(this)
        btn_binding.setOnClickListener(this)
        titleBar.setOnClickListener(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login_select
    }

    override fun onBackPressed() {
        super.onBackPressed()
        cancelThreeParty(SHARE_MEDIA.QQ)
    }

    fun cancelThreeParty(share_media: SHARE_MEDIA) {
        UMShareAPI.get(this).deleteOauth(this, share_media, object : UMAuthListener {
            override fun onComplete(p0: SHARE_MEDIA?, p1: Int, p2: MutableMap<String, String>?) {
                Log.d("取消授权成功")
                Toast.makeText(this@LoginSelectActivity, "取消授权成功", Toast.LENGTH_SHORT)
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
