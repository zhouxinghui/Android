package com.yzy.ebag.parents.mvp.presenter

import com.yzy.ebag.parents.http.ParentsAPI
import com.yzy.ebag.parents.mvp.CreateTaskContract
import ebag.core.http.network.RequestCallBack
import ebag.mobile.bean.MyChildrenBean

class CreateTaskPersenter(private val view: CreateTaskContract.CreateTaskView) : CreateTaskContract.Presenter {


    override fun queryChild() {
        ParentsAPI.searchMyChildren(object : RequestCallBack<List<MyChildrenBean>>() {

            override fun onSuccess(entity: List<MyChildrenBean>?) {

                view.showContents(entity!!)
            }

            override fun onError(exception: Throwable) {
                view.showError(exception)
            }

        })
    }


    override fun createTask(title: String, content: String, uid: String) {

        ParentsAPI.createTask(title, content, uid, object : RequestCallBack<String>() {

            override fun onSuccess(entity: String?) {
                view.createSuccess()
            }

            override fun onError(exception: Throwable) {
                view.createFailed(exception)
            }

        })

    }

    override fun start() {
        queryChild()
    }

}