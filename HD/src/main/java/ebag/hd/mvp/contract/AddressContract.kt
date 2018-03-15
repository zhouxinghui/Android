package ebag.hd.mvp.contract

import ebag.hd.base.BasePresenter
import ebag.hd.base.BaseView
import ebag.hd.bean.AddressListBean

/**
 * Created by fansan on 2018/3/14.
 */
interface AddressContract {

    interface Persenter:BasePresenter{

        fun request()
        fun deleteAddress(id:String,position: Int)
    }

    interface View:BaseView<Persenter>{

        fun showLoading()
        fun showError()
        fun showEmpty()
        fun showSuccess(data:MutableList<AddressListBean>)
        fun delete(position: Int)
        fun edit(position:Int)
        fun setDefult()
    }
}