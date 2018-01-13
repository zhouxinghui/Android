package com.yzy.ebag.student.activity.homework

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.LinearLayoutManager
import com.yzy.ebag.student.R
import com.yzy.ebag.student.base.Constants
import com.yzy.ebag.student.bean.response.SubjectBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.xRecyclerView.adapter.RecyclerAdapter
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder
import kotlinx.android.synthetic.main.activity_homework.*



/**
 * Created by unicho on 2018/1/9.
 */
class HomeworkActivity : BaseActivity() {

    private var type = "1"
    private var classId = ""

    private val adapter by lazy { Adapter() }

    private val request = object: RequestCallBack<List<SubjectBean>>(){

        override fun onStart() {
            stateView.showLoading()
        }

        override fun onSuccess(entity: List<SubjectBean>?) {
            if(entity == null || entity.isEmpty()){
                stateView.showEmpty()
            }else{
                adapter.datas = entity
                viewPager.adapter = SectionsPagerAdapter(supportFragmentManager, arrayOfNulls(adapter.itemCount))
                viewPager.setCurrentItem(0,false)
                stateView.showContent()
            }
        }

        override fun onError(exception: Throwable) {
            stateView.showError()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_homework
    }

    override fun initViews() {

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter.setOnItemClickListener { _, _, position ->
            adapter.selectedPosition = position
            adapter.notifyDataSetChanged()
            viewPager.setCurrentItem(position,false)
        }

        type = intent.getStringExtra("type")
        classId = intent.getStringExtra("classId")
        when(type){
            Constants.KHZY_TYPE -> {
                titleView.setTitle(R.string.main_khzy)
                request()
            }

            Constants.STZY_TYPE -> {
                titleView.setTitle(R.string.main_stzy)
                request()
            }

            else -> {
                titleView.setTitle(R.string.main_kssj)
            }
        }

        stateView.setOnRetryClickListener {
            request()
        }
    }

    private fun request(){
        StudentApi.subjectWorkList(type, classId, "", 1, 10, request)
    }

    fun getFragment(position: Int): Fragment{
        return HomeworkListFragment.newInstance(
                type,classId,
                adapter.datas[position].subCode,
                adapter.datas[position].homeWorkInfoVos
        )
    }

    inner class SectionsPagerAdapter(fm: FragmentManager,private val fragments: Array<Fragment?>) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            if (fragments[position] == null) {
                fragments[position] = getFragment(position)
            }
            return fragments[position]!!
        }

        override fun getCount(): Int {
            return adapter.itemCount
        }
    }


    private inner class Adapter: RecyclerAdapter<SubjectBean>(R.layout.activity_homework_subject_item){

        var selectedPosition = 0

        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: SubjectBean) {
            setter.setText(R.id.text,entity.subject)
            setter.setBackgroundRes(
                    R.id.dot,
                    if(entity.homeWorkComplete != "0")
                        R.drawable.homework_subject_dot_undo_selector
                    else
                        R.drawable.homework_subject_dot_done_selector
            )
            setter.setSelected(R.id.text ,position == selectedPosition)
            setter.setSelected(R.id.dot ,position == selectedPosition)
        }
    }

}
