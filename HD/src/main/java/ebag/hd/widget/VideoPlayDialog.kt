package ebag.hd.widget

import android.content.Context
import android.view.View
import android.widget.ImageView
import cn.jzvd.JZVideoPlayer
import cn.jzvd.JZVideoPlayerStandard
import ebag.core.base.BaseDialog
import ebag.core.util.loadImage
import ebag.hd.R
import kotlinx.android.synthetic.main.dialog_video_play.*

/**
 * Created by YZY on 2018/4/2.
 */
class VideoPlayDialog(context: Context): BaseDialog(context) {
    override fun getLayoutRes(): Int {
        return R.layout.dialog_video_play
    }

    private var url = ""
    override fun setWidth(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.x1052)
    }

    override fun setHeight(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.x680)
    }

    init {
        videoPlayer.findViewById<ImageView>(R.id.fullscreen).visibility = View.GONE
        setOnDismissListener {
            JZVideoPlayer.releaseAllVideos()
        }
    }

    fun show(url: String, fileName: String) {
        if (this.url != url){
            videoPlayer.setUp(url, JZVideoPlayerStandard.SCREEN_STATE_ON, fileName)
            videoPlayer.thumbImageView.loadImage(url)
        }
        super.show()
    }
}