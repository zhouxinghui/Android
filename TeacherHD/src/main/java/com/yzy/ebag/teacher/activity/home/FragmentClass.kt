package com.yzy.ebag.teacher.activity.home

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.activity.clazz.CreateClassActivity
import com.yzy.ebag.teacher.activity.clazz.SpaceActivity
import com.yzy.ebag.teacher.base.Constants
import com.yzy.ebag.teacher.bean.SpaceBean
import com.yzy.ebag.teacher.http.TeacherApi
import com.yzy.ebag.teacher.widget.AddTeacherDialog
import ebag.core.base.BaseFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.loadHead
import ebag.core.xRecyclerView.adapter.RecyclerAdapter
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder
import ebag.hd.activity.ClazzmateActivity
import kotlinx.android.synthetic.main.fragment_class.*

/**
 * Created by YZY on 2017/12/21.
 */
class FragmentClass : BaseFragment() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_class
    }
    private val addTeacherDialog by lazy {
        val dialog = AddTeacherDialog(mContext)
        dialog.onAddSuccess = {TeacherApi.clazzSpace(request)}
        dialog
    }
    private val adapter by lazy{ ClazzAdapter() }

    private val request by lazy {
        object : RequestCallBack<List<SpaceBean>>(){
            override fun onStart() {
                stateView.showLoading()
            }
            override fun onSuccess(entity: List<SpaceBean>?) {
                if (entity == null || entity.isEmpty()){
                    stateView.showEmpty()
                    return
                }
                stateView.showContent()
                adapter.datas = entity
            }

            override fun onError(exception: Throwable) {
                stateView.showError("${exception.message}")
                exception.handleThrowable(mContext)
            }

        }
    }
    companion object {
        fun newInstance() : Fragment {
            val fragment = FragmentClass()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getBundle(bundle: Bundle?) {

    }

    override fun initViews(rootView: View) {
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener { holder, view, position ->
        }
        adapter.setOnItemChildClickListener { holder, view, position ->
            when(view.id){
                R.id.add_teacher_btn ->{
                    addTeacherDialog.show(adapter.datas[position].classId, adapter.datas[position].gradeCode)
                }
                R.id.class_space_btn ->{
                    val bean = adapter.datas[position]
                    SpaceActivity.jump(mContext, bean.classId, bean.clazzName, bean.gradeCode)
                }

                R.id.student_list_btn ->{
                    val intent = Intent(activity,ClazzmateActivity::class.java)
                    intent.putExtra("classId",adapter.datas[position].classId)
                    startActivity(intent)
                }
            }
        }
        createClazz.setOnClickListener {
            CreateClassActivity.jump(this)
        }
        TeacherApi.clazzSpace(request)
        stateView.setOnRetryClickListener {
            TeacherApi.clazzSpace(request)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.CREATE_CLASS_REQUEST && resultCode == Constants.CREATE_CLASS_RESULT)
            TeacherApi.clazzSpace(request)
    }

    inner class ClazzAdapter : RecyclerAdapter<SpaceBean>(R.layout.item_fragment_class){
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: SpaceBean?) {
            setter.setText(R.id.class_name_id, entity?.clazzName)
            setter.setText(R.id.class_desc_id, getString(R.string.desc, entity?.inviteCode, entity?.studentCount))
            setter.addClickListener(R.id.add_teacher_btn)
            setter.addClickListener(R.id.class_space_btn)
            setter.addClickListener(R.id.student_list_btn)
            val adapter = ClassMemberAdapter()
            val recyclerView = setter.getView<RecyclerView>(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = adapter
            adapter.datas = entity?.clazzUserVoList
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