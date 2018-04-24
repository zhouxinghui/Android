package com.yzy.ebag.parents.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.mvp.ExcitationJobContract
import com.yzy.ebag.parents.ui.adapter.ExcitationAdapter
import ebag.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_excitation.*

@SuppressLint("ValidFragment")
class ExcitationJobFragment(private val type:String) : BaseFragment(), ExcitationJobContract.ExcitationJobView {


    override fun getLayoutRes(): Int = R.layout.fragment_excitation

    override fun getBundle(bundle: Bundle?) {
    }

    override fun initViews(rootView: View) {

        if (type == "1"){
            imageview.setImageResource(R.drawable.img_excitation_study)
        }

        recyclerview.layoutManager = LinearLayoutManager(activity)
        recyclerview.adapter = ExcitationAdapter(arrayListOf("hahah","haha","gha"))

    }

    override fun showLoading() {
    }

    override fun showEmpty() {
    }

    override fun <T> showContents(data: List<T>) {
    }

    override fun showMoreComplete() {
    }

    override fun loadmoreEnd() {
    }

    override fun loadmoreFail() {

    }

    override fun <T> showContent(data: T?) {
        super.showContent(data)
    }

    override fun showError(e: Throwable?) {
        super.showError(e)
    }


}