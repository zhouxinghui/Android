package com.yzy.ebag.student.activity.center

import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.activity.center.fragment.*
import com.yzy.ebag.student.activity.growth.GrowthEnterTabFragment
import com.yzy.ebag.student.dialog.PerformanceDialog
import ebag.core.base.mvp.MVPActivity
import ebag.core.util.SerializableUtils
import ebag.core.util.loadHead
import ebag.hd.base.Constants
import ebag.hd.bean.response.UserEntity
import kotlinx.android.synthetic.main.activity_personal.*

/**
 * Created by caoyu on 2018/1/12.
 */
class PersonalActivity: MVPActivity() {

    private val itemStrIds = arrayListOf(
            R.string.center_personal,
            R.string.my_task,
            R.string.my_class,
            R.string.classroom_performance,
            R.string.my_parents,
            R.string.growth_trajectory,
            R.string.center_cloud_coin,
            R.string.operating_guide
    )

    override fun destroyPresenter() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_personal
    }

    override fun initViews() {

        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = Adapter()
        adapter.bindToRecyclerView(recyclerView)
        adapter.setNewData(itemStrIds)

        adapter.setOnItemClickListener { _, _, position ->
            if(adapter.selectedPosition != position){
                when(position){
                    3 -> {
                        performanceDialog.show(supportFragmentManager,"performanceDialog")
                    }
                    else -> {
                        adapter.selectedPosition = position
                        changeFragment(position)
                    }
                }
            }
        }

        val userEntity = SerializableUtils.getSerializable<UserEntity>(Constants.STUDENT_USER_ENTITY)
        if(userEntity != null){
            tvName.text = userEntity.name
            tvId.text = userEntity.ysbCode
            ivHead.loadHead(userEntity.headUrl)
        }

        tvMain.setOnClickListener {
            finish()
        }

        changeFragment(0)
    }


    private val fragmentArrays = arrayOfNulls<Fragment?>(itemStrIds.size)

    private var tempFragment : Fragment? = null

    private fun getFragment(index: Int): Fragment{

        if(fragmentArrays[index] == null){
            fragmentArrays[index] =  when(index){
                0 -> { PersonalFragment.newInstance() }//个人信息
                1 -> { TaskTabFragment.newInstance("")}//我的任务
                2 -> { ClassesFragment.newInstance()}//我的班级
                4 -> {
                    ParentFragment.newInstance()
                }//我的家长
                5 -> {//成长轨迹
                    GrowthEnterTabFragment.newInstance()
                }
                6 -> {
                    YbCenterFragment.newInstance()
                }
                else ->{ PersonalFragment.newInstance() }
            }
        }

        return fragmentArrays[index]!!
    }
    /**
     * 显示指定的Fragment
     *
     * @param index
     */
    private fun changeFragment(index: Int) {
        val fragment = getFragment(index)
        //v4中Fragment: 获取Fragment事务管理
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        //true: 表示当前的Fragment已经添加到FragmentManager中， false:则反
        if (tempFragment != null) {
            if (fragment.isAdded) {
                fragmentTransaction.hide(tempFragment).show(fragment)
            } else {
                //hide上一个Fragment，add：当前Fragment
                fragmentTransaction.hide(tempFragment).add(R.id.fragmentLayout, fragment)
            }
        } else {
            fragmentTransaction.add(R.id.fragmentLayout, fragment)
        }

        tempFragment = fragment
        if (!isDestroy) {
            /**
             * 警告：你只能在activity处于可保存状态的状态时，比如running中，onPause()方法和onStop()方法中提交事务，否则会引发异常。
             * 这是因为fragment的状态会丢失。如果要在可能丢失状态的情况下提交事务，请使用commitAllowingStateLoss()。
             */
            fragmentTransaction.commitAllowingStateLoss()   //提交
        }
    }


    val performanceDialog by lazy {
        PerformanceDialog.newInstance()
    }

    private class Adapter: BaseQuickAdapter<Int,BaseViewHolder>(R.layout.item_activity_personal_left){

        var selectedPosition = 0
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun convert(helper: BaseViewHolder?, item: Int?) {
            helper?.setText(R.id.tvText,item ?: R.string.empty)
                    ?.setGone(R.id.line, helper.adapterPosition == itemCount)
                    ?.getView<TextView>(R.id.tvText)?.isSelected = ((helper?.adapterPosition ?: 0) == selectedPosition)
        }
    }

}