package ebag.mobile.module.shop


import ebag.mobile.bean.AddressListBean


/**
 * Created by fansan on 2018/3/14.
 */
interface AddressContract {

    interface Persenter: BasePresenter {

        fun request()
        fun deleteAddress(id:String,position: Int)
    }

    interface View: BaseView<Persenter> {

        fun showLoading()
        fun showError()
        fun showEmpty()
        fun showSuccess(data:MutableList<AddressListBean>)
        fun delete(position: Int)
        fun edit(position:Int)
        fun setDefult()
    }
}