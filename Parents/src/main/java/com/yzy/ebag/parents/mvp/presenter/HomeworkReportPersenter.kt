package com.yzy.ebag.parents.mvp.presenter

import com.yzy.ebag.parents.bean.HomeworkAbstractBean
import com.yzy.ebag.parents.http.ParentsAPI
import com.yzy.ebag.parents.mvp.HomeworkReportContract
import ebag.core.http.network.RequestCallBack

class HomeworkReportPersenter(private val view:HomeworkReportContract.HomeworkReportView):HomeworkReportContract.Presenter {

    override fun request(homeworkId: String, uid: String) {
        ParentsAPI.getHomeworkReport(homeworkId,uid,object:RequestCallBack<HomeworkAbstractBean>(){
            override fun onStart() {
                view.showLoading()
            }


            override fun onSuccess(entity: HomeworkAbstractBean?) {
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