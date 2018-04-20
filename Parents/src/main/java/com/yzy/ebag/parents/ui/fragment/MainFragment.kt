package com.yzy.ebag.parents.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.flyco.tablayout.listener.CustomTabEntity
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.model.MainRVModel
import com.yzy.ebag.parents.model.TabEntity
import com.yzy.ebag.parents.ui.activity.ChooseChildrenActivity
import com.yzy.ebag.parents.ui.adapter.MainRVAdapter
import com.yzy.ebag.parents.utils.GlideImageLoader
import ebag.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : BaseFragment() {

    private var list: MutableList<Int> = mutableListOf()
    private val iconList: Array<Int> = arrayOf(R.drawable.icon_home_mistakes, R.drawable.icon_home_parental_incentives, R.drawable.icon_home_student_examination, R.drawable.icon_home_study_room, R.drawable.icon_home_my_student, R.drawable.icon_home_classroom_performance)
    private val dataList: MutableList<MainRVModel> = mutableListOf()
    private lateinit var mainRVAdapter: MainRVAdapter
    private val tabTitle: Array<String> = arrayOf("语文", "数学", "英语", "历史")
    private val entityList: ArrayList<CustomTabEntity> = arrayListOf()
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

        tabTitle.forEach {
            entityList.add(TabEntity(it, 0, 0))
            fragmentList.add(HomeworkFragment.newInstance())
        }
        tablayout.setTabData(entityList, activity, R.id.container_layout, fragmentList)
        tablayout.showMsg(1, 3)
        tablayout.showMsg(2, 100)

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

    }


}