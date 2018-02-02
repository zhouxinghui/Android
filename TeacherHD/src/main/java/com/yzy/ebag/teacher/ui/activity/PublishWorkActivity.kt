package com.yzy.ebag.teacher.ui.activity

import android.app.Activity
import android.content.Intent
import android.view.View
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseActivity
import ebag.core.util.DateUtil
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.hd.widget.DatePickerDialog
import ebag.hd.widget.TitleBar
import kotlinx.android.synthetic.main.activity_publish_work.*
import java.util.*

class PublishWorkActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_publish_work
    }
    private val datePickerDialog by lazy {
        val dialog = DatePickerDialog(this)
        dialog.onConfirmClick = {
            dateTv.text = it
            deadTime = it + " 23:59"
        }
        dialog
    }
    private lateinit var deadTime: String
    companion object {
        fun jump(activity: Activity, isGroup: Boolean, isTest: Boolean){
            activity.startActivity(Intent(activity, PublishWorkActivity::class.java)
                    .putExtra("isGroup", isGroup)
                    .putExtra("isTest", isTest)
            )
        }
    }

    override fun initViews() {
        val isGroup = intent.getBooleanExtra("isGroup", false)
        val isTest = intent.getBooleanExtra("isTest", false)
        var isCustom = false
        publishTime.text = "布置时间：${DateUtil.getFormatDateTime(Date(System.currentTimeMillis()), "yyyy-M-d")}"
        dateTv.text = DateUtil.getFormatDateTime(Date(System.currentTimeMillis()), "yyyy-M-d")
        if (isGroup){
            titleBar.setTitle("发布小组")
            publishPerson.text = "布置小组：点击选择小组"
        }else{
            titleBar.setTitle("发布班级")
            publishPerson.text = "布置班级："
        }
        if(isTest){
            deadTimeTv.text = "考试时间："
            deadTime = "45"
            time_a.text = "45分钟"
            time_b.text = "90分钟"
            time_c.text = "120分钟"
        }else{
            deadTimeTv.text = "截止时间："
            deadTime = DateUtil.getFormatDateAdd(Date(System.currentTimeMillis()), 0, "yyyy-M-d") + " 23:59"
            time_a.text = "今天内"
            time_b.text = "明天内"
            time_c.text = "三天内"
        }
        deadTimeGroup.setOnCheckedChangeListener { group, checkedId ->
            isCustom = false
            dateTv.visibility = View.GONE
            timeTv.visibility = View.GONE
            testTimeEdit.visibility = View.GONE
            testTimeTv.visibility = View.GONE
            when(checkedId){
                R.id.time_a ->{
                    deadTime = if (isTest)
                        "45"
                    else
                        DateUtil.getFormatDateAdd(Date(System.currentTimeMillis()), 0, "yyyy-M-d") + " 23:59"
                }
                R.id.time_b ->{
                    deadTime = if (isTest)
                        "90"
                    else
                        DateUtil.getFormatDateAdd(Date(System.currentTimeMillis()), 1, "yyyy-M-d") + " 23:59"
                }
                R.id.time_c ->{
                    deadTime = if (isTest)
                        "120"
                    else
                        DateUtil.getFormatDateAdd(Date(System.currentTimeMillis()), 2, "yyyy-M-d") + " 23:59"
                }
                R.id.time_d ->{
                    isCustom = true
                    if (isTest){
                        testTimeEdit.visibility = View.VISIBLE
                        testTimeTv.visibility = View.VISIBLE
                    }else{
                        dateTv.visibility = View.VISIBLE
                        timeTv.visibility = View.VISIBLE
                    }
                }
            }
        }
        dateTv.setOnClickListener { datePickerDialog.show() }

        titleBar.setOnTitleBarClickListener(object : TitleBar.OnTitleBarClickListener{
            override fun leftClick() {
                finish()
            }
            override fun rightClick() {
                if (isTest && isCustom){
                    if (StringUtils.isEmpty(testTimeEdit.text.toString())){
                        T.show(this@PublishWorkActivity, "请输入自定义考试时间")
                        return
                    }
                    deadTime = testTimeEdit.text.toString()
                }
                T.show(this@PublishWorkActivity, "截止时间 : $deadTime")
            }
        })
    }
}
