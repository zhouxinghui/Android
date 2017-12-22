package ebag.core.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ebag.core.R

/**
 * 图片加载工具类
 * Created by YZY on 2017/11/9.
 */

object ImageViewUtils {
    val requestNormal: RequestOptions? = RequestOptions()
            .placeholder(R.drawable.ic_launcher)
            .error(R.drawable.ic_launcher)

    val requestCircle: RequestOptions? = RequestOptions()
            .placeholder(R.drawable.ic_launcher)
            .error(R.drawable.ic_launcher)
            .circleCrop()

}

/**
 * 加载图片（默认加载中和加载失败图片）
 */
fun ImageView.loadImage(context: Context?, url : String) {
    ImageViewUtils.requestNormal?.let {
        Glide
        .with(context)
        .load(url)
        .apply(it)
        .into(this)
    }//显示到目标View中
}
/**
 * 加载图片（自定义加载中和加载失败图片）
 */
fun ImageView.loadImage(context: Context, url : String, loadImg : Int, errorImg : Int) {
    Glide
            .with(context)
            .load(url)
            .apply(RequestOptions()
                    .placeholder(loadImg)
                    .error(errorImg))
            .into(this)
}

/**
 * 加载图片为圆形图片
 */
fun ImageView.loadImageToCircle(context: Context, url : String) {
    ImageViewUtils.requestCircle?.let {
        Glide
        .with(context)
        .load(url)
        .apply(it)
        .into(this)
    }//显示到目标View中
}
