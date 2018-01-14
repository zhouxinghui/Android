package com.yzy.ebag.student.activity.main

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.activity.SettingActivity
import com.yzy.ebag.student.activity.tools.ToolsActivity
import com.yzy.ebag.student.activity.book.BookListActivity
import com.yzy.ebag.student.activity.center.PersonalActivity
import com.yzy.ebag.student.activity.homework.HomeworkActivity
import com.yzy.ebag.student.bean.response.ClassesInfoBean
import ebag.core.base.mvp.MVPActivity
import ebag.core.util.SerializableUtils
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.core.util.loadHead
import ebag.hd.base.Constants
import ebag.hd.bean.response.UserEntity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MVPActivity(), MainView {

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

        rvTeacherName.layoutManager = LinearLayoutManager(this)
        rvTeacherName.adapter = adapter
        initUserInfo()
        initListener()
        getMainClassInfo()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
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

    private fun initUserInfo(){

        val userEntity = SerializableUtils.getSerializable<UserEntity>(Constants.STUDENT_USER_ENTITY)
        if(userEntity != null){
            tvName.text = userEntity.name
            tvId.text = userEntity.ysbCode
            ivHead.loadHead(userEntity.headUrl)
        }
    }

    private fun initListener(){

        val click = View.OnClickListener{
            startActivity(Intent(this, PersonalActivity::class.java))
        }
        //名字
        tvName.setOnClickListener(click)
        //id
        tvId.setOnClickListener(click)
        //头像
        ivHead.setOnClickListener(click)

        //课后作业
        tvKHZY.setOnClickListener{
            if(StringUtils.isEmpty(classId)) {
                T.show(this, "请点击加载左侧班级数据！")
            }else{
                startActivity(
                    Intent(this, HomeworkActivity::class.java)
                            .putExtra("type", com.yzy.ebag.student.base.Constants.KHZY_TYPE)
                            .putExtra("classId",classId)
                )
            }
        }
        //随堂作业
        tvSTZY.setOnClickListener{
            if(StringUtils.isEmpty(classId)) {
                T.show(this, "请点击加载左侧班级数据！")
            }else{
                startActivity(
                    Intent(this, HomeworkActivity::class.java)
                            .putExtra("type",com.yzy.ebag.student.base.Constants.STZY_TYPE)
                            .putExtra("classId",classId)
                )
            }
        }

        //考试试卷
        tvKSSJ.setOnClickListener{
            startActivity(Intent(this, HomeworkActivity::class.java).putExtra("type",3))
        }
        //学习课本点击事件
        tvXXKB.setOnClickListener{
            startActivity(Intent(this, BookListActivity::class.java))
        }
        //课程表
        tvKCB.setOnClickListener{

        }
        //设置
        tvSetup.setOnClickListener{
            startActivity(Intent(this, SettingActivity::class.java))
        }
        //定位
        tvPosition.setOnClickListener{

        }
        //学习小组
        llLearnGroup.setOnClickListener{

        }
        //学习园地
        llLearnPlace.setOnClickListener{

        }

        //我的错题
        btnMyError.setOnClickListener{

        }
        //我的同学
        btnMyClassmate.setOnClickListener{

        }
        //我的书库
        btnMyBook.setOnClickListener{

        }
        //学习工具
        btnDailyPractice.setOnClickListener{
            startActivity(Intent(this, ToolsActivity::class.java))
        }



        stateView.setOnRetryClickListener {
            getMainClassInfo()
        }
    }

    class Adapter: BaseQuickAdapter<ClassesInfoBean,BaseViewHolder>(R.layout.activity_main_class_info_item){
        override fun convert(helper: BaseViewHolder?, item: ClassesInfoBean?) {
            helper?.setText(R.id.tv, "${item?.subject} : ${item?.teacherName}")
        }
    }
}
