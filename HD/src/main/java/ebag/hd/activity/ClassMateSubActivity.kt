package ebag.hd.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.http.network.RequestCallBack
import ebag.core.util.loadHead
import ebag.hd.R
import ebag.hd.base.BaseListActivity
import ebag.hd.base.Constants
import ebag.hd.bean.ClassMemberBean
import ebag.hd.dialog.ClazzmateInfoDIalog

/**
 * Created by YZY on 2018/5/2.
 */
class ClassMateSubActivity: BaseListActivity<List<ClassMemberBean.SubMemberBean>, ClassMemberBean.SubMemberBean>() {
    companion object {
        fun jump(context: Context, list: ArrayList<ClassMemberBean.SubMemberBean>?, role: Int){
            context.startActivity(
                    Intent(context, ClassMateSubActivity::class.java)
                            .putExtra("list", list ?: ArrayList<ClassMemberBean.SubMemberBean>())
                            .putExtra("role", role)
            )
        }
    }
    override fun loadConfig(intent: Intent) {
        val list = intent.getSerializableExtra("list") as ArrayList<ClassMemberBean.SubMemberBean>
        val role = intent.getIntExtra("role", 1)
        withFirstPageData(list, false)
        when(role){
            Constants.ROLE_TEACHER ->{
                titleBar.setTitle("班级老师")
            }
            Constants.ROLE_STUDENT ->{
                titleBar.setTitle("班级学生")
            }
            Constants.ROLE_PARENT ->{
                titleBar.setTitle("班级家长")
            }
        }
        enableNetWork(false)
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<ClassMemberBean.SubMemberBean>>) {
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<ClassMemberBean.SubMemberBean>?): List<ClassMemberBean.SubMemberBean>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<ClassMemberBean.SubMemberBean, BaseViewHolder> = ClazzmateAdapter()

    override fun getLayoutManager(adapter: BaseQuickAdapter<ClassMemberBean.SubMemberBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return GridLayoutManager(this, 5)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        adapter as ClazzmateAdapter
        val b = Bundle()
        b.putSerializable("data", adapter.getItem(position))
        val f = ClazzmateInfoDIalog.newInstance()
        f.arguments = b
        f.show(supportFragmentManager, "clazzDialog")
    }

    private inner class ClazzmateAdapter : BaseQuickAdapter<ClassMemberBean.SubMemberBean, BaseViewHolder>(R.layout.item_clazzmate) {
        override fun convert(helper: BaseViewHolder, item: ClassMemberBean.SubMemberBean?) {
            helper.setText(R.id.clazz_name, item?.name)
            helper.getView<ImageView>(R.id.clazz_head).loadHead(item?.headUrl,true)
        }
    }
}