package com.yzy.ebag.student.activity.tools

import android.content.Intent
import android.graphics.Paint
import android.graphics.drawable.AnimationDrawable
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
import com.yzy.ebag.student.bean.LetterBean
import ebag.core.http.network.RequestCallBack
import ebag.core.util.VoicePlayerOnline


/**
 * @author caoyu
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

    val adapter = LetterAdapter()
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

    override fun getLayoutManager(adapter: BaseQuickAdapter<LetterBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return GridLayoutManager(this,6)
    }

    private val playerD = lazy {
        val pl = VoicePlayerOnline(this)
        pl.setOnPlayChangeListener(object :VoicePlayerOnline.OnPlayChangeListener{
            override fun onProgressChange(progress: Int) {
            }

            override fun onCompletePlay() {
                adapter.selectedPosition = -1
                animDrawable?.stop()
                animDrawable?.selectDrawable(0)
            }
        })
        pl
    }
    private val player by playerD
    private var animDrawable: AnimationDrawable? = null
    override fun onItemClick(a: BaseQuickAdapter<*, *>, view: View, position: Int) {
        //点击  normal 才有效
        if(adapter.getItemViewType(position) == LetterBean.NORMAL_TYPE)
            if(adapter.selectedPosition != position){
                if(!player.isPlaying){
                    if(animDrawable != null){
                        animDrawable?.stop()
                        animDrawable?.selectDrawable(0)
                    }
                    animDrawable = view.getTag(R.id.soundView) as AnimationDrawable
                    animDrawable?.start()
                    player.playUrl(adapter.getItem(position)?.mp3)
                    adapter.selectedPosition = position
//            }else{
//                animDrawable.stop()
//                animDrawable = view.findViewById<View>(R.id.soundView).background as AnimationDrawable
//                animDrawable.start()
//                player.playUrl((adapter as LetterAdapter).getItem(position)?.mp3)
//                adapter.selectedPosition = position
                }
            }

    }

    override fun onDestroy() {
        super.onDestroy()
        if(playerD.isInitialized() && player.isPlaying){
            player.stop()
        }
    }

    inner class LetterAdapter: BaseMultiItemQuickAdapter<LetterBean, BaseViewHolder>(null) {

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
        list.add(LetterBean("b", "波", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("p", "泼", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("m", "摸", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("f", "佛", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("d", "的", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("t", "特", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("n", "呢", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("l", "了", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("g", "哥", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("k", "科", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("h", "喝", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("j", "鸡", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("q", "期", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("x", "西", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("z", "兹", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("c", "呲", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("s", "丝", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("r", "日", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("zh", "知", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("ch", "痴", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("sh", "狮", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("y", "衣", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("w", "乌", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))

        list
    }

    private val ymList by lazy {
        val list = ArrayList<LetterBean>()
        list.add(LetterBean("单韵母"))
        list.add(LetterBean("a", "啊", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("o", "喔", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("e", "鹅", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("i", "衣", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("u", "乌", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("ü", "鱼", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))

        list.add(LetterBean("复韵母"))
        list.add(LetterBean("ai", "唉", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("ei", "诶", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("ui", "威", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("ao", "奥", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("ou", "欧", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("iu", "优", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("ie", "耶", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("üe", "约", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("er", "耳", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))

        list.add(LetterBean("前鼻韵母"))
        list.add(LetterBean("an", "安", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("en", "恩", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("in", "因", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("un", "温", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("̈ün", "晕", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))

        list.add(LetterBean("后鼻韵母"))
        list.add(LetterBean("ang", "昂", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("eng", "鞥", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("ing", "英", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("ong", "翁", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))

        list
    }

    private val ztList by lazy {
        val list = ArrayList<LetterBean>()
        list.add(LetterBean("zhi", "知", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("chi", "痴", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("shi", "狮", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("ri", "日", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("zi", "兹", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("ci", "呲", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("si", "丝", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("yuan", "渊", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("yin", "因", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("yun", "晕", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("ying", "英", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))

        list
    }

    private val zmList by lazy {
        val list = ArrayList<LetterBean>()
        list.add(LetterBean("A a [ei]", "诶", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("B b [bi:]", "哔", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("C c [si:]", "司仪", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("D d [di:]", "低", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("E e [i:]", "衣", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("F f [ef]", "爱抚", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("G g [dʒi:]", "计一", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("H h [eit∫]", "诶吃", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("I i [ai]", "唉一", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("J j [dʒei]", "之诶", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("K k [kei]", "尅", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("L l [el]", "爱凹", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("M m [em]", "爱母", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("N n [en]", "爱恩", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("O o [əʊ]", "欧", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("P p [pi:]", "劈", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("Q q [kju:]", "棵右", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("R r [ɑ:]", "啊二", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("S s [es]", "爱丝", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("T t [ti:]", "踢", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("U u [ju:]", "优", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("V v [vi:]", "威", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("W w ['dʌblju:]", "答不留", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("X x [eks]", "爱克丝", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("Y y [wai]", "外", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list.add(LetterBean("Z z [zi:]", "紫一", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/%5E-sound.mp3"))
        list
    }

    private val ybList by lazy {
        val list = ArrayList<LetterBean>()
        list.add(LetterBean("元音"))
        list.add(LetterBean("[i:]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/i_.mp3"))
        list.add(LetterBean("[ɪ]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/ɪ.mp3"))

        list.add(LetterBean("[ɔ:]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/ɔ_.mp3"))
        list.add(LetterBean("[ɒ]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/ɒ.mp3"))

        list.add(LetterBean("[u:]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/u_.mp3"))
        list.add(LetterBean("[ʊ]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/ʊ.mp3"))

        list.add(LetterBean("[ɜ:]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/ɜ_.mp3"))
        list.add(LetterBean("[ə]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/ə.mp3"))

        list.add(LetterBean("[ɑ:]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/ɑ_.mp3"))
        list.add(LetterBean("[ʌ]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/ʌ.mp3"))

        list.add(LetterBean("[e]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/e.mp3"))
        list.add(LetterBean("[æ]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/æ.mp3"))

        list.add(LetterBean("[aɪ]", "", 
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/aɪ.mp3"))
        list.add(LetterBean("[eɪ]", "", 
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/eɪ.mp3"))
        list.add(LetterBean("[aʊ]", "", 
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/aʊ.mp3"))
        list.add(LetterBean("[əʊ]", "", 
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/əʊ.mp3"))
        list.add(LetterBean("[ɔɪ]", "", 
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/ɔɪ.mp3"))
        list.add(LetterBean("[ɪə]", "", 
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/ɪə.mp3"))
        list.add(LetterBean("[eə]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/eə.mp3"))
        list.add(LetterBean("[ʊə]", "", 
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/ʊə.mp3"))

        list.add(LetterBean("辅音"))
        list.add(LetterBean("[p]", "", 
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/p.mp3"))
        list.add(LetterBean("[t]", "", 
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/t.mp3"))
        list.add(LetterBean("[k]", "", 
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/k.mp3"))
        list.add(LetterBean("[b]", "", 
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/b.mp3"))
        list.add(LetterBean("[d]", "", 
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/d.mp3"))
        list.add(LetterBean("[g]", "", 
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/g.mp3"))
        list.add(LetterBean("[f]", "", 
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/f.mp3"))
        list.add(LetterBean("[s]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/s.mp3"))
        list.add(LetterBean("[ʃ]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/ʃ.mp3"))
        list.add(LetterBean("[θ]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/θ.mp3"))
        list.add(LetterBean("[h]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/h.mp3"))
        list.add(LetterBean("[v]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/v.mp3"))
        list.add(LetterBean("[z]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/z.mp3"))
        list.add(LetterBean("[ʒ]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/ʒ.mp3"))
        list.add(LetterBean("[ð]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/ð.mp3"))
        list.add(LetterBean("[r]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/r.mp3"))
        list.add(LetterBean("[tʃ]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/tʃ.mp3"))
        list.add(LetterBean("[tr]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/tr.mp3"))
        list.add(LetterBean("[ts]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/ts.mp3"))
        list.add(LetterBean("[dʒ]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/dʒ.mp3"))
        list.add(LetterBean("[dr]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/dr.mp3"))
        list.add(LetterBean("[dz]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/dz.mp3"))
        list.add(LetterBean("[m]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/m.mp3"))
        list.add(LetterBean("[n]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/n.mp3"))
        list.add(LetterBean("[ŋ]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/ŋ.mp3"))
        list.add(LetterBean("[l]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/l.mp3"))
        list.add(LetterBean("[j]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/j.mp3"))
        list.add(LetterBean("[w]", "",
                "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/w.mp3"))

        list
    }

}