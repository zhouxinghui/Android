package ebag.hd.activity

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.base.BaseActivity
import ebag.core.util.*
import ebag.hd.R
import ebag.hd.widget.PaletteView
import ebag.hd.widget.TitleBar
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
        baseFab.registerButton(noteBtn)
        baseFab.registerButton(clearBtn)
        baseFab.registerButton(eraserBtn)
        baseFab.registerButton(penBtn)
        baseFab.registerButton(saveBtn)
        noteBtn.setOnClickListener(this)
        baseFab.setOnClickListener(this)
        clearBtn.setOnClickListener(this)
        eraserBtn.setOnClickListener(this)
        penBtn.setOnClickListener(this)
        saveBtn.setOnClickListener(this)
        pageView.setOnPageTurnListener { count, currentPosition ->
            if(!baseFab.isDraftable)
                FabAnimationUtil.slideButtons(this,baseFab)
        }
        paletteView.setCanDraw(false)
        paletteView.setFirstLoadBitmap(FileUtil.getBookTrackPath() + "textTrack.png")

        dragView.setOnTouchListener { v, event ->
            if(!baseFab.isDraftable) {
                FabAnimationUtil.slideButtons(this, baseFab)
                paletteView.setCanDraw(false)
            }
            super.onTouchEvent(event)
        }
        noteTitle.setOnTitleBarClickListener(object : TitleBar.OnTitleBarClickListener{
            override fun leftClick() {
                if (backEvent())
                    finish()//占位代码，永远不会执行
            }
            override fun rightClick() {
            }
        })
        rootView.setOnBottomHiddenChange { isShow ->  dragView.isSelected = isShow}
        dragView.setOnClickListener{
            if (noteLayout.height == 0)
                setNoteVisible(ScreenUtil.getScreenHeight(this)/2, true)
            else
                setNoteVisible(noteLayout.height)
        }

        val list = ArrayList<String>()
        for (i in 0..9){
            list.add("测试笔记测试笔记测试笔记测试笔记测试笔记测试笔记测试笔记测试笔记测试笔记测试笔记测试笔记")
        }
        val adapter = NoteAdapter()
        noteRecycler.adapter = adapter
        noteRecycler.layoutManager = LinearLayoutManager(this)
        adapter.setNewData(list)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.baseFab ->{
                FabAnimationUtil.slideButtons(this,baseFab)
                if(!baseFab.isDraftable) {
                    paletteView.setCanDraw(false)
                }else{
                    paletteView.setCanDraw(true)
                }
            }
            R.id.noteBtn ->{
                T.show(this,"笔记本")
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

    inner class NoteAdapter: BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_note){
        override fun convert(helper: BaseViewHolder, item: String?) {
            helper.setText(R.id.content_tv, item)
                    .itemView.setOnClickListener {
                noteEdit.setText(item)
                noteEdit.visibility = View.VISIBLE
            }
        }
    }

    override fun onBackPressed() {
        if (backEvent())
            super.onBackPressed()
    }

    private fun backEvent(): Boolean{
        return if(noteEdit.visibility == View.VISIBLE){
            noteEdit.visibility = View.GONE
            false
        }else if(noteEdit.visibility == View.GONE && noteLayout.height != 0){
            setNoteVisible(noteLayout.height)
            false
        }else{
            true
        }
    }

    private fun setNoteVisible(height: Int, isShow: Boolean = false) {
        //属性动画对象
        val va: ValueAnimator
        if (isShow){
            va = ValueAnimator.ofInt(0, height)
            dragView.isSelected = true
        }else{
            va = ValueAnimator.ofInt(height, 0)
            dragView.isSelected = false
        }
        va.addUpdateListener { valueAnimator ->
            //获取当前的height值
            val h = valueAnimator.animatedValue as Int
            //动态更新view的高度
            noteLayout.layoutParams.height = h
            noteLayout.requestLayout()
        }
        va.duration = 500
        //开始动画
        va.start()
    }
}
