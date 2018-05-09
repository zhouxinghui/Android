package com.yzy.ebag.student.activity.main

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.jpush.android.api.JPushInterface
import com.baidu.trace.LBSTraceClient
import com.baidu.trace.Trace
import com.baidu.trace.model.OnTraceListener
import com.baidu.trace.model.PushMessage
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.activity.SettingActivity
import com.yzy.ebag.student.activity.account.InviteActivity
import com.yzy.ebag.student.activity.center.PersonalActivity
import com.yzy.ebag.student.activity.group.GroupListActivity
import com.yzy.ebag.student.activity.homework.ErrorTopicActivity
import com.yzy.ebag.student.activity.homework.HomeworkActivity
import com.yzy.ebag.student.activity.location.LocationActivity
import com.yzy.ebag.student.activity.tools.ToolsActivity
import com.yzy.ebag.student.bean.ClassListInfoBean
import com.yzy.ebag.student.bean.ClassesInfoBean
import com.yzy.ebag.student.dialog.ClassesDialog
import ebag.core.base.mvp.MVPActivity
import ebag.core.http.network.MsgException
import ebag.core.util.*
import ebag.hd.activity.ClassScheduleActivity
import ebag.hd.activity.ClazzmateActivity
import ebag.hd.base.Constants
import ebag.hd.bean.response.UserEntity
import ebag.hd.ui.activity.BookListActivity
import ebag.hd.ui.activity.account.BInviteActivity
import ebag.hd.util.checkUpdate
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : MVPActivity(), MainView {

    private val mainPresenter = MainPresenter(this,this)
    private val adapter = Adapter()
    private var classId = ""
    private var classesInfo: List<ClassListInfoBean>? = null

    override fun destroyPresenter() {
        mainPresenter.onDestroy()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onResume() {
        super.onResume()
        checkUpdate(Constants.UPDATE_STUDENT, false)
        getMainClassInfo()
        initUserInfo()
    }

    override fun initViews() {

        JPushInterface.init(applicationContext)
        rvTeacherName.layoutManager = LinearLayoutManager(this)
        rvTeacherName.adapter = adapter

        stateView.setBackgroundRes(R.color.main_bg_color)
        initUserInfo()
        initListener()
        getMainClassInfo()

        initTrack()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initUserInfo()
        getMainClassInfo()
    }

    override fun mainInfoStart() {
        stateView.showLoading()
    }

    override fun mainInfoSuccess(classesInfoBean: ClassesInfoBean) {
        showTeachers(classesInfoBean)
        SPUtils.put(this,Constants.CLASS_NAME,classesInfoBean.className)
        SPUtils.put(this,Constants.GRADE_CODE,classesInfoBean.gradeCode?.toInt())
        tvAnnounceContent.text = classesInfoBean.resultClassNoticeVo?.content ?: "暂无公告"
        stateView.showContent()

    }

    override fun mainInfoError(exception: Throwable) {
        tvAnnounceContent.text = "暂无公告"
        if(exception is MsgException){
            stateView.showError(exception.message)
            if(exception.code == "2001"){// 没有加入班级
                InviteActivity.jump(this, BInviteActivity.CODE_INVITE)
            }
        }else{
            stateView.showError()
        }

    }

    private fun showTeachers(classesInfoBean: ClassesInfoBean){
        classesInfo = classesInfoBean.resultAllClazzInfoVos
        classId = classesInfoBean.classId ?: ""
        SPUtils.put(this,Constants.CLASS_ID,classId)
        tvGrade.text = getString(R.string.main_class_name,classesInfoBean.className)
        tvClassTeacher.text = getString(R.string.main_teacher_name, classesInfoBean.teacherName)
        tvTeachersTip.text = getString(R.string.main_teachers_tip)

        adapter.setNewData(classesInfoBean.resultClazzInfoVos)
    }

    private fun getMainClassInfo(){
        mainPresenter.mainInfo(classId)
    }

    private fun initUserInfo(){

        val userEntity = SerializableUtils.getSerializable<UserEntity>(Constants.STUDENT_USER_ENTITY)
        tvName.text = userEntity?.name
        tvId.text = userEntity?.ysbCode
        ivHead.loadHead(userEntity?.headUrl, true, System.currentTimeMillis().toString())
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
                HomeworkActivity.jump(this,com.yzy.ebag.student.base.Constants.KHZY_TYPE,classId)
            }
        }
        //随堂作业
        tvSTZY.setOnClickListener{
            if(StringUtils.isEmpty(classId)) {
                T.show(this, "请点击加载左侧班级数据！")
            }else{
                HomeworkActivity.jump(this,com.yzy.ebag.student.base.Constants.STZY_TYPE,classId)
            }
        }

        //考试试卷
        tvKSSJ.setOnClickListener{
            if(StringUtils.isEmpty(classId)) {
                T.show(this, "请点击加载左侧班级数据！")
            }else{
                HomeworkActivity.jump(this,com.yzy.ebag.student.base.Constants.KSSJ_TYPE,classId)
            }
        }
        //学习课本点击事件
        tvXXKB.setOnClickListener{
            BookListActivity.jump(this, classId)
        }
        //课程表
        tvKCB.setOnClickListener{
            if(StringUtils.isEmpty(classId)) {
                T.show(this, "请点击加载左侧班级数据！")
            }else{
                ClassScheduleActivity.jump(this,classId)
            }
        }
        //设置
        tvSetup.setOnClickListener{
            SettingActivity.jump(this)
        }
        //定位
        tvPosition.setOnClickListener{
            startActivity(Intent(this,LocationActivity::class.java))
        }
        //学习小组
        llLearnGroup.setOnClickListener{
            if(StringUtils.isEmpty(classId)) {
                T.show(this, "请点击加载左侧班级数据！")
            }else{
                GroupListActivity.jump(this, classId)
            }
        }
        //学习园地
        llLearnPlace.setOnClickListener{
        }

        //我的错题
        btnMyError.setOnClickListener{
            if(StringUtils.isEmpty(classId)) {
                T.show(this, "请点击加载左侧班级数据！")
            }else{
                ErrorTopicActivity.jump(this, classId)
            }
        }
        //我的同学
        btnMyClassmate.setOnClickListener{
            startActivity(Intent(this, ClazzmateActivity::class.java))
        }
        //我的书库
        btnMyBook.setOnClickListener{

        }
        //学习工具
        btnDailyPractice.setOnClickListener{
            ToolsActivity.jump(this, classId)
        }

        //点击班级
        tvGradeLayout.setOnClickListener {
            showClasses()
        }


        stateView.setOnRetryClickListener {
            getMainClassInfo()
        }
        tvMoreAnnounce.setOnClickListener {
            if(StringUtils.isEmpty(classId)) {
                T.show(this, "请点击加载左侧班级数据！")
            }else{
                AnnounceActivity.jump(this, classId)
            }
        }

    }

    private val classesDialog by lazy {
        val classes = ClassesDialog.newInstance()
        classes.setOnClassChooseListener{
            classes.dismiss()
            if(it?.classId != classId){
                classId = it?.classId ?: ""
                SPUtils.put(this,Constants.CLASS_ID,classId)
                getMainClassInfo()
            }
        }
        classes
    }

    private fun showClasses(){
        classesDialog.updateData(classesInfo, classId)
        classesDialog.show(supportFragmentManager,"classes")
    }

    class Adapter: BaseQuickAdapter<ClassesInfoBean,BaseViewHolder>(R.layout.item_activity_main_class_info){
        override fun convert(helper: BaseViewHolder?, item: ClassesInfoBean?) {
            helper?.setText(R.id.tv, "${item?.subject} : ${item?.teacherName}")
        }
    }

    /**
     * 鹰眼追踪
     */
    private fun initTrack(){
        // 轨迹服务ID
        val serviceId: Long = 116060
        val userEntity = SerializableUtils.getSerializable<UserEntity>(Constants.STUDENT_USER_ENTITY)
        // 设备标识
        val entityName = userEntity?.uid ?: "myTrack"
        // 是否需要对象存储服务，默认为：false，关闭对象存储服务。
        // 注：鹰眼 Android SDK v3.0以上版本支持随轨迹上传图像等对象数据，若需使用此功能，该参数需设为 true，且需导入bos-android-sdk-1.0.2.jar。
        val isNeedObjectStorage = false
        // 初始化轨迹服务
        val mTrace = Trace(serviceId, entityName, isNeedObjectStorage)
        // 初始化轨迹服务客户端
        val mTraceClient = LBSTraceClient(applicationContext)

        // 初始化轨迹服务监听器
        val mTraceListener = object : OnTraceListener {
            override fun onBindServiceCallback(p0: Int, p1: String?) {
            }

            override fun onInitBOSCallback(p0: Int, p1: String?) {
            }

            // 开启服务回调
            override fun onStartTraceCallback(status: Int, message: String) {
                if (status == 0)
                // 开启采集
                    mTraceClient.startGather(null)
            }

            // 停止服务回调
            override fun onStopTraceCallback(status: Int, message: String){}

            // 开启采集回调
            override fun onStartGatherCallback(status: Int, message: String) {}

            // 停止采集回调
            override fun onStopGatherCallback(status: Int, message: String) {}

            // 推送回调
            override fun onPushCallback(messageNo: Byte, message: PushMessage) {}
        }

        mTraceClient.setOnTraceListener(mTraceListener)
        // 定位周期(单位:秒)
        val gatherInterval = 30
        // 打包回传周期(单位:秒)
        val packInterval = 60
        // 设置定位和打包周期
        mTraceClient.setInterval(gatherInterval, packInterval)
        // 开启服务
        mTraceClient.startTrace(mTrace, null)
    }
}
