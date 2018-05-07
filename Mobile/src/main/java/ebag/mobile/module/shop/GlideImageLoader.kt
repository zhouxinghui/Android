package ebag.mobile.module.shop

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.youth.banner.loader.ImageLoader


/**
 * Created by fansan on 2018/3/17.
 */
class GlideImageLoader: ImageLoader(){

    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        Glide.with(context).load(path).into(imageView)
    }

}