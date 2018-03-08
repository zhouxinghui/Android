package ebag.hd.activity

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.base.BaseActivity
import ebag.core.http.network.MsgException
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.hd.R
import ebag.hd.base.Constants
import ebag.hd.bean.ClassScheduleBean
import ebag.hd.bean.request.ClassScheduleEditVo
import ebag.hd.bean.response.BaseInfoEntity
import ebag.hd.http.EBagApi
import ebag.hd.widget.ListBottomShowDialog
import kotlinx.android.synthetic.main.activity_class_schedule.*

/**
 * Created by unicho on 2018/3/6.
 */
class ClassScheduleActivity: BaseActivity(){

    companion object {
        fun jump(context: Context, classId: String, role: Int = Constants.ROLE_STUDENT){
            context.startActivity(
                    Intent(context, ClassScheduleActivity::class.java)
                            .putExtra("classId", classId)
                            .putExtra("role", role)
            )
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_class_schedule
    }

    private val adapter = Adapter()
    private var currentWeek = ""
    private lateinit var classId: String
    private var role = Constants.ROLE_STUDENT
    private var subjectList : ArrayList<BaseInfoEntity>? = null

    private val mScheduleList = ArrayList<ClassScheduleBean.ScheduleBean>()
    private var editPosition = 0

    private val subjectDialog by lazy {
        val dialog = object : ListBottomShowDialog<BaseInfoEntity>(this, null){
            override fun setText(data: BaseInfoEntity?): String {
                return data?.keyValue ?: ""
            }
        }

        dialog.setOnDialogItemClickListener { mDialog, data, position ->
            adapter.getItem(editPosition)?.subject = data.keyValue
            adapter.notifyItemChanged(editPosition)
            mDialog.dismiss()
        }
        dialog
    }

    override fun initViews() {
        classId = intent.getStringExtra("classId") ?: ""
        role = intent.getIntExtra("role", Constants.ROLE_STUDENT)

        scheduleRecycler.layoutManager = GridLayoutManager(this, 10, LinearLayoutManager.HORIZONTAL, false)
        scheduleRecycler.adapter = adapter

        if(role == Constants.ROLE_TEACHER){
            titleView.setRightText("编辑"){
                if(adapter.isEdit){
                    editSchedule()
                }else{
                    getSubjectInfo()
                    titleView.setRightText("完成")
                    adapter.isEdit = true
                }
            }
        }

        if(role == Constants.ROLE_TEACHER){
            adapter.setOnItemClickListener { _, _, pos ->
                if(subjectList != null){
                    editPosition = pos
                    subjectDialog.show()
                }
            }
        }

        stateView.setOnRetryClickListener {
            getClassSchedule()
        }
        getClassSchedule()
    }

    private val scheduleRequest = object :RequestCallBack<ClassScheduleBean>(){
        override fun onStart() {
            stateView.showLoading()
        }
        override fun onSuccess(entity: ClassScheduleBean?) {
            stateView.showContent()
            mScheduleList.clear()
            if(entity?.scheduleList != null && entity.scheduleList.isNotEmpty()){
                currentWeek = entity.currentWeek
                // 周一 到 周五
                (1 until 6).forEach { week ->
                    // 查找是否有当前星期的数据
                    val list = entity.scheduleList.filter { it.week == "$week" }
                    // 判断当前星期有没有返回 数据
                    if(list.isEmpty() || list[0].scheduleCardVos == null || list[0].scheduleCardVos.isEmpty()){
                        (1 until 11).forEach { mScheduleList.add(ClassScheduleBean.ScheduleBean("$week", "$it", classId)) }
                    }else{
                        (1 until 11).forEach { xClass ->
                            val schedule = list[0].scheduleCardVos.filter { it.curriculum == "$xClass" }
                            if(schedule.isEmpty()){
                                mScheduleList.add(ClassScheduleBean.ScheduleBean("$week", "$xClass", classId))
                            }else{
                                mScheduleList.add(schedule[0].addClassId(classId))
                            }
                        }
                    }
                }
                adapter.isEdit = false
            }else if(role == Constants.ROLE_TEACHER){ // 填充初始化假数据
                getSubjectInfo()
                (0 until 50).forEach { position->
                    mScheduleList.add(ClassScheduleBean.ScheduleBean("${(position / 10) + 1}", "${(position % 10) + 1}", classId))
                }
                adapter.isEdit = true
            }

            adapter.setNewData(mScheduleList)
        }

        override fun onError(exception: Throwable) {
            if(exception is MsgException){
                stateView.showError(exception.message)
            }else{
                stateView.showError()
            }
            exception.handleThrowable(this@ClassScheduleActivity)
        }

    }

    private fun getClassSchedule(){
        EBagApi.classSchedule(classId, scheduleRequest)
    }

    private var editRequest: RequestCallBack<String>? = null
    private fun editSchedule(){
        val scheduleVo = ClassScheduleEditVo()
        scheduleVo.scheduleCards = adapter.data.filter { !StringUtils.isEmpty(it.subject) }
        if(editRequest == null){
            editRequest = object: RequestCallBack<String>(){

                override fun onStart() {
                    LoadingDialogUtil.showLoading(this@ClassScheduleActivity, "请稍后...")
                }

                override fun onSuccess(entity: String?) {
                    LoadingDialogUtil.closeLoadingDialog()
                    titleView.setRightText("编辑")
                    adapter.isEdit = false
                }

                override fun onError(exception: Throwable) {
                    LoadingDialogUtil.closeLoadingDialog()
                    exception.handleThrowable(this@ClassScheduleActivity)
                }

            }
        }
        EBagApi.editSchedule(scheduleVo, editRequest!!)
    }

    private var infoRequest: RequestCallBack<ArrayList<BaseInfoEntity>>? = null
    private fun getSubjectInfo(){
        if(subjectList == null){
            if(infoRequest == null){
                infoRequest = object :RequestCallBack<ArrayList<BaseInfoEntity>>(){
                    override fun onStart() {
                        LoadingDialogUtil.showLoading(this@ClassScheduleActivity, "获取科目信息中...")
                    }

                    override fun onSuccess(entity: ArrayList<BaseInfoEntity>?) {
                        LoadingDialogUtil.closeLoadingDialog()
                        if(entity == null){
                            T.show(this@ClassScheduleActivity, "无数据，请稍后再试")
                            titleView.setRightText("编辑")
                            adapter.isEdit = false
                        }else{
                            subjectDialog.setData(entity)
                        }
                        subjectList = entity
                    }

                    override fun onError(exception: Throwable) {
                        exception.handleThrowable(this@ClassScheduleActivity)
                        LoadingDialogUtil.closeLoadingDialog()
                        titleView.setRightText("编辑")
                        adapter.isEdit = false
                    }
                }
            }
            EBagApi.getBaseInfo("subject", infoRequest!!)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scheduleRequest.cancelRequest()
        if(editRequest != null)
            editRequest!!.cancelRequest()
        if(infoRequest != null)
            infoRequest!!.cancelRequest()
    }

    override fun onBackPressed() {
        if(adapter.isEdit){
            adapter.isEdit = false
            titleView.setRightText("编辑")
        }else{
            super.onBackPressed()
        }
    }

    inner class Adapter: BaseQuickAdapter<ClassScheduleBean.ScheduleBean, BaseViewHolder>(R.layout.item_class_schedule){

        var isEdit = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

        override fun convert(helper: BaseViewHolder, item: ClassScheduleBean.ScheduleBean?) {
            val tv = helper.getView<TextView>(R.id.text)
            tv.text = item?.subject
            when(helper.adapterPosition / 5){
                0 -> tv.setTextColor(resources.getColor(R.color.schedule_monday_color))
                1 -> tv.setTextColor(resources.getColor(R.color.schedule_tuesday_color))
                2 -> tv.setTextColor(resources.getColor(R.color.schedule_wednesday_color))
                3 -> tv.setTextColor(resources.getColor(R.color.schedule_thursday_color))
                4 -> tv.setTextColor(resources.getColor(R.color.schedule_friday_color))
            }
            tv.isSelected = currentWeek == item?.week
            helper.setGone(R.id.arrowView, isEdit)
        }
    }
}