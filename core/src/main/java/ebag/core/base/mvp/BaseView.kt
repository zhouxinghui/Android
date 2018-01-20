package ebag.core.base.mvp

/**
 * Created by caoyu on 2017/11/1.
 */
interface BaseView<P> {

    //这个可以在Activity中包裹Fragment的时候应用，这时候继承MVPBaseActivity
    //Activity中初始化Presenter的实例 ，然后通过view调用该方法将Presenter塞给Fragment
    fun setPresenter(presenter: P)
}