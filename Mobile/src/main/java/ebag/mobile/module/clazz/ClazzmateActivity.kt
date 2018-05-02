package ebag.mobile.module.clazz

import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.mobile.R
import ebag.mobile.base.Constants
import ebag.mobile.bean.BaseClassesBean
import ebag.mobile.bean.ClassMemberBean
import ebag.mobile.http.EBagApi
import kotlinx.android.synthetic.main.activity_myclassmate.*

/**
 * Created by YZY on 2018/4/20.
 */
class ClazzmateActivity : BaseActivity() {

    private var clazzList: MutableList<BaseClassesBean> = mutableListOf()
    private lateinit var clazzAdapter: ClazzItemAdapter
    override fun getLayoutId(): Int = R.layout.activity_myclassmate
    private lateinit var clazzId: String
    private var nowPosition: Int = 0
    private var memberBean: ClassMemberBean? = null
    override fun initViews() {

        if (intent.hasExtra("classId")) {
            clazzId = intent.getStringExtra("classId")
            clazz_rv.visibility = View.GONE
            getClzzmate(clazzId)

        } else {
            clazzAdapter = ClazzItemAdapter(clazzList)
            clazz_rv.layoutManager = GridLayoutManager(this, 8)
            clazz_rv.adapter = clazzAdapter

            clazzAdapter.setOnItemClickListener { adapter, view, position ->

                switch(position)
                getClzzmate(clazzList[position].classId)
            }

            getClazz()
        }

        second_stateviwe.setOnRetryClickListener {
            if (clazzId.isEmpty()) {
                getClzzmate(clazzList[nowPosition].classId)
            } else {
                getClzzmate(clazzId)
            }

        }

        first_stateviwe.setOnRetryClickListener {
            getClazz()
        }

        teacherRl.setOnClickListener {
            ClassMateSubActivity.jump(this, memberBean?.teachers as ArrayList<ClassMemberBean.SubMemberBean>, Constants.ROLE_TEACHER)
        }
        studentRl.setOnClickListener {
            ClassMateSubActivity.jump(this, memberBean?.students as ArrayList<ClassMemberBean.SubMemberBean>, Constants.ROLE_STUDENT)
        }
        parentRl.setOnClickListener {
            ClassMateSubActivity.jump(this, memberBean?.parents as ArrayList<ClassMemberBean.SubMemberBean>, Constants.ROLE_PARENT)
        }
    }

    private fun getClazz() {

        EBagApi.getMyClasses(object : RequestCallBack<List<BaseClassesBean>>() {

            override fun onStart() {
                first_stateviwe.showLoading()
            }

            override fun onSuccess(entity: List<BaseClassesBean>?) {
                if (entity!!.isNotEmpty()) {
                    clazzList.addAll(entity as MutableList)
                    clazzAdapter.notifyDataSetChanged()
                    first_stateviwe.showContent()
                    clazzId = clazzList[0].classId
                    getClzzmate(clazzId)
                    switch(0)
                } else {
                    first_stateviwe.showEmpty()
                }
            }

            override fun onError(exception: Throwable) {
                first_stateviwe.showError()
                exception.handleThrowable(this@ClazzmateActivity)
            }
        })
    }


    private fun getClzzmate(clazzId: String) {

        EBagApi.clazzMember(clazzId, object : RequestCallBack<ClassMemberBean>() {

            override fun onStart() {
                second_stateviwe.showLoading()
            }

            override fun onSuccess(entity: ClassMemberBean?) {
                if (entity != null) {
                    memberBean = entity
                    teacherCount.text = "老师人数：${entity.teachers.size}"
                    studentCount.text = "学生人数：${entity.students.size}"
                    parentCount.text = "家长人数：${entity.parents.size}"
                    second_stateviwe.showContent()
                } else {
                    second_stateviwe.showEmpty()
                }
            }

            override fun onError(exception: Throwable) {
                second_stateviwe.showError()
                exception.handleThrowable(this@ClazzmateActivity)
            }

        })
    }

    private fun switch(index: Int) {
        nowPosition = index
        clazzList.forEachIndexed { i, it ->
            it.checked = i == index
        }

        clazzAdapter.notifyDataSetChanged()
    }

    private inner class ClazzItemAdapter(data: List<BaseClassesBean>) : BaseQuickAdapter<BaseClassesBean, BaseViewHolder>(R.layout.item_clazztitle, data) {
        override fun convert(helper: BaseViewHolder, item: BaseClassesBean?) {
            helper.setChecked(R.id.clazz_title, item!!.checked)
            helper.setText(R.id.clazz_title, item.className)
        }
    }
}