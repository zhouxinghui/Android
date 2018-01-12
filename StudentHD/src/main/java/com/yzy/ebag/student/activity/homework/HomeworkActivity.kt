package com.yzy.ebag.student.activity.homework

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.LinearLayoutManager
import com.yzy.ebag.student.R
import ebag.core.base.mvp.MVPActivity
import ebag.core.xRecyclerView.adapter.RecyclerAdapter
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder
import kotlinx.android.synthetic.main.activity_homework.*



/**
 * Created by unicho on 2018/1/9.
 */
class HomeworkActivity : MVPActivity(), HomeworkView {

    private val presenter by lazy { HomeworkPresenter(this,this) }
    private val adapter by lazy { Adapter() }

    override fun destroyPresenter() {
        presenter.onDestroy()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_homework
    }

    override fun initViews() {
        val type = intent.getIntExtra("type",1)
        when(type){
            1 -> { titleView.setTitle(R.string.main_khzy) }
            2 -> { titleView.setTitle(R.string.main_stzy) }
            3 -> { titleView.setTitle(R.string.main_kssj) }
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter.datas = listOf(
                Subject("语文", false),
                Subject("数学", true),
                Subject("英语", false),
                Subject("物理", true)
        )

        adapter.setOnItemClickListener { _, _, position ->
            adapter.selectedPosition = position
            adapter.notifyDataSetChanged()
            viewPager.setCurrentItem(position,false)
        }

        viewPager.adapter = SectionsPagerAdapter(supportFragmentManager, arrayOfNulls(adapter.itemCount))
        viewPager.setCurrentItem(0,false)
    }

    fun getFragment(position: Int): Fragment{
        return HomeworkListFragment.newInstance(adapter.datas[position].title)
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


    private inner class Adapter: RecyclerAdapter<Subject>(R.layout.activity_homework_subject_item){

        var selectedPosition = 0

        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: Subject) {
            setter.setText(R.id.text,entity.title)
            setter.setBackgroundRes(
                    R.id.dot,
                    if(entity.unDo)
                        R.drawable.homework_subject_dot_undo_selector
                    else
                        R.drawable.homework_subject_dot_done_selector
            )
            setter.setSelected(R.id.text ,position == selectedPosition)
            setter.setSelected(R.id.dot ,position == selectedPosition)
        }
    }

}

data class Subject(var title: String, var unDo: Boolean)