package com.yzy.ebag.teacher.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.SpaceBean
import com.yzy.ebag.teacher.http.TeacherApi
import com.yzy.ebag.teacher.ui.activity.CreateClassActivity
import com.yzy.ebag.teacher.ui.activity.SpaceActivity
import com.yzy.ebag.teacher.widget.AddTeacherDialog
import ebag.core.base.BaseFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.util.T
import ebag.core.util.loadHead
import ebag.core.xRecyclerView.adapter.RecyclerAdapter
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder
import kotlinx.android.synthetic.main.fragment_class.*

/**
 * Created by YZY on 2017/12/21.
 */
class FragmentClass : BaseFragment() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_class
    }
    private val addTeacherDialog by lazy { AddTeacherDialog(mContext) }
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
                stateView.showError()
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
            T.show(mContext, "点击条目")
        }
        adapter.setOnItemChildClickListener { holder, view, position ->
            when(view.id){
                R.id.add_teacher_btn ->{
                    T.show(mContext, "添加老师")
                    addTeacherDialog.show()
                }
                R.id.class_space_btn ->{
                    SpaceActivity.jump(mContext, adapter.datas[position].classId, adapter.datas[position].clazzName)
                }
            }
        }
        createClazz.setOnClickListener {
            startActivity(Intent(mContext, CreateClassActivity::class.java))
        }
        TeacherApi.clazzSpace(request)
        stateView.setOnRetryClickListener {
            TeacherApi.clazzSpace(request)
        }
    }

    inner class ClazzAdapter : RecyclerAdapter<SpaceBean>(R.layout.item_fragment_class){
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: SpaceBean?) {
            setter.setText(R.id.class_name_id, entity?.clazzName)
            setter.setText(R.id.class_desc_id, String.format(getString(R.string.desc), entity?.inviteCode, entity?.studentCount))
            setter.addClickListener(R.id.add_teacher_btn)
            setter.addClickListener(R.id.class_space_btn)
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