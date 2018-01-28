package ebag.hd.activity

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
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



class ReaderActivity : BaseActivity() , View.OnClickListener, TextWatcher{
    override fun getLayoutId(): Int {
        return R.layout.activity_reader
    }
    companion object {
        fun jump(context: Context, fileName: String){
            context.startActivity(Intent(context, ReaderActivity::class.java).putExtra("fileName", fileName))
        }
    }
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var isEditNote = false
    private lateinit var lastNote: String
    private lateinit var currentNote: String
    //记录当前笔记是新建还是旧的
    private var currentNotePosition = -1
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
        dragView.setOnClickListener{
            if (isNoteLayoutVisible())
                setNoteVisible(noteLayout.height)
            else
                setNoteVisible(ScreenUtil.getScreenHeight(this)/2, true)
        }
        noteTitle.setOnTitleBarClickListener(object : TitleBar.OnTitleBarClickListener{
            override fun leftClick() {
                if (backEvent())
                    finish()//占位代码，永远不会执行
            }
            override fun rightClick() {
                if (!isEditNote) {
                    showNoteEdit("", -1)
                }else{
                    if (lastNote == currentNote){
                        T.show(this@ReaderActivity, "你未对笔记作任何更新操作")
                        return
                    }else{
                        //TODO 保存笔记
                        if (currentNotePosition != -1){
                            noteAdapter.setData(currentNotePosition, currentNote)
                        }else{
                            noteAdapter.addData(0, currentNote)
                        }
                        //TODO 保存笔记成功后
                        hideNoteEdit()
                        layoutManager.scrollToPosition(0)
                    }
                }
            }
        })
        rootView.setOnBottomHiddenChange { isShow ->  dragView.isSelected = isShow}
        noteEdit.addTextChangedListener(this)

        val list = ArrayList<String>()
        for (i in 0..9){
            list.add("测试笔记测试笔记测试笔记测试笔记测试笔记测试笔记测试笔记测试笔记测试笔记测试笔记测试笔记")
        }
        noteAdapter = NoteAdapter()
        layoutManager = LinearLayoutManager(this)
        noteRecycler.adapter = noteAdapter
        noteRecycler.layoutManager = layoutManager
        noteAdapter.setNewData(list)
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
                showNoteEdit(item!!, helper.adapterPosition)
            }
        }
    }

    override fun onBackPressed() {
        if (backEvent())
            super.onBackPressed()
    }

    private fun backEvent(): Boolean{
        return if(isNoteEditVisible()){
            hideNoteEdit()
            false
        }else if(!isNoteEditVisible() && isNoteLayoutVisible()){
            setNoteVisible(noteLayout.height)
            false
        }else{
            true
        }
    }

    /**
     * “笔记本”是否可见
     */
    private fun isNoteLayoutVisible(): Boolean{
        return noteLayout.height != 0
    }

    /**
     * 编辑笔记界面是否可见
     */
    private fun isNoteEditVisible(): Boolean{
        return noteEdit.visibility == View.VISIBLE
    }

    private fun showNoteEdit(text: String, currentNotePosition: Int){
        noteEdit.visibility = View.VISIBLE
        noteEdit.setText(text)
        noteEdit.setSelection(text.length)
        noteTitle.setRightText("完成")
        isEditNote = true
        lastNote = text
        currentNote = text
        this.currentNotePosition = currentNotePosition
    }
    private fun hideNoteEdit(){
        noteEdit.visibility = View.GONE
        noteTitle.setRightText("新增")
        isEditNote = false
    }
    override fun afterTextChanged(s: Editable?) {
        currentNote = noteEdit.text.toString()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
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
