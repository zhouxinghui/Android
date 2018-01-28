package com.yzy.ebag.student.activity.growth

import android.content.Context
import android.content.Intent
import com.yzy.ebag.student.R
import ebag.core.base.BaseActivity
import kotlinx.android.synthetic.main.activity_growth_type.*

/**
 * @author caoyu
 * @date 2018/1/28
 * @description
 */
class GrowthTypeActivity: BaseActivity(){

    companion object {
        fun jump(context: Context, gradeId: String){
            context.startActivity(
                    Intent(context,GrowthTypeActivity::class.java)
                            .putExtra("gradeId",gradeId)
            )
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_growth_type
    }

    private lateinit var gradeId: String
    override fun initViews() {
        gradeId = intent.getStringExtra("gradeId") ?: ""
        resultBtn.setOnClickListener {

        }

        courseBtn.setOnClickListener {
            CourseDetailActivity.jump(this, gradeId)
        }

        momentBtn.setOnClickListener {

        }

        experienceBtn.setOnClickListener {

        }

        diaryBtn.setOnClickListener {

        }
    }

}