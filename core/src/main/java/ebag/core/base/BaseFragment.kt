package ebag.core.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by unicho on 2017/10/31.
 */
abstract class BaseFragment : Fragment() {

    val mContext: Context by lazy { activity}
    private var rootView : View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBundle(arguments)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(rootView == null){
            rootView = inflater.inflate(getLayoutRes(), container, false)
        }else{
            val view = rootView!!.parent
            if(view != null)
                (view as ViewGroup).removeAllViewsInLayout()
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initEvent(view)
    }

    /**获取RootLayout的ID*/
    protected abstract fun getLayoutRes(): Int
    /**获取数据源*/
    protected abstract fun getBundle(bundle: Bundle?)
    /**初始化View*/
    protected abstract fun initViews(rootView: View)
    /**初始化监听事件*/
    protected fun initEvent(rootView: View) {
    }
}