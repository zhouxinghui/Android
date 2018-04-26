package com.yzy.ebag.parents.mvp.presenter

import com.yzy.ebag.parents.http.ParentsAPI
import com.yzy.ebag.parents.mvp.ClazzMainContract
import ebag.core.http.network.RequestCallBack
import ebag.mobile.bean.NoticeBean

class ClazzMainPresenter(private val view: ClazzMainContract.ClazzMainView):ClazzMainContract.Presenter{

    override fun queryClazzNews(id: String) {

        ParentsAPI.newestNotice(id,object:RequestCallBack<NoticeBean>(){

            override fun onStart() {
                view.showLoading()
            }

            override fun onSuccess(entity: NoticeBean?) {
                view.showContent(entity)
            }

            override fun onError(exception: Throwable) {
                view.showError(exception)
            }

        })
    }

    override fun start() {

    }

}