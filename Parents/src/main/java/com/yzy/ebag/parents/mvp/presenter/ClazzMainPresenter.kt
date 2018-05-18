package com.yzy.ebag.parents.mvp.presenter

import com.yzy.ebag.parents.http.ParentsAPI
import com.yzy.ebag.parents.mvp.ClazzMainContract
import ebag.core.http.network.RequestCallBack
import ebag.mobile.bean.MyChildrenBean
import ebag.mobile.bean.NoticeBean

class ClazzMainPresenter(private val view: ClazzMainContract.ClazzMainView) : ClazzMainContract.Presenter {

    private lateinit var request: RequestCallBack<NoticeBean>
    private lateinit var requestUpdate: RequestCallBack<MyChildrenBean>

    override fun queryClazzNews(id: String) {

        request = object : RequestCallBack<NoticeBean>() {

            override fun onStart() {
                view.showLoading()
            }

            override fun onSuccess(entity: NoticeBean?) {
                view.showContent(entity)
            }

            override fun onError(exception: Throwable) {
                view.showError(exception)
            }

        }

        ParentsAPI.newestNotice(id, request)
    }

    override fun queryChildUpdate(id: String) {

        requestUpdate = object : RequestCallBack<MyChildrenBean>() {

            override fun onSuccess(entity: MyChildrenBean?) {
                if (!entity?.classId.isNullOrEmpty())
                    view.updateChilden(entity?.classId, entity?.className)
            }

            override fun onError(exception: Throwable) {

            }


        }

        ParentsAPI.queryUserDetail(id, requestUpdate)
    }


    override fun start() {

    }

    fun destroyRequest() {
        if (::request.isInitialized)
            request.cancelRequest()
        if (::requestUpdate.isInitialized) {
            requestUpdate.cancelRequest()
        }
    }

}