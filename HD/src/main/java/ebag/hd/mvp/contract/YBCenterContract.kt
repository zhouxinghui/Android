package ebag.hd.mvp.contract

import ebag.hd.base.BasePresenter
import ebag.hd.base.BaseView
import ebag.hd.mvp.model.YBCenterModel

/**
 * Created by fansan on 2018/3/13.
 */

interface YBCenterContract{

    interface Presenter:BasePresenter{

        fun request(page:Int,pageSize:Int,isFirst:Boolean,isLoadmore:Boolean)
        fun switch(page: Int,pageSize: Int,type:String,isLoadmore:Boolean)
    }


    interface View:BaseView<Presenter>{

        fun showSuccess(currentMoney:String,incomeMoney:String,expendMoney:String,data:MutableList<YBCenterModel>)
        fun showError()
        fun showLoading()
        fun loadmoreError()
        fun loadmoreComplete(data:MutableList<YBCenterModel>)
        fun showTimePicker()
        fun showDataLoading()
        fun showDataError()
        fun showDataEmpty()
        fun dataLoadSuccess(data:MutableList<YBCenterModel>)
        fun showIncome()
        fun showExpend()
        fun showAll()
        fun loadmoreEnd()
    }
}