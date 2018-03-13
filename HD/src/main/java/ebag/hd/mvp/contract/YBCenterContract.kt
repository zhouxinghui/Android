package ebag.hd.mvp.contract

import ebag.hd.base.BasePresenter
import ebag.hd.base.BaseView
import ebag.hd.mvp.model.YBCenterModel

/**
 * Created by fansan on 2018/3/13.
 */

interface YBCenterContract{

    interface Presenter:BasePresenter{

        fun request()
    }


    interface View:BaseView<Presenter>{

        fun setSpannableString()
        fun showSuccess(currentMoney:String,incomeMoney:String,expendMoney:String,data:MutableList<YBCenterModel>)
        fun showError()
        fun showEmpty()
        fun showLoading()
        fun loadmoreError()
        fun loadmoreFail()
        fun showTimePicker()
        fun showDataLoading()
        fun showDataError()
        fun showDataEmpty()
        fun showIncome()
        fun showExpend()
        fun showAll()
    }
}