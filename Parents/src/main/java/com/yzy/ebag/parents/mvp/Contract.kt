package com.yzy.ebag.parents.mvp

import com.yzy.ebag.parents.mvp.presenter.BasePresenter

interface ChooseChildrenContract{
    interface ChooseChildrenView:BaseView.BaseListView

    interface Presenter:BasePresenter<ChooseChildrenView>{
        fun request()
    }
}