package com.yzy.ebag.teacher.ui.activity

import android.content.Context
import android.content.Intent
import android.view.View
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseActivity
import kotlinx.android.synthetic.main.activity_space.*

class SpaceActivity : BaseActivity(), View.OnClickListener {
    override fun getLayoutId(): Int {
        return R.layout.activity_space
    }

    lateinit var classId: String
    companion object {
        fun jump(context: Context, classId: String, className: String){
            context.startActivity(
                    Intent(context, SpaceActivity::class.java)
                            .putExtra("classId", classId)
                            .putExtra("className", className)
            )
        }
    }

    override fun initViews() {
        teacherName.text = "英语老师：李老师"
        noticeTime.text = getString(R.string.notice_time, "2018-01-01 24:00")
        descTv.text = getString(R.string.notice_desc, "哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间哦啊积分计费按揭房阿萨德积分时间")

        classId = intent.getStringExtra("classId")
        val className = intent.getStringExtra("className")
        chat.text = className

        course.setOnClickListener(this)
        noticeHistoryBtn.setOnClickListener(this)
        studyGroup.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.course ->{
                startActivity(Intent(this, MyCourseActivity::class.java))
            }
            R.id.noticeHistoryBtn ->{
                startActivity(Intent(this, NoticeHistoryActivity::class.java))
            }
            R.id.studyGroup ->{
                StudyGroupActivity.jump(this, classId)
            }
        }
    }

}
