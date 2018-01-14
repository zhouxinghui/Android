package com.yzy.ebag.student.activity.tools

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.base.BaseListActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.util.T

/**
 * Created by unicho on 2018/1/8.
 */
class ToolsActivity : BaseListActivity<List<Int>,Int>() {

    override fun getAdapter(): BaseQuickAdapter<Int, BaseViewHolder>? {
        return ToolsAdapter()
    }

    private val list = intArrayOf(R.drawable.tool_btn_calligraphy,R.drawable.tool_btn_read,
            R.drawable.tool_btn_pinyin,R.drawable.tool_btn_letter,R.drawable.tool_btn_formula,
            R.drawable.tool_btn_song)

    override fun loadConfig(intent: Intent) {
        setPadding(resources.getDimensionPixelSize(R.dimen.x25),
                resources.getDimensionPixelSize(R.dimen.x15),
                resources.getDimensionPixelSize(R.dimen.x25), 0)
        withFirstPageData(list.asList())

    }

    override fun initViews() {
        super.initViews()
        //添加插画
        val params = RelativeLayout.LayoutParams(resources.getDimensionPixelSize(R.dimen.x172),resources.getDimensionPixelSize(R.dimen.x165))
        val imageView = ImageView(this)
        imageView.setImageResource(R.drawable.tool_img_corner)
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE)
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE)
        params.bottomMargin = resources.getDimensionPixelSize(R.dimen.x24)
        params.rightMargin = resources.getDimensionPixelSize(R.dimen.x24)
        rootLayout.addView(imageView,params)
        titleBar.setTitle(R.string.tool)
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<Int>>) {

    }

    override fun parentToList(isFirstPage: Boolean, parent: List<Int>?): List<Int>? {
        return parent
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager? {
        return GridLayoutManager(this,4)
    }


    class ToolsAdapter : BaseQuickAdapter<Int,BaseViewHolder>(R.layout.activity_tools_item) {
        override fun convert(helper: BaseViewHolder, item: Int?) {
            if (item != null) {
                helper.setImageResource(R.id.image,item)
            }
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when(list[position]){
            R.drawable.tool_btn_formula -> {
                startActivity(Intent(this,MathFormulaActivity::class.java))
            }
            else -> {
                T.show(this,"点击了第${position + 1}个条目")
            }
        }
    }

}