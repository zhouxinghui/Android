package com.yzy.ebag.teacher.ui.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.youth.banner.loader.ImageLoader
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.HomeProgressBean
import com.yzy.ebag.teacher.ui.activity.AssignmentActivity
import com.yzy.ebag.teacher.ui.adapter.HomeProgressAdapter
import ebag.core.base.BaseFragment
import ebag.core.util.loadImage
import kotlinx.android.synthetic.main.fragment_first_page.*

/**
 * Created by YZY on 2017/12/21.
 */
class FragmentFirstPage : BaseFragment() {
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

    override fun getBundle(bundle: Bundle) {

    }

    override fun initViews(rootView: View) {
        val images = ArrayList<String>()
        images.add("http://static9.photo.sina.com.cn/orignal/4af8a5e8856933841a998")
        images.add("http://img.zcool.cn/community/01902d554c0125000001bf72a28724.jpg@1280w_1l_2o_100sh.jpg")
        banner.setImageLoader(MyImageLoader()).setImages(images).start()

        classTest.setOnClickListener {
            startActivity(Intent(mContext, AssignmentActivity::class.java))
        }

        setTextStyle(classTest.text.toString(), classTest)
        setTextStyle(afterClass.text.toString(), afterClass)
        setTextStyle(testPaper.text.toString(), testPaper)
        setTextStyle(checkHomework.text.toString(), checkHomework)
        setTextStyle(book.text.toString(), book)
        setTextStyle(prepare.text.toString(), prepare)
        setTextStyle(zixi.text.toString(), zixi)

        val adapter = HomeProgressAdapter()
        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        val list = ArrayList<HomeProgressBean>()
        for(i in 0..10){
            list.add(HomeProgressBean())
        }
        adapter.datas = list

    }

    private fun setTextStyle(string: String, textView : TextView){
        val spannableString = SpannableString(string)
        spannableString.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.x24),false), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.x18),false), 4, string.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(StyleSpan(Typeface.NORMAL), 4, string.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannableString
    }

    inner private class MyImageLoader : ImageLoader(){
        override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
            imageView!!.loadImage(context, path as String)
        }

    }

}