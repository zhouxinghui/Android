package ebag.hd.activity

import android.content.Context
import android.content.Intent
import android.os.Environment
import ebag.core.base.BaseActivity
import ebag.hd.R
import kotlinx.android.synthetic.main.activity_display_office_file.*
import java.io.File

/**
 * Created by YZY on 2018/3/2.
 */
class DisplayOfficeFileActivity : BaseActivity() {
    companion object {
        fun jump(context: Context) {
            context.startActivity(Intent(context, DisplayOfficeFileActivity::class.java))
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_display_office_file
    }

    override fun initViews() {
        superFileView.displayFile(File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "test.docx"))
        superFileView.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (superFileView != null) {
            superFileView.onStopDisplay()
        }
    }
}