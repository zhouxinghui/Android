package com.yzy.ebag.parents.ui.activity

import android.content.Intent
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.View
import android.widget.RadioGroup
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMWeb
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.ui.adapter.MainPagerAdapter
import com.yzy.ebag.parents.ui.fragment.ClazzFragment
import com.yzy.ebag.parents.ui.fragment.MainFragment
import com.yzy.ebag.parents.ui.fragment.PersonalFragment
import com.yzy.ebag.parents.ui.widget.SharePupopwindow
import ebag.core.base.BaseActivity
import ebag.core.util.T
import ebag.mobile.base.Constants
import ebag.mobile.checkUpdate
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private lateinit var pagerAdapter: MainPagerAdapter
    private val fragmentList: MutableList<Fragment> = mutableListOf(MainFragment.newInstance(), ClazzFragment.newInstance(), PersonalFragment.newInstance())

    private var mExitTime: Long = 0

    override fun getLayoutId(): Int = R.layout.activity_main
    private lateinit var popupWindow: SharePupopwindow

    override fun initViews() {

        pagerAdapter = MainPagerAdapter(fragmentList, supportFragmentManager)
        rb_main.performClick()
        viewpager.offscreenPageLimit = 3
        viewpager.adapter = pagerAdapter
        group.setOnCheckedChangeListener(this)
        main_title_right.setOnClickListener(this)
        main_title_left.setOnClickListener(this)
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.rb_main -> {
                viewpager.setCurrentItem(0, false)
                setTilteVisiable(true)
                setTitle("学习中心")
            }

            R.id.rb_clazz -> {
                viewpager.setCurrentItem(1, false)
                setTilteVisiable(false)
                setTitle("班级")
            }

            R.id.rb_personal -> {
                viewpager.setCurrentItem(2, false)
                setTilteVisiable(false)
                setTitle("我的")
            }
        }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            T.show(this, "再按一次退出程序")
            mExitTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }

    private fun setTilteVisiable(flag: Boolean) {
        if (flag) {
            main_title_right.visibility = View.VISIBLE
            main_title_left.visibility = View.VISIBLE
            return
        }

        main_title_right.visibility = View.GONE
        main_title_left.visibility = View.GONE
    }

    private fun setTitle(str: String) {
        main_title_tv.text = str
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.main_title_right -> {
                popupWindow = SharePupopwindow(this, object : ShareClickCallback {
                    override fun wxClick() {
                        if (UMShareAPI.get(this@MainActivity).isInstall(this@MainActivity, SHARE_MEDIA.WEIXIN)) {
                            doShare(SHARE_MEDIA.WEIXIN)
                        } else {
                            T.show(this@MainActivity, "请先安装微信客户端")
                        }
                    }

                    override fun weiboClick() {
                        if (UMShareAPI.get(this@MainActivity).isInstall(this@MainActivity, SHARE_MEDIA.SINA)) {
                            doShare(SHARE_MEDIA.SINA)
                        } else {
                            T.show(this@MainActivity, "请先安装微博客户端")
                        }

                    }

                    override fun momentClick() {
                        if (UMShareAPI.get(this@MainActivity).isInstall(this@MainActivity, SHARE_MEDIA.WEIXIN)) {
                            doShare(SHARE_MEDIA.WEIXIN_CIRCLE)
                        } else {
                            T.show(this@MainActivity, "请先安装微信客户端")
                        }
                    }

                    override fun qqClick() {
                        if (UMShareAPI.get(this@MainActivity).isInstall(this@MainActivity, SHARE_MEDIA.QQ)) {
                            doShare(SHARE_MEDIA.QQ)
                        } else {
                            T.show(this@MainActivity, "请先安装QQ客户端")
                        }
                    }

                })
                popupWindow.showAtLocation(rootLayout, Gravity.BOTTOM, 0, 0)
            }

            R.id.main_title_left -> {
                LocationActivity.start(this)
            }
        }
    }

    private fun doShare(media: SHARE_MEDIA) {
        var text = "移动端上免费基础教育应用平台,关注小孩安全及学习情况,家长必备神器!"
        if (media == SHARE_MEDIA.SINA)
            text += "http://www.yun-bag.com/ebag-portal/index.dhtml"
        val web = UMWeb("http://www.yun-bag.com/ebag-portal/index.dhtml")
        web.title = "云书包教育云平台"
        val thumb = UMImage(this@MainActivity, "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/logo_parents.png")
        web.setThumb(thumb)
        web.description = "云书包教育云平台"
        val shareAction = ShareAction(this@MainActivity)
        shareAction.setPlatform(media).setCallback(umShareListener).withMedia(web).withText(text)
        if (media == SHARE_MEDIA.SINA)
            shareAction.withMedia(thumb)
        shareAction.share()
    }

    interface ShareClickCallback {

        fun wxClick()
        fun weiboClick()
        fun momentClick()
        fun qqClick()
    }

    private val umShareListener = object : UMShareListener {

        override fun onResult(p0: SHARE_MEDIA?) {
            T.show(this@MainActivity, "分享成功")
            if (popupWindow != null) {
                popupWindow.dismiss()
            }
        }

        override fun onCancel(p0: SHARE_MEDIA?) {
            T.show(this@MainActivity, "分享取消")
            if (popupWindow != null) {
                popupWindow.dismiss()
            }
        }

        override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
            T.show(this@MainActivity, "分享失败")
            if (popupWindow != null) {
                popupWindow.dismiss()
            }
        }

        override fun onStart(p0: SHARE_MEDIA?) {
            T.show(this@MainActivity, "开始分享..")
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 999) {
            (fragmentList[1] as ClazzFragment).updataClazz()
        }
    }

    override fun onResume() {
        super.onResume()
        checkUpdate(Constants.UPDATE_PARENT, false)
        if ((fragmentList[0] as MainFragment).init) {
            (fragmentList[0] as MainFragment).getOnePageInfo()
        }
    }
}
