package ebag.core.base.mvp

import android.os.Bundle
import android.view.View
import ebag.core.base.BaseFragment

/**
 * @author 曹宇
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
        if(isVisibleToUser){
            isUIVisible = true
            if(isViewCreated){
                isUIVisible = false
                isViewCreated = false
                lazyLoad()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
        if(isUIVisible){
            isUIVisible = false
            isViewCreated = false
            lazyLoad()
        }
    }
}