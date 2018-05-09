package com.yzy.ebag.parents.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.ui.fragment.ExcitationJobFragment
import ebag.core.base.BaseActivity
import kotlinx.android.synthetic.main.activity_excitation.*

class ExcitationActivity : BaseActivity() {

    private val titleData: Array<String> = arrayOf("劳动任务", "学习任务")

    override fun getLayoutId(): Int = R.layout.activity_excitation
    private val list: ArrayList<Fragment> = arrayListOf(ExcitationJobFragment("0"), ExcitationJobFragment("1"))

    override fun initViews() {

        tablayout.setTabData(titleData, this, R.id.container_layout, list)

        add_job.setOnClickListener {

            if (tablayout.currentTab == 0) {
                CreateTaskActivity.start(this)
            }else{

            }


        }

        back_btn.setOnClickListener {
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 999) {
            (list[0] as ExcitationJobFragment).refresh()
        }
    }

    companion object {
        fun start(c: Context) {
            c.startActivity(Intent(c, ExcitationActivity::class.java))
        }
    }

}