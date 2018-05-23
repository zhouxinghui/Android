package com.yzy.ebag.student.activity.tools.read

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import com.google.gson.Gson
import com.iflytek.cloud.*
import com.yzy.ebag.student.kdxftest.SpeechEvaluatorVo
import ebag.core.util.L
import org.json.JSONException
import org.json.JSONObject
import org.json.XML

/**
 * Created by YZY on 2018/5/17.
 */
class IflytekUtil private constructor(mContext: Context){
    /*private object Holder{val INSTANCE = IflytekUtil()}
    companion object {
        val instance by lazy {

            Holder.INSTANCE
        }

    }*/

    companion object {
        @Volatile
        private var instance: IflytekUtil? = null
        fun getInstance(context: Context): IflytekUtil {
            if (instance == null) {
                synchronized(IflytekUtil::class) {
                    if (instance == null) {
                        instance = IflytekUtil(context)
                    }
                }
            }
            return instance!!
        }
    }

    private var evaluator: SpeechEvaluator? = SpeechEvaluator.createEvaluator(mContext, null)
    var onEvaluatingResult: ((jsonObject: JSONObject, score: Double, resultSpannable: SpannableString) -> Unit)? = null
    private var isRecording = false
    init {
        evaluator?.setParameter(SpeechConstant.LANGUAGE, "en_us")
        evaluator?.setParameter(SpeechConstant.ISE_CATEGORY, "read_sentence")
        evaluator?.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8")
        evaluator?.setParameter(SpeechConstant.AUDIO_FORMAT, "wav")
//        evaluator.setParameter(SpeechConstant.SAMPLE_RATE,"16000")
    }

    fun isRecording(): Boolean = isRecording

    /**
     * 开始评估
     * @param str 待评估的文字
     */
    fun startEvaluating(str: String, fileName: String){
        evaluator?.setParameter(SpeechConstant.ISE_AUDIO_PATH, fileName)
        evaluator?.startEvaluating("[content]$str", null, object : EvaluatorListener {
            override fun onVolumeChanged(i: Int, bytes: ByteArray) {
            }

            override fun onBeginOfSpeech() {
                L.e("onBeginOfSpeech: 开始")
                isRecording = true
            }

            override fun onEndOfSpeech() {}

            override fun onResult(evaluatorResult: EvaluatorResult, b: Boolean) {
                isRecording = false
                val resultString = evaluatorResult.resultString
                try {
                    val jsonObject = XML.toJSONObject(resultString).toString()
                    L.e("TAG2", jsonObject)
                    val gson = Gson()
                    val speechEvaluatorVo = gson.fromJson(jsonObject, SpeechEvaluatorVo::class.java)
                    if (null != speechEvaluatorVo.xml_result) {
                        //                        isIs_rejected  为 true证明为乱读
                        val isIs_rejected = speechEvaluatorVo.xml_result.read_sentence.rec_paper.read_chapter.isIs_rejected
                        Log.d("TAG", "onResult: $isIs_rejected")

                        //                    文字变色
                        val word = speechEvaluatorVo.xml_result.read_sentence.rec_paper.read_chapter.sentence.word
                        val totalScore = speechEvaluatorVo.xml_result.read_sentence.rec_paper.read_chapter.sentence.total_score
                        val spannableString = SpannableString(str)
                        for (i in word.indices) {
                            if (null != word[i].index) {
                                val currentWord = "${word[i].content}"
                                val startIndex = str.indexOf(currentWord, 0, true)
                                if (word[i].total_score < 2.8) {
                                    spannableString.setSpan(ForegroundColorSpan(Color.RED), startIndex, startIndex + currentWord.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                                } else {
                                    spannableString.setSpan(ForegroundColorSpan(Color.GREEN), startIndex, startIndex + currentWord.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                                }
                                L.e("cca", word[i].index)
                            }
                        }
                        onEvaluatingResult?.invoke(XML.toJSONObject(resultString), totalScore, spannableString)
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onError(speechError: SpeechError) {
                Log.d("TAG", "onError: " + speechError.toString())
                isRecording = false
            }

            override fun onEvent(i: Int, i1: Int, i2: Int, bundle: Bundle) {

            }
        })
    }

    fun stopEvaluating(){
        evaluator?.stopEvaluating()
    }
}