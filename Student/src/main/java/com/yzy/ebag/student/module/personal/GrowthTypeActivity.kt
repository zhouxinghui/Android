package com.yzy.ebag.student.module.personal

import android.content.Context
import android.content.Intent
import com.yzy.ebag.student.R
import ebag.core.base.BaseActivity
import ebag.mobile.module.clazz.AchievementActivity
import kotlinx.android.synthetic.main.activity_growth_type.*

/**
 * 成长轨迹  的分类
 * @author caoyu
 * @date 2018/1/28
 * @description
 */
class GrowthTypeActivity: BaseActivity(){

    companion object {
        fun jump(context: Context, gradeId: String,gradeCode: String){
            context.startActivity(
                    Intent(context,GrowthTypeActivity::class.java)
                            .putExtra("gradeId",gradeId)
                            .putExtra("gradeCode",gradeCode)
            )
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_growth_type
    }

    private lateinit var gradeId: String
    private lateinit var gradeCode: String
    override fun initViews() {
        gradeId = intent.getStringExtra("gradeId") ?: ""
        gradeCode = intent.getStringExtra("gradeCode") ?: ""
        resultBtn.setOnClickListener {
            AchievementActivity.jump(this, gradeCode)
        }

        courseBtn.setOnClickListener {
            CourseDetailActivity.jump(this, gradeId,gradeCode)
        }

        momentBtn.setOnClickListener {
            DiaryListActivity.jump(this,gradeId,gradeCode,"2")
        }

        experienceBtn.setOnClickListener {
            DiaryListActivity.jump(this,gradeId,gradeCode,"3")
        }

        diaryBtn.setOnClickListener {
           DiaryListActivity.jump(this,gradeId,gradeCode,"4")
        }
    }

}