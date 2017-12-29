package com.yzy.ebag.student

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_demo.*

class DemoActivity : AppCompatActivity() {
    var answer1 = -1
    var answer2 = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        tv_a.setOnClickListener {
            answer1 = 0
            if(answer2 != -1) {
                myView.setLine(answer1, answer2)
                answer1 = -1
                answer2 = -1
            }
        }
        tv_b.setOnClickListener {
            answer1 = 2
            if(answer2 != -1) {
                myView.setLine(answer1, answer2)
                answer1 = -1
                answer2 = -1
            }
        }
        tv_c.setOnClickListener {
            answer2 = 1
            if(answer1 != -1) {
                myView.setLine(answer1, answer2)
                answer1 = -1
                answer2 = -1
            }
        }
        tv_d.setOnClickListener {
            answer2 = 3
            if(answer1 != -1) {
                myView.setLine(answer1, answer2)
                answer1 = -1
                answer2 = -1
            }
        }
    }
}
