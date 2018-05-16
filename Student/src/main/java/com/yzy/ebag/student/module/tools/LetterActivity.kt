package com.yzy.ebag.student.module.tools

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.LetterBean
import ebag.core.base.BaseActivity
import kotlinx.android.synthetic.main.activity_letter.*

/**
 * Created by YZY on 2018/5/15.
 */
class LetterActivity: BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_letter

    companion object {
        fun jump(context: Context, type: Int){
            context.startActivity(
                    Intent(context, LetterActivity::class.java)
                            .putExtra("type", type)
            )
        }
        val ZH = 0
        val EN = 1
    }
    override fun initViews() {
        val type = intent.getIntExtra("type", ZH)
        val fragments = ArrayList<LetterFragment>()
        val titleList = ArrayList<String>()
        if (type == ZH){
            titleBar.setTitle("汉语拼音")
            fragments.add(LetterFragment.newInstance(smList, type))
            fragments.add(LetterFragment.newInstance(ymList, type))
            fragments.add(LetterFragment.newInstance(ztList, type))
            titleList.add("声母表")
            titleList.add("韵母表")
            titleList.add("整体认读音节")
        }else{
            titleBar.setTitle("英文字母")
            fragments.add(LetterFragment.newInstance(zmList, type))
            fragments.add(LetterFragment.newInstance(ybList, type))
            titleList.add("字母表")
            titleList.add("音标表")
        }
        viewPager.adapter = ViewPagerAdapter(fragments, titleList)
        tabLayout.setupWithViewPager(viewPager)
    }

    private inner class ViewPagerAdapter(private val fragments: List<LetterFragment>, private val titleList: List<String>): FragmentPagerAdapter(supportFragmentManager){
        override fun getItem(position: Int): Fragment = fragments[position]

        override fun getCount(): Int = fragments.size

        override fun getPageTitle(position: Int): CharSequence {
            return titleList[position]
        }
    }

    private val smList by lazy {
        val list = ArrayList<LetterBean>()
        list.add(LetterBean("b", "波", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/sm/b.mp3"))
        list.add(LetterBean("p", "泼", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/sm/p.mp3"))
        list.add(LetterBean("m", "摸", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/sm/m.mp3"))
        list.add(LetterBean("f", "佛", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/sm/f.mp3"))
        list.add(LetterBean("d", "的", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/sm/d.mp3"))
        list.add(LetterBean("t", "特", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/sm/t.mp3"))
        list.add(LetterBean("n", "呢", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/sm/n.mp3"))
        list.add(LetterBean("l", "了", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/sm/l.mp3"))
        list.add(LetterBean("g", "哥", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/sm/g.mp3"))
        list.add(LetterBean("k", "科", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/sm/k.mp3"))
        list.add(LetterBean("h", "喝", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/sm/h.mp3"))
        list.add(LetterBean("j", "鸡", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/sm/j.mp3"))
        list.add(LetterBean("q", "期", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/sm/q.mp3"))
        list.add(LetterBean("x", "西", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/sm/x.mp3"))
        list.add(LetterBean("z", "兹", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/sm/z.mp3"))
        list.add(LetterBean("c", "呲", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/sm/c.mp3"))
        list.add(LetterBean("s", "丝", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/sm/s.mp3"))
        list.add(LetterBean("r", "日", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/sm/r.mp3"))
        list.add(LetterBean("zh", "知", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/sm/zh.mp3"))
        list.add(LetterBean("ch", "痴", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/sm/ch.mp3"))
        list.add(LetterBean("sh", "狮", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/sm/sh.mp3"))
        list.add(LetterBean("y", "衣", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/sm/y.mp3"))
        list.add(LetterBean("w", "乌", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/sm/w.mp3"))

        list
    }

    private val ymList by lazy {
        val list = ArrayList<LetterBean>()
        list.add(LetterBean("单韵母"))
        list.add(LetterBean("a", "啊", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ym/a.mp3"))
        list.add(LetterBean("o", "喔", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ym/o.mp3"))
        list.add(LetterBean("e", "鹅", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ym/e.mp3"))
        list.add(LetterBean("i", "衣", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ym/i.mp3"))
        list.add(LetterBean("u", "乌", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ym/u.mp3"))
        list.add(LetterBean("ü", "鱼", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ym/v.mp3"))

        list.add(LetterBean("复韵母"))
        list.add(LetterBean("ai", "唉", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ym/ai.mp3"))
        list.add(LetterBean("ei", "诶", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ym/ei.mp3"))
        list.add(LetterBean("ui", "威", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ym/ui.mp3"))
        list.add(LetterBean("ao", "奥", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ym/ao.mp3"))
        list.add(LetterBean("ou", "欧", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ym/ou.mp3"))
        list.add(LetterBean("iu", "优", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ym/iu.mp3"))
        list.add(LetterBean("ie", "耶", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ym/ie.mp3"))
        list.add(LetterBean("üe", "约", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ym/ve.mp3"))
        list.add(LetterBean("er", "耳", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ym/er.mp3"))

        list.add(LetterBean("前鼻韵母"))
        list.add(LetterBean("an", "安", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ym/an.mp3"))
        list.add(LetterBean("en", "恩", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ym/en.mp3"))
        list.add(LetterBean("in", "因", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ym/in.mp3"))
        list.add(LetterBean("un", "温", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ym/un.mp3"))
        list.add(LetterBean("̈ün", "晕", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ym/vn.mp3"))

        list.add(LetterBean("后鼻韵母"))
        list.add(LetterBean("ang", "昂", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ym/ang.mp3"))
        list.add(LetterBean("eng", "鞥", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ym/eng.mp3"))
        list.add(LetterBean("ing", "英", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ym/ing.mp3"))
        list.add(LetterBean("ong", "翁", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ym/ong.mp3"))

        list
    }

    private val ztList by lazy {
        val list = ArrayList<LetterBean>()
        list.add(LetterBean("zhi", "知", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ztrtyj/zhi1.mp3"))
        list.add(LetterBean("chi", "痴", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ztrtyj/chi1.mp3"))
        list.add(LetterBean("shi", "狮", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ztrtyj/shi1.mp3"))
        list.add(LetterBean("ri", "日", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ztrtyj/ri1.mp3"))
        list.add(LetterBean("zi", "兹", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ztrtyj/zi1.mp3"))
        list.add(LetterBean("ci", "呲", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ztrtyj/ci1.mp3"))
        list.add(LetterBean("si", "丝", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ztrtyj/si1.mp3"))
        list.add(LetterBean("ye", "耶", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ztrtyj/ye1.mp3"))
        list.add(LetterBean("yu", "迂", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ztrtyj/yu1.mp3"))
        list.add(LetterBean("yuan", "渊", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ztrtyj/yuan1.mp3"))
        list.add(LetterBean("yue", "约", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ztrtyj/yue1.mp3"))
        list.add(LetterBean("yun", "晕", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ztrtyj/yun1.mp3"))
        list.add(LetterBean("yi", "一", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ztrtyj/yi1.mp3"))
        list.add(LetterBean("yin", "因", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ztrtyj/yin1.mp3"))
        list.add(LetterBean("ying", "英", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ztrtyj/ying1.mp3"))
        list.add(LetterBean("wu", "乌", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/ztrtyj/wu1.mp3"))

        list
    }

    private val zmList by lazy {
        val list = ArrayList<LetterBean>()
        list.add(LetterBean("A a [ei]", "诶", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/A.wav"))
        list.add(LetterBean("B b [bi:]", "哔", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/B.wav"))
        list.add(LetterBean("C c [si:]", "司仪", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/C.wav"))
        list.add(LetterBean("D d [di:]", "低", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/D.wav"))
        list.add(LetterBean("E e [i:]", "衣", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/E.wav"))
        list.add(LetterBean("F f [ef]", "爱抚", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/F.wav"))
        list.add(LetterBean("G g [dʒi:]", "计一", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/G.wav"))
        list.add(LetterBean("H h [eit∫]", "诶吃", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/H.wav"))
        list.add(LetterBean("I i [ai]", "唉一", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/I.wav"))
        list.add(LetterBean("J j [dʒei]", "之诶", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/J.wav"))
        list.add(LetterBean("K k [kei]", "尅", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/K.wav"))
        list.add(LetterBean("L l [el]", "爱凹", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/L.wav"))
        list.add(LetterBean("M m [em]", "爱母", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/M.wav"))
        list.add(LetterBean("N n [en]", "爱恩", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/N.wav"))
        list.add(LetterBean("O o [əʊ]", "欧", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/O.wav"))
        list.add(LetterBean("P p [pi:]", "劈", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/P.wav"))
        list.add(LetterBean("Q q [kju:]", "棵右", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/Q.wav"))
        list.add(LetterBean("R r [ɑ:]", "啊二", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/R.wav"))
        list.add(LetterBean("S s [es]", "爱丝", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/S.wav"))
        list.add(LetterBean("T t [ti:]", "踢", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/T.wav"))
        list.add(LetterBean("U u [ju:]", "优", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/U.wav"))
        list.add(LetterBean("V v [vi:]", "威", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/V.wav"))
        list.add(LetterBean("W w ['dʌblju:]", "答不留", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/W.wav"))
        list.add(LetterBean("X x [eks]", "爱克丝", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/X.wav"))
        list.add(LetterBean("Y y [wai]", "外", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/Y.wav"))
        list.add(LetterBean("Z z [zi:]", "紫一", "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/letter/Z.wav"))
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