package com.yzy.ebag.student.activity.tools.fragment

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.activity.tools.practise.WriteActivity
import com.yzy.ebag.student.bean.Practise
import com.yzy.ebag.student.bean.WordsBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.BaseFragment
import ebag.core.http.network.MsgException
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.T
import kotlinx.android.synthetic.main.fragment_practise.*

/**
 * @author caoyu
 * @date 2018/1/22
 * @description
 */
class PractiseFragment: BaseFragment(),BaseQuickAdapter.OnItemClickListener {

    companion object {
        fun newInstance(unitCode: String?): PractiseFragment{
            val fragment = PractiseFragment()
            val bundle = Bundle()
            bundle.putString("unitCode",unitCode)
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun getLayoutRes(): Int {
        return R.layout.fragment_practise
    }

    private lateinit var unitCode: String
    override fun getBundle(bundle: Bundle?) {
        unitCode = bundle?.getString("unitCode") ?: ""
    }

    lateinit var adapter: Adapter
    override fun initViews(rootView: View) {
        recyclerView.layoutManager = GridLayoutManager(mContext,5)
        adapter = Adapter()
        recyclerView.adapter = adapter
        adapter.onItemClickListener = this

        tvConfirm.setOnClickListener {
            val list = ArrayList<Practise>()
            adapter.data.forEach {
                if(it.isSelected)
                    list.add(it)
            }

            if(list.isEmpty()){
                T.show(mContext,"请选择生字")
            }else{
                WriteActivity.jump(mContext, list)
            }
        }
        cbSelect.setOnClickListener { view ->
            view.isSelected = !view.isSelected
            adapter.data.forEach { it.isSelected = view.isSelected }
            adapter.notifyDataSetChanged()
        }

        stateView.setOnRetryClickListener {
            request()
        }
        request()
    }

    fun update(unitCode: String?){
        if(this.unitCode != unitCode){
            this.unitCode = unitCode ?: ""
            requestCallBack.cancelRequest()
            request()
        }
    }

    private fun request(){
        StudentApi.getWordsList(this.unitCode, requestCallBack)
    }

    override fun onDestroy() {
        super.onDestroy()
        requestCallBack.cancelRequest()
    }

    private val requestCallBack by lazy { object: RequestCallBack<WordsBean>(){

        override fun onStart() {
            stateView.showLoading()
            cbSelect.isSelected = false
        }

        override fun onSuccess(entity: WordsBean?) {
            if(entity == null){
                stateView.showEmpty()
            }else{
                val words = entity.word?.split(",")
                val pinyins = entity.pinYin?.split(",")
                val audios = entity.audioUrl?.split(",")


                val length = Math.min(words?.size ?: -1, pinyins?.size ?: -1)

                if (length == -1){
                    stateView.showEmpty()
                }else{
                    stateView.showContent()

                    val list = ArrayList<Practise>()
                    (0 until length).forEach {
                        if(audios != null && audios.size > it){
                            list.add(Practise(pinyins!![it], words!![it], audios[it]))
                        }else
                            list.add(Practise(pinyins!![it], words!![it], ""))
                    }

                    adapter.setNewData(list)
                }

            }
        }

        override fun onError(exception: Throwable) {
            if(exception is MsgException){
                stateView.showError(exception.message)
            }else{
                stateView.showError()
                exception.handleThrowable(mContext)
            }
        }

    } }

    class Adapter: BaseQuickAdapter<Practise,BaseViewHolder>(R.layout.item_fragment_practise){
        override fun convert(helper: BaseViewHolder, item: Practise?) {
            helper.setText(R.id.tvPinyin, item?.pinyin)
                    .setText(R.id.tvChar, item?.hanzi)
                    .getView<TextView>(R.id.tvChar).isSelected = item?.isSelected == true
        }

    }

    override fun onItemClick(a: BaseQuickAdapter<*,*>, view: View?, position: Int) {
        adapter.getItem(position)?.isSelected = !(adapter.getItem(position)?.isSelected ?: true)
        adapter.notifyItemChanged(position)
    }
}