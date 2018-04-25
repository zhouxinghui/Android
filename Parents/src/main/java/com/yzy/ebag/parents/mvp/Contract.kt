package com.yzy.ebag.parents.mvp

import com.yzy.ebag.parents.mvp.presenter.BasePresenter

interface ChooseChildrenContract {
    interface ChooseChildrenView : BaseView.BaseListView

    interface Presenter : BasePresenter<ChooseChildrenView> {
        fun request()
    }
}

interface HomeworkReportContract {
    interface HomeworkReportView : BaseView

    interface Persenter : BasePresenter<HomeworkReportView> {

        fun request(homeworkId: String, uid: String)
    }
}

interface ExcitationJobContract {

    interface ExcitationJobView : BaseView.BaseListView {

        fun notifyBtn(position:Int)

        fun finishError(e: Throwable?): Unit = TODO("no impl")
    }

    interface Persenter : BasePresenter<ExcitationJobView> {

        fun requestTask(page: String, uid: String)

        fun requestHomeWork()

        fun finish(id: String,position:Int)
    }
}


interface CreateTaskContract{

    interface CreateTaskView:BaseView.BaseListView{

        fun createSuccess()

        fun createFailed(e: Throwable?)
    }

    interface Parsenter:BasePresenter<CreateTaskView>{

        fun queryChild()

        fun createTask(title:String,content:String,uid:String)
    }
}