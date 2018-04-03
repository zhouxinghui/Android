package ebag.hd.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import ebag.core.base.BaseActivity
import ebag.hd.R
import kotlinx.android.synthetic.main.activity_display_office_file.*

/**
 * Created by YZY on 2018/3/2.
 */
class DisplayOfficeFileActivity : BaseActivity() {
    companion object {
        fun jump(context: Context, fileUrl: String) {
            context.startActivity(
                    Intent(context, DisplayOfficeFileActivity::class.java)
                            .putExtra("fileUrl", fileUrl)
            )
        }
    }

    private var firtNotFond = true

    override fun getLayoutId(): Int {
        return R.layout.activity_display_office_file
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initViews() {
        val fileUrl = intent.getStringExtra("fileUrl")
        webView.settings.javaScriptEnabled = true

        webView.loadUrl("https://view.officeapps.live.com/op/view.aspx?src=$fileUrl")
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url == "https://view.officeapps.live.com/op/filenotfound.htm?llcc=zh-CN" && !firtNotFond)
                    finish()
                if (url == "https://view.officeapps.live.com/op/filenotfound.htm?llcc=zh-CN" && firtNotFond)
                    firtNotFond = false
                view.loadUrl(url)
                return true
            }
        }

        webView.webChromeClient = object : WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress == 100){
                    stateView.showContent()
                }else{
                    stateView.showLoading()
                }
            }
        }

        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false
    }

    override fun onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack()
        }else {
            super.onBackPressed()
        }
    }
}