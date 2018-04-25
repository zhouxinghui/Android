package com.yzy.ebag.teacher.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.*
import android.widget.ImageView
import cn.jpush.android.api.JPushInterface
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.activity.account.LoginActivity
import com.yzy.ebag.teacher.activity.home.MainActivity
import ebag.core.base.App
import ebag.core.base.BaseActivity
import ebag.core.util.L
import ebag.core.util.SPUtils
import ebag.core.util.StringUtils
import ebag.hd.base.Constants
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : BaseActivity(), View.OnTouchListener {
    override fun getLayoutId(): Int = R.layout.activity_welcome

    private var key = ""
    private lateinit var imgList: List<Int>
    private var pointX = 0
    private var currentPageIndex = 0
    private var canJump = false
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        // 将activity设置为全屏显示
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun initViews() {
        JPushInterface.init(applicationContext)
        key = packageManager.getPackageInfo(packageName, 0).versionCode.toString()
        val isFirst = SPUtils.get(this, key, true) as Boolean
        if (isFirst){
            imgList = arrayListOf(R.drawable.t_welcome1,R.drawable.t_welcome2,R.drawable.t_welcome3)
            viewPager.adapter = ViewPagerAdapter()
            viewPager.setOnTouchListener(this)
            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    currentPageIndex = position
                    canJump = position == imgList.size - 1
                }

            })
        }else{
            jump(2000)
        }
        jumpBtn.setOnClickListener {
            if (canJump)
                jump(0)
        }
    }

    private fun jump(millis: Long){
        if (millis == 0L)
            SPUtils.put(this, key, false)
        val token: String = App.TOKEN
        L.e("token", token)
        Handler().postDelayed({
            startActivity(
                    if (!StringUtils.isEmpty(token)) {
                        Intent(this@WelcomeActivity, MainActivity::class.java)
                    } else {
                        Intent(this, LoginActivity::class.java).putExtra(Constants.KEY_TO_MAIN, true)
                    }
            )
            finish()
        }, millis)
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        when(event.action){
            MotionEvent.ACTION_DOWN ->{
                pointX = event.x.toInt()
            }
            MotionEvent.ACTION_MOVE ->{
                if (pointX - event.x > 100 && currentPageIndex == imgList.size - 1) {
                    jump(0)
                }
            }
        }
        return false
    }
    private inner class ViewPagerAdapter: PagerAdapter(){
        override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
            return view == `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val imageView = ImageView(this@WelcomeActivity)
            val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            imageView.layoutParams = lp
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.setImageResource(imgList[position])
            container.addView(imageView)
            return imageView
        }

        override fun getCount(): Int = imgList.size

        /**销毁 一个 页卡*/
        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            // 删除
            container.removeView(`object` as View)
        }
    }
}
