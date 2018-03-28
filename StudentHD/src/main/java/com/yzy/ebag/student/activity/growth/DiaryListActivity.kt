package com.yzy.ebag.student.activity.growth

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.base.BaseListActivity
import com.yzy.ebag.student.bean.Diary
import com.yzy.ebag.student.http.StudentApi
import ebag.core.http.network.RequestCallBack
import ebag.core.util.DateUtil
import ebag.core.util.T
import ebag.core.util.loadImage

/**
 * @author caoyu
 * @date 2018/2/1
 * @description
 */
class DiaryListActivity : BaseListActivity<List<Diary>, Diary.ResultUserGrowthByPageVoBean.UserGrowthResultVoListBean>() {


    companion object {
        fun jump(context: Context, gradeId: String) {
            context.startActivity(
                    Intent(context, DiaryListActivity::class.java)
                            .putExtra("gradeId", gradeId)
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 998){
            onRefresh()
        }
    }

    lateinit var edit: EditText
    lateinit var gradeId: String
    override fun loadConfig(intent: Intent) {
        gradeId = intent.getStringExtra("gradeId") ?: ""

        titleBar.setRightText("添加") {
            DiaryDetailActivity.jump(this, gradeId, null)
        }

        val view = layoutInflater.inflate(R.layout.layout_record_search, null)
        edit = view.findViewById(R.id.editText)
        view.findViewById<View>(R.id.image).setOnClickListener { search() }
        edit.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                handled = true
                if (isEditEmpty()) {
                    T.show(this, "请输入关键字")
                } else {
                    /*隐藏软键盘*/
                    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                }
            }
            handled
        }

        val p = RelativeLayout.LayoutParams(resources.getDimensionPixelOffset(R.dimen.x465)
                , resources.getDimensionPixelOffset(R.dimen.x48))
        p.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        titleBar.addView(view, p)

        /*val list = ArrayList<Diary>()
        list.add(Diary("今天很愉快", "今天我都做了什么呢", 1516623323000,
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/5/1605/ht/201801222014340," +
                        "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/5/1605/ht/201801222014341"))
        list.add(Diary("今天很愉快", "今天我都做了什么呢", 1516623323000,null))
        list.add(Diary("今天很愉快", "今天我都做了什么呢", 1516623323000,
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/5/1605/ht/201801222014340," +
                        "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/5/1605/ht/201801222014341"))

        withFirstPageData(list)*/
    }

    private fun search() {
        if (isEditEmpty()) {
            T.show(this, "请输入关键字")
            return
        }
    }

    private fun isEditEmpty(): Boolean {
        return edit.text.toString().isBlank()
    }

    override fun getPageSize(): Int {
        return 10
    }


    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<Diary>>) {
        StudentApi.searchUserGrowthList(page,getPageSize(),gradeId,"4",requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<Diary>?): List<Diary.ResultUserGrowthByPageVoBean.UserGrowthResultVoListBean>? {
        return parent!![0].resultUserGrowthByPageVo.userGrowthResultVoList

    }



    override fun getAdapter(): BaseQuickAdapter<Diary.ResultUserGrowthByPageVoBean.UserGrowthResultVoListBean, BaseViewHolder> {
        return Adapter()
    }


    override fun getLayoutManager(adapter: BaseQuickAdapter<Diary.ResultUserGrowthByPageVoBean.UserGrowthResultVoListBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }



    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        DiaryDetailActivity.jump(this, gradeId, (adapter as Adapter).getItem(position))
    }


    inner class Adapter : BaseQuickAdapter<Diary.ResultUserGrowthByPageVoBean.UserGrowthResultVoListBean, BaseViewHolder>(R.layout.item_activity_diary) {
        override fun convert(helper: BaseViewHolder, item: Diary.ResultUserGrowthByPageVoBean.UserGrowthResultVoListBean) {
            helper.setText(R.id.tvTitle, item?.title)
                    .setText(R.id.tvContent, item?.content)
                    .setText(R.id.tvTime, DateUtil.getDateTime(item!!.createDate))

            val recycler = helper.getView<RecyclerView>(R.id.recyclerView)
            recycler.isNestedScrollingEnabled = false

            if (recycler.adapter == null) {
                recycler.adapter = ImageAdapter()
            }
            if (recycler.layoutManager == null) {
                recycler.layoutManager = GridLayoutManager(mContext, 6)
            }

            if (item.image!=null && item.image.isNotEmpty()) {
                recycler.postDelayed({
                    (recycler.adapter as ImageAdapter).setNewData(item.image.split(","))
                }, 20)
            }

        }

    }

    inner class ImageAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_activity_announce_image) {
        override fun convert(helper: BaseViewHolder, item: String?) {
            helper.getView<ImageView>(R.id.image).loadImage(item)
        }
    }


}