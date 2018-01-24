package com.yzy.ebag.student.activity.tools.fragment

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.activity.tools.practise.WriteActivity
import ebag.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_practise.*

/**
 * @author caoyu
 * @date 2018/1/22
 * @description
 */
class PractiseFragment: BaseFragment(),BaseQuickAdapter.OnItemClickListener {

    companion object {
        fun newInstance(): PractiseFragment{
            return PractiseFragment()
        }
    }
    override fun getLayoutRes(): Int {
        return R.layout.fragment_practise
    }

    override fun getBundle(bundle: Bundle?) {

    }

    lateinit var adapter: Adapter
    override fun initViews(rootView: View) {
        recyclerView.layoutManager = GridLayoutManager(mContext,5)
        adapter = Adapter()
        recyclerView.adapter = adapter
        adapter.onItemClickListener = this

        val list = ArrayList<Practise>()
        list.add(Practise())
        list.add(Practise())
        list.add(Practise())
        list.add(Practise())
        list.add(Practise())
        list.add(Practise())
        list.add(Practise())
        list.add(Practise())
        list.add(Practise())
        adapter.setNewData(list)

        tvConfirm.setOnClickListener {
            WriteActivity.jump(mContext)
        }
    }

    class Adapter: BaseQuickAdapter<Practise,BaseViewHolder>(R.layout.item_fragment_practise){
        override fun convert(helper: BaseViewHolder, item: Practise?) {
            helper.setText(R.id.tvPinyin, item?.pinyin)
                    .setText(R.id.tvChar, item?.hanzi)
                    .getView<TextView>(R.id.tvChar).isSelected = item?.selected == true
        }

    }

    override fun onItemClick(a: BaseQuickAdapter<*,*>, view: View?, position: Int) {
        adapter.getItem(position)?.selected = !(adapter.getItem(position)?.selected ?: true)
        adapter.notifyItemChanged(position)
    }

    data class Practise(
        val pinyin: String = "hua",
        val hanzi: String = "华",
        var selected: Boolean = false
    )
}