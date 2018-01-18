package com.yzy.ebag.teacher.widget

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.GridLayoutManager
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.view.*
import android.widget.PopupWindow
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.BaseStudentBean
import com.yzy.ebag.teacher.bean.ClassMemberBean
import com.yzy.ebag.teacher.bean.GroupBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.http.network.RequestCallBack
import ebag.core.util.DensityUtil
import ebag.core.util.T
import ebag.hd.base.Constants
import ebag.hd.http.handleThrowable
import kotlinx.android.synthetic.main.dialog_group_manager.*

/**
 * Created by YZY on 2018/1/18.
 */
class GroupManageDialog(context: Context, classId: String): Dialog(context)  {
    private val classMemberRequest = object: RequestCallBack<ClassMemberBean>(){
        override fun onStart() {
            stateView.showLoading()
        }
        override fun onSuccess(entity: ClassMemberBean?) {
            val studentList = entity?.students
            if (studentList == null || studentList.isEmpty()) {
                stateView.showEmpty()
                return
            }
            allAdapter.setNewData(studentList)
            stateView.showContent()
        }

        override fun onError(exception: Throwable) {
            stateView.showError()
            exception.handleThrowable(context)
        }
    }
    private val createGroupRequest = object : RequestCallBack<String>(){
        override fun onStart() {
            T.showLong(context, "正在创建小组...")
        }
        override fun onSuccess(entity: String?) {
            T.show(context, "创建成功")
            dismiss()
            onGroupChangeListener?.invoke()
        }

        override fun onError(exception: Throwable) {
            exception.handleThrowable(context)
        }
    }
    var onGroupChangeListener: (() -> Unit)? = null

    private val allAdapter by lazy { AllAdapter() }
    private val memberAdapter by lazy { MemberAdapter() }
    private val groupMemberList by lazy { ArrayList<BaseStudentBean>() }
    private var isModify = false
    private val popupContentView = layoutInflater.inflate(R.layout.set_group_charge, null) as TextView
    private val popupWindow by lazy {
        val pop = PopupWindow()
        pop.width = WindowManager.LayoutParams.WRAP_CONTENT
        pop.height = WindowManager.LayoutParams.WRAP_CONTENT
        pop.contentView = popupContentView
        pop.isFocusable = true
        pop.isOutsideTouchable = true
        popupContentView.setOnClickListener {
            if (currentBaseStudentBean?.duties == Constants.GROUP_LEADER){//取消组长
                currentBaseStudentBean?.duties = Constants.STUDENT
                tempStudentBean = null
            }else{ // 设置为组长
                currentBaseStudentBean?.duties = Constants.GROUP_LEADER
                tempStudentBean?.duties = Constants.STUDENT
                tempStudentBean = currentBaseStudentBean
            }
            pop.dismiss()
            memberAdapter.notifyDataSetChanged()
        }
        pop.setBackgroundDrawable(ColorDrawable(0))
        pop
    }
    private var currentBaseStudentBean: BaseStudentBean? = null
    private var tempStudentBean: BaseStudentBean? = null
    init {
        val contentView = LayoutInflater.from(context).inflate(R.layout.dialog_group_manager, null)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        setContentView(contentView)
        window.setBackgroundDrawable(BitmapDrawable())
        val params = window.attributes
        params.width = DensityUtil.dip2px(context, context.resources.getDimensionPixelSize(R.dimen.x550).toFloat())
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = params
        val sps = SpannableString(context.getString(R.string.group_member))
        sps.setSpan(AbsoluteSizeSpan(context.resources.getDimensionPixelSize(R.dimen.tv_normal),false), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sps.setSpan(AbsoluteSizeSpan(context.resources.getDimensionPixelSize(R.dimen.tv_third),false), 4, sps.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        groupMemberTv.text = sps

        allRecycler.adapter = allAdapter
        allRecycler.layoutManager = GridLayoutManager(context, 5)
        selectedRecycler.adapter = memberAdapter
        selectedRecycler.layoutManager = GridLayoutManager(context, 5)

        confirmTv.setOnClickListener {
            val groupName = groupNameEdit.text.toString()
            //TODO if(isModify)
            TeacherApi.createGroup(classId, groupName, groupMemberList, createGroupRequest)
        }
        cancelTv.setOnClickListener { dismiss() }

        memberAdapter.setNewData(groupMemberList)
        TeacherApi.clazzMember(classId, classMemberRequest)

        memberAdapter.setOnItemLongClickListener { _, view, position ->
            currentBaseStudentBean = memberAdapter.getItem(position)
            view.tag = currentBaseStudentBean?.duties
            showGroupSet(view)
            true
        }
    }

    fun show(groupList: List<GroupBean.ClazzUserVosBean>, groupName: String, groupId: String) {
        isModify = true
        groupMemberList.clear()
        groupNameEdit.setText(groupName)
        groupList.forEach {
            val groupMemberBean = BaseStudentBean()
            groupMemberBean.uid = it.uid
            groupMemberBean.duties = it.duties
            groupMemberBean.name = it.name
            if (groupMemberBean.duties == Constants.GROUP_LEADER)
                tempStudentBean = groupMemberBean
            groupMemberList.add(groupMemberBean)
        }
        memberAdapter.notifyDataSetChanged()
        allAdapter.notifyDataSetChanged()
        super.show()
    }

    override fun show() {
        isModify = false
        groupNameEdit.setText("")
        groupMemberList.clear()
        memberAdapter.notifyDataSetChanged()
        allAdapter.notifyDataSetChanged()
        super.show()
    }

    inner class AllAdapter: BaseQuickAdapter<ClassMemberBean.StudentsBean, BaseViewHolder>(R.layout.item_all_group_member){
        override fun convert(helper: BaseViewHolder, item: ClassMemberBean.StudentsBean) {
            helper.setText(R.id.name_id, item.name)
                    .itemView.setOnClickListener {
                if (groupMemberList.contains(item)) {
                    groupMemberList.remove(item)
                }else {
                    val groupMemberBean = BaseStudentBean()
                    groupMemberBean.uid = item.uid
                    groupMemberBean.duties = item.duties
                    groupMemberBean.name = item.name
                    groupMemberList.add(groupMemberBean)
                }
                notifyDataSetChanged()
                memberAdapter.notifyDataSetChanged()
            }
            val nameTv = helper.getView<TextView>(R.id.name_id)
            nameTv.isSelected = groupMemberList.contains(item)
        }
    }

    inner class MemberAdapter : BaseQuickAdapter<BaseStudentBean, BaseViewHolder>(R.layout.item_group_member) {
        override fun convert(helper: BaseViewHolder, item: BaseStudentBean) {
            val deleteTv = helper.getView<TextView>(R.id.deleteTv)
            val position = helper.adapterPosition
            helper.setText(R.id.name_id, item.name)
            deleteTv.setOnClickListener {
                remove(position)
                if (groupMemberList.contains(item))
                    groupMemberList.remove(item)
                allAdapter.notifyDataSetChanged()
            }
            val nameTv = helper.getView<TextView>(R.id.name_id)
            nameTv.isSelected = item.duties == Constants.GROUP_LEADER
        }
    }

    private fun showGroupSet(view: View){
        val location = IntArray(2)
        view.getLocationInWindow(location)
        val x = location[0]
        val y = location[1]
        val duties = view.tag as String
        if (duties == Constants.GROUP_LEADER)
            popupContentView.text = context.resources.getString(R.string.cancel_group_leader)
        else
            popupContentView.text = context.resources.getString(R.string.set_group_leader)
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, x, y - view.height * 2)
    }
}