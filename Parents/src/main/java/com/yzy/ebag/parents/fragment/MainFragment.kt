package com.yzy.ebag.parents.fragment

import android.os.Bundle
import android.view.View
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.utils.GlideImageLoader
import ebag.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : BaseFragment() {

    private var list: MutableList<Int> = mutableListOf()

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



    }

}