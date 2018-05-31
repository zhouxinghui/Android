package com.yzy.ebag.student.module.tools

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.os.Message
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.Practise
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.mvp.MVPActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.util.*
import ebag.mobile.bean.UserEntity
import kotlinx.android.synthetic.main.activity_write.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class WriteActivity : MVPActivity() {


    private var maxIndex = 0
    private var currentIndex = 0

    private lateinit var list: ArrayList<Practise>
    private lateinit var userId: String
    private lateinit var practise: Practise
    private var uploadPosition = 0

    private lateinit var animDrawable: AnimationDrawable
    private lateinit var classId: String
    private lateinit var unitCode: String
    private lateinit var dir: File
    private val hanzi = StringBuilder()
    private val myHandler by lazy { WriteActivity.MyHandler(this) }

    companion object {
        fun jump(context: Context, list: ArrayList<Practise>, classId: String, unitCode: String) {
            context.startActivity(
                    Intent(context, WriteActivity::class.java)
                            .putExtra("list", list).putExtra("classId", classId).putExtra("unitCode", unitCode)
            )
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_write
    }


    override fun initViews() {


        val ser = intent.getSerializableExtra("list") ?: return
        classId = intent.getStringExtra("classId") ?: return
        unitCode = intent.getStringExtra("unitCode") ?: return

        list = ser as ArrayList<Practise>

        val userEntity = SerializableUtils.getSerializable<UserEntity>(ebag.mobile.base.Constants.STUDENT_USER_ENTITY)
        userId = userEntity?.uid ?: "userId"
        dir = File("${FileUtil.getRecorderPath()}/$userId/write")
        maxIndex = list.size - 1

        tvPinyin.text = list[currentIndex].pinyin
        tvHanzi.text = list[currentIndex].hanzi

        drawView.setPenRawSize(resources.getDimension(R.dimen.y10))

        pen_size_group.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.pen_size_one -> {
                    drawView.setPenRawSize(resources.getDimension(R.dimen.y5))
                }
                R.id.pen_size_two -> {
                    drawView.setPenRawSize(resources.getDimension(R.dimen.y10))
                }
                R.id.pen_size_three -> {
                    drawView.setPenRawSize(resources.getDimension(R.dimen.y15))
                }
                R.id.pen_size_four -> {
                    drawView.setPenRawSize(resources.getDimension(R.dimen.y20))
                }
            }
        }

        btnEraser.setOnClickListener {
            drawView.clear()
        }

        tvCommit.setOnClickListener {
            val bitmap = drawView.buildBitmap()
            if (bitmap == null) {
                T.show(this, "需要默写才能进行下一步操作哦")
                return@setOnClickListener
            }
            practise = list[currentIndex]
            // 这里写 保存bitmap到本地的操作
            saveBitmap(bitmap, practise.hanzi)
            if (currentIndex == maxIndex) {
                LoadingDialogUtil.showLoading(this, "正在上传...")
                hanzi.append("${practise.hanzi}")
                OSSUploadUtils.getInstance().UploadPhotoToOSS(this, File(dir, practise.hanzi), "recorder/$userId", practise.hanzi, myHandler)
            } else {
                hanzi.append("${practise.hanzi},")
                drawView.clear()
                tvPinyin.text = list[++currentIndex].pinyin
            }
        }

        animDrawable = playAnim.background as AnimationDrawable
        playAnim.setOnClickListener {
            if (!player.isPlaying) {
                if (list[currentIndex].audio.isNullOrEmpty()) {
                    T.show(this, "暂无对应音频")
                } else {
                    animDrawable.start()
                    player.playUrl(list[currentIndex].audio)
                }
            }
        }
    }


    private val playerD = lazy {
        val pl = VoicePlayerOnline(this)
        pl.setOnPlayChangeListener(object : VoicePlayerOnline.OnPlayChangeListener {
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
        if (playerD.isInitialized())
            player.stop()
    }

    fun commit() {
        val sb = StringBuilder()
        list.forEachIndexed { index, p ->
            val url = "${Constants.OSS_BASE_URL}/recorder/$userId/${p.hanzi}"
            if (index != list.size - 1) {
                sb.append("$url,")
            } else {
                sb.append(url)
            }
        }
        StudentApi.uploadWord(classId,hanzi.toString(), unitCode, sb.toString(), object : RequestCallBack<String>() {
            override fun onSuccess(entity: String?) {
                LoadingDialogUtil.closeLoadingDialog()
                T.show(this@WriteActivity, "提交成功")
                finish()
            }

            override fun onError(exception: Throwable) {
                LoadingDialogUtil.closeLoadingDialog()
                T.show(this@WriteActivity, "提交失败")
            }

        })
    }

    /** 保存方法  */
    private fun saveBitmap(bm: Bitmap, pinyin: String) {


        if (!dir.exists()) {
            dir.mkdirs()
        }
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


    class MyHandler(activity: WriteActivity) : HandlerUtil<WriteActivity>(activity) {
        override fun handleMessage(activity: WriteActivity, msg: Message) {
            when (msg.what) {
                Constants.UPLOAD_SUCCESS -> {
                    activity.uploadPosition += 1
                    if (activity.uploadPosition <= activity.list.size) {
                        val child = activity.list[activity.uploadPosition-1].hanzi
                        OSSUploadUtils.getInstance().UploadPhotoToOSS(activity, File(activity.dir, child), "recorder/${activity.userId}", child, activity.myHandler)
                    } else {
                        activity.commit()
                    }
                }
                Constants.UPLOAD_FAIL -> {
                    LoadingDialogUtil.closeLoadingDialog()
                    T.show(activity, "上传图片失败，请稍后重试")
                }

            }
        }
    }
}