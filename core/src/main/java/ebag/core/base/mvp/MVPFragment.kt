package ebag.core.base.mvp

import android.os.Bundle
import ebag.core.base.BaseFragment
import ebag.core.util.T

/**
 * Created by unicho on 2017/11/1.
 */
abstract class MVPFragment: BaseFragment(), OnToastListener {



    override fun onCreate(savedInstanceState: Bundle?) {
        createPresenter()//创建Presenter
        super.onCreate(savedInstanceState)
    }

    protected abstract fun createPresenter()

    /**
     * 释放Presenter资源
     */
    protected abstract fun destroyPresenter()

    override fun onDestroy() {
        destroyPresenter()
        super.onDestroy()
    }

    override fun toast(msg: String, isShort: Boolean) {
        T.show(mContext, msg, isShort)
    }

    override fun toast(StringId: Int, isShort: Boolean) {
        T.show(mContext, StringId, isShort)
    }
}