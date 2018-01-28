package ebag.hd.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import ebag.core.base.BaseActivity
import ebag.core.util.*
import ebag.hd.R
import ebag.hd.widget.PaletteView
import kotlinx.android.synthetic.main.activity_reader.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ReaderActivity : BaseActivity() , View.OnClickListener{
    companion object {
        fun jump(context: Context, fileName: String){
            context.startActivity(Intent(context, ReaderActivity::class.java).putExtra("fileName", fileName))
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_reader
    }

    override fun initViews() {
        pageView.setAdapter(PageViewAdapter(this, ZipUtils.getAllImgs(intent.getStringExtra("fileName"))))
        baseFab.registerButton(clearBtn)
        baseFab.registerButton(eraserBtn)
        baseFab.registerButton(penBtn)
        baseFab.registerButton(saveBtn)
        baseFab.setOnClickListener(this)
        clearBtn.setOnClickListener(this)
        eraserBtn.setOnClickListener(this)
        penBtn.setOnClickListener(this)
        saveBtn.setOnClickListener(this)
        pageView.setOnPageTurnListener { count, currentPosition ->
            if(!baseFab.isDraftable)
                FabAnimationUtil.slideButtons(this,baseFab)
        }
        paletteView.postDelayed({
            paletteView.setCacheBitmap(FileUtil.getBookTrackPath() + "textTrack.png")
        },1000)

        imageView.loadImage(FileUtil.getBookTrackPath() + "textTrack.png")
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.baseFab ->{
                FabAnimationUtil.slideButtons(this,baseFab)
//                if(!baseFab.isDraftable) {
//                    paletteView.visibility = View.GONE
//                }else{
//                    paletteView.visibility = View.VISIBLE
//                }
            }
            R.id.clearBtn ->{
                T.show(this, "清空")
                paletteView.clear()
                paletteView.setCanDraw(true)
            }
            R.id.eraserBtn ->{
                T.show(this, "橡皮擦")
                paletteView.mode = PaletteView.Mode.ERASER
                paletteView.setCanDraw(true)
            }
            R.id.penBtn ->{
                T.show(this, "画笔")
                paletteView.mode = PaletteView.Mode.DRAW
                paletteView.setCanDraw(true)
            }
            R.id.saveBtn ->{
                paletteView.setCanDraw(false)
                val bitmap = paletteView.buildBitmap()
//                imageView.setImageBitmap(bitmap)
                saveTrack(bitmap)
            }
        }
    }
    private fun saveTrack(bitmap: Bitmap?){
        if (bitmap == null) {
            T.show(this,"没有可保存的笔记")
            return
        }
        val fileName = FileUtil.getBookTrackPath() + "textTrack.png"
        val file = File(fileName)
        try {
            if (file.exists() && file.isFile)
                FileUtil.deleteFile(fileName)

            file.createNewFile()
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)// 以100%的品质创建png
            // 人走带门
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}
