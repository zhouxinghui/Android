package com.yzy.ebag.parents.fragment

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.view.ViewGroup
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.adapter.MainRVAdapter
import com.yzy.ebag.parents.model.MainRVModel
import com.yzy.ebag.parents.utils.GlideImageLoader
import ebag.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : BaseFragment() {

    private var list: MutableList<Int> = mutableListOf()
    private val iconList: Array<Int> = arrayOf(R.drawable.icon_home_mistakes, R.drawable.icon_home_parental_incentives, R.drawable.icon_home_student_examination, R.drawable.icon_home_study_room, R.drawable.icon_home_my_student, R.drawable.icon_home_classroom_performance)
    private val dataList: MutableList<MainRVModel> = mutableListOf()
    private lateinit var mainRVAdapter: MainRVAdapter
    private val tabTitle: Array<String> = arrayOf("语文", "数学", "英语", "历史")

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

        viewpager.offscreenPageLimit = 3
        viewpager.adapter = HomeAdapter()
        tablayout.setupWithViewPager(viewpager)

        val labelArray = activity.resources.getStringArray(R.array.main_rv_label)

        iconList.forEachIndexed { index, i ->
            dataList.add(MainRVModel(labelArray[index], i))
        }

        mainRVAdapter = MainRVAdapter(dataList)
        main_rv.layoutManager = GridLayoutManager(activity, 3)
        main_rv.adapter = mainRVAdapter

    }


    inner class HomeAdapter : PagerAdapter() {
        override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
            return view == `object`
        }

        override fun getCount(): Int = 4

        override fun instantiateItem(container: ViewGroup?, position: Int): Any {

            val view = View.inflate(activity,R.layout.item_homeworkcontent,null)
            container!!.addView(view)

            return view
        }

        override fun getPageTitle(position: Int): CharSequence {
            return tabTitle[position]
        }


    }


}