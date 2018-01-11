package com.yzy.ebag.teacher.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.ui.activity.SpaceActivity
import com.yzy.ebag.teacher.widget.AddTeacherDialog
import ebag.core.base.BaseFragment
import ebag.core.util.T
import ebag.core.util.loadImageToCircle
import ebag.core.xRecyclerView.adapter.RecyclerAdapter
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder
import kotlinx.android.synthetic.main.fragment_class.*

/**
 * Created by YZY on 2017/12/21.
 */
class FragmentClass : BaseFragment() {
    private val addTeacherDialog by lazy {
        AddTeacherDialog(mContext)
    }
    companion object {
        fun newInstance() : Fragment {
            val fragment = FragmentClass()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun getLayoutRes(): Int {
        return R.layout.fragment_class
    }

    override fun getBundle(bundle: Bundle) {

    }

    override fun initViews(rootView: View) {
        val adapter = ClazzAdapter()
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerView.adapter = adapter
        val list = ArrayList<String>()
        for (i in 0..9){
            list.add("测试")
        }
        adapter.datas = list
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
                    startActivity(Intent(mContext, SpaceActivity::class.java))
                }
            }
        }
        createClazz.setOnClickListener {
            T.show(mContext, "创建班级")
        }
    }

    inner class ClazzAdapter : RecyclerAdapter<String>(R.layout.item_fragment_class){
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: String?) {
            setter.setText(R.id.class_name_id, entity)
            setter.addClickListener(R.id.add_teacher_btn)
            setter.addClickListener(R.id.class_space_btn)
            val adapter = ClassMemberAdapter()
            val recyclerView = setter.getView<RecyclerView>(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = adapter
            val list = ArrayList<String>()
            for (i in 0..9){
                list.add("测试")
            }
            adapter.datas = list
        }
    }
    inner class ClassMemberAdapter: RecyclerAdapter<String>(R.layout.item_class_member) {
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: String?) {
            val imageView = setter.getImageView(R.id.img_head)
            imageView.loadImageToCircle("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1515596866962&di=e76fb5e0914eee393affee2451f04aa3&imgtype=0&src=http%3A%2F%2Fimg.taopic.com%2Fuploads%2Fallimg%2F120727%2F201995-120HG1030762.jpg")
            setter.setText(R.id.subject_tv_id, "英")
            setter.setText(R.id.name_id, "李老师")
        }
    }
}