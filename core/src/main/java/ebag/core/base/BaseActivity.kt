package ebag.core.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ebag.core.util.AppManager

/**
 * Created by unicho on 2017/10/31.
 */
abstract class BaseActivity : AppCompatActivity() {

    private var isDestroy : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawable(null)
        isDestroy = true
        AppManager.addActivity(this)
        setContentView(getLayoutId())
        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        isDestroy = true
        AppManager.removeActivity(this)
    }

    protected abstract fun getLayoutId():Int

    protected abstract fun initViews()

}