package ebag.mobile.module.clazz.album

import android.content.Context
import android.view.View
import android.widget.TextView
import ebag.core.base.BasePopupWindow
import ebag.mobile.R
import ebag.mobile.base.Constants

/**
 * Created by YZY on 2018/4/20.
 */
class AlbumDetailPopup(mContext: Context, type: String): BasePopupWindow(mContext) {
    override fun getLayoutRes(): Int = R.layout.popup_album_detail

    override fun setHeight(): Int = contentView.resources.getDimensionPixelSize(R.dimen.y300)

    override fun setWidth(): Int = contentView.resources.getDimensionPixelSize(R.dimen.x80)

    private val selectAllTv = contentView.findViewById<TextView>(R.id.selectAll)
    private val shareTv = contentView.findViewById<TextView>(R.id.share)
    private val shareLine = contentView.findViewById<View>(R.id.shareLine)
    private val deleteTv = contentView.findViewById<TextView>(R.id.delete)
    private val cancelTv = contentView.findViewById<TextView>(R.id.cancel)
    var onModifyClick: ((type: Int) -> Unit)? = null
    init {
        isOutsideTouchable = false
        isFocusable = false

        selectAllTv.setOnClickListener {
            onModifyClick?.invoke(1)
            selectAllTv.text = if (selectAllTv.text.toString() == "全选")
                "全不选"
            else
                "全选"
        }
        deleteTv.setOnClickListener {
            onModifyClick?.invoke(3)
            dismiss()
        }
        cancelTv.setOnClickListener {
            onModifyClick?.invoke(4)
            dismiss()
        }
        if (type == Constants.PERSONAL_TYPE){
            shareTv.visibility = View.VISIBLE
            shareLine.visibility = View.VISIBLE
            shareTv.setOnClickListener {
                onModifyClick?.invoke(2)
                dismiss()
            }
        }
    }
}