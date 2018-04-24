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

interface ExcitationJobContract{

    interface ExcitationJobView:BaseView.BaseListView

    interface Persenter:BasePresenter<ExcitationJobView>{

        fun request()

        fun finish()
    }
}