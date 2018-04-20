package com.yzy.ebag.parents.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.flyco.tablayout.listener.CustomTabEntity
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.bean.OnePageInfoBean
import com.yzy.ebag.parents.common.Constants
import com.yzy.ebag.parents.http.ParentsAPI
import com.yzy.ebag.parents.model.MainRVModel
import com.yzy.ebag.parents.model.TabEntity
import com.yzy.ebag.parents.ui.activity.ChooseChildrenActivity
import com.yzy.ebag.parents.ui.adapter.MainRVAdapter
import com.yzy.ebag.parents.utils.GlideImageLoader
import ebag.core.base.BaseFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.util.SPUtils
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : BaseFragment() {

    private var list: MutableList<Int> = mutableListOf()
    private val iconList: Array<Int> = arrayOf(R.drawable.icon_home_mistakes, R.drawable.icon_home_parental_incentives, R.drawable.icon_home_student_examination, R.drawable.icon_home_study_room, R.drawable.icon_home_my_student, R.drawable.icon_home_classroom_performance)
    private val dataList: MutableList<MainRVModel> = mutableListOf()
    private lateinit var mainRVAdapter: MainRVAdapter
    private val entityList: ArrayList<CustomTabEntity> = arrayListOf()
    private val msgCountList: MutableList<Int> = mutableListOf()
    private val fragmentList: ArrayList<Fragment> = arrayListOf()

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

        mainRVAdapter = MainRVAdapter(dataList)
        main_rv.layoutManager = GridLayoutManager(activity, 3)
        main_rv.adapter = mainRVAdapter

        mainRVAdapter.setOnItemClickListener { adapter, view, position ->

            when (position) {
                4 -> ChooseChildrenActivity.start(activity)
            }
        }
        getOnePageInfo()

    }

    private fun getOnePageInfo() {

        ParentsAPI.onePageInfo(SPUtils.get(activity, Constants.CURRENT_CHILDREN_YSBCODE, "") as String, object : RequestCallBack<List<OnePageInfoBean>>() {

            override fun onStart() {
                super.onStart()
                homework_loading.visibility = View.VISIBLE
            }

            override fun onSuccess(entity: List<OnePageInfoBean>?) {
                entityList.clear()
                fragmentList.clear()
                entity?.forEachIndexed { index, it ->
                    entityList.add(TabEntity(it.subject, 0, 0))
                    if (it.homeWorkNoCompleteCount != 0) {
                        msgCountList.add(index, it.homeWorkNoCompleteCount)
                    }

                    it.homeWorkInfoVos.forEach {
                        val b = Bundle()
                        b.putString("status", it.state)
                        b.putString("content", it.content)
                        b.putString("endTime", it.endTime)
                        val f = HomeworkFragment.newInstance()
                        f.arguments = b
                        fragmentList.add(f)
                    }

                }

                tablayout.setTabData(entityList, activity, R.id.container_layout, fragmentList)



                msgCountList.forEachIndexed { index, it ->
                    if (it != 0) {
                        tablayout.showMsg(index, it)
                    }
                }

                homework_loading.visibility = View.GONE
            }

            override fun onError(exception: Throwable) {
            }

        })
    }


}