package ebag.core.base

import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import ebag.core.R
import ebag.core.util.AppManager


/**
 * Created by caoyu on 2017/10/31.
 */
abstract class BaseActivity : AppCompatActivity() {

    protected var isDestroy : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.startactivity,R.anim.finishactivity)
        setContentView(getLayoutId())
        AppManager.addActivity(this)
        window.setBackgroundDrawableResource(R.color.pageBackground)
        isDestroy = false
        initViews()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.startactivity,R.anim.finishactivity)
    }

    override fun onDestroy() {
        super.onDestroy()
        isDestroy = true
        AppManager.removeActivity(this)
    }

    protected abstract fun getLayoutId():Int

    protected abstract fun initViews()

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideKeyboard(v, ev)) {
                val res = hideKeyboard(v!!.windowToken)
                if (res) {
                    //隐藏了输入法，则不再分发事件
                    return true
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return !(event.x > left && event.x < right
                    && event.y > top && event.y < bottom)
                // 点击EditText的事件，忽略它。
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false
    }
    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private fun hideKeyboard(token: IBinder?): Boolean {
        if (token != null) {
            val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
        }
        return false
    }
}