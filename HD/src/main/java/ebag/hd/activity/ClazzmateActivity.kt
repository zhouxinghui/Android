package ebag.hd.activity

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.hd.R
import ebag.hd.adapter.ClazzItemAdapter
import ebag.hd.adapter.ClazzmateAdapter
import ebag.hd.bean.BaseClassesBean
import ebag.hd.bean.ClassMemberBean
import ebag.hd.dialog.ClazzmateInfoDIalog
import ebag.hd.http.EBagApi
import kotlinx.android.synthetic.main.activity_myclassmate.*

/**
 * Created by fansan on 2018/4/2.
 */
class ClazzmateActivity : BaseActivity() {

    private var clazzList: MutableList<BaseClassesBean> = mutableListOf()
    private var clazzmateList: MutableList<ClassMemberBean.StudentsBean> = mutableListOf()
    private lateinit var clazzAdapter: ClazzItemAdapter
    private lateinit var clazzmateAdapter: ClazzmateAdapter
    override fun getLayoutId(): Int = R.layout.activity_myclassmate
    private lateinit var clazzId: String
    private var nowPosition: Int = 0

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


        clazzmateAdapter = ClazzmateAdapter(clazzmateList)
        clazzmate_rv.layoutManager = GridLayoutManager(this, 5)
        clazzmate_rv.adapter = clazzmateAdapter
        clazzmateAdapter.setOnItemClickListener { adapter, view, position ->
            val b = Bundle()
            b.putSerializable("data", clazzmateList[position])
            val f = ClazzmateInfoDIalog.newInstance()
            f.arguments = b
            f.show(supportFragmentManager, "clazzDialog")

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
                    getClzzmate(clazzList[0].classId)
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
                if (entity?.students!!.isNotEmpty()) {
                    clazzmateList.addAll(entity.students)
                    clazzmateAdapter.notifyDataSetChanged()
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

}