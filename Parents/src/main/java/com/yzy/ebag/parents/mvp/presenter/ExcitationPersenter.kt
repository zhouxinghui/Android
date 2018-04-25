package com.yzy.ebag.parents.mvp.presenter

import com.yzy.ebag.parents.bean.ExcitationWorkBean
import com.yzy.ebag.parents.http.ParentsAPI
import com.yzy.ebag.parents.mvp.ExcitationJobContract
import ebag.core.http.network.RequestCallBack

class ExcitationPersenter(private val view: ExcitationJobContract.ExcitationJobView) : ExcitationJobContract.Persenter {


    override fun requestTask(page: String, uid: String) {
        ParentsAPI.queryTask(uid, page, object : RequestCallBack<List<ExcitationWorkBean>>() {

            override fun onStart() {
                if (page.toInt() <= 1)
                    view.showLoading()
            }

            override fun onSuccess(entity: List<ExcitationWorkBean>?) {
                if (entity!!.isEmpty()) {
                    if (page.toInt() <= 1)
                        view.showEmpty()
                } else {
                    if (page.toInt() > 1) {
                        view.showMoreComplete(entity)
                    } else {
                        view.showContents(entity)
                    }
                }

                if (entity.size < 10) {
                    view.loadmoreEnd()
                }
            }

            override fun onError(exception: Throwable) {
                if (page.toInt() > 1) {
                    view.loadmoreFail()
                } else {
                    view.showError(exception)
                }
            }

        })
    }

    override fun requestHomeWork() {

    }


    override fun finish(id: String, position: Int) {
        ParentsAPI.updateTask(id, object : RequestCallBack<String>() {
            override fun onSuccess(entity: String?) {
                view.notifyBtn(position)
            }

            override fun onError(exception: Throwable) {
                view.finishError(exception)
            }

        })

    }

    override fun start() {

    }

}