package ebag.core.base.mvp

import ebag.core.base.BaseActivity
import ebag.core.util.T

/**
 * Created by unicho on 2017/11/1.
 */
abstract class MVPActivity : BaseActivity(), OnToastListener {
    override fun onDestroy() {
        destroyPresenter()
        super.onDestroy()
    }

    protected abstract fun destroyPresenter()

    override fun toast(msg: String, isShort: Boolean) {
        T.show(this, msg, isShort)
    }

    override fun toast(StringId: Int, isShort: Boolean) {
        T.show(this, StringId, isShort)
    }

}
