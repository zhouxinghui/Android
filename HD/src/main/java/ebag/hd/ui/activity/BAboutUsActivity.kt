package ebag.hd.ui.activity

import ebag.core.base.BaseActivity
import ebag.hd.R
import kotlinx.android.synthetic.main.activity_about_us.*

/**
 * Created by YZY on 2018/1/12.
 */
abstract class BAboutUsActivity: BaseActivity(){
    override fun getLayoutId(): Int {
        return R.layout.activity_about_us
    }

    override fun initViews() {
        ivLogo.setImageResource(getLogo())
        tvSub.text = resources.getString(R.string.app_name)
    }

    abstract fun getLogo(): Int

}