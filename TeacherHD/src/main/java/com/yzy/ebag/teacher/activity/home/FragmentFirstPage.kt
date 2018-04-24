package com.yzy.ebag.teacher.activity.home

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.youth.banner.loader.ImageLoader
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.activity.BookListActivity
import com.yzy.ebag.teacher.activity.ZixiActivity
import com.yzy.ebag.teacher.activity.assignment.AssignmentActivity
import com.yzy.ebag.teacher.activity.correcting.CorrectingActivity
import com.yzy.ebag.teacher.activity.correcting.CorrectingDescActivity
import com.yzy.ebag.teacher.activity.prepare.MyPrepareActivity
import com.yzy.ebag.teacher.base.Constants
import com.yzy.ebag.teacher.bean.FirstPageBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.base.BaseFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.core.util.loadImage
import ebag.core.xRecyclerView.adapter.RecyclerAdapter
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder
import ebag.hd.util.checkUpdate
import kotlinx.android.synthetic.main.fragment_first_page.*

/**
 * Created by YZY on 2017/12/21.
 */
class FragmentFirstPage : BaseFragment() {
    private var request: RequestCallBack<FirstPageBean>? = null
    companion object {
        fun newInstance() : Fragment {
            val fragment = FragmentFirstPage()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun getLayoutRes(): Int {
        return R.layout.fragment_first_page
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden){
            mContext.checkUpdate(ebag.hd.base.Constants.UPDATE_TEACHER, false)
            TeacherApi.firstPage(request!!)
        }
    }

    override fun getBundle(bundle: Bundle?) {

    }

    override fun initViews(rootView: View) {
        val intent = Intent(mContext, AssignmentActivity::class.java)
        classTest.setOnClickListener {
            startActivity(intent
                    .putExtra(Constants.ASSIGN_CATEGORY, Constants.ASSIGN_WORK)
                    .putExtra(Constants.ASSIGN_TITLE, resources.getString(R.string.assign_class_test)))
        }
        afterClass.setOnClickListener {
            startActivity(intent
                    .putExtra(Constants.ASSIGN_CATEGORY, Constants.ASSIGN_AFTER)
                    .putExtra(Constants.ASSIGN_TITLE, resources.getString(R.string.assign_after_class)))
        }
        testPaper.setOnClickListener {
            startActivity(intent
                    .putExtra(Constants.ASSIGN_CATEGORY, Constants.ASSIGN_TEST_PAPER)
                    .putExtra(Constants.ASSIGN_TITLE, getString(R.string.assign_system_test_paper)))
        }
        prepare.setOnClickListener {
            MyPrepareActivity.jump(mContext)
        }
        checkHomework.setOnClickListener {
            startActivity(Intent(mContext, CorrectingActivity::class.java))
        }
        book.setOnClickListener {
            BookListActivity.jump(mContext)
        }
        zixi.setOnClickListener {
            ZixiActivity.jump(mContext)
        }

        setTextStyle(classTest.text.toString(), classTest)
        setTextStyle(afterClass.text.toString(), afterClass)
        setTextStyle(testPaper.text.toString(), testPaper)
        setTextStyle(checkHomework.text.toString(), checkHomework)

        val adapter = HomeProgressAdapter()
        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        adapter.setOnItemClickListener { holder, view, position ->
            val bean = adapter.datas[position]
            val id = bean.id
            val type = bean.type
            if (StringUtils.isEmpty(id) || StringUtils.isEmpty(type))
                T.show(mContext, "作业信息不全！")
            else
                CorrectingDescActivity.jump(mContext, bean.id, bean.type)
        }

        if (request == null)
            request = object : RequestCallBack<FirstPageBean>(){
                override fun onStart() {
                    LoadingDialogUtil.showLoading(mContext)
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
        TeacherApi.firstPage(request!!)
    }

    private inner class MyImageLoader : ImageLoader(){
        override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
            imageView!!.loadImage(path as String)
        }
    }
    inner class HomeProgressAdapter: RecyclerAdapter<FirstPageBean.ResultHomeWorkVosBean>(R.layout.item_home_schedule) {
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: FirstPageBean.ResultHomeWorkVosBean) {
            val numTv = setter.getTextView(R.id.numTv)
            val progressBar = setter.getView<ProgressBar>(R.id.progress_id)
            val current = Integer.parseInt(entity.homeWorkCompleteCount)
            val total = Integer.parseInt(entity.studentHomeWorkCount)
            progressBar.max = total
            progressBar.progress = current

            setter.setText(R.id.subjectTv, entity.subject)
            setter.setText(R.id.class_tv_id, "${entity.gradeByClazzName}")
            setter.getTextView(R.id.class_tv_id).isSelected = true

            val name = entity.className ?: ""
            setWorkTextStyle(entity.homeWorkCompleteCount, entity.studentHomeWorkCount,numTv)
        }
    }

    /**
     * 卡片字体样式
     */
    private fun setTextStyle(string: String, textView : TextView){
        val spannableString = SpannableString(string)
        spannableString.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.x24),false), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.x18),false), 4, string.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(StyleSpan(Typeface.NORMAL), 4, string.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannableString
    }

    /**
     * 作业进度字体样式
     */
    private fun setWorkTextStyle(current: String, total: String, textView: TextView){
        val spannableString = SpannableString("$current/$total")
        spannableString.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.x30),false), 0, current.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan(resources.getColor(R.color.progress_second_bg)), 0, current.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.x22),false), current.length, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan(resources.getColor(R.color.title_text_color)), current.length, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannableString
    }

    override fun onDestroy() {
        if (request != null)
            request!!.cancelRequest()
        super.onDestroy()
    }

}