package com.yzy.ebag.teacher.module.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.SpaceBean
import com.yzy.ebag.teacher.http.TeacherApi
import com.yzy.ebag.teacher.module.clazz.SpaceActivity
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.util.loadHead
import ebag.core.xRecyclerView.adapter.RecyclerAdapter
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder
import ebag.mobile.bean.ClassMemberBean
import ebag.mobile.module.clazz.ClazzmateActivity
import ebag.mobile.widget.ClazzmateInfoDIalog

/**
 * Created by YZY on 2018/4/16.
 */
class ClassFragment: BaseListFragment<List<SpaceBean>, SpaceBean>() {
    companion object {
        fun newInstance(): ClassFragment{
            val fragment = ClassFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
    private val addTeacherDialog by lazy {
        val dialog = AddTeacherDialog(mContext)
        dialog.onAddSuccess = {onRetryClick()}
        dialog
    }
    override fun getBundle(bundle: Bundle?) {

    }

    override fun isPagerFragment(): Boolean = false

    override fun loadConfig() {
        loadMoreEnabled(false)
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<SpaceBean>>) {
        TeacherApi.clazzSpace(requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<SpaceBean>?): List<SpaceBean>? = parent

    override fun getAdapter(): BaseQuickAdapter<SpaceBean, BaseViewHolder> = ClazzAdapter()

    override fun getLayoutManager(adapter: BaseQuickAdapter<SpaceBean, BaseViewHolder>): RecyclerView.LayoutManager? = null

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        adapter as ClazzAdapter
        when(view?.id){
            R.id.add_teacher_btn ->{
                addTeacherDialog.show(adapter.data[position].classId, adapter.data[position].gradeCode)
            }
            R.id.class_space_btn ->{
                val bean = adapter.data[position]
                SpaceActivity.jump(mContext, bean.classId, bean.clazzName, bean.gradeCode)
            }

            R.id.student_list_btn ->{
                val intent = Intent(activity, ClazzmateActivity::class.java)
                intent.putExtra("classId",adapter.data[position].classId)
                startActivity(intent)
            }
        }
    }

    inner class ClazzAdapter : BaseQuickAdapter<SpaceBean, BaseViewHolder>(R.layout.item_fragment_class){
        override fun convert(setter: BaseViewHolder, entity: SpaceBean?) {
            setter.setText(R.id.class_name_id, entity?.clazzName)
            setter.setText(R.id.class_desc_id, "班级邀请码：${entity?.inviteCode}    学生人数：${entity?.studentCount}")
            setter.addOnClickListener(R.id.add_teacher_btn)
            setter.addOnClickListener(R.id.class_space_btn)
            setter.addOnClickListener(R.id.student_list_btn)
            val adapter = ClassMemberAdapter()
            val recyclerView = setter.getView<RecyclerView>(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = adapter
            adapter.datas = entity?.clazzUserVoList
            adapter.setOnItemChildClickListener { holder, view, position ->
                val b = entity!!.clazzUserVoList[position]
                val bean = ClassMemberBean.StudentsBean()
                bean.city = b.city
                bean.birthday = b.birthday
                bean.county = b.county
                bean.headUrl = b.headUrl
                bean.phone = b.phone
                bean.name = b.name
                bean.schoolName = b.schoolName
                bean.ysbCode = b.ysbCode
                bean.sex = b.sex

                val bundle = Bundle()
                bundle.putSerializable("data", bean)
                val f = ClazzmateInfoDIalog.newInstance()
                f.arguments = bundle
                f.show(fragmentManager, "clazzinfoDialog")
            }
        }
    }
    inner class ClassMemberAdapter: RecyclerAdapter<SpaceBean.ClazzUserVoListBean>(R.layout.item_class_member) {
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: SpaceBean.ClazzUserVoListBean?) {
            val imageView = setter.getImageView(R.id.img_head)
            imageView.loadHead(entity?.headUrl)
            setter.setText(R.id.name_id, entity?.name)
        }
    }

}