package com.yzy.ebag.parents.ui.widget

import android.content.Context
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.ui.activity.MainActivity

class SharePupopwindow(private val context: Context, private val shareCallback: MainActivity.ShareClickCallback) : PopupWindow() {

    init {
        val view = View.inflate(context, R.layout.popupwindow_share, null)
        val wxView = view.findViewById<TextView>(R.id.weixin)
        val momentView = view.findViewById<TextView>(R.id.pengyouquan)
        val weiboView = view.findViewById<TextView>(R.id.weibo)
        val qqView = view.findViewById<TextView>(R.id.qq)
        view.findViewById<TextView>(R.id.cancel_btn).setOnClickListener {
            this.dismiss()
        }

        wxView.setOnClickListener {
            shareCallback.wxClick()
        }

        momentView.setOnClickListener {
            shareCallback.momentClick()
        }

        weiboView.setOnClickListener {
            shareCallback.weiboClick()
        }

        qqView.setOnClickListener {
            shareCallback.qqClick()
        }

        this.contentView = view
        this.isOutsideTouchable = false
        this.width = WindowManager.LayoutParams.MATCH_PARENT
        this.height = WindowManager.LayoutParams.MATCH_PARENT
    }


}