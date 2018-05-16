package com.yzy.ebag.parents.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.GridLayoutManager
import android.view.MotionEvent
import android.view.View
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.bean.OnePageInfoBean
import com.yzy.ebag.parents.common.Constants
import com.yzy.ebag.parents.http.ParentsAPI
import com.yzy.ebag.parents.mvp.model.MainRVModel
import com.yzy.ebag.parents.ui.activity.*
import com.yzy.ebag.parents.ui.adapter.MainRVAdapter
import com.yzy.ebag.parents.ui.widget.PerformanceDialog
import com.yzy.ebag.parents.utils.GlideImageLoader
import ebag.core.base.BaseFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.util.SPUtils
import ebag.core.util.SerializableUtils
import ebag.core.util.T
import ebag.mobile.bean.MyChildrenBean
import kotlinx.android.synthetic.main.fragment_main.*
import java.io.Serializable


class MainFragment : BaseFragment() {

    private var list: MutableList<Int> = mutableListOf()
    private val iconList: Array<Int> = arrayOf(R.drawable.icon_home_mistakes, R.drawable.icon_home_parental_incentives, R.drawable.icon_home_student_examination, R.drawable.icon_home_study_room, R.drawable.icon_home_my_student, R.drawable.icon_home_classroom_performance)
    private val dataList: MutableList<MainRVModel> = mutableListOf()
    private lateinit var mainRVAdapter: MainRVAdapter
    private val msgCountList: MutableList<Int> = mutableListOf()
    private val fragmentList: ArrayList<Fragment> = arrayListOf()
    private val titleList: ArrayList<String> = arrayListOf()
    private lateinit var homeworkData: ArrayList<OnePageInfoBean>
    private lateinit var mAdapter: MainAdapter
    var init = false
    private lateinit var request: RequestCallBack<List<OnePageInfoBean>>

    companion object {
        fun newInstance(): MainFragment {
            val fragment = MainFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun getLayoutRes(): Int = R.layout.fragment_main

    override fun getBundle(bundle: Bundle?) {

    }

    override fun initViews(rootView: View) {

        list.add(R.drawable.banner)
        banner.setImageLoader(GlideImageLoader())
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        banner.setBannerAnimation(Transformer.Default)
        banner.setDelayTime(3000)
        banner.setIndicatorGravity(BannerConfig.CENTER)
        banner.setImages(list)
        banner.start()

        val labelArray = activity.resources.getStringArray(R.array.main_rv_label)

        iconList.forEachIndexed { index, i ->
            dataList.add(MainRVModel(labelArray[index], i))
        }
        mAdapter = MainAdapter()
        mainRVAdapter = MainRVAdapter(dataList)
        main_rv.layoutManager = GridLayoutManager(activity, 3)
        main_rv.adapter = mainRVAdapter


        mainRVAdapter.setOnItemClickListener { adapter, view, position ->
            val id = SerializableUtils.getSerializable<MyChildrenBean>(ebag.mobile.base.Constants.CHILD_USER_ENTITY).classId
            when (position) {
                4 -> activity.startActivityForResult(Intent(activity, ChooseChildrenActivity::class.java).putExtra("flag", false), 998)
                1 -> ExcitationActivity.start(activity)
                5 -> PerformanceDialog(activity).show()
                3 -> if (id.isNotEmpty()) ZixiActivity.jump(activity) else T.show(activity,"暂未加入班级")
                2 -> if (id.isNotEmpty()) PaperActivity.start(activity, "4") else T.show(activity,"暂未加入班级")
                0 -> if (id.isNotEmpty()) ErrorBookActivity.start(activity) else T.show(activity,"暂未加入班级")
            }
        }

        viewpager.adapter = mAdapter
        tablayout.setViewPager(viewpager)

        viewpager.setOnTouchListener { _, event ->
            if (event!!.action == MotionEvent.ACTION_DOWN) {
                val data = homeworkData[tablayout.currentTab].homeWorkInfoVos
                if (data.size != 0) {
                    val i = Intent(activity, HomeworkListActivity::class.java)
                    i.putExtra("datas", data as Serializable)
                    i.putExtra("subject", homeworkData[tablayout.currentTab].subject)
                    startActivity(i)
                }
            }
            false
        }

        allhomework.setOnClickListener {
            PaperActivity.start(activity, "2")
        }

        init = true

        request = object : RequestCallBack<List<OnePageInfoBean>>() {

            override fun onStart() {

            }

            override fun onSuccess(entity: List<OnePageInfoBean>?) {
                val flist: ArrayList<Fragment> = arrayListOf()
                if (entity!!.isEmpty()) {
                    homework_loading.text = "没有对应科目"
                    homework_loading.visibility = View.VISIBLE
                } else {
                    homework_loading.visibility = View.GONE
                    titleList.clear()
                    msgCountList.clear()
                    homeworkData = entity!! as ArrayList<OnePageInfoBean>

                    entity.forEachIndexed { index, it ->
                        titleList.add(it.subject)
                        msgCountList.add(it.homeWorkNoCompleteCount)

                        if (it.homeWorkInfoVos.isNotEmpty()) {
                            val b = Bundle()
                            b.putString("status", it.homeWorkInfoVos[0].state)
                            b.putString("content", it.homeWorkInfoVos[0].content)
                            b.putString("endTime", it.homeWorkInfoVos[0].endTime)
                            val f = HomeworkFragment.newInstance()
                            f.arguments = b
                            flist.add(f)
                        } else {
                            val b = Bundle()
                            b.putBoolean("empty", true)
                            val f = HomeworkFragment.newInstance()
                            f.arguments = b
                            flist.add(f)
                        }

                    }

                    mAdapter.setFragments(flist)
                    tablayout.notifyDataSetChanged()


                    msgCountList.forEachIndexed { index, it ->
                        if (it != 0) {
                            tablayout.showMsg(index, it)
                        }
                    }

                }

            }

            override fun onError(exception: Throwable) {
                homework_loading.visibility = View.VISIBLE
                homework_loading.text = exception.message
            }

        }

        getOnePageInfo()


    }

    fun getOnePageInfo() {

        ParentsAPI.onePageInfo(SPUtils.get(activity, Constants.CURRENT_CHILDREN_YSBCODE, "") as String, request)

    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser && init) {
            getOnePageInfo()
        }
    }

    inner class MainAdapter : FragmentPagerAdapter(childFragmentManager) {

        override fun getItem(position: Int): Fragment = fragmentList[position]

        override fun getCount(): Int = fragmentList.size

        override fun getItemPosition(`object`: Any?): Int = PagerAdapter.POSITION_NONE

        override fun getPageTitle(position: Int): CharSequence {
            return titleList[position]
        }

        fun setFragments(mFragmentList: ArrayList<Fragment>) {

            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentList.forEach {
                fragmentTransaction.remove(it)
            }
            try {
                fragmentTransaction.commit()
                childFragmentManager.executePendingTransactions()
                fragmentList.clear()
                fragmentList.addAll(mFragmentList)
                notifyDataSetChanged()
            } catch (e: Exception) {
            }
        }


    }

    override fun onDestroy() {
        request.cancelRequest()
        super.onDestroy()
    }
}