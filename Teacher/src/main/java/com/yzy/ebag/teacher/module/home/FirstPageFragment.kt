package com.yzy.ebag.teacher.module.home

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.youth.banner.loader.ImageLoader
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.base.Constants
import com.yzy.ebag.teacher.bean.FirstPageBean
import com.yzy.ebag.teacher.http.TeacherApi
import com.yzy.ebag.teacher.module.book.BookListActivity
import com.yzy.ebag.teacher.module.homework.AssignmentActivity
import ebag.core.base.BaseFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.core.util.loadImage
import ebag.core.xRecyclerView.adapter.RecyclerAdapter
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder
import kotlinx.android.synthetic.main.fragment_first_page.*

/**
 * Created by YZY on 2018/4/16.
 */
class FirstPageFragment: BaseFragment() {
    private var request = object : RequestCallBack<FirstPageBean>(){
        override fun onStart() {
//            LoadingDialogUtil.showLoading(mContext)
        }
        override fun onSuccess(entity: FirstPageBean?) {
            LoadingDialogUtil.closeLoadingDialog()
            //轮播图
            val images = ArrayList<String>()
            entity?.resultAdvertisementVos?.mapTo(images) { it.adverUrl }
            banner.setImageLoader(MyImageLoader()).setImages(images).start()
            //作业进度
            adapter.datas = entity?.resultHomeWorkVos
        }

        override fun onError(exception: Throwable) {
            LoadingDialogUtil.closeLoadingDialog()
        }
    }
    private val adapter = HomeProgressAdapter()
    companion object {
        fun newInstance(): FirstPageFragment{
            val fragment = FirstPageFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun getLayoutRes(): Int {
        return R.layout.fragment_first_page
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userVisibleHint = userVisibleHint
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (!isVisibleToUser){
//            mContext.checkUpdate(ebag.hd.base.Constants.UPDATE_TEACHER, false)
            TeacherApi.firstPage(request)
        }
    }

    /*override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden){
//            mContext.checkUpdate(ebag.hd.base.Constants.UPDATE_TEACHER, false)
            TeacherApi.firstPage(request!!)
        }
    }*/


    override fun getBundle(bundle: Bundle?) {

    }

    override fun initViews(rootView: View) {
        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        classTest.setOnClickListener {
            AssignmentActivity.jump(mContext, Constants.ASSIGN_WORK, classTest.text.toString())
        }
        afterClass.setOnClickListener {
            AssignmentActivity.jump(mContext, Constants.ASSIGN_AFTER, afterClass.text.toString())
        }
        testPaper.setOnClickListener {
            AssignmentActivity.jump(mContext, Constants.ASSIGN_TEST_PAPER, testPaper.text.toString())
        }
        prepare.setOnClickListener {

        }
        checkHomework.setOnClickListener {
        }
        book.setOnClickListener {
            BookListActivity.jump(mContext)
        }
        zixi.setOnClickListener {
        }

        adapter.setOnItemClickListener { holder, view, position ->
            val bean = adapter.datas[position]
            val id = bean.id
            val type = bean.type
            if (StringUtils.isEmpty(id) || StringUtils.isEmpty(type))
                T.show(mContext, "作业信息不全！")
//            else
//                CorrectingDescActivity.jump(mContext, bean.id, bean.type)
        }
    }

    private inner class MyImageLoader : ImageLoader(){
        override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
            imageView!!.loadImage(path as String)
        }
    }
    inner class HomeProgressAdapter: RecyclerAdapter<FirstPageBean.ResultHomeWorkVosBean>(R.layout.item_home_schedule) {
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: FirstPageBean.ResultHomeWorkVosBean) {
            val classTv = setter.getTextView(R.id.class_tv_id)
            val progressBar = setter.getView<ProgressBar>(R.id.progress_id)
            val current = Integer.parseInt(entity.homeWorkCompleteCount)
            val total = Integer.parseInt(entity.studentHomeWorkCount)
            progressBar.max = total
            progressBar.progress = current

            setter.setText(R.id.subjectTv, entity.subject)

            val name = entity.className ?: ""
            setWorkTextStyle(entity.homeWorkCompleteCount, entity.studentHomeWorkCount,classTv,entity.gradeByClazzName)
        }
    }

    /**
     * 作业进度字体样式
     */
    private fun setWorkTextStyle(current: String, total: String, textView: TextView, grade:String){
        val spannableString = SpannableString("$current/$total\n$grade")
        spannableString.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.tv_big),false), 0, current.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan(resources.getColor(R.color.blue)), 0, current.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.tv_normal),false), current.length, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan(resources.getColor(R.color.tv_normal)), current.length, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannableString
    }

    override fun onDestroy() {
        if (request != null)
            request!!.cancelRequest()
        super.onDestroy()
    }
}