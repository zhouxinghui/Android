package com.yzy.ebag.student

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_demo.*

class DemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        save.setOnClickListener {
            image.setImageBitmap(drawView.bitmap)
        }

        clear.setOnClickListener {
            drawView.clearPaint()
        }
    }
}
