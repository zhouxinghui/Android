package ebag.hd.ui.view

/**
 * Created by caoyu on 2017/11/11.
 */
interface ForgetView {
    fun onForgetStart()

    fun onForgetSuccess()

    fun onForgetError(t: Throwable)
}