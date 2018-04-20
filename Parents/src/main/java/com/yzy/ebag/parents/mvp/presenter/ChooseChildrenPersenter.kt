package com.yzy.ebag.parents.mvp.presenter

import android.content.Context
import com.yzy.ebag.parents.bean.MyChildrenBean
import com.yzy.ebag.parents.http.ParentsAPI
import com.yzy.ebag.parents.mvp.ChooseChildrenContract
import ebag.core.http.network.RequestCallBack

class ChooseChildrenPersenter(context: Context, private val view: ChooseChildrenContract.ChooseChildrenView) : ChooseChildrenContract.Presenter {

    override fun request() {
        ParentsAPI.searchMyChildren(object : RequestCallBack<List<MyChildrenBean>>() {

            override fun onStart() {
                view.showLoading()
            }

            override fun onSuccess(entity: List<MyChildrenBean>?) {
                view.showContent(entity)
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