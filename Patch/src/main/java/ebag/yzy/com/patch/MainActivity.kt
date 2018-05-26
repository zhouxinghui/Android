package ebag.yzy.com.patch

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import ebag.core.util.T
import ha.excited.BigNews
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val path = Environment.getExternalStorageDirectory().path
        val newApkPath = path + File.separator + "new.apk"
        val oldApkPath = path + File.separator + "old.apk"
        val patchPath = path + File.separator + "patch"
        patchBtn.setOnClickListener {
            val flag = BigNews.diff(oldApkPath, newApkPath, patchPath)
            if (flag)
                T.show(this, "拆分包打包成功")
            else
                T.show(this, "拆分包打包失败")
        }

        mergeBtn.setOnClickListener {
            val flag = BigNews.make(oldApkPath, newApkPath, patchPath)
            if (flag)
                T.show(this, "合并成功")
            else
                T.show(this, "合并失败")
        }
    }

    private fun getApkPath(context: Context): String? {
        var apkPath: String? = null
        try {
            val applicationInfo = context.applicationInfo ?: return null
            apkPath = applicationInfo.sourceDir
        } catch (e: Throwable) {
        }

        return apkPath
    }
}
