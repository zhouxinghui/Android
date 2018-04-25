package com.yzy.ebag.parents.mvp


interface BaseView {

    fun showLoading()

    fun <T> showContent(data: T?): Unit = TODO("no impl")

    fun showError(e: Throwable?): Unit = TODO("no impl")

    fun showEmpty()


    interface BaseListView : BaseView {

        fun <T> showContents(data: List<T>)

        fun <T> showMoreComplete(data: List<T>)

        fun loadmoreEnd()

        fun loadmoreFail()
    }

}