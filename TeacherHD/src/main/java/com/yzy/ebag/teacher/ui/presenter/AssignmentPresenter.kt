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
    private var baseRequest: RequestCallBack<AssignmentBean>? = null
    fun loadBaseData(type: String, subCode:String){
        if (baseRequest == null){
            baseRequest = createRequest(object : RequestCallBack<AssignmentBean>(){
                override fun onStart() {
                    getView()!!.loadStart()
                }
                override fun onSuccess(entity: AssignmentBean) {
                    getView()!!.showBaseData(entity)
                }

                override fun onError(exception: Throwable) {
                    getView()!!.loadError(exception)
                }
            })
        }
        TeacherApi.assignmentData(type, subCode, baseRequest!!)
    }
}