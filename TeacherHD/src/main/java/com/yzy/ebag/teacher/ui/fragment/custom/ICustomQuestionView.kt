package com.yzy.ebag.teacher.ui.fragment.custom

/**
 * 鸡肋功能，不考虑布局和代码优化
 * Created by YZY on 2018/2/8.
 */
interface ICustomQuestionView {
    fun getTitle(): String?

    fun getContent(): String?

    fun getAnswer(): String?

    fun getAnalyse(): String?

    fun upload(onConfirmClickListener: OnConfirmClickListener)
}