package ebag.core.base

import android.os.Bundle
import android.view.View

/**
 * @author caoyu
 * @date 2018/1/15
 * @description
 */
abstract class LazyFragment: BaseFragment(){

    abstract fun lazyLoad()

    //Fragment的View加载完毕的标记
    private var isViewCreated = false
    //Fragment对用户可见的标记
    private var isUIVisible = false

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isPagerFragment()){
            if(isVisibleToUser){
                isUIVisible = true
                if(isViewCreated){
                    load()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(isPagerFragment()){
            isViewCreated = true
            if(isUIVisible){
                load()
            }
        }else{
            lazyLoad()
        }
    }

    fun load(){
        if(isUIVisible && isViewCreated){
            lazyLoad()
            isUIVisible = false
            isViewCreated = false
        }
    }

    /**
     * 不是 FragmentPagerAdapter 中的Fragment 中的要重写这个方法
     * 并且返回 false
     * 不然不会第一次加载
     *
     * 原因是  只有在FragmentPagerAdapter中的 Fragment 会回调
     * setUserVisibleHint 方法
     */
    open fun isPagerFragment(): Boolean{
        return true
    }

}