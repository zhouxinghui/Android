package com.yzy.ebag.teacher

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.http.network.RequestCallBack
import ebag.core.util.T
import ebag.mobile.base.BaseListActivity

/**
 * Created by YZY on 2018/3/20.
 */
class ZixiActivity: BaseListActivity<List<Int>, Int>() {
    private val list = intArrayOf(R.drawable.tool_btn_calligraphy, R.drawable.tool_btn_read)
    companion object {
        fun jump(context: Context){
            context.startActivity(
                    Intent(context, ZixiActivity::class.java)
            )
        }
    }
    override fun loadConfig(intent: Intent) {
        /*setPadding(resources.getDimensionPixelSize(R.dimen.x25),
                resources.getDimensionPixelSize(R.dimen.x15),
                resources.getDimensionPixelSize(R.dimen.x25), 0)*/
        withFirstPageData(list.asList())
    }

    override fun initViews() {
        super.initViews()
        //添加插画
        val params = RelativeLayout.LayoutParams(resources.getDimensionPixelSize(R.dimen.x86),resources.getDimensionPixelSize(R.dimen.x82))
        val imageView = ImageView(this)
        imageView.setImageResource(R.drawable.tool_img_corner)
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
        params.bottomMargin = resources.getDimensionPixelSize(R.dimen.x24)
        params.rightMargin = resources.getDimensionPixelSize(R.dimen.x24)
        rootLayout.addView(imageView,params)
        titleBar.setTitle("学生自习")
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<Int>>) {
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<Int>?): List<Int>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<Int, BaseViewHolder> {
        return ToolsAdapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<Int, BaseViewHolder>): RecyclerView.LayoutManager? {
        return GridLayoutManager(this,2)
    }

    class ToolsAdapter : BaseQuickAdapter<Int,BaseViewHolder>(R.layout.item_activity_zixi) {
        override fun convert(helper: BaseViewHolder, item: Int?) {
            if (item != null) {
                helper.setImageResource(R.id.image,item)
            }
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when(list[position]){

            R.drawable.tool_btn_calligraphy -> {//每日练字
//                LetterRecordActivity.jump(this)
            }

            R.drawable.tool_btn_read -> {//每日练字
//                ReadRecordActivity.jump(this)
            }
            else -> {
                T.show(this,"点击了第${position + 1}个条目")
            }
        }
    }

}