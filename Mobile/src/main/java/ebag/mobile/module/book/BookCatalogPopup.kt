package ebag.mobile.module.book

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.PopupWindow
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.http.network.RequestCallBack
import ebag.core.util.StringUtils
import ebag.core.widget.empty.StateView
import ebag.mobile.R
import ebag.mobile.bean.BookCategoryBean
import ebag.mobile.bean.BookCategoryListBean
import ebag.mobile.http.EBagApi

/**
 * Created by YZY on 2018/2/8.
 */
class BookCatalogPopup(context: Context): PopupWindow(context) {
    private var bookId = 0
    private val adapter = MyAdapter()
    private var stateView: StateView? = null
    var onCategoryClick : ((page: Int) -> Unit)? = null
    private val request = object : RequestCallBack<BookCategoryBean>(){
        override fun onStart() {
            stateView?.showLoading()
        }

        override fun onSuccess(entity: BookCategoryBean?) {
            stateView?.showContent()
            if (entity != null && !StringUtils.isEmpty(entity.bookChapter)){
                val categoryStr = entity.bookChapter.replace("\\","")
                val jsonObject = JSONObject.parseObject(categoryStr)
                val subBean = JSON.toJavaObject<BookCategoryListBean>(jsonObject, BookCategoryListBean::class.java)
                adapter.setNewData(subBean.toc)
            }
        }

        override fun onError(exception: Throwable) {
            stateView?.showError()
        }
    }
    init {
        contentView = LayoutInflater.from(context).inflate(R.layout.popup_catalog, null)
        width = context.resources.getDimensionPixelSize(R.dimen.x130)
        height = context.resources.getDimensionPixelSize(R.dimen.y500)
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable())
        animationStyle = R.style.book_catalog_anim

        stateView = contentView.findViewById(R.id.stateView)
        stateView?.setOnRetryClickListener {
            EBagApi.bookCategory(bookId, request)
        }

        val recyclerView = contentView.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener { _, _, position ->
            onCategoryClick?.invoke(adapter.data[position].p)
        }
    }

    fun loadDate(bookId: Int){
        EBagApi.bookCategory(bookId, request)
    }

    inner class MyAdapter: BaseQuickAdapter<BookCategoryListBean.TocBean, BaseViewHolder>(R.layout.item_book_category){
        override fun convert(helper: BaseViewHolder, item: BookCategoryListBean.TocBean) {
            val textView = helper.getView<TextView>(R.id.text)
            textView.text = item.t
            if (item.type == "2"){ //一级目录
                textView.setPadding(mContext.resources.getDimensionPixelSize(R.dimen.x10),0,0,0)
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.resources.getDimension(R.dimen.tv_big))
            }else{//二级目录
                textView.setPadding(mContext.resources.getDimensionPixelSize(R.dimen.x20),0,0,0)
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.resources.getDimension(R.dimen.tv_normal))
            }
        }
    }
}