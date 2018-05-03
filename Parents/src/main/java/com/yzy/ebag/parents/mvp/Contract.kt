package com.yzy.ebag.parents.mvp

import com.yzy.ebag.parents.mvp.presenter.BasePresenter

interface ChooseChildrenContract {
    interface ChooseChildrenView : BaseView.BaseListView

    interface Presenter : BasePresenter<ChooseChildrenView> {
        fun request()
        fun refresh()
    }
}

interface HomeworkReportContract {
    interface HomeworkReportView : BaseView

    interface Presenter : BasePresenter<HomeworkReportView> {

        fun request(homeworkId: String, uid: String)
    }
}

interface ExcitationJobContract {

    interface ExcitationJobView : BaseView.BaseListView {

        fun notifyBtn(position: Int)

        fun finishError(e: Throwable?): Unit = TODO("no impl")
    }

    interface Persenter : BasePresenter<ExcitationJobView> {

        fun requestTask(page: String, uid: String)

        fun requestHomeWork()

        fun finish(id: String, position: Int)
    }
}


interface CreateTaskContract {

    interface CreateTaskView : BaseView.BaseListView {

        fun createSuccess()

        fun createFailed(e: Throwable?)
    }

    interface Presenter : BasePresenter<CreateTaskView> {

        fun queryChild()

        fun createTask(title: String, content: String, uid: String)
    }
}

interface ClazzMainContract {

    interface ClazzMainView : BaseView

    interface Presenter : BasePresenter<ClazzMainView> {

        fun queryClazzNews(id: String)

    }
}

interface NoticeListContract {

    interface NoticeListView : BaseView.BaseListView

    interface Presenter : BasePresenter<NoticeListView> {

        fun firstList(clazzId: String)

        fun loadMore()

    }
}