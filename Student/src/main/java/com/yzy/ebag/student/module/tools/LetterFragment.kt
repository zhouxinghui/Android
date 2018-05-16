package com.yzy.ebag.student.module.tools

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.LetterBean
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.util.VoicePlayerOnline

/**
 * Created by YZY on 2018/5/15.
 */
class LetterFragment: BaseListFragment<List<LetterBean>, LetterBean>() {
    companion object {
        fun newInstance(data: ArrayList<LetterBean>, type: Int): LetterFragment{
            val fragment = LetterFragment()
            val bundle = Bundle()
            bundle.putSerializable("list", data)
            bundle.putInt("type", type)
            fragment.arguments = bundle
            return fragment
        }
        val ZH = 0
        val EN = 1
    }
    private var type = ZH
    private val list = ArrayList<LetterBean>()
    private var defaultBigSize = 0
//    private var defaultSize = resources.getDimensionPixelOffset(R.dimen.tv_sub)
    private var maxWidth = 0
    private var bigMinSize = 0
    private var minSize = 0
    override fun getBundle(bundle: Bundle?) {
        type = bundle?.getInt("type", 0) ?: 0
        val list = bundle?.getSerializable("list") as ArrayList<LetterBean>
        this.list.addAll(list)
    }

    override fun loadConfig() {
        if(type == ZH){
            defaultBigSize = resources.getDimensionPixelOffset(R.dimen.x50)
        }else{
            defaultBigSize = resources.getDimensionPixelOffset(R.dimen.x40)
        }
        withFirstPageData(list)
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<LetterBean>>) {

    }

    override fun parentToList(isFirstPage: Boolean, parent: List<LetterBean>?): List<LetterBean>? = parent

    private val adapter = LetterAdapter()
    override fun getAdapter(): BaseQuickAdapter<LetterBean, BaseViewHolder> {
        adapter.setSpanSizeLookup { gridLayoutManager, position ->
            if(adapter.getItemViewType(position) == LetterBean.GROUP_TYPE){
                gridLayoutManager.spanCount
            }else{
                1
            }
        }
        return adapter
    }

    private val playerD = lazy {
        val pl = VoicePlayerOnline(mContext)
        pl.setOnPlayChangeListener(object : VoicePlayerOnline.OnPlayChangeListener{
            override fun onProgressChange(progress: Int) {
            }

            override fun onCompletePlay() {
                adapter.selectedPosition = -1
                animDrawable?.stop()
                animDrawable?.selectDrawable(0)
                animDrawable = null
            }
        })
        pl
    }
    private val player by playerD
    private var animDrawable: AnimationDrawable? = null

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        adapter as LetterAdapter
        //点击  normal 才有效
        if(adapter.getItemViewType(position) == LetterBean.NORMAL_TYPE)
            if(adapter.selectedPosition != position){
                if(!player.isPlaying){
                    if(animDrawable != null){
                        animDrawable?.stop()
                        animDrawable?.selectDrawable(0)
                    }
                    animDrawable = view?.getTag(R.id.soundView) as AnimationDrawable
                    animDrawable?.start()
                    player.playUrl(adapter.getItem(position)?.mp3)
                    adapter.selectedPosition = position
                }
            }
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<LetterBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return GridLayoutManager(mContext,3)
    }

    private inner class LetterAdapter: BaseMultiItemQuickAdapter<LetterBean, BaseViewHolder>(null) {

        init {
            addItemType(LetterBean.NORMAL_TYPE, R.layout.item_activity_letter_normal)
            addItemType(LetterBean.GROUP_TYPE, R.layout.item_activity_letter_group)
        }

        private var lastPosition = -1
        var selectedPosition = -1
            set(value) {
                if(selectedPosition != value){
                    field = value
                    notifyItemChanged(value)
                    notifyItemChanged(lastPosition)
                    lastPosition = value
                }
            }

        override fun setNewData(data: MutableList<LetterBean>?) {
            super.setNewData(data)
            selectedPosition = -1
            lastPosition = -1
            notifyDataSetChanged()
        }


        override fun convert(helper: BaseViewHolder, item: LetterBean?) {
            when(helper.itemViewType){
                LetterBean.GROUP_TYPE -> {
                    helper.setText(R.id.tvContent,item?.content ?: "")
                }
                LetterBean.NORMAL_TYPE -> {
                    val animDrawable: AnimationDrawable = helper.getView<View>(R.id.soundView).background as AnimationDrawable

                    helper.itemView.setTag(R.id.soundView, animDrawable)

                    helper.getView<View>(R.id.bg).isSelected = helper.adapterPosition == selectedPosition

                    /*if(type == ZH){
                        (helper.getView<TextView>(R.id.tvContent).layoutParams as  ConstraintLayout.LayoutParams)
                                .bottomMargin = resources.getDimensionPixelOffset(R.dimen.x15)
                    }else{
                        (helper.getView<TextView>(R.id.tvContent).layoutParams as  ConstraintLayout.LayoutParams)
                                .bottomMargin = 0
                    }*/
                    helper.setText(R.id.tvContent, item?.letters)
//                    reSizeTextView(helper.getView(R.id.tvContent),item?.letters ?: "", item?.content ?: "")
                }
            }
        }

        /*private fun reSizeTextView(textView: TextView, letters: String, content: String) {

            val paint: Paint = textView.paint

            var defaultBigSize = defaultBigSize
            var defaultSize = defaultSize

            var textBigWidth: Float
            var textDefWidth: Float

            do{
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultBigSize.toFloat())
                textBigWidth = paint.measureText(letters)

                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultSize.toFloat())
                textDefWidth = paint.measureText(" $content")
                defaultBigSize--
                if(defaultSize != minSize)
                    defaultSize--

            }while (defaultBigSize != bigMinSize && textBigWidth + textDefWidth > maxWidth)

            val spannableString = SpannableString("$letters $content")
            spannableString.setSpan(AbsoluteSizeSpan(defaultBigSize),
                    0, letters.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(AbsoluteSizeSpan(defaultSize),
                    letters.length, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            textView.text = spannableString

            textView.invalidate()

        }*/
    }
}