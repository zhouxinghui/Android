package com.yzy.ebag.student.activity.tools

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.activity.tools.formula.MathFormulaActivity
import com.yzy.ebag.student.activity.tools.practise.PractiseActivity
import com.yzy.ebag.student.activity.tools.read.ReadActivity
import com.yzy.ebag.student.base.BaseListActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.util.T

/**
 * Created by caoyu on 2018/1/8.
 */
class ToolsActivity : BaseListActivity<List<Int>,Int>() {

    private val list = intArrayOf(R.drawable.tool_btn_calligraphy,R.drawable.tool_btn_read,
            R.drawable.tool_btn_pinyin,R.drawable.tool_btn_letter,R.drawable.tool_btn_formula,
            R.drawable.tool_btn_song)

    companion object {
        fun jump(context: Context, classId: String){
            context.startActivity(
                    Intent(context,ToolsActivity::class.java)
                            .putExtra("classId", classId)
            )
        }
    }

    private lateinit var classId: String

    override fun loadConfig(intent: Intent) {
        classId = intent.getStringExtra("classId") ?: ""
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

    override fun getLayoutManager(adapter: BaseQuickAdapter<Int, BaseViewHolder>): RecyclerView.LayoutManager? {
        return GridLayoutManager(this,4)
    }

    override fun getAdapter(): BaseQuickAdapter<Int, BaseViewHolder> {
        return ToolsAdapter()
    }

    class ToolsAdapter : BaseQuickAdapter<Int,BaseViewHolder>(R.layout.item_activity_tools) {
        override fun convert(helper: BaseViewHolder, item: Int?) {
            if (item != null) {
                helper.setImageResource(R.id.image,item)
            }
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when(list[position]){

            R.drawable.tool_btn_calligraphy -> {//每日练字
                if(classId.isNullOrEmpty()){
                    T.show(this,"请返回首页获取左侧班级信息")
                }else{
                    PractiseActivity.jump(this, classId)
                }
            }

            R.drawable.tool_btn_read -> {//每日练字
                if(classId.isNullOrEmpty()){
                    T.show(this,"请返回首页获取左侧班级信息")
                }else{
                    ReadActivity.jump(this, classId)
                }
            }


            R.drawable.tool_btn_formula -> {
                startActivity(Intent(this, MathFormulaActivity::class.java))
            }
            R.drawable.tool_btn_pinyin -> {
                startActivity(
                        Intent(this, LetterActivity::class.java)
                                .putExtra("type", LetterActivity.ZH)
                )
            }
            R.drawable.tool_btn_letter -> {
                startActivity(
                        Intent(this, LetterActivity::class.java)
                                .putExtra("type", LetterActivity.EN)
                )
            }
            else -> {
                T.show(this,"点击了第${position + 1}个条目")
            }
        }
    }

}