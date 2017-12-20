package com.yzy.ebag.student

import android.os.Environment
import ebag.core.base.BaseActivity
import ebag.core.util.PatchUtils
import kotlinx.android.synthetic.main.activity_test.*
import java.io.File

/**
 * Created by unicho on 2017/12/19.
 */
class TestActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_test
    }

    override fun initViews() {
        install.setOnClickListener {  }

        patch.setOnClickListener {
            val root = Environment.getExternalStorageDirectory().absolutePath

            val file = File(root+File.separator+"test")

            if(!file.exists())
                file.mkdirs()

            PatchUtils.patch(file.absolutePath+File.separator+"app_old1.apk",
                    file.absolutePath+File.separator+"app_new.apk",
                    file.absolutePath+File.separator+"app_patch1.patch")

            PatchUtils.install(this,file.absolutePath+File.separator+"app_new.apk")
        }
    }
}