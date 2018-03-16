package ebag.hd.activity

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.*
import ebag.hd.R
import ebag.hd.bean.ReaderBean
import ebag.hd.http.EBagApi
import ebag.hd.ui.fragment.BookNoteFragment
import ebag.hd.widget.BookCatalogPopup
import ebag.hd.widget.PaletteView
import kotlinx.android.synthetic.main.activity_reader.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by YZY on 2018/2/8.
 */
class ReaderActivity : BaseActivity() , View.OnClickListener, TextWatcher, RadioGroup.OnCheckedChangeListener, BookNoteFragment.NoteChangeListener{
    override fun getLayoutId(): Int {
        return R.layout.activity_reader
    }
    companion object {
        fun jump(context: Context, fileName: String, bookId: Int){
            context.startActivity(
                    Intent(context, ReaderActivity::class.java)
                            .putExtra("fileName", fileName)
                            .putExtra("bookId", bookId))
        }
    }
    private var bookId = 0
    private var pageAdapter : PageViewAdapter? = null
    private val bookCategoryPopup by lazy {
        val popup = BookCatalogPopup(this)
        popup.onCategoryClick = {
            pageView.setAdapter(pageAdapter, it)
        }
        popup
    }
    private var currentPage = 0
    private lateinit var trackAdapter: TrackAdapter
    private var isEditNote = false
    private lateinit var lastNote: String
    private lateinit var currentNote: String
    private lateinit var noteFragment: BookNoteFragment
    //记录当前笔记是新建还是旧的
    private var currentNotePosition = -1
    private var isModifyTrack = false
    private val saveTrackDialog by lazy {
        val dialog = AlertDialog.Builder(this)
                .setMessage("当前页面草稿有更新操作，是否保存？")
                .setCancelable(false)
                .setNegativeButton("不保存", { dialog, _ ->
                    isModifyTrack = false
                    dialog.dismiss()
                })
                .setPositiveButton("保存", { dialog, _ ->
                    //TODO 保存草稿轨迹图
                    isModifyTrack = false
                    dialog.dismiss()
                }).create()
        dialog
    }
    private var penSize = 0F
    private var penColor = "#000000"
    /**
     * 修改笔记
     */
    private val modifyNoteRequest by lazy {
        object : RequestCallBack<String>(){
            override fun onStart() {
                LoadingDialogUtil.showLoading(this@ReaderActivity, "正在修改...")
            }

            override fun onSuccess(entity: String?) {
                LoadingDialogUtil.closeLoadingDialog()
                T.show(this@ReaderActivity, "修改成功")
                noteFragment.onRetryClick()
//                noteFragment.setNoteData(currentNotePosition, currentNote)
                hideNoteEdit()
            }

            override fun onError(exception: Throwable) {
                LoadingDialogUtil.closeLoadingDialog()
                exception.handleThrowable(this@ReaderActivity)
            }
        }
    }
    private val addNoteRequest by lazy {
        object : RequestCallBack<String>(){
            override fun onStart() {
                LoadingDialogUtil.showLoading(this@ReaderActivity, "正在添加...")
            }

            override fun onSuccess(entity: String?) {
                LoadingDialogUtil.closeLoadingDialog()
                T.show(this@ReaderActivity, "添加成功")
                noteFragment.onRetryClick()
//                noteFragment.addNoteData(0, currentNote)
                hideNoteEdit()
                noteFragment.scrollToPosition(0)
            }

            override fun onError(exception: Throwable) {
                LoadingDialogUtil.closeLoadingDialog()
                exception.handleThrowable(this@ReaderActivity)
            }
        }
    }
    override fun initViews() {
        bookId = intent.getIntExtra("bookId", 0)
        penSize = resources.getDimension(R.dimen.x2)
        initBook()
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
        penColorGroup.setOnCheckedChangeListener(this)
        penSizeGroup.setOnCheckedChangeListener(this)
        hidePenSet.setOnClickListener(this)

        pageView.setOnPageTurnListener { count, currentPosition ->
            currentPage = currentPosition
            if(!baseFab.isDraftable)
                FabAnimationUtil.slideButtons(this,baseFab)
            trackPager.setCurrentItem(currentPosition, false)
        }
        getPaletteView().setCanDraw(false)
        getPaletteView().setFirstLoadBitmap(FileUtil.getBookTrackPath() + "textTrack.png")
        titleBar.setOnLeftClickListener {
            val fileFolder = FileUtil.getBookTrackPath()
            FileUtil.deleteFile(fileFolder.substring(0, fileFolder.length - 1))
            finish()
        }

        titleBar.setOnRightClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                bookCategoryPopup.showAsDropDown(titleBar, 0, 0, Gravity.END)
                bookCategoryPopup.loadDate(bookId)
            }
        }

        dragView.setOnTouchListener { v, event ->
            if(!baseFab.isDraftable) {
                FabAnimationUtil.slideButtons(this, baseFab)
                getPaletteView().setCanDraw(false)
                penSetLayout.visibility = View.GONE
            }
            super.onTouchEvent(event)
        }
        dragView.setOnClickListener{
            if (isNoteLayoutVisible())
                setNoteVisible(noteLayout.height)
            else
                setNoteVisible(ScreenUtil.getScreenHeight(this)/2, true)
        }
        noteFragment = BookNoteFragment.newInstance(bookId)
        noteFragment.setNoteChangeListener(this)
        supportFragmentManager.beginTransaction().replace(R.id.noteListLayout, noteFragment).commitAllowingStateLoss()
        noteTitle.setOnLeftClickListener {
            if (backEvent())
                finish()//占位代码，永远不会执行
        }
        noteTitle.setOnRightClickListener {
            if (!isEditNote) {
                showNoteEdit("", -1)
            }else{
                if (lastNote == currentNote){
                    T.show(this@ReaderActivity, "你未对笔记作任何更新操作")
                    return@setOnRightClickListener
                }else{
                    //保存笔记
                    if (currentNotePosition != -1){
                        EBagApi.modifyNote(noteFragment.getCurrentNote(currentNotePosition).id, currentNote, modifyNoteRequest)
                    }else{
                        EBagApi.addBookNote(bookId.toString(), currentNote, addNoteRequest)
                    }
                }
            }
        }
        rootView.setOnBottomHiddenChange { isShow ->  dragView.isSelected = isShow}
        noteEdit.addTextChangedListener(this)

    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.baseFab ->{
                if (isModifyTrack){
                    saveTrack()
                }
                FabAnimationUtil.slideButtons(this,baseFab)
                if(!baseFab.isDraftable) {
                    getPaletteView().setCanDraw(false)
                    penSetLayout.visibility = View.GONE
                }else{
                    getPaletteView().setCanDraw(true)
                    penSetLayout.visibility = View.VISIBLE
                }
            }
            R.id.noteBtn ->{
                T.show(this,"笔记本")
            }
            R.id.clearBtn ->{
                T.show(this, "清空")
                getPaletteView().clear()
                getPaletteView().setCanDraw(true)
            }
            R.id.eraserBtn ->{
                T.show(this, "橡皮擦")
                getPaletteView().mode = PaletteView.Mode.ERASER
                getPaletteView().setCanDraw(true)
            }
            R.id.penBtn ->{
                T.show(this, "画笔")
                getPaletteView().mode = PaletteView.Mode.DRAW
                getPaletteView().setCanDraw(true)
                penSetLayout.visibility = View.VISIBLE
            }
            R.id.saveBtn ->{
                getPaletteView().setCanDraw(false)
                saveTrack()
            }
            R.id.hidePenSet ->{
                penSetLayout.visibility = View.GONE
            }
        }
    }
    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when(checkedId){
            R.id.penSize1 ->{
                penSize = resources.getDimension(R.dimen.x2)
            }
            R.id.penSize2 ->{
                penSize = resources.getDimension(R.dimen.x4)
            }
            R.id.penSize3 ->{
                penSize = resources.getDimension(R.dimen.x6)
            }
            R.id.penSize4 ->{
                penSize = resources.getDimension(R.dimen.x8)
            }
            R.id.penBlack ->{
                penColor = "#000000"
            }
            R.id.penBlue ->{
                penColor = "#007BFF"
            }
            R.id.penRed ->{
                penColor = "#FF4E4E"
            }
            R.id.penOrange ->{
                penColor = "#FF9100"
            }
        }
        getPaletteView().setPenRawSize(penSize)
        getPaletteView().setPenColor(Color.parseColor(penColor))
    }
    private fun saveTrack(){
        val bitmap = getPaletteView().buildBitmap() ?: return
        val fileName = "${FileUtil.getBookTrackPath()}Track$currentPage.png"
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

    private fun initBook(){
        val imagePaths = ZipUtils.getAllImgs(intent.getStringExtra("fileName"))
        imagePaths.sortWith(Comparator { lhs, rhs ->
            val l = Integer.valueOf(lhs.substring(lhs.indexOf("page") + 4,
                    lhs.indexOf(".")))!!
            val r = Integer.valueOf(rhs.substring(rhs.indexOf("page") + 4,
                    rhs.indexOf(".")))!!
            // 书本按照页码排序
            when {
                l < r -> -1
                l > r -> 1
                else -> 0
            }
        })
        val tracks = arrayOfNulls<String>(imagePaths.size)
        tracks[0] = FileUtil.getBookTrackPath() + "textTrack.png"
        tracks[1] = FileUtil.getBookTrackPath() + "textTrack1.png"
        val list = ArrayList<ReaderBean>()
        for (i in 0 until imagePaths.size){
            val readerBean = ReaderBean()
            readerBean.imagePath = imagePaths[i]
            readerBean.notePath = tracks[i]
            list.add(readerBean)
        }
        pageAdapter = PageViewAdapter(this, imagePaths)
        pageView.setAdapter(pageAdapter)
        trackAdapter = TrackAdapter(tracks.asList())
        trackPager.offscreenPageLimit = 2
        trackPager.adapter = trackAdapter
    }

    private inner class TrackAdapter(private val trackPaths: List<String?>): PagerAdapter(){
        var mCurrentView: PaletteView? = null
        init {
            val palletView = PaletteView(this@ReaderActivity)
            palletView.setFirstLoadBitmap(trackPaths[0])
            palletView.setPenColor(Color.parseColor(penColor))
            palletView.setPenRawSize(resources.getDimension(R.dimen.x2))
            mCurrentView = palletView
        }
        override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            return trackPaths.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val palletView = PaletteView(this@ReaderActivity)
            val trackPath = "${FileUtil.getBookTrackPath()}Track$position.png"
            if (FileUtil.isFileExists(trackPath))
                palletView.setFirstLoadBitmap(trackPath)
            palletView.setCanDraw(false)
            container.addView(palletView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            return palletView
        }
        /**
         * 销毁 一个 页卡
         */
        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            // 删除
            container.removeView(`object` as View)
        }

        override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
            mCurrentView = `object` as PaletteView
        }

        fun getPrimaryItem(): PaletteView {
            mCurrentView!!.setPenRawSize(penSize)
            mCurrentView!!.setPenColor(Color.parseColor(penColor))
            return mCurrentView!!
        }
    }

    private fun getPaletteView(): PaletteView{
        val paletteView = trackAdapter.getPrimaryItem()
        paletteView.setOnFingerMoveListener { isModifyTrack = true }
        return trackAdapter.getPrimaryItem()
    }

    override fun onBackPressed() {
        if (backEvent())
            super.onBackPressed()
    }

    private fun backEvent(): Boolean{
        return if(isNoteEditVisible()){
            hideNoteEdit()
            false
        }else if(!isNoteEditVisible() && isNoteLayoutVisible()) {
            setNoteVisible(noteLayout.height)
            false
        }else{
            val fileFolder = FileUtil.getBookTrackPath()
            FileUtil.deleteFile(fileFolder.substring(0, fileFolder.length - 1))
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

    override fun showNoteEdit(text: String, currentNotePosition: Int){
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
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

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
