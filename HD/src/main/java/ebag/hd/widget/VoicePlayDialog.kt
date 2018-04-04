package ebag.hd.widget

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import ebag.core.base.BaseDialog
import ebag.core.util.VoicePlayerOnline
import ebag.hd.R
import kotlinx.android.synthetic.main.dialog_voice_play.*

/**
 * Created by YZY on 2018/4/4.
 */
class VoicePlayDialog(private val mContext: Context): BaseDialog(mContext) {
    override fun getLayoutRes(): Int {
        return R.layout.dialog_voice_play
    }

    override fun setWidth(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.x400)
    }

    override fun setHeight(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.y180)
    }
    private var anim : AnimationDrawable = voiceImg.background as AnimationDrawable
    private var url = ""
    private var tempUrl = ""
    private var isSameUrl = false
    private val voicePlayer : VoicePlayerOnline by lazy {
        val player = VoicePlayerOnline(mContext)
        player.setOnPlayChangeListener(object : VoicePlayerOnline.OnPlayChangeListener{
            override fun onProgressChange(progress: Int) {
                progressBar.progress = progress
            }
            override fun onCompletePlay() {
                anim.stop()
                anim.selectDrawable(0)
            }
        })
        player
    }

    init {
        player.setOnClickListener {
            if (isSameUrl) {
                if (voicePlayer.isPlaying && !voicePlayer.isPause) {
                    voicePlayer.pause()
                    anim.stop()
                } else {
                    if (tempUrl == url) {
                        voicePlayer.play()
                    }else{
                        voicePlayer.playUrl(url)
                        tempUrl = url
                    }
                    anim.start()
                }
            }else{
                voicePlayer.playUrl(url)
                tempUrl = url
                anim.start()
                isSameUrl = true
            }
        }
        setOnDismissListener {
            if (voicePlayer.isPlaying && !voicePlayer.isPause) {
                voicePlayer.pause()
                anim.stop()
                anim.selectDrawable(0)
            }
        }
    }

    fun stop(){
        voicePlayer.stop()
    }

    fun show(url: String, fileName: String) {
        titleTv.text = fileName
        if (this.url == url){
            isSameUrl = true
        }else{
            this.url = url
            isSameUrl = false
            progressBar.progress = 0
        }
        anim.stop()
        anim.selectDrawable(0)
        super.show()
    }
}