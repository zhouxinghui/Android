package com.yzy.ebag.student.activity.tools

import android.content.Intent
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.RadioGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.base.BaseListActivity
import com.yzy.ebag.student.bean.response.LetterBean
import ebag.core.http.network.RequestCallBack


/**
 * @author 曹宇
 * @date 2018/1/15
 * @description
 */
class LetterActivity : BaseListActivity<List<LetterBean>, LetterBean>() {

    companion object {
        val ZH = 0
        val EN = 1
    }

    private var type = ZH

    private var defaultBigSize = 0
    private var defaultSize = 0
    private var maxWidth = 0
    private var bigMinSize = 0
    private var minSize = 0

    override fun loadConfig(intent: Intent) {


        type = intent.getIntExtra("type",ZH)
        setPadding(resources.getDimensionPixelOffset(R.dimen.x5),
                resources.getDimensionPixelOffset(R.dimen.x5),
                resources.getDimensionPixelOffset(R.dimen.x5),0)

        defaultSize = resources.getDimensionPixelOffset(R.dimen.x22)
        maxWidth = resources.getDimensionPixelOffset(R.dimen.x113)
        bigMinSize = resources.getDimensionPixelOffset(R.dimen.x18)
        minSize = resources.getDimensionPixelOffset(R.dimen.x12)

        if(type == ZH){
            setPageTitle("汉语拼音")
            titleBar.setRightText("声母表") { showZHPop() }
            defaultBigSize = resources.getDimensionPixelOffset(R.dimen.x50)
            withFirstPageData(smList)

        }else{
            setPageTitle("英文字母")
            titleBar.setRightText("字母表") { showENPop() }
            defaultBigSize = resources.getDimensionPixelOffset(R.dimen.x40)
            withFirstPageData(zmList)
        }
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<LetterBean>>) {
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<LetterBean>?): List<LetterBean>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<LetterBean, BaseViewHolder> {
        val adapter = LetterAdapter()
        adapter.setSpanSizeLookup { gridLayoutManager, position ->
            if(adapter.getItemViewType(position) == LetterBean.GROUP_TYPE){
                gridLayoutManager.spanCount
            }else{
                1
            }
        }
        return adapter
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<LetterBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return GridLayoutManager(this,6)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        (adapter as LetterAdapter).selectedPosition = position
    }

    inner class LetterAdapter: BaseMultiItemQuickAdapter<LetterBean, BaseViewHolder>(null) {

        init {
            addItemType(LetterBean.NORMAL_TYPE, R.layout.activity_letter_normal_item)
            addItemType(LetterBean.GROUP_TYPE, R.layout.activity_letter_group_item)
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
                    helper.getView<View>(R.id.bg).isSelected = helper.adapterPosition == selectedPosition
                    if(type == ZH){
                        (helper.getView<TextView>(R.id.tvContent).layoutParams as  ConstraintLayout.LayoutParams)
                                .bottomMargin = resources.getDimensionPixelOffset(R.dimen.x15)
                    }else{
                        (helper.getView<TextView>(R.id.tvContent).layoutParams as  ConstraintLayout.LayoutParams)
                                .bottomMargin = 0
                    }
                    reSizeTextView(helper.getView(R.id.tvContent),item?.letters ?: "", item?.content ?: "")
                }
            }
        }

        private fun reSizeTextView(textView: TextView, letters: String, content: String) {

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

        }

    }

    private val popZhWnd by lazy {
        val ppppp = PopupWindow(this)
        val contentView = layoutInflater.inflate(R.layout.popup_letter_zh_selected, null)
        ppppp.contentView = contentView
        ppppp.width = resources.getDimensionPixelOffset(R.dimen.x268)
        ppppp.height = ViewGroup.LayoutParams.WRAP_CONTENT

        // 设置SelectPicPopupWindow弹出窗体可点击
        ppppp.isFocusable = true
        ppppp.isOutsideTouchable = true
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        ppppp.setBackgroundDrawable(ColorDrawable(0))
        ppppp.animationStyle = R.style.AnimationPreview
        val radioGroup: RadioGroup = contentView as RadioGroup

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbSm -> {
                    titleBar.setRightText("声母表")
                    withFirstPageData(smList)
                }
                R.id.rbYm -> {
                    titleBar.setRightText("韵母表")
                    withFirstPageData(ymList)
                }
                R.id.rbZt -> {
                    titleBar.setRightText("整体认读音节")
                    withFirstPageData(ztList)
                }
            }
            ppppp.dismiss()
        }

        ppppp
    }

    private fun showZHPop(){
        if(titleBar.rightView !=null)
            popZhWnd.showAsDropDown(titleBar.rightView)
    }

    private val popEnWnd by lazy {
        val ppppp = PopupWindow(this)
        val contentView = layoutInflater.inflate(R.layout.popup_letter_en_selected, null)
        ppppp.contentView = contentView
        ppppp.width = resources.getDimensionPixelOffset(R.dimen.x268)
        ppppp.height = ViewGroup.LayoutParams.WRAP_CONTENT

        // 设置SelectPicPopupWindow弹出窗体可点击
        ppppp.isFocusable = true
        ppppp.isOutsideTouchable = true
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        ppppp.setBackgroundDrawable(ColorDrawable(0))
        ppppp.animationStyle = R.style.AnimationPreview
        val radioGroup: RadioGroup = contentView as RadioGroup

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbZm -> {
                    titleBar.setRightText("字母表")
                    withFirstPageData(zmList)
                }
                R.id.rbYb -> {
                    titleBar.setRightText("音标表")
                    withFirstPageData(ybList)
                }
            }
            ppppp.dismiss()
        }

        ppppp
    }

    private fun showENPop(){
        if(titleBar.rightView !=null)
            popEnWnd.showAsDropDown(titleBar.rightView)
    }

    private val smList by lazy {
        val list = ArrayList<LetterBean>()
        list.add(LetterBean("b","波",""))
        list.add(LetterBean("p","泼",""))
        list.add(LetterBean("m","摸",""))
        list.add(LetterBean("f","佛",""))
        list.add(LetterBean("d","的",""))
        list.add(LetterBean("t","特",""))
        list.add(LetterBean("n","呢",""))
        list.add(LetterBean("l","了",""))
        list.add(LetterBean("g","哥",""))
        list.add(LetterBean("k","科",""))
        list.add(LetterBean("h","喝",""))
        list.add(LetterBean("j","鸡",""))
        list.add(LetterBean("q","期",""))
        list.add(LetterBean("x","西",""))
        list.add(LetterBean("z","兹",""))
        list.add(LetterBean("c","呲",""))
        list.add(LetterBean("s","丝",""))
        list.add(LetterBean("r","日",""))
        list.add(LetterBean("zh","知",""))
        list.add(LetterBean("ch","痴",""))
        list.add(LetterBean("sh","狮",""))
        list.add(LetterBean("y","衣",""))
        list.add(LetterBean("w","乌",""))

        list
    }

    private val ymList by lazy {
        val list = ArrayList<LetterBean>()
        list.add(LetterBean("单韵母"))
        list.add(LetterBean("a","啊",""))
        list.add(LetterBean("o","喔",""))
        list.add(LetterBean("e","鹅",""))
        list.add(LetterBean("i","衣",""))
        list.add(LetterBean("u","乌",""))
        list.add(LetterBean("ü","鱼",""))

        list.add(LetterBean("复韵母"))
        list.add(LetterBean("ai","唉",""))
        list.add(LetterBean("ei","诶",""))
        list.add(LetterBean("ui","威",""))
        list.add(LetterBean("ao","奥",""))
        list.add(LetterBean("ou","欧",""))
        list.add(LetterBean("iu","优",""))
        list.add(LetterBean("ie","耶",""))
        list.add(LetterBean("üe","约",""))
        list.add(LetterBean("er","耳",""))

        list.add(LetterBean("前鼻韵母"))
        list.add(LetterBean("an","安",""))
        list.add(LetterBean("en","恩",""))
        list.add(LetterBean("in","因",""))
        list.add(LetterBean("un","温",""))
        list.add(LetterBean("̈ün","晕",""))

        list.add(LetterBean("后鼻韵母"))
        list.add(LetterBean("ang","昂",""))
        list.add(LetterBean("eng","鞥",""))
        list.add(LetterBean("ing","英",""))
        list.add(LetterBean("ong","翁",""))

        list
    }

    private val ztList by lazy {
        val list = ArrayList<LetterBean>()
        list.add(LetterBean("zhi","知",""))
        list.add(LetterBean("chi","痴",""))
        list.add(LetterBean("shi","狮",""))
        list.add(LetterBean("ri","日",""))
        list.add(LetterBean("zi","兹",""))
        list.add(LetterBean("ci","呲",""))
        list.add(LetterBean("si","丝",""))
        list.add(LetterBean("yuan","渊",""))
        list.add(LetterBean("yin","因",""))
        list.add(LetterBean("yun","晕",""))
        list.add(LetterBean("ying","英",""))

        list
    }

    private val zmList by lazy {
        val list = ArrayList<LetterBean>()
        list.add(LetterBean("A a [ei]","诶",""))
        list.add(LetterBean("B b [bi:]","哔",""))
        list.add(LetterBean("C c [si:]","司仪",""))
        list.add(LetterBean("D d [di:]","低",""))
        list.add(LetterBean("E e [i:]","衣",""))
        list.add(LetterBean("F f [ef]","爱抚",""))
        list.add(LetterBean("G g [dʒi:]","计一",""))
        list.add(LetterBean("H h [eit∫]","诶吃",""))
        list.add(LetterBean("I i [ai]","唉一",""))
        list.add(LetterBean("J j [dʒei]","之诶",""))
        list.add(LetterBean("K k [kei]","尅",""))
        list.add(LetterBean("L l [el]","爱凹",""))
        list.add(LetterBean("M m [em]","爱母",""))
        list.add(LetterBean("N n [en]","爱恩",""))
        list.add(LetterBean("O o [əʊ]","欧",""))
        list.add(LetterBean("P p [pi:]","劈",""))
        list.add(LetterBean("Q q [kju:]","棵右",""))
        list.add(LetterBean("R r [ɑ:]","啊二",""))
        list.add(LetterBean("S s [es]","爱丝",""))
        list.add(LetterBean("T t [ti:]","踢",""))
        list.add(LetterBean("U u [ju:]","优",""))
        list.add(LetterBean("V v [vi:]","威",""))
        list.add(LetterBean("W w ['dʌblju:]","答不留",""))
        list.add(LetterBean("X x [eks]","爱克丝",""))
        list.add(LetterBean("Y y [wai]","外",""))
        list.add(LetterBean("Z z [zi:]","紫一",""))
        list
    }

    private val ybList by lazy {
        val list = ArrayList<LetterBean>()
        list.add(LetterBean("元音"))
        list.add(LetterBean("[i:]","",""))
        list.add(LetterBean("[ɪ]","",""))
        list.add(LetterBean("[e]","",""))
        list.add(LetterBean("[æ]","",""))
        list.add(LetterBean("[ɜ]","",""))
        list.add(LetterBean("[ə]","",""))
        list.add(LetterBean("[ʌ]","",""))
        list.add(LetterBean("[ɔ:]","",""))
        list.add(LetterBean("[ɒ]","",""))
        list.add(LetterBean("[u:]","",""))
        list.add(LetterBean("[ʊ]","",""))
        list.add(LetterBean("[ɑ:]","",""))
        list.add(LetterBean("[aɪ]","",""))
        list.add(LetterBean("[eɪ]","",""))
        list.add(LetterBean("[aʊ]","",""))
        list.add(LetterBean("[əʊ]","",""))
        list.add(LetterBean("[ɔɪ]","",""))
        list.add(LetterBean("[ɪə]","",""))
        list.add(LetterBean("[eə]","",""))
        list.add(LetterBean("[ʊə]","",""))

        list.add(LetterBean("辅音"))
        list.add(LetterBean("[p]","",""))
        list.add(LetterBean("[t]","",""))
        list.add(LetterBean("[k]","",""))
        list.add(LetterBean("[b]","",""))
        list.add(LetterBean("[d]","",""))
        list.add(LetterBean("[g]","",""))
        list.add(LetterBean("[f]","",""))
        list.add(LetterBean("[s]","",""))
        list.add(LetterBean("[∫]","",""))
        list.add(LetterBean("[θ]","",""))
        list.add(LetterBean("[h]","",""))
        list.add(LetterBean("[v]","",""))
        list.add(LetterBean("[z]","",""))
        list.add(LetterBean("[ʒ]","",""))
        list.add(LetterBean("[ð]","",""))
        list.add(LetterBean("[r]","",""))
        list.add(LetterBean("[tʃ]","",""))
        list.add(LetterBean("[tr]","",""))
        list.add(LetterBean("[ts]","",""))
        list.add(LetterBean("[dʒ]","",""))
        list.add(LetterBean("[dr]","",""))
        list.add(LetterBean("[dz]","",""))
        list.add(LetterBean("[m]","",""))
        list.add(LetterBean("[n]","",""))
        list.add(LetterBean("[ŋ]","",""))
        list.add(LetterBean("[l]","",""))
        list.add(LetterBean("[j]","",""))
        list.add(LetterBean("[w]","",""))

        list
    }

}