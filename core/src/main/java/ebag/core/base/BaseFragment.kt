package ebag.core.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by caoyu on 2017/10/31.
 */
abstract class BaseFragment : Fragment() {

    val mContext: Context by lazy { activity}
    private var mRootView : View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBundle(arguments)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(mRootView == null){
            mRootView = inflater.inflate(getLayoutRes(), container, false)
            initRootView(mRootView!!)
        }else{
            (mRootView?.parent as ViewGroup?)?.removeAllViewsInLayout()
        }
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    /**初始化View*/
    protected open fun initRootView(rootView: View){

    }

    /**获取RootLayout的ID*/
    protected abstract fun getLayoutRes(): Int
    /**获取数据源*/
    protected abstract fun getBundle(bundle: Bundle?)
    /**初始化View*/
    protected abstract fun initViews(rootView: View)

}