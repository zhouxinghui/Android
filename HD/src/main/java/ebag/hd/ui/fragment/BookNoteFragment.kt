package ebag.hd.ui.fragment

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.T
import ebag.hd.R
import ebag.hd.bean.BookNoteBean
import ebag.hd.http.EBagApi

/**
 * Created by YZY on 2018/3/12.
 */
class BookNoteFragment: BaseListFragment<List<BookNoteBean>, BookNoteBean>() {
    private lateinit var adapter: NoteAdapter
    private var bookId = 0
    private var noteChangeListener: NoteChangeListener? = null
    private lateinit var layoutManager: LinearLayoutManager
    private var currentPosition = 0
    private val deleteNoteRequest by lazy {
        object : RequestCallBack<String>(){
            override fun onStart() {
                LoadingDialogUtil.showLoading(mContext, "正在删除...")
            }

            override fun onSuccess(entity: String?) {
                LoadingDialogUtil.closeLoadingDialog()
                T.show(mContext, "删除成功")
                adapter.remove(currentPosition)
            }

            override fun onError(exception: Throwable) {
                LoadingDialogUtil.closeLoadingDialog()
                exception.handleThrowable(mContext)
            }
        }
    }
    private val deleteDialog by lazy {
        val mDialog = AlertDialog.Builder(mContext)
                .setMessage("删除此条笔记？")
                .setPositiveButton("删除", { dialog, which ->
                    dialog.dismiss()
                    EBagApi.deleteNote(adapter.data[currentPosition].id, deleteNoteRequest)
                })
                .setNegativeButton("取消", {dialog, which ->
                    dialog.dismiss()
                })
                .create()
        mDialog
    }
    companion object {
        fun newInstance(bookId: Int): BookNoteFragment{
            val fragment = BookNoteFragment()
            val bundle = Bundle()
            bundle.putInt("bookId", bookId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun isPagerFragment(): Boolean {
        return false
    }

    override fun getBundle(bundle: Bundle?) {
        bookId = bundle!!.getInt("bookId", 0)
    }

    override fun loadConfig() {
        adapter.setOnItemLongClickListener { _, view, position ->
            currentPosition = position
            deleteDialog.show()
            true
        }
    }

    override fun getPageSize(): Int {
        return 10
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<BookNoteBean>>) {
        EBagApi.bookNoteList(bookId.toString(), page, getPageSize(), requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<BookNoteBean>?): List<BookNoteBean>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<BookNoteBean, BaseViewHolder> {
        adapter = NoteAdapter()
        return adapter
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<BookNoteBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        layoutManager = LinearLayoutManager(mContext)
        return layoutManager
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        adapter as NoteAdapter
        noteChangeListener?.showNoteEdit(adapter.data[position].note, position)
    }

    private inner class NoteAdapter: BaseQuickAdapter<BookNoteBean, BaseViewHolder>(R.layout.item_note){
        override fun convert(helper: BaseViewHolder, item: BookNoteBean) {
            helper.setText(R.id.content_tv, item.note)
        }
    }
    fun getCurrentNote(position: Int): BookNoteBean{
        return adapter.data[position]
    }

    fun setNoteData(position: Int, note: String){
        val noteBean = adapter.data[position]
        noteBean.note = note
        adapter.setData(position, noteBean)
    }

    fun addNoteData(position: Int, note: String){

        val noteBean = adapter.data[position]
        noteBean.note = note
        adapter.addData(position, noteBean)
    }

    fun scrollToPosition(position: Int){
        layoutManager.scrollToPosition(position)
    }

    fun setNoteChangeListener(noteChangeListener: NoteChangeListener){
        this.noteChangeListener = noteChangeListener
    }

    interface NoteChangeListener{
        fun showNoteEdit(text: String, currentNotePosition: Int)
    }
}