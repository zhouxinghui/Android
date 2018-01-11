package com.yzy.ebag.teacher.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseFragment
import ebag.core.util.loadImageToCircle
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * Created by YZY on 2017/12/21.
 */
class FragmentMine : BaseFragment() {
    companion object {
        fun newInstance() : Fragment {
            val fragment = FragmentMine()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun getLayoutRes(): Int {
        return R.layout.fragment_mine
    }

    override fun getBundle(bundle: Bundle) {

    }

    override fun initViews(rootView: View) {
        headImg.loadImageToCircle("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1515596866962&di=e76fb5e0914eee393affee2451f04aa3&imgtype=0&src=http%3A%2F%2Fimg.taopic.com%2Fuploads%2Fallimg%2F120727%2F201995-120HG1030762.jpg")
        subjectTv.text = "英"
        name.text = "李老师"
        bagNumber.text = String.format(resources.getString(R.string.bag_number), "1000734")
    }


}