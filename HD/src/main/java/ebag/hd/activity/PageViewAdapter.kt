package ebag.hd.activity

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import ebag.core.util.loadPhoto
import java.util.*

/**
 * Created by YZY on 2018/1/25.
 */
class PageViewAdapter(private val mContext: Context, private val images: ArrayList<String>?) : BaseAdapter() {

    override fun getCount(): Int {
        return images?.size ?: 0
    }

    override fun getItem(position: Int): Any {
        return images!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView: ImageView?
        if (convertView == null) {
            imageView = ImageView(mContext)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
        }else{
            imageView = convertView as ImageView
        }

        imageView.loadPhoto(images!![position])
        return imageView
    }


}