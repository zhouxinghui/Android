package com.yzy.ebag.student.activity.main

import com.yzy.ebag.student.bean.response.ClassesInfoBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.mvp.BasePresenter
import ebag.core.base.mvp.OnToastListener
import ebag.core.http.network.RequestCallBack

/**
 * Created by unicho on 2018/1/12.
 */
class MainPresenter(view: MainView, listener: OnToastListener): BasePresenter<MainView>(view,listener) {

    private val mainInfoRequest = createRequest(object: RequestCallBack<ClassesInfoBean>(){
        override fun onStart() {
            getView()?.mainInfoStart()
        }
        override fun onSuccess(entity: ClassesInfoBean?) {
            if (entity != null){
                getView()?.mainInfoSuccess(entity)
            }
        }

        override fun onError(exception: Throwable) {
            getView()?.mainInfoError(exception)
        }

    })

    fun mianInfo(){
        StudentApi.mainInfo(mainInfoRequest)
    }

}