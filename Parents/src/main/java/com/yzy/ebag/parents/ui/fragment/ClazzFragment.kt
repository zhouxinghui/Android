package com.yzy.ebag.parents.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.mvp.ClazzMainContract
import com.yzy.ebag.parents.mvp.model.PersonalItemModel
import com.yzy.ebag.parents.mvp.presenter.ClazzMainPresenter
import com.yzy.ebag.parents.ui.activity.AchievementActivity
import com.yzy.ebag.parents.ui.activity.NoticeListActivity
import com.yzy.ebag.parents.ui.adapter.PersonalAdapter
import com.yzy.ebag.parents.ui.widget.ClassJoinDialog
import ebag.core.base.BaseFragment
import ebag.core.http.network.handleThrowable
import ebag.core.util.DateUtil
import ebag.core.util.SerializableUtils
import ebag.core.util.loadHead
import ebag.mobile.base.Constants
import ebag.mobile.bean.MyChildrenBean
import ebag.mobile.bean.NoticeBean
import ebag.mobile.module.clazz.ClassScheduleActivity
import ebag.mobile.module.clazz.ClazzmateActivity
import ebag.mobile.module.clazz.album.AlbumActivity
import kotlinx.android.synthetic.main.fragment_class.*

class ClazzFragment : BaseFragment(), ClazzMainContract.ClazzMainView {


    private lateinit var mPersenter: ClazzMainPresenter
    private val datas: MutableList<PersonalItemModel> = mutableListOf()
    private val labels: Array<String> = arrayOf("成绩统计", "班级相册", "课程表", "","加入班级")
    private val icons: Array<Int> = arrayOf(R.drawable.icon_class_tongji, R.drawable.icon_class_class_photo, R.drawable.icon_class_class_schedule_card, R.drawable.icon_class_banji,R.drawable.my_class_add_icon)
    private var isFirst = true
    private var isViewPrepare = false
    private var bean = SerializableUtils.getSerializable<MyChildrenBean>(Constants.CHILD_USER_ENTITY)
    private lateinit var mAdapter:PersonalAdapter

    companion object {
        fun newInstance(): ClazzFragment {
            val fragment = ClazzFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val joinDialog by lazy {
        val dialog = ClassJoinDialog.newInstance()
        dialog.successListener = {
            updataClazz()
            mPersenter.queryClazzNews(bean.classId)
            dialog.dismiss()
        }
        dialog
    }

    override fun getLayoutRes(): Int = R.layout.fragment_class

    override fun getBundle(bundle: Bundle?) {

    }

    override fun initViews(rootView: View) {
        mPersenter = ClazzMainPresenter(this)
        recyclerview.layoutManager = LinearLayoutManager(activity)
        labels[3] = SerializableUtils.getSerializable<MyChildrenBean>(Constants.CHILD_USER_ENTITY).className
        labels.forEachIndexed { index, s ->
            datas.add(PersonalItemModel(icons[index], s))
        }
        mAdapter = PersonalAdapter(datas)
        recyclerview.adapter = mAdapter
        recyclerview.addItemDecoration(ebag.core.xRecyclerView.manager.DividerItemDecoration(DividerItemDecoration.VERTICAL, 1, Color.parseColor("#e0e0e0")))
        isViewPrepare = true

        more_notice.setOnClickListener {

            NoticeListActivity.start(activity)
        }

        mAdapter.setOnItemClickListener { _, _, position ->

            when (position) {
                1 -> {
                    AlbumActivity.jump(activity, (SerializableUtils.getSerializable(Constants.CHILD_USER_ENTITY) as MyChildrenBean).classId, Constants.ROLE_PARENT)
                }
                2 -> {
                    ClassScheduleActivity.jump(activity, (SerializableUtils.getSerializable(Constants.CHILD_USER_ENTITY) as MyChildrenBean).classId)
                }
                3 -> {
                    startActivity(Intent(activity, ClazzmateActivity::class.java).putExtra("classId", bean.classId))
                }

                0 -> {
                    AchievementActivity.jump(activity,(SerializableUtils.getSerializable(Constants.CHILD_USER_ENTITY) as MyChildrenBean).grade)
                }

                4 -> joinDialog.show(childFragmentManager,"joindialog")
            }
        }


    }


    fun updataClazz(){
        bean = SerializableUtils.getSerializable<MyChildrenBean>(Constants.CHILD_USER_ENTITY)
        datas[3].label = SerializableUtils.getSerializable<MyChildrenBean>(Constants.CHILD_USER_ENTITY).className?:"暂无班级"
        mAdapter.notifyDataSetChanged()
        isFirst = true
    }


    override fun showLoading() {
        stateview.showLoading()
    }

    override fun showEmpty() {
        stateview.showEmpty()
    }

    override fun <T> showContent(data: T?) {
        val bean = (data as NoticeBean)
        head.loadHead(bean.headUrl)
        name.text = bean.name
        date.text = DateUtil.getDateTime(bean.createDate)
        content.text = bean.content
        stateview.showContent()
    }

    override fun showError(e: Throwable?) {
        e?.handleThrowable(activity)
        stateview.showError("暂无公告")
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isViewPrepare && isFirst) {
            mPersenter.queryClazzNews(bean.classId)
            isFirst = false
        }
    }
}