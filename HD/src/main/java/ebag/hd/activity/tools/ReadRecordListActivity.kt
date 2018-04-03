package ebag.hd.activity.tools

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.util.DateUtil
import ebag.core.util.StringUtils
import ebag.core.util.VoicePlayerOnline
import ebag.core.util.loadHead
import ebag.hd.R
import ebag.hd.bean.ReadRecordAnswerBean
import ebag.hd.bean.ReadRecordVoiceBean
import ebag.hd.http.EBagApi
import kotlinx.android.synthetic.main.activity_read_record_list.*

/**
 * Created by YZY on 2018/3/21.
 */
class ReadRecordListActivity: BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_read_record_list
    }
    companion object {
        fun jump(languageId: String, classId: String, deadTime: Long, context: Context){
            context.startActivity(
                    Intent(context, ReadRecordListActivity::class.java)
                            .putExtra("languageId", languageId)
                            .putExtra("classId", classId)
                            .putExtra("deadTime", deadTime)
            )
        }
    }
    private var languageId = ""
    private var classId = ""
    private var deadTime: Long = 0
    private val leftAdapter = LeftAdapter()
    private val rightAdapter = RightAdapter()
    private val leftRequest = object : RequestCallBack<List<ReadRecordVoiceBean>>(){
        override fun onStart() {
            stateView.showLoading()
        }
        override fun onSuccess(entity: List<ReadRecordVoiceBean>?) {
            if(entity == null || entity.isEmpty())
                stateView.showEmpty()
            else
                stateView.showContent()
            leftAdapter.setNewData(entity)
            leftAdapter.selectedPosition = 0
            EBagApi.getReadRecordDesc(classId, entity!![0].languageDetailId, deadTime, rightRequest)
        }

        override fun onError(exception: Throwable) {
            stateView.showError(exception.message.toString())
        }
    }
    private val rightRequest = object : RequestCallBack<List<ReadRecordAnswerBean>>(){
        override fun onStart() {
            rightStateView.showLoading()
        }

        override fun onSuccess(entity: List<ReadRecordAnswerBean>?) {
            if (entity == null || entity.isEmpty()){
                rightStateView.showEmpty()
                return
            }
            rightStateView.showContent()
            rightAdapter.setNewData(entity)
        }

        override fun onError(exception: Throwable) {
            rightStateView.showError(exception.message.toString())
        }
    }
    override fun initViews() {
        languageId = intent.getStringExtra("languageId") ?: ""
        classId = intent.getStringExtra("classId") ?: ""
        deadTime = intent.getLongExtra("deadTime", 0)

        leftRecycler.layoutManager = LinearLayoutManager(this)
        leftRecycler.adapter = leftAdapter

        rightRecycler.layoutManager = LinearLayoutManager(this)
        rightRecycler.adapter = rightAdapter

        leftAdapter.setOnItemClickListener { adapter, view, position ->
            adapter as LeftAdapter
            adapter.selectedPosition = position
            EBagApi.getReadRecordDesc(classId, adapter.data[position].languageDetailId, deadTime, rightRequest)
        }
        leftAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.play_id)
                voicePlaySetting(view)
        }
        rightAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.play_id)
                voicePlaySetting(view)
        }

        EBagApi.getReadDetailList(languageId, leftRequest)
    }

    private val voicePlayer : VoicePlayerOnline by lazy {
        val player = VoicePlayerOnline(this)
        player.setOnPlayChangeListener(object : VoicePlayerOnline.OnPlayChangeListener{
            override fun onProgressChange(progress: Int) {
                progressBar!!.progress = progress
            }
            override fun onCompletePlay() {
                tempUrl = null
                anim!!.stop()
                anim!!.selectDrawable(0)
                progressBar!!.progress = 0
            }
        })
        player
    }
    private var anim : AnimationDrawable? = null
    private var progressBar : ProgressBar? = null
    private var tempUrl: String? = null

    private fun voicePlaySetting(view: View){
        val url : String = view.getTag(R.id.play_id) as String
        if (StringUtils.isEmpty(url))
            return
        if (url != tempUrl){
            if(anim != null) {
                anim!!.stop()
                anim!!.selectDrawable(0)
                progressBar!!.progress = 0
            }
            anim = view.getTag(R.id.image_id) as AnimationDrawable
            progressBar = view.getTag(R.id.progress_id) as ProgressBar
            voicePlayer.playUrl(url)
            anim!!.start()
            tempUrl = url
        }else{
            if (voicePlayer.isPlaying && !voicePlayer.isPause){
                voicePlayer.pause()
                anim!!.stop()
            }else{
                voicePlayer.play()
                anim!!.start()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (voicePlayer.isPlaying && !voicePlayer.isPause) {
            voicePlayer.pause()
            anim!!.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        voicePlayer.stop()
        leftRequest.cancelRequest()
        rightRequest.cancelRequest()
    }
    inner class LeftAdapter: BaseQuickAdapter<ReadRecordVoiceBean, BaseViewHolder>(R.layout.item_read_record_voice){
        private var oldPosition = -1
        var selectedPosition = -1
            set(value) {
                if(field != value){
                    field = value
                    if(oldPosition != -1)
                        notifyItemChanged(oldPosition)
                    notifyItemChanged(field)
                    oldPosition = value
                }
            }

        override fun convert(helper: BaseViewHolder, item: ReadRecordVoiceBean?) {
            val isSelected = selectedPosition == helper.adapterPosition
            helper.setText(R.id.tvFirst, item?.languageEn)
                    .setText(R.id.tvSecond, item?.languageCn)
                    .setGone(R.id.play_id, isSelected)
                    .addOnClickListener(R.id.play_id)
                    .getView<View>(R.id.layout).isSelected = isSelected

            val linearLayout = helper.getView<LinearLayout>(R.id.play_id)
            val imageView = helper.getView<ImageView>(R.id.image_id)
            val progressBar = helper.getView<ProgressBar>(R.id.progress_id)
            val drawable = imageView.background as AnimationDrawable
            linearLayout.setTag(ebag.hd.R.id.image_id, drawable)
            linearLayout.setTag(ebag.hd.R.id.progress_id, progressBar)
            linearLayout.setTag(ebag.hd.R.id.play_id, item?.languageUrl)
            helper.addOnClickListener(R.id.play_id)
        }
    }

    inner class RightAdapter: BaseQuickAdapter<ReadRecordAnswerBean, BaseViewHolder>(R.layout.item_read_record_detail){
        override fun convert(helper: BaseViewHolder, item: ReadRecordAnswerBean?) {
            helper.getView<ImageView>(R.id.img_id).loadHead(item?.headUrl)
            helper.setText(R.id.tvTime, DateUtil.getDateTime(item?.dateTime ?: 0, "yyyy-MM-dd HH:mm"))

            val linearLayout = helper.getView<LinearLayout>(R.id.play_id)
            val imageView = helper.getView<ImageView>(R.id.image_id)
            val progressBar = helper.getView<ProgressBar>(R.id.progress_id)
            val drawable = imageView.background as AnimationDrawable
            linearLayout.setTag(ebag.hd.R.id.image_id, drawable)
            linearLayout.setTag(ebag.hd.R.id.progress_id, progressBar)
            linearLayout.setTag(ebag.hd.R.id.play_id, item?.myAudioUrl)
            helper.addOnClickListener(R.id.play_id)
        }
    }
}