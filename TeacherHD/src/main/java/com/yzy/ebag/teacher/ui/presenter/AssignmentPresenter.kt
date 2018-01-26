package com.yzy.ebag.teacher.ui.presenter

import com.yzy.ebag.teacher.bean.AssignmentBean
import com.yzy.ebag.teacher.http.TeacherApi
import com.yzy.ebag.teacher.ui.view.AssignmentView
import ebag.core.base.mvp.BasePresenter
import ebag.core.base.mvp.OnToastListener
import ebag.core.http.network.RequestCallBack

/**
 * Created by YZY on 2018/1/8.
 */
class AssignmentPresenter(view: AssignmentView, listener: OnToastListener): BasePresenter<AssignmentView>(view, listener) {
    private var baseRequest = createRequest(object : RequestCallBack<AssignmentBean>(){
        override fun onStart() {
            getView()?.loadBaseStart()
        }
        override fun onSuccess(entity: AssignmentBean?) {
            getView()?.showBaseData(entity)
        }

        override fun onError(exception: Throwable) {
            getView()?.loadBaseError(exception)
        }
    })
    private val unitRequest by lazy {
        createRequest(object : RequestCallBack<AssignmentBean>(){
            override fun onStart() {
                getView()?.loadUnitAndQuestionStart()
            }

            override fun onSuccess(entity: AssignmentBean?) {
                getView()?.getUnitAndQuestion(entity)
            }

            override fun onError(exception: Throwable) {
                getView()?.loadUnitAndQuestionError(exception)
            }
        })
    }
    fun loadBaseData(type: String){
        TeacherApi.assignmentData(type, baseRequest)
    }
    fun loadUnitAndQuestion(type: String, gradeCode: String, bookVersionId: String? = null){
        TeacherApi.unitAndQuestion(type, gradeCode, bookVersionId, unitRequest)
    }
}