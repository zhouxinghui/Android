package ebag.hd.activity

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import ebag.core.base.BaseActivity
import ebag.hd.R
import kotlinx.android.synthetic.main.activity_protocol.*

/**
 * Created by YZY on 2018/3/16.
 */
class ProtocolActivity : BaseActivity(){
    override fun getLayoutId(): Int {
        return R.layout.activity_protocol
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initViews() {
        webView.settings.javaScriptEnabled = true

        webView.loadUrl("http://www.yun-bag.com/ebag-portal/privacyProtection.jsp")
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
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
    }
}