package com.yzy.ebag.teacher.module.homework

import android.app.Activity
import android.content.Intent
import android.view.View
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.AssignClassBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.base.BaseActivity
import ebag.core.bean.QuestionBean
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.DateUtil
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.mobile.bean.UnitBean
import ebag.mobile.widget.DatePickerDialog
import kotlinx.android.synthetic.main.activity_publish_work.*
import java.util.*
import kotlin.collections.ArrayList

class PublishWorkActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_publish_work
    }
    private val publishRequest = object : RequestCallBack<String>(){
        override fun onStart() {
            LoadingDialogUtil.showLoading(this@PublishWorkActivity, "正在发布...")
        }
        override fun onSuccess(entity: String?) {
            LoadingDialogUtil.closeLoadingDialog()
            T.show(this@PublishWorkActivity, "发布成功")
            startActivity(
                    Intent(this@PublishWorkActivity, AssignmentActivity::class.java)
                            .putExtra("clearQuestion", true)
            )
        }

        override fun onError(exception: Throwable) {
            LoadingDialogUtil.closeLoadingDialog()
            exception.handleThrowable(this@PublishWorkActivity)
        }

    }
    private val datePickerDialog by lazy {
        val dialog = DatePickerDialog(this)
        dialog.onConfirmClick = {
            dateTv.text = it
            deadTime = it + " 23:59"
        }
        dialog
    }
    private val selectGroupDialog by lazy {
        val dialog = DialogSelectGroup(this)
        dialog.onConfirmClick = {
            if (it.isEmpty()){
                publishPerson.text = "布置小组：点击选择小组"
            }else {
                val stringBuilder = StringBuilder("布置小组：")
                groupIds = ArrayList()
                it.forEach {
                    stringBuilder.append("${it.groupName}、")
                    groupIds!!.add(it.groupId)
                }
                stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("、"))
                publishPerson.text = stringBuilder.toString()
            }
        }
        dialog
    }
    private lateinit var deadTime: String
    private var groupIds: ArrayList<String>? = null
    companion object {
        fun jump(activity: Activity,
                 isGroup: Boolean,
                 isTest: Boolean,
                 classes: ArrayList<AssignClassBean>,
                 unitBean: UnitBean.UnitSubBean?,
                 questionList: ArrayList<QuestionBean>,
                 workType: Int,
                 subCode: String,
                 bookVersionId: String,
                 testPaperId: String? = null,
                 testPaperName: String? = null,
                 unitName: String? = null
                 ){
            activity.startActivity(Intent(activity, PublishWorkActivity::class.java)
                    .putExtra("isGroup", isGroup)
                    .putExtra("isTest", isTest)
                    .putExtra("classes", classes)
                    .putExtra("unitBean", unitBean)
                    .putExtra("questionList", questionList)
                    .putExtra("workType", workType)
                    .putExtra("subCode", subCode)
                    .putExtra("bookVersionId", bookVersionId)
                    .putExtra("testPaperId", testPaperId)
                    .putExtra("testPaperName", testPaperName)
                    .putExtra("unitName", unitName)
            )
        }
    }

    override fun initViews() {
        val isGroup = intent.getBooleanExtra("isGroup", false)
        val isTest = intent.getBooleanExtra("isTest", false)
        val classes = intent.getSerializableExtra("classes") as ArrayList<AssignClassBean>
        val unitBean = intent.getSerializableExtra("unitBean") as UnitBean.UnitSubBean
        val questionList = intent.getSerializableExtra("questionList") as ArrayList<QuestionBean>
        val workType = intent.getIntExtra("workType", 0).toString()
        val subCode = intent.getStringExtra("subCode")
        val bookVersionId = intent.getStringExtra("bookVersionId")
        val unitName = intent.getStringExtra("unitName")
        var isCustom = false
        publishTime.text = "布置时间：${DateUtil.getFormatDateTime(Date(System.currentTimeMillis()), "yyyy-M-d")}"
        dateTv.text = DateUtil.getFormatDateTime(Date(System.currentTimeMillis()), "yyyy-M-d")
        var content = "${if (unitBean.unitCode == null) "全部" else unitName} (共${questionList.size}题)"
        publishContent.text = "发布内容：$content"
        if (isGroup){
            titleBar.setTitle("发布小组")
            publishPerson.text = "布置小组：点击选择小组"
            publishPerson.setOnClickListener {
                if (classes.isEmpty()){
                    T.show(this, "未选择班级")
                    return@setOnClickListener
                }
                if (classes.size >1) {
                    T.show(this, "发布小组时班级不能多选")
                    return@setOnClickListener
                }
                selectGroupDialog.show(classes[0].classId)
            }
        }else{
            titleBar.setTitle("发布班级")
            val stringBuilder = StringBuilder("布置班级：")
            classes.forEach { stringBuilder.append("${it.className}、") }
            stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("、"))
            publishPerson.text = stringBuilder.toString()
        }
        var testPaperId: String? = null
        if(isTest){
            deadTimeTv.text = "考试时间："
            deadTime = "45"
            time_a.text = "45分钟"
            time_b.text = "90分钟"
            time_c.text = "120分钟"
            testPaperId = intent.getStringExtra("testPaperId")
            content = intent.getStringExtra("testPaperName")
            publishContent.text = "发布内容：$content"
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

        titleBar.setOnRightClickListener {
            if (isTest && isCustom){
                if (StringUtils.isEmpty(testTimeEdit.text.toString())){
                    T.show(this@PublishWorkActivity, "请输入自定义考试时间")
                    return@setOnRightClickListener
                }
                deadTime = testTimeEdit.text.toString()
                if (deadTime.toInt() > 150 || deadTime.toInt() < 30){
                    T.show(this@PublishWorkActivity, "考试时间范围：30-150分钟")
                    return@setOnRightClickListener

                }
            }else{

            }
            if (classes.isEmpty()){
                T.show(this@PublishWorkActivity, "未选择班级")
                return@setOnRightClickListener
            }
            if (isGroup && (groupIds == null || groupIds!!.isEmpty())){
                T.show(this@PublishWorkActivity, "请选择小组")
                return@setOnRightClickListener
            }else if (isGroup){
                TeacherApi.publishHomework(classes, groupIds, isGroup, workType, attentionEdit.text.toString(), content, deadTime, subCode, bookVersionId, questionList, null, publishRequest)
            }else{
                if (isTest){
                    TeacherApi.publishHomework(classes,null, isGroup, workType, attentionEdit.text.toString(), content, deadTime, subCode, bookVersionId, null, testPaperId, publishRequest)
                }else{
                    TeacherApi.publishHomework(classes,null, isGroup, workType, attentionEdit.text.toString(), content, deadTime, subCode, bookVersionId, questionList, null, publishRequest)
                }
            }
        }
    }
}
