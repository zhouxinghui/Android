package ebag.mobile.module.account


interface YBCenterContract{

    interface YBCenterView{

        fun <T> showContents(data: T)

        fun showLoading()

        fun <T> showLoadMoreComplete(data: T)

        fun loadmoreFail()

        fun loadmoreEnd()

        fun showEmpty()

        fun showError(e:Throwable?)

        fun <T> showData(data:T)
    }

    interface Persenter<in V>{

        fun startFirstpage(v:V)

        fun loadmore(v:V)

    }
}