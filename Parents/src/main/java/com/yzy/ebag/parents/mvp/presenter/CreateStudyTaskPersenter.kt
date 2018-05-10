package com.yzy.ebag.parents.mvp.presenter

import com.yzy.ebag.parents.bean.StudentSubjectBean
import com.yzy.ebag.parents.http.ParentsAPI
import com.yzy.ebag.parents.mvp.CreateStudyTaskContract
import ebag.core.http.network.RequestCallBack
import ebag.mobile.bean.MyChildrenBean


class CreateStudyTaskPersenter(private val view:CreateStudyTaskContract.CreateStudyTaskView):CreateStudyTaskContract.Persenter{

    override fun queryChild() {

        ParentsAPI.searchMyChildren(object : RequestCallBack<List<MyChildrenBean>>() {

            override fun onSuccess(entity: List<MyChildrenBean>?) {

                if (entity!!.isNotEmpty()) {
                    entity[0].isSelected = true
                    view.showContents(entity)
                }
            }

            override fun onError(exception: Throwable) {
                view.showError(exception)
            }

        })
    }

    override fun querySubject(uid: String) {
        ParentsAPI.getTaughtCourses(uid,object:RequestCallBack<List<StudentSubjectBean>>(){

            override fun onSuccess(entity: List<StudentSubjectBean>?) {

                if (entity!!.isNotEmpty()) {
                    view.showSubject(entity)
                }else{
                    view.showSubjectEmpty()
                }
            }

            override fun onError(exception: Throwable) {
                view.showError(exception)
            }

        })
    }

    override fun queryDetails(id: String) {

    }

    override fun start() {

    }

}