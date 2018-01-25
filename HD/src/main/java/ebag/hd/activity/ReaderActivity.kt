package ebag.hd.activity

import android.content.Context
import android.content.Intent
import ebag.core.base.BaseActivity
import ebag.core.util.ZipUtils
import ebag.hd.R
import kotlinx.android.synthetic.main.activity_reader.*

class ReaderActivity : BaseActivity() {
    companion object {
        fun jump(context: Context, fileName: String){
            context.startActivity(Intent(context, ReaderActivity::class.java).putExtra("fileName", fileName))
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_reader
    }

    override fun initViews() {
        pageView.setAdapter(PageViewAdapter(this, ZipUtils.getAllImgs(intent.getStringExtra("fileName"))))
    }

}
