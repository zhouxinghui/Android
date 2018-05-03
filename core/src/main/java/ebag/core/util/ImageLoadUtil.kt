package ebag.core.util

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import ebag.core.R


/**
 * 图片加载工具类
 * Created by YZY on 2017/11/9.
 */

object ImageViewUtils {
    val requestOptions = RequestOptions()
}

/**
 * 加载头像
 */
fun ImageView.loadHead(url: String?, skipCache: Boolean = false, signStr: String = "0000000000"){
    ImageViewUtils.requestOptions
            .skipMemoryCache(skipCache)
            .diskCacheStrategy(if (skipCache) DiskCacheStrategy.NONE else DiskCacheStrategy.AUTOMATIC)
            .signature({
                it.update(signStr.toByte())
            })
            .placeholder(R.drawable.head_default)
            .error(R.drawable.head_default)
            .circleCrop()
            .let {
                Glide
                    .with(this)
                    .load(url)
                    .apply(it)
                    .into(this)
            }
}

/**
 * 加载图片 java中调用
 */
fun ImageView.loadImage(url: String?) {
    ImageViewUtils.requestOptions
            .placeholder(R.drawable.replace_img)
            .error(R.drawable.replace_img)
            .centerCrop()
            .let {
                Glide
                    .with(this)
                    .load(url)
                    .apply(it)
                    .into(this)
            }
}

fun ImageView.loadInsideImage(url: String?) {
    ImageViewUtils.requestOptions
            .placeholder(R.drawable.replace_img)
            .error(R.drawable.replace_img)
            .centerCrop()
            .let {
                Glide
                    .with(this)
                    .asBitmap()
                    .load(url)
                    .apply(it)
                    .into(object : SimpleTarget<Bitmap>(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL, com.bumptech.glide.request.target.Target.SIZE_ORIGINAL) {
                        override fun onResourceReady(resource: Bitmap, glideAnimation: Transition<in Bitmap>) {
                            val imageWidth = resource.width
                            val imageHeight = resource.height
                            val width = DensityUtil.dip2px(context, 100F)//固定宽度
                            //宽度固定,然后根据原始宽高比得到此固定宽度需要的高度
                            val height = width * imageHeight / imageWidth
                            val para = this@loadInsideImage.layoutParams
                            para.height = height
                            para.width = width
                            this@loadInsideImage.setImageBitmap(resource)
                        }
                    })
            }
}
fun ImageView.loadPhoto(url: String?) {
    ImageViewUtils.requestOptions
            .placeholder(R.drawable.replace_img)
            .error(R.drawable.replace_img)
            .fitCenter()
            .let {
                Glide
                    .with(this)
                    .load(url)
                    .apply(it)
                    .into(this)
            }
}

/**
 * 加载图片（可自定义加载中和加载失败图片）
 */
fun ImageView.loadImage(url: String?, loadImg: Int = R.drawable.replace_img, errorImg: Int = R.drawable.replace_img) {
    ImageViewUtils.requestOptions
            .placeholder(loadImg)
            .error(errorImg)
            .centerCrop()
            .let {
                Glide
                    .with(this)
                    .load(url)
                    .apply(it)
                    .into(this)
            }
}

/**
 * 加载图片为圆形图片
 */
fun ImageView.loadImageToCircle(url: String?, loadImg: Int = R.drawable.replace_round_img, errorImg: Int = R.drawable.replace_round_img) {
    ImageViewUtils.requestOptions
            .placeholder(loadImg)
            .error(errorImg)
            .circleCrop()
            .let {
                Glide
                    .with(this)
                    .load(url)
                    .apply(it)
                    .into(this)
            }
}
