package ebag.core.widget.empty

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.widget.ImageView
import ebag.core.R
import ebag.core.util.DensityUtil
import kotlinx.android.synthetic.main.dialog_loading.*

/**
 * 正在加载
 * Created by YZY on 2018/1/6.
 */
class LoadingDialog(context: Context?) : Dialog(context) {
    var anim: AnimationDrawable? = null

    fun show(message: String?){
        super.show()
        loadingTv.text = message ?: "正在加载..."
        anim!!.start()
    }

    init {
        val contentView = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
        val imageView = contentView.findViewById<ImageView>(R.id.loadingImg)
        anim = imageView.background as AnimationDrawable?
        setContentView(contentView)
        setCancelable(false)
        window.setBackgroundDrawable(BitmapDrawable())
        val params = window.attributes
        params.width = DensityUtil.dip2px(context!!, 120F)
        params.height = DensityUtil.dip2px(context, 200F)
        window.attributes = params
    }
}