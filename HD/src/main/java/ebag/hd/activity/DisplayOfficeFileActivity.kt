package ebag.hd.activity

import android.content.Context
import android.content.Intent
import ebag.core.base.BaseActivity
import ebag.hd.R
import kotlinx.android.synthetic.main.activity_display_office_file.*
import java.io.File

/**
 * Created by YZY on 2018/3/2.
 */
class DisplayOfficeFileActivity : BaseActivity() {
    companion object {
        fun jump(context: Context, filePath: String) {
            context.startActivity(
                    Intent(context, DisplayOfficeFileActivity::class.java)
                            .putExtra("filePath", filePath)
            )
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_display_office_file
    }

    override fun initViews() {
        val filePath = intent.getStringExtra("filePath")
        superFileView.displayFile(File(filePath))
        superFileView.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (superFileView != null) {
            superFileView.onStopDisplay()
        }
    }
}