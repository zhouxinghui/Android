package ebag.core.widget.empty

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import ebag.core.R
import kotlinx.android.synthetic.main.dialog_loading.*

/**
 * 正在加载
 * Created by YZY on 2018/1/6.
 */
class LoadingDialog(context: Context) : Dialog(context) {
    var anim: AnimationDrawable? = null
    var isShowingNow = false
    fun show(message: String?){
        loadingTv.text = message ?: "正在加载..."
        if (!isShowingNow) {
            anim!!.start()
            super.show()
            isShowingNow = true
        }
    }

    init {
        val contentView = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
        val imageView = contentView.findViewById<ImageView>(R.id.loadingImg)
        anim = imageView.background as AnimationDrawable?
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(contentView)
        setCancelable(false)
        window.setBackgroundDrawable(ColorDrawable())
        val params = window.attributes
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = params

        setOnDismissListener {
            isShowingNow = false
        }
    }
}