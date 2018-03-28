package com.yzy.ebag.student.activity.tools.practise

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.Practise
import ebag.core.base.mvp.MVPActivity
import ebag.core.util.FileUtil
import ebag.core.util.SerializableUtils
import ebag.core.util.T
import ebag.core.util.VoicePlayerOnline
import ebag.hd.bean.response.UserEntity
import kotlinx.android.synthetic.main.activity_write.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException



/**
 * @author caoyu
 * @date 2018/1/23
 * @description
 */
class WriteActivity: MVPActivity() {


    private var maxIndex = 0
    private var currentIndex = 0

    private lateinit var list: ArrayList<Practise>
    private lateinit var userId: String
    private lateinit var practise: Practise

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

        val userEntity = SerializableUtils.getSerializable<UserEntity>(ebag.hd.base.Constants.STUDENT_USER_ENTITY)
        userId = userEntity?.uid ?: "userId"

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
            practise = list[currentIndex]
            // 这里写 保存bitmap到本地的操作
            val bm = drawView.buildBitmap()

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

    /** 保存方法  */
    fun saveBitmap(bm: Bitmap, pinyin: String) {

        val f = File("${FileUtil.getRecorderPath()}/$userId/write", pinyin)
        if (f.exists()) {
            f.delete()
        }
        try {
            val out = FileOutputStream(f)
            bm.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.flush()
            out.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}