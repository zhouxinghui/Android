package ebag.hd.widget

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.PopupWindow
import ebag.hd.R

/**
 * Created by YZY on 2018/2/8.
 */
class BookCatalogPopup(context: Context): PopupWindow(context) {
    init {
        contentView = LayoutInflater.from(context).inflate(R.layout.popup_catalog, null)
        width = context.resources.getDimensionPixelSize(R.dimen.x200)
        height = context.resources.getDimensionPixelSize(R.dimen.y300)
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable())
        animationStyle = R.style.book_catalog_anim
    }
}