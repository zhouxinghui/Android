package com.yzy.ebag.student.activity.tools.practise

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.Practise
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


    private var maxIndex = 0
    private var currentIndex = 0

    private lateinit var list: ArrayList<Practise>

    private lateinit var animDrawable: AnimationDrawable
    companion object {
        fun jump(context: Context, list: ArrayList<Practise>){
            context.startActivity(
                    Intent(context,WriteActivity::class.java)
                            .putExtra("list", list)
            )
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_write
    }


    override fun initViews() {
        val ser = intent.getSerializableExtra("list") ?: return

        list = ser as ArrayList<Practise>

        maxIndex = list.size - 1

        tvPinyin.text = list[currentIndex].hanzi

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
                tvPinyin.text = list[++currentIndex].hanzi
            }
        }

        animDrawable = playAnim.background as AnimationDrawable
        playAnim.setOnClickListener {
            if(!player.isPlaying){
                if(list[currentIndex].audio.isNullOrEmpty()){
                    T.show(this,"暂无对应音频")
                }else{
                    animDrawable.start()
                    player.playUrl(list[currentIndex].audio)
                }
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

    override fun destroyPresenter() {
        if(playerD.isInitialized())
            player.stop()
    }
}