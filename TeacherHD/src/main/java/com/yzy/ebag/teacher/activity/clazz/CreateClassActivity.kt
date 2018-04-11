package com.yzy.ebag.teacher.activity.clazz

import android.content.Context
import android.content.Intent
import android.view.View
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.activity.home.FragmentClass
import com.yzy.ebag.teacher.base.Constants
import com.yzy.ebag.teacher.bean.BaseSubjectBean
import com.yzy.ebag.teacher.http.TeacherApi
import com.yzy.ebag.teacher.widget.SelectSubjectDialog
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.SerializableUtils
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.hd.bean.CurrentCityBean
import ebag.hd.widget.ListBottomShowDialog
import kotlinx.android.synthetic.main.activity_create_class.*
import java.io.Serializable

class CreateClassActivity : BaseActivity(), View.OnClickListener {
    override fun getLayoutId(): Int {
        return R.layout.activity_create_class
    }
    companion object {
        fun jump(fragment: FragmentClass){
            fragment.startActivityForResult(Intent(fragment.activity, CreateClassActivity::class.java), Constants.CREATE_CLASS_REQUEST)
        }
        fun jump(context: Context){
            context.startActivity(Intent(context, CreateClassActivity::class.java))
        }
    }
    private val createRequest = object: RequestCallBack<String>(){
        override fun onStart() {
            LoadingDialogUtil.showLoading(this@CreateClassActivity, "正在创建...")
        }

        override fun onSuccess(entity: String?) {
            LoadingDialogUtil.closeLoadingDialog()
            setResult(Constants.CREATE_CLASS_RESULT)
            finish()
        }

        override fun onError(exception: Throwable) {
            LoadingDialogUtil.closeLoadingDialog()
            exception.handleThrowable(this@CreateClassActivity)
        }
    }
    private val subjectRequest = object : RequestCallBack<List<BaseSubjectBean>>() {
        override fun onStart() {
            LoadingDialogUtil.showLoading(this@CreateClassActivity)
        }
        override fun onSuccess(entity: List<BaseSubjectBean>?) {
            LoadingDialogUtil.closeLoadingDialog()
            if (entity == null || entity.isEmpty()){
                T.show(this@CreateClassActivity, "该年级暂无课程数据")
            }else{
                val subjectList = entity as ArrayList<BaseSubjectBean>
                subjectDialog.show(subjectList)
            }
        }
        override fun onError(exception: Throwable) {
            LoadingDialogUtil.closeLoadingDialog()
            exception.handleThrowable(this@CreateClassActivity)
        }

    }
    private val gradeDialog by lazy {
        object : ListBottomShowDialog<MyGradeBean>(this@CreateClassActivity, ArrayList<MyGradeBean>()){
            override fun setText(data: MyGradeBean): String {
                return data.gradeName
            }
        }
    }
    private val subjectDialog by lazy {
        val dialog = SelectSubjectDialog(this)
        dialog.onConfirmClick = {subjectList ->
            val nameSb = StringBuilder()
            val codeSb = StringBuilder()
            subjectList.forEach {
                nameSb.append("${it.keyValue}、")
                codeSb.append("${it.keyCode},")
            }
            courseName.text = nameSb.deleteCharAt(nameSb.length - 1)
            courseName.isSelected = true
            subjectCode = codeSb.deleteCharAt(codeSb.length - 1).toString()
        }
        dialog
    }
    private val stageList by lazy {
        val list = ArrayList<MyGradeBean>()
        val gradeBean1 = MyGradeBean()
        gradeBean1.gradeCode = Constants.PRIMARY_SCHOOL
        gradeBean1.gradeName = "小学"
        val gradeBean2 = MyGradeBean()
        gradeBean2.gradeCode = Constants.JUNIOR_HIGH_SCHOOL
        gradeBean2.gradeName = "初中"
        val gradeBean3 = MyGradeBean()
        gradeBean3.gradeCode = Constants.HIGH_SCHOOL
        gradeBean3.gradeName = "高中"
        list.add(gradeBean1)
        list.add(gradeBean2)
        list.add(gradeBean3)
        list
    }
    private val primaryList by lazy {
        val list = ArrayList<MyGradeBean>()
        val gradeBean1 = MyGradeBean()
        gradeBean1.gradeCode = "1"
        gradeBean1.gradeName = "一年级"
        val gradeBean2 = MyGradeBean()
        gradeBean2.gradeCode = "2"
        gradeBean2.gradeName = "二年级"
        val gradeBean3 = MyGradeBean()
        gradeBean3.gradeCode = "3"
        gradeBean3.gradeName = "三年级"
        val gradeBean4 = MyGradeBean()
        gradeBean4.gradeCode = "4"
        gradeBean4.gradeName = "四年级"
        val gradeBean5 = MyGradeBean()
        gradeBean5.gradeCode = "5"
        gradeBean5.gradeName = "五年级"
        val gradeBean6 = MyGradeBean()
        gradeBean6.gradeCode = "6"
        gradeBean6.gradeName = "六年级"
        list.add(gradeBean1)
        list.add(gradeBean2)
        list.add(gradeBean3)
        list.add(gradeBean4)
        list.add(gradeBean5)
        list.add(gradeBean6)
        list
    }
    private val juniorHighList by lazy {
        val list = ArrayList<MyGradeBean>()
        val gradeBean1 = MyGradeBean()
        gradeBean1.gradeCode = "7"
        gradeBean1.gradeName = "七年级"
        val gradeBean2 = MyGradeBean()
        gradeBean2.gradeCode = "8"
        gradeBean2.gradeName = "八年级"
        val gradeBean3 = MyGradeBean()
        gradeBean3.gradeCode = "9"
        gradeBean3.gradeName = "九年级"
        list.add(gradeBean1)
        list.add(gradeBean2)
        list.add(gradeBean3)
        list
    }
    private val highList by lazy {
        val list = ArrayList<MyGradeBean>()
        val gradeBean1 = MyGradeBean()
        gradeBean1.gradeCode = "10"
        gradeBean1.gradeName = "高一"
        val gradeBean2 = MyGradeBean()
        gradeBean2.gradeCode = "11"
        gradeBean2.gradeName = "高二"
        val gradeBean3 = MyGradeBean()
        gradeBean3.gradeCode = "12"
        gradeBean3.gradeName = "高三"
        list.add(gradeBean1)
        list.add(gradeBean2)
        list.add(gradeBean3)
        list
    }
    private var stage = ""
    private var currentCityBean: CurrentCityBean? = null
    private var currentGradeCode = "1"
    private var subjectCode = "yw"

    override fun initViews() {
        schoolBtn.setOnClickListener(this)
        gradeBtn.setOnClickListener(this)
        courseBtn.setOnClickListener(this)
        phaseBtn.setOnClickListener(this)
        titleBar.setRightText("确定", {
            if (isAllItemSelected()){
                TeacherApi.createClass(
                        currentCityBean?.schoolCode,
                        currentGradeCode,
                        classNameEdit.text.toString(),
                        subjectCode,
                        createRequest)
            }
        })

        currentCityBean = SerializableUtils.getSerializable<CurrentCityBean>(ebag.hd.base.Constants.CURRENT_CITY)
        if (currentCityBean != null) {
            schoolName.text = currentCityBean?.schoolName
            schoolName.isSelected = true
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.schoolBtn ->{
                SelectSchoolActivity.jump(this, currentCityBean)
            }
            R.id.phaseBtn ->{
                gradeDialog.setOnDialogItemClickListener { dialog, data, _ ->
                    phaseName.text = data.gradeName
                    phaseName.isSelected = true
                    stage = data.gradeCode
                    dialog.dismiss()

                    courseName.text = "请选择课程"
                    courseName.isSelected = false

                    gradeName.text = "请选择年级"
                    gradeName.isSelected = false
                }
                gradeDialog.show(stageList)
            }
            R.id.gradeBtn ->{
                if(StringUtils.isEmpty(stage)){
                    T.show(this, "请选择“阶段”")
                    return
                }
                gradeDialog.setOnDialogItemClickListener { dialog, data, _ ->
                    gradeName.text = data.gradeName
                    gradeName.isSelected = true
                    currentGradeCode = data.gradeCode
                    dialog.dismiss()

                    courseName.text = "请选择课程"
                    courseName.isSelected = false
                }
                when(stage){
                    Constants.PRIMARY_SCHOOL ->{
                        gradeDialog.show(primaryList)
                    }
                    Constants.JUNIOR_HIGH_SCHOOL ->{
                        gradeDialog.show(juniorHighList)
                    }
                    Constants.HIGH_SCHOOL ->{
                        gradeDialog.show(highList)
                    }
                }
            }
            R.id.courseBtn ->{
                if(gradeName.text == "请选择年级"){
                    T.show(this, "请选择年级")
                    return
                }
                TeacherApi.getSubject(currentGradeCode, subjectRequest)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ebag.hd.base.Constants.SELECT_SCHOOL
                && resultCode == ebag.hd.base.Constants.BACK_TO_CREATE_CLASS && data != null){
            currentCityBean = data.getSerializableExtra(ebag.hd.base.Constants.CURRENT_CITY) as CurrentCityBean
            schoolName.text = currentCityBean?.schoolName
            schoolName.isSelected = true
        }
    }

    private fun isAllItemSelected(): Boolean {
        when {
            gradeName.text == "请选择年级" -> {
                T.show(this, "请选择年级")
                return false
            }
            StringUtils.isEmpty(classNameEdit.text.toString()) -> {
                T.show(this, "请输入班别")
                return false
            }
            courseName.text == "请选择课程" -> {
                T.show(this, "请选择课程")
                return false
            }
            schoolName.text == "请选择学校" -> {
                T.show(this, "请选择学校")
                return false
            }
            else -> return true
        }
    }

    inner class MyGradeBean: Serializable{
        var gradeCode = ""
        var gradeName = ""
    }

}
