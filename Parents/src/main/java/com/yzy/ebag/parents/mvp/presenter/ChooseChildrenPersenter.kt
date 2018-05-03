package com.yzy.ebag.parents.mvp.presenter

import android.content.Context
import com.yzy.ebag.parents.http.ParentsAPI
import com.yzy.ebag.parents.mvp.ChooseChildrenContract
import ebag.core.http.network.RequestCallBack
import ebag.mobile.bean.MyChildrenBean

class ChooseChildrenPersenter(context: Context, private val view: ChooseChildrenContract.ChooseChildrenView) : ChooseChildrenContract.Presenter {

    override fun request() {
        ParentsAPI.searchMyChildren(object : RequestCallBack<List<MyChildrenBean>>() {

            override fun onStart() {
                view.showLoading()
            }

            override fun onSuccess(entity: List<MyChildrenBean>?) {
                view.showContents(entity!!)
            }

            override fun onError(exception: Throwable) {
                view.showError(exception)
            }

        })
    }

    override fun refresh() {
        ParentsAPI.searchMyChildren(object : RequestCallBack<List<MyChildrenBean>>() {

            override fun onStart() {

            }

            override fun onSuccess(entity: List<MyChildrenBean>?) {
                view.showMoreComplete(entity!!)
            }

            override fun onError(exception: Throwable) {
                view.showError(exception)
            }

        })
    }

    override fun start() {
        request()
    }

}