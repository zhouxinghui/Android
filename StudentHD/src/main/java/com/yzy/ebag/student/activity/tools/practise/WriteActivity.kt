package com.yzy.ebag.student.activity.tools.practise

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import com.yzy.ebag.student.R
import ebag.core.base.mvp.MVPActivity
import ebag.core.util.T
import ebag.core.util.VoicePlayerOnline
import kotlinx.android.synthetic.main.activity_write.*

/**
 * @author caoyu
 * @date 2018/1/23
 * @description
 */

const val SIZE_ONE = 10
const val SIZE_TWO = 11
const val SIZE_THREE = 12
const val SIZE_FOUR = 13

class WriteActivity: MVPActivity() {


    private var pinyins: String = "ping,ha,he,wu"
    private var mp3s: String = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/dʒ.mp3," +
            "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/dr.mp3," +
            "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/dz.mp3," +
            "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/mp3/phonogram_en/m.mp3"

    private lateinit var pinyinList: List<String>
    private lateinit var mp3List: List<String>
    private var maxIndex = 0
    private var currentIndex = 0

    private lateinit var animDrawable: AnimationDrawable
    companion object {
        fun jump(context: Context){
            context.startActivity(Intent(context,WriteActivity::class.java))
        }
    }

    override fun destroyPresenter() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_write
    }

    override fun initViews() {

        pinyinList = pinyins.split(",")
        mp3List = mp3s.split(",")
        maxIndex = Math.min(pinyinList.size, mp3List.size) - 1

        tvPinyin.text = pinyinList[currentIndex]

        drawView.setPenRawSize(resources.getDimension(R.dimen.x10))

        pen_size_group.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.pen_size_one -> {
                    drawView.setPenRawSize(resources.getDimension(R.dimen.x5))
                }
                R.id.pen_size_two -> {
                    drawView.setPenRawSize(resources.getDimension(R.dimen.x10))
                }
                R.id.pen_size_three -> {
                    drawView.setPenRawSize(resources.getDimension(R.dimen.x15))
                }
                R.id.pen_size_four -> {
                    drawView.setPenRawSize(resources.getDimension(R.dimen.x20))
                }
            }
        }

        btnEraser.setOnClickListener {
            drawView.clear()
        }

        tvCommit.setOnClickListener {
            val bitmap = drawView.buildBitmap()
            if(bitmap == null){
                T.show(this,"需要默写才能进行下一步操作哦")
                return@setOnClickListener
            }

            // 这里写 保存bitmap到本地的操作


            if(currentIndex == maxIndex){

            }else{
                drawView.clear()
                tvPinyin.text = pinyinList[currentIndex++]
            }
        }

        animDrawable = playAnim.background as AnimationDrawable
        playAnim.setOnClickListener {
            if(!player.isPlaying){
                animDrawable.start()
                player.playUrl(mp3List[currentIndex])
            }
        }
    }

    private val playerD = lazy {
        val pl = VoicePlayerOnline(this)
        pl.setOnPlayChangeListener(object : VoicePlayerOnline.OnPlayChangeListener{
            override fun onProgressChange(progress: Int) {
            }

            override fun onCompletePlay() {
                animDrawable.stop()
                animDrawable.selectDrawable(0)
            }
        })
        pl
    }
    private val player by playerD
}