package ebag.core.base.mvp

import ebag.core.base.BaseActivity
import ebag.core.util.T

/**
 * Created by caoyu on 2017/11/1.
 */
abstract class MVPActivity : BaseActivity(), OnToastListener {
    protected abstract fun destroyPresenter()

    override fun onDestroy() {
        destroyPresenter()
        super.onDestroy()
    }

    override fun toast(msg: String) {
        toast(msg, true)
    }

    override fun toast(msg: String, isShort: Boolean) {
        T.show(this, msg, isShort)
    }

    override fun toast(StringId: Int, isShort: Boolean) {
        T.show(this, StringId, isShort)
    }

}
