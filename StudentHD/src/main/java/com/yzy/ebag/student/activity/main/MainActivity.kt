package com.yzy.ebag.student.activity.main

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.activity.SettingActivity
import com.yzy.ebag.student.activity.ToolsActivity
import com.yzy.ebag.student.activity.book.BookListActivity
import com.yzy.ebag.student.activity.center.PersonalActivity
import com.yzy.ebag.student.activity.homework.HomeworkActivity
import com.yzy.ebag.student.bean.response.ClassesInfoBean
import ebag.core.base.mvp.MVPActivity
import ebag.core.util.SerializableUtils
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.core.util.loadImage
import ebag.hd.base.Constants
import ebag.hd.bean.response.UserEntity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MVPActivity(), MainView, View.OnClickListener {

    private val mainPresenter = MainPresenter(this,this)
    private val adapter = Adapter()
    private var classId = ""

    override fun destroyPresenter() {
        mainPresenter.onDestroy()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initViews() {

        val userEntity = SerializableUtils.getSerializable<UserEntity>(Constants.STUDENT_USER_ENTITY)
        if(userEntity != null){
            tvName.text = userEntity.name
            tvId.text = userEntity.ysbCode
            ivHead.loadImage(userEntity.headUrl)
        }

        rvTeacherName.layoutManager = LinearLayoutManager(this)
        rvTeacherName.adapter = adapter

        initListener()
        getMainClassInfo()
    }

    override fun mainInfoStart() {
        stateView.showLoading()
    }

    override fun mainInfoSuccess(classesInfoBean: ClassesInfoBean) {
        showTeachers(classesInfoBean)
        stateView.showContent()

    }

    override fun mainInfoError(exception: Throwable) {
        stateView.showError()
    }

    private fun showTeachers(classesInfoBean: ClassesInfoBean){
        classId = classesInfoBean.classId
        tvGrade.text = getString(R.string.main_class_name,classesInfoBean.className)
        tvClassTeacher.text = getString(R.string.main_teacher_name, classesInfoBean.teacherName)
        tvTeachersTip.text = getString(R.string.main_teachers_tip)

        adapter.setNewData(classesInfoBean.resultClazzInfoVos)
    }

    private fun getMainClassInfo(){
        mainPresenter.mianInfo()
    }

    private fun initListener(){
        tvName.setOnClickListener(this)
        tvId.setOnClickListener(this)
        ivHead.setOnClickListener(this)

        tvKHZY.setOnClickListener(this)
        tvSTZY.setOnClickListener(this)
        tvKSSJ.setOnClickListener(this)
        tvXXKB.setOnClickListener(this)
        tvSetup.setOnClickListener(this)
        btnDailyPractice.setOnClickListener(this)

        stateView.setOnRetryClickListener {
            getMainClassInfo()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnDailyPractice -> {//学习工具
                startActivity(Intent(this, ToolsActivity::class.java))
            }
            R.id.tvSetup -> {//设置
                startActivity(Intent(this, SettingActivity::class.java))
            }
            R.id.tvXXKB -> {//学习课本点击事件
                startActivity(Intent(this, BookListActivity::class.java))
            }
            R.id.tvKSSJ -> { //考试试卷
                startActivity(Intent(this, HomeworkActivity::class.java).putExtra("type",3))
            }
            R.id.tvSTZY -> { //随堂作业

                if(StringUtils.isEmpty(classId)) {
                    T.show(this, "请点击加载左侧班级数据！")
                    return
                }

                startActivity(
                        Intent(this, HomeworkActivity::class.java)
                                .putExtra("type",com.yzy.ebag.student.base.Constants.STZY_TYPE)
                                .putExtra("classId",classId)
                )

            }
            R.id.tvKHZY -> {//课后作业

                if(StringUtils.isEmpty(classId)) {
                    T.show(this, "请点击加载左侧班级数据！")
                    return
                }

                startActivity(
                        Intent(this, HomeworkActivity::class.java)
                                .putExtra("type", com.yzy.ebag.student.base.Constants.KHZY_TYPE)
                                .putExtra("classId",classId)
                )

            }
            R.id.tvName,R.id.tvId ,R.id.ivHead -> {//名字，id，头像
                startActivity(Intent(this, PersonalActivity::class.java))
            }
        }
    }

    class Adapter: BaseQuickAdapter<ClassesInfoBean,BaseViewHolder>(R.layout.activity_main_class_info_item){
        override fun convert(helper: BaseViewHolder?, item: ClassesInfoBean?) {
            helper?.setText(R.id.tv, "${item?.subject} : ${item?.teacherName}")
        }
    }
}
