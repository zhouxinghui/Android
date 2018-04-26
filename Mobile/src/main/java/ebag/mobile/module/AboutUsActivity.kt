package ebag.mobile.module

import ebag.core.base.BaseActivity
import ebag.mobile.R
import kotlinx.android.synthetic.main.activity_about_us.*

/**
 * Created by YZY on 2018/4/26.
 */
class AboutUsActivity: BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_about_us
    }

    override fun initViews() {

       tvSub.text = resources.getString(packageManager.getPackageInfo(packageName,0).applicationInfo.labelRes)

    }
}