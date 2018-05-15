package com.yzy.ebag.student

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.yzy.ebag.student.bean.ClassListInfoBean
import com.yzy.ebag.student.bean.ClassesInfoBean
import com.yzy.ebag.student.http.StudentApi
import com.yzy.ebag.student.module.mission.MyMissionActivity
import com.yzy.ebag.student.module.personal.ClassesDialog
import com.yzy.ebag.student.module.personal.ParentsActivity
import com.yzy.ebag.student.module.personal.PerformanceDialog
import com.yzy.ebag.student.module.personal.PersonalInfoActivity
import com.yzy.ebag.student.util.StatusUtil
import ebag.core.http.network.MsgException
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.SPUtils
import ebag.core.util.SerializableUtils
import ebag.core.util.loadHead
import ebag.mobile.base.Constants
import ebag.mobile.bean.UserEntity
import ebag.mobile.module.shop.YBActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StatusUtil.initStatusView(this)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        val headView = nav_view.getHeaderView(0)
        headImg = headView.findViewById(R.id.imageView)
        nameTv = headView.findViewById(R.id.nameTv)
        ysbCodeTv = headView.findViewById(R.id.ysbCodeTv)
        clazzTv = headView.findViewById(R.id.clazzTv)
        headImg.setOnClickListener {
            startActivity(Intent(this, PersonalInfoActivity::class.java))
        }
        clazzTv.setOnClickListener {
            classesDialog.show(classesInfo, classId)
        }
        request()
        initUserInfo()
    }
    private lateinit var headImg: ImageView
    private lateinit var nameTv: TextView
    private lateinit var ysbCodeTv: TextView
    private lateinit var clazzTv: TextView
    private var classId = ""
    private var classesInfo: List<ClassListInfoBean>? = null
    private val mainInfoRequest = object: RequestCallBack<ClassesInfoBean>(){
        override fun onStart() {
            LoadingDialogUtil.showLoading(this@MainActivity)
        }
        override fun onSuccess(entity: ClassesInfoBean?) {
            LoadingDialogUtil.closeLoadingDialog()
            if (entity != null){
                SPUtils.put(this@MainActivity,Constants.CLASS_NAME,entity.className)
                SPUtils.put(this@MainActivity,Constants.GRADE_CODE,entity.gradeCode?.toInt())
//                tvAnnounceContent.text = classesInfoBean.resultClassNoticeVo?.content ?: "暂无公告"
                classesInfo = entity.resultAllClazzInfoVos
                classId = entity.classId ?: ""
                SPUtils.put(this@MainActivity,Constants.CLASS_ID,classId)
                clazzTv.text = entity.className
            }
        }

        override fun onError(exception: Throwable) {
            LoadingDialogUtil.closeLoadingDialog()
//            tvAnnounceContent.text = "暂无公告"
            if(exception is MsgException){
                if(exception.code == "2001"){// 没有加入班级
//                    InviteActivity.jump(this, BInviteActivity.CODE_INVITE)
                }
            }
            exception.handleThrowable(this@MainActivity)
        }
    }
    private val classesDialog by lazy {
        val dialog = ClassesDialog(this)
        dialog.listener = {
            if(it?.classId != classId){
                classId = it?.classId ?: ""
                SPUtils.put(this,Constants.CLASS_ID,classId)
                request()
            }
        }
        dialog
    }
    private fun request(){
        StudentApi.mainInfo(classId, mainInfoRequest)
    }
    private fun initUserInfo(){
        val userEntity = SerializableUtils.getSerializable<UserEntity>(Constants.STUDENT_USER_ENTITY)
        nameTv.text = userEntity?.name
        ysbCodeTv.text = userEntity?.ysbCode
        headImg.loadHead(userEntity?.headUrl, true, System.currentTimeMillis().toString())
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.myMission -> {
                MyMissionActivity.jump(this, classId)
            }
            R.id.performance -> {
                PerformanceDialog(this).show()
            }
            R.id.myParents -> {
                startActivity(Intent(this, ParentsActivity::class.java))
            }
            R.id.coinCenter -> {
                YBActivity.start(this)
            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
