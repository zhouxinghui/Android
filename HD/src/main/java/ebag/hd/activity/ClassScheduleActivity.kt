package ebag.hd.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import ebag.hd.R
import ebag.hd.base.Constants
import ebag.hd.bean.ClassScheduleBean
import ebag.hd.bean.request.ClassScheduleEditVo
import ebag.hd.bean.response.BaseInfoEntity
import ebag.hd.dialog.ScheduleEditDialog
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

    private val EDIT_VALUE = "自定义"
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
            if(data.keyValue == EDIT_VALUE){
                editDialog.show()
            }else{
                adapter.getItem(editPosition)?.subject = data.keyValue
                adapter.notifyItemChanged(editPosition)
            }
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
                if(entity != null && entity.duties == Constants.TEACHER_IN_CHARGE){
                    adapter.isEdit = true
                    getSubjectInfo()
                }else{
                    titleView.setRightBtnVisable(false)
                }
                (0 until 50).forEach { position->
                    mScheduleList.add(ClassScheduleBean.ScheduleBean("${(position / 10) + 1}", "${(position % 10) + 1}", classId))
                }
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
                        subjectList = entity ?: ArrayList()
                        val editInfo = BaseInfoEntity()
                        editInfo.keyValue = EDIT_VALUE
                        subjectList!!.add(editInfo)
                        subjectDialog.setData(subjectList)
                    }

                    override fun onError(exception: Throwable) {
                        LoadingDialogUtil.closeLoadingDialog()
                        subjectList = ArrayList()
                        val editInfo = BaseInfoEntity()
                        editInfo.keyValue = EDIT_VALUE
                        subjectList!!.add(editInfo)
                        subjectDialog.setData(subjectList)
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

    private val editDialog by lazy {
        val dialog = ScheduleEditDialog(this)
        dialog.onConfirmClickListener = {
            adapter.getItem(editPosition)?.subject = it
            adapter.notifyItemChanged(editPosition)
        }
        dialog
    }

    inner class Adapter: BaseQuickAdapter<ClassScheduleBean.ScheduleBean, BaseViewHolder>(R.layout.item_class_schedule){

        var isEdit = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

        private val normalColor = Color.parseColor("#37394c")
        private val mondayColor = Color.parseColor("#D44C8C")
        private val tuesdayColor = Color.parseColor("#ff7801")
        private val wednesdayColor = Color.parseColor("#4faf4e")
        private val thursdayColor = Color.parseColor("#ff9f00")
        private val fridayColor = Color.parseColor("#28acff")

        override fun convert(helper: BaseViewHolder, item: ClassScheduleBean.ScheduleBean?) {
            val tv = helper.getView<TextView>(R.id.text)
            tv.text = item?.subject
            if(currentWeek != item?.week){
                tv.setTextColor(normalColor)
            }else{
                tv.setTextColor(when(helper.adapterPosition / 10){
                    0 -> mondayColor
                    1 -> tuesdayColor
                    2 -> wednesdayColor
                    3 -> thursdayColor
                    4 -> fridayColor
                    else -> normalColor
                })
            }
            helper.setGone(R.id.arrowView, isEdit)
        }
    }
}