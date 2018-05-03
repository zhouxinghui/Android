package com.yzy.ebag.teacher.module.correcting

import android.content.res.Resources
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.util.TypedValue
import android.widget.LinearLayout
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.CorrectingBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.base.BaseActivity
import ebag.core.http.network.MsgException
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.mobile.base.Constants
import kotlinx.android.synthetic.main.activity_correcting.*
import java.lang.reflect.Field

class CorrectingActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_correcting
    var classId = ""
    var subCode = ""
    private val classList = ArrayList<CorrectingBean>()
    private lateinit var fragments: List<CorrectingFragment>
    private var currentPage = 0
    private val request = object : RequestCallBack<List<CorrectingBean>>(){
        override fun onStart() {
            stateView.showLoading()
        }

        override fun onSuccess(entity: List<CorrectingBean>?) {
            if (entity == null || entity.isEmpty()){
                stateView.showEmpty()
                return
            }
            var classBean : CorrectingBean? = null
            for (i in entity.indices){
                if (!entity[i].subjectVos.isEmpty()){
                    classBean = entity[i]
                    break
                }
            }
            if (classBean == null) {
                stateView.showEmpty("暂无所教课程")
                return
            }
            stateView.showContent()
            classList.clear()
            classList.addAll(entity)
            classId = classBean.classId
            val subjectBean = classBean.subjectVos[0]
            subCode = subjectBean.subCode
            clazzTv.text = "${classBean.className} - ${subjectBean.subject}"

            initFragment(classId, subCode)

            fragments[0].setData(subjectBean.homeWorkInfoVos)
        }

        override fun onError(exception: Throwable) {
            if (exception is MsgException){
                stateView.showError("${exception.message.toString()}")
            }else{
                exception.handleThrowable(this@CorrectingActivity)
            }
        }
    }

    private val classPopup by lazy {
        val popup = CorrectingSubjectPopup(this)
        popup.onSubjectSelect = {classId, className, subCode, subName ->
            clazzTv.text = "$className - $subName"
            if (this.classId != classId || this.subCode != subCode){
                this.classId = classId
                this.subCode = subCode
                fragments[currentPage].update(classId, subCode)
                fragments.forEach {
                    it.setReloadData(classId, subCode)
                }
            }
        }
        popup
    }

    override fun initViews() {
        TeacherApi.searchPublish(Constants.STZY_TYPE, request)
        subjectBtn.setOnClickListener {
            classPopup.show(classList, subjectBtn)
        }
        stateView.setOnRetryClickListener {
            TeacherApi.searchPublish(Constants.STZY_TYPE, request)
        }
    }

    private fun initFragment(classId: String, subCode: String){
        fragments = arrayListOf(
                CorrectingFragment.newInstance(Constants.STZY_TYPE, classId, subCode),
                CorrectingFragment.newInstance(Constants.KHZY_TYPE, classId, subCode),
                CorrectingFragment.newInstance(Constants.KSSJ_TYPE, classId, subCode))
        val titleList = arrayListOf("随堂作业", "课后作业", "考试试卷")

        val adapter = ViewPagerAdapter(
                supportFragmentManager, fragments, titleList)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 2
        tabLayout.post{setIndicator(tabLayout, 5, 5)}
        tabLayout.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
            override fun onPageSelected(position: Int) {
                currentPage = position
            }

        })
    }

    inner class ViewPagerAdapter(fm: FragmentManager, private val fragments: List<Fragment>, private val titleList: List<String>): FragmentPagerAdapter(fm){
        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return titleList[position]
        }
    }

    private fun setIndicator(tab: TabLayout, leftDip: Int, rightDip: Int) {
        val tabLayout = tab.javaClass
        val tabStrip: Field?
        tabStrip = tabLayout.getDeclaredField("mTabStrip")
        tabStrip.isAccessible = true
        val llTab: LinearLayout?
        llTab = tabStrip.get(tab) as LinearLayout
        val left = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip.toFloat(), Resources.getSystem().displayMetrics).toInt()
        val right = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip.toFloat(), Resources.getSystem().displayMetrics).toInt()
        for (i in 0 until llTab.childCount) {
            val child = llTab.getChildAt(i)
            child.setPadding(0, 0, 0, 0)
            val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            params.leftMargin = left
            params.rightMargin = right
            child.layoutParams = params
            child.invalidate()
        }

    }

}
