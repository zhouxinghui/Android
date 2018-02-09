package com.yzy.ebag.teacher.ui.fragment.custom

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Message
import android.support.v4.app.Fragment
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseFragment
import ebag.core.util.*
import ebag.hd.widget.startSelectPicture
import kotlinx.android.synthetic.main.fragment_choice.*
import java.io.File

/**
 * Created by YZY on 2018/2/8.
 */
class ChoiceFragment : BaseFragment(), ICustomQuestionView, View.OnClickListener {
    companion object {
        fun newInstance(): Fragment{
            val fragment = ChoiceFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
    private var isPic = false
    //1：题目标题图片；2：选项A图片；3：选项B图片；4：选项C图片；5：选项D图片
    private var currentImage = 1
    private var titleImagePath = ""
    private var tempTitleUrl = ""
    private var titleUrl = ""
    private val imagePaths by lazy { ArrayList<String>() }
    private var uploadPosition = 0
    private val sb = StringBuilder()
    private var onConfirmClickListener: OnConfirmClickListener? = null
    override fun getLayoutRes(): Int {
        return R.layout.fragment_choice
    }

    override fun getBundle(bundle: Bundle?) {

    }

    override fun initViews(rootView: View) {
        setWordPicStyle("文⇌图", picWordBtn)
        setBracketsWordStyle(contentTv)
        setBracketsWordStyle(rightAnswerTv)
        image_a.setOnClickListener(this)
        image_b.setOnClickListener(this)
        image_c.setOnClickListener(this)
        image_d.setOnClickListener(this)

        picWordBtn.setOnClickListener {
            if (isPic){
                wordLayout.visibility = View.VISIBLE
                picLayout.visibility = View.GONE
                setWordPicStyle("文⇌图", picWordBtn)
                isPic = false
            }else{
                wordLayout.visibility = View.GONE
                picLayout.visibility = View.VISIBLE
                setWordPicStyle("图⇌文", picWordBtn)
                isPic = true
            }
        }
        titleImage.setOnClickListener {
            startSelectPicture(false)
        }
    }
    override fun getTitle(): String? {
        var str = titleEdit.text.toString()
        if (StringUtils.isEmpty(str)){
            T.show(mContext, "请输入题目标题")
            return null
        }
        return str
    }

    override fun getContent(): String? {
        if (isPic){

        }else{
            return getWordOptions()
        }
        return null
    }

    override fun getAnswer(): String? {
        val str = answerEdit.text.toString()
        if (StringUtils.isEmpty(str)){
            T.show(mContext, "请输入题目正确答案")
            return null
        }
        return str
    }

    override fun getAnalyse(): String? {
        val str = analyseEdit.text.toString()
        if (StringUtils.isEmpty(str)){
            T.show(mContext, "请输入题目分析")
            return null
        }
        return str
    }

    override fun upload(onConfirmClickListener: OnConfirmClickListener) {
        this.onConfirmClickListener = onConfirmClickListener
        if (!StringUtils.isEmpty(titleImagePath)) {
            val fileName = System.currentTimeMillis().toString()
            tempTitleUrl = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/customQuestion/$fileName"
            OSSUploadUtils.getInstance().UploadPhotoToOSS(
                    mContext,
                    File(titleImagePath),
                    "personal/customQuestion",
                    fileName,
                    titleHandler)
        }else{
            val fileName = System.currentTimeMillis().toString()
            val url = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/customQuestion/$fileName"
            OSSUploadUtils.getInstance().UploadPhotoToOSS(
                    mContext,
                    File(imagePaths[0]),
                    "personal/customQuestion",
                    fileName,
                    optionHandler)
            sb.append("$url,")
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.image_a ->{
                currentImage = 2
            }
            R.id.image_b ->{
                if (imagePaths.isEmpty()){
                    T.show(mContext, "请按顺序添加图片")
                    return
                }
                currentImage = 3
            }
            R.id.image_c ->{
                if (imagePaths.size < 2){
                    T.show(mContext, "请按顺序添加图片")
                    return
                }
                currentImage = 4
            }
            R.id.image_d ->{
                if (imagePaths.size < 3){
                    T.show(mContext, "请按顺序添加图片")
                    return
                }
                currentImage = 5
            }
        }
        startSelectPicture(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null && requestCode == PictureConfig.CHOOSE_REQUEST) {
            val imagePath = PictureSelector.obtainMultipleResult(data)[0].path
            when(currentImage){
                1 ->{
                    titleImagePath = imagePath
                    titleImage.loadImage(imagePath)
                }
                2 ->{//AAAAAAAAAAA
                    if (imagePaths.isEmpty())
                        imagePaths.add(imagePath)
                    else
                        imagePaths[0] = imagePath
                    image_a.loadImage(imagePath)
                }
                3 ->{//BBBBBBBBBBBBB
                    if (imagePaths.size < 2)
                        imagePaths.add(imagePath)
                    else
                        imagePaths[1] = imagePath
                    image_b.loadImage(imagePath)
                }
                4 ->{//CCCCCCCCCCC
                    if (imagePaths.size < 3)
                        imagePaths.add(imagePath)
                    else
                        imagePaths[2] = imagePath
                    image_c.loadImage(imagePath)
                }
                5 ->{//DDDDDDDDDDDDD
                    if (imagePaths.size < 4)
                        imagePaths.add(imagePath)
                    else
                        imagePaths[3] = imagePath
                    image_d.loadImage(imagePath)
                }
            }
        }
    }

    private val optionHandler by lazy { MyHandler(this) }
    class MyHandler(fragment: ChoiceFragment): HandlerUtil<ChoiceFragment>(fragment){
        override fun handleMessage(fragment: ChoiceFragment, msg: Message) {
            when(msg.what){
                Constants.UPLOAD_SUCCESS ->{
                    fragment.uploadPosition ++
                    if (fragment.uploadPosition < fragment.imagePaths.size) {
                        val fileName = System.currentTimeMillis().toString()
                        val url = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/customQuestion/$fileName"
                        OSSUploadUtils.getInstance().UploadPhotoToOSS(
                                fragment.mContext,
                                File(fragment.imagePaths[fragment.uploadPosition]),
                                "personal/customQuestion",
                                fileName,
                                fragment.optionHandler)
                        fragment.sb.append("$url,")
                    }else{
                        fragment.sb.substring(0, fragment.sb.lastIndexOf(","))
                    }
                }
                Constants.UPLOAD_FAIL ->{
                    T.show(fragment.mContext, "上传图片失败，请稍后重试")
                }
            }
        }
    }
    private val titleHandler by lazy { TitleHandler(this) }
    class TitleHandler(fragment: ChoiceFragment): HandlerUtil<ChoiceFragment>(fragment) {
        override fun handleMessage(fragment: ChoiceFragment, msg: Message) {
            when(msg.what){
                Constants.UPLOAD_SUCCESS ->{
                    val fileName = System.currentTimeMillis().toString()
                    val url = "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/customQuestion/$fileName"
                    OSSUploadUtils.getInstance().UploadPhotoToOSS(
                            fragment.mContext,
                            File(fragment.imagePaths[0]),
                            "personal/customQuestion",
                            fileName,
                            fragment.optionHandler)
                    fragment.sb.append("$url,")
                    fragment.titleUrl = fragment.tempTitleUrl
                }
                Constants.UPLOAD_FAIL ->{
                    T.show(fragment.mContext, "上传图片失败，请稍后重试")
                }
            }
        }
    }

    private fun getWordOptions(): String?{
        sb.append(if (StringUtils.isEmpty(optionAEdit.text.toString())) "" else "${optionAEdit.text},")
        sb.append(if (StringUtils.isEmpty(optionBEdit.text.toString())) "" else "${optionBEdit.text},")
        sb.append(if (StringUtils.isEmpty(optionCEdit.text.toString())) "" else "${optionCEdit.text},")
        sb.append(if (StringUtils.isEmpty(optionDEdit.text.toString())) "" else "${optionDEdit.text},")
        L.e(sb)
        val split = sb.split(",")
        if (split.size < 3){
            T.show(mContext, "选项少于2项")
            return null
        }
        return sb.substring(0, sb.lastIndexOf(","))
    }

    fun isReadyToUpload(): Boolean{
        var str = titleEdit.text.toString()
        if (StringUtils.isEmpty(str)){
            T.show(mContext, "请输入题目标题")
            return false
        }
        return true
    }

    private fun setWordPicStyle(text: String, textView: TextView){
        val spannableString = SpannableString(text)
        spannableString.setSpan(ForegroundColorSpan(mContext.resources.getColor(R.color.blue)), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannableString
    }

    private fun setBracketsWordStyle(textView: TextView){
        val spannableString = SpannableString(textView.text.toString())
        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#FF770C")), spannableString.indexOf("（"), spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(AbsoluteSizeSpan(mContext.resources.getDimensionPixelSize(R.dimen.tv_sub)), spannableString.indexOf("（"), spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannableString
    }
}