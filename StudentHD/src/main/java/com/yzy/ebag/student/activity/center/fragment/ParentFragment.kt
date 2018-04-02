package com.yzy.ebag.student.activity.center.fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.dialog.ParentAddDialog
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.util.SerializableUtils
import ebag.core.util.loadHead
import ebag.hd.base.Constants
import ebag.hd.bean.ParentBean
import ebag.hd.bean.response.UserEntity

/**
 * @author caoyu
 * @date 2018/1/18
 * @description
 */
class ParentFragment : BaseListFragment<List<ParentBean>, ParentBean>() {



    companion object {
        fun newInstance(): ParentFragment {
            return ParentFragment()
        }
    }

    override fun getBundle(bundle: Bundle?) {
    }

    override fun isPagerFragment(): Boolean {
        return false
    }

    override fun loadConfig() {
        loadMoreEnabled(false)
        val button = Button(mContext)
        button.text = "添加家长"
        button.setTextColor(resources.getColor(R.color.white))
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.tv_normal))
        button.setBackgroundResource(R.drawable.my_class_btn_add)
        button.setPadding(resources.getDimensionPixelOffset(R.dimen.x24), 0,
                resources.getDimensionPixelOffset(R.dimen.x24), 0)
        val p = RelativeLayout.LayoutParams(resources.getDimensionPixelOffset(R.dimen.x108),
                resources.getDimensionPixelOffset(R.dimen.x108))
        p.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
        p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        rootView.addView(button, p)
        rootView.setPadding(0, resources.getDimensionPixelOffset(R.dimen.x20), 0, 0)

        button.setOnClickListener {
            addDialog.show(childFragmentManager, "joinDialog")
        }

        /* val list = ArrayList<Parent>()
         list.add(Parent("我住在这里"))
         list.add(Parent())
         list.add(Parent("我住在那里"))
         list.add(Parent())
         list.add(Parent())
         list.add(Parent())
         list.add(Parent())
         list.add(Parent())
         list.add(Parent())
         withFirstPageData(list)*/
    }


    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<ParentBean>>) {
        StudentApi.searchFamily(requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<ParentBean>?): List<ParentBean>? {

        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<ParentBean, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<ParentBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    private val addDialog by lazy { ParentAddDialog.newInstance() }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {

    }


    inner class Adapter : BaseQuickAdapter<ParentBean, BaseViewHolder>(R.layout.item_fragment_parents) {

        private val userEntity = SerializableUtils.getSerializable<UserEntity>(Constants.STUDENT_USER_ENTITY)

        override fun convert(helper: BaseViewHolder, item: ParentBean?) {
            helper.setText(R.id.tvName, item?.name)
                    .setText(R.id.tvStudentName, userEntity?.name)
                    .setText(R.id.tvEBagCode, "书包号: ${item?.ysbCode}")
                    .setText(R.id.tvPhone, item?.phone)
                    .setText(R.id.tvAddress, item?.address ?: "暂无地址")
                    .getView<ImageView>(R.id.ivAvatar).loadHead(item?.headUrl)

        }
    }

}