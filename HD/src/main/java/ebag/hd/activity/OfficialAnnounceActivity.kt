package ebag.hd.activity

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.base.BaseListActivity
import ebag.core.http.network.RequestCallBack
import ebag.hd.R
import ebag.hd.bean.OfficialAnnounceBean
import ebag.hd.http.EBagApi

/**
 * Created by YZY on 2018/4/10.
 */
class OfficialAnnounceActivity: BaseListActivity<OfficialAnnounceBean, OfficialAnnounceBean.ListBean>() {
    companion object {
        fun jump(context: Context, roleCode: String){
            context.startActivity(
                    Intent(context, OfficialAnnounceActivity::class.java)
                            .putExtra("roleCode", roleCode)
            )
        }
    }
    private var roleCode = ""
    override fun loadConfig(intent: Intent) {
        titleBar.setTitle("官方公告")
        roleCode = intent.getStringExtra("roleCode") ?: "teacher"
    }

    override fun getPageSize(): Int {
        return 10
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<OfficialAnnounceBean>) {
        EBagApi.officialAnnounce("3", roleCode, page, getPageSize(), requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: OfficialAnnounceBean?): List<OfficialAnnounceBean.ListBean>? {
        return parent?.list
    }

    override fun getAdapter(): BaseQuickAdapter<OfficialAnnounceBean.ListBean, BaseViewHolder> {
        return MyAdapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<OfficialAnnounceBean.ListBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    inner class MyAdapter: BaseQuickAdapter<OfficialAnnounceBean.ListBean, BaseViewHolder>(R.layout.item_official_announce){
        override fun convert(helper: BaseViewHolder, item: OfficialAnnounceBean.ListBean?) {
            helper.setText(R.id.tv, "${item?.versionName}\n${item?.mark}")
        }
    }
}