package ebag.hd.widget

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.PopupWindow
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.hd.R
import ebag.hd.bean.BaseClassesBean

/**
 * Created by YZY on 2018/3/19.
 */
class ClazzListPopup(context: Context): PopupWindow(context) {
    private val adapter = MyAdapter()
    private val classes = ArrayList<BaseClassesBean>()
    var onClassSelectListener : ((classBean: BaseClassesBean) -> Unit)? = null
    init {
        contentView = LayoutInflater.from(context).inflate(R.layout.recycler_view_layout, null)
        width = context.resources.getDimensionPixelSize(R.dimen.x350)
        height = context.resources.getDimensionPixelSize(R.dimen.y300)
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable())

        val recyclerView = contentView.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener { _, view, position ->
            onClassSelectListener?.invoke(adapter.data[position])
            adapter.selectPosition = position
            dismiss()
        }
    }

    fun setData(classes: ArrayList<BaseClassesBean>){
        if (this.classes.isEmpty()){
            this.classes.addAll(classes)
            adapter.setNewData(classes)
        }
    }

    inner class MyAdapter: BaseQuickAdapter<BaseClassesBean, BaseViewHolder>(R.layout.unit_sub_item){
        var selectPosition = -1
        set(value) {
            field = value
            notifyDataSetChanged()
        }
        override fun convert(helper: BaseViewHolder, item: BaseClassesBean?) {
            val textView = helper.getView<TextView>(R.id.text)
            textView.text = item?.className
            textView.isSelected = selectPosition == helper.adapterPosition
        }
    }
}