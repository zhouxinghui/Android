package ebag.core.base.mvp

/**
 * 在 Presenter 中的關於Toast的回掉
 * Created by caoyu on 2017/11/1.
 */
interface OnToastListener {
    fun toast(msg: String)

    fun toast(msg: String, isShort: Boolean)

    fun toast(StringId: Int, isShort: Boolean)
}