package ebag.hd.activity

import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import ebag.core.http.network.RequestCallBack
import ebag.hd.adapter.UnitAdapter
import ebag.hd.base.BaseListTabActivity

/**
 * Created by YZY on 2018/3/15.
 */
class StudyRoomActivity: BaseListTabActivity<List<String>, MultiItemEntity>() {
    override fun loadConfig() {

    }

    override fun requestData(requestCallBack: RequestCallBack<List<String>>) {
    }

    override fun parentToList(parent: List<String>?): List<MultiItemEntity>? {
        return null
    }

    private lateinit var adapter: UnitAdapter
    override fun getLeftAdapter(): BaseQuickAdapter<MultiItemEntity, BaseViewHolder> {
        adapter = UnitAdapter()
        return adapter
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<MultiItemEntity, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    override fun getFragment(pagerIndex: Int, adapter: BaseQuickAdapter<MultiItemEntity, BaseViewHolder>): Fragment {
        return Fragment()
    }

    override fun getViewPagerSize(adapter: BaseQuickAdapter<MultiItemEntity, BaseViewHolder>): Int {
        return 1
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {

    }
}