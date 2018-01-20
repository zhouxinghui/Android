package ebag.hd.widget

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.SPUtils
import ebag.hd.R
import ebag.hd.base.Constants
import ebag.hd.bean.ChildNodeBean
import ebag.hd.bean.CurrentCityBean
import ebag.hd.http.EBagApi
import kotlinx.android.synthetic.main.dialog_city_picker.*

/**
 * Created by YZY on 2018/1/19.
 */
class CityPickerDialog(context: Context): Dialog(context, R.style.ActionSheetDialogStyle), WheelView.OnSelectListener, View.OnClickListener {
    val request = object : RequestCallBack<List<ChildNodeBean>>(){
        override fun onStart() {
            stateView.showLoading()
        }

        override fun onSuccess(entity: List<ChildNodeBean>?) {
            if (entity == null || entity.isEmpty()){
                stateView.showEmpty()
                return
            }
            stateView.showContent()
            list = entity[0].childNode

            provinceIndex = SPUtils.get(context, Constants.PROVINCE_INDEX, 0) as Int
            cityIndex = SPUtils.get(context, Constants.CITY_INDEX, 0) as Int
            areaIndex = SPUtils.get(context, Constants.AREA_INDEX, 0) as Int
            cityList = list[provinceIndex].childNode
            val areaList = cityList[cityIndex].childNode

            provinceView.setData(list)
            cityView.setData(cityList)
            provinceView.setDefault(provinceIndex)
            cityView.setDefault(cityIndex)

            currentCityBean.province = list[provinceIndex].charCode
            currentCityBean.provinceName = list[provinceIndex].districtCnName
            currentCityBean.city = cityList[cityIndex].charCode
            currentCityBean.cityName = cityList[cityIndex].districtCnName

            if(areaList != null && !areaList.isEmpty()){
                areaView.setData(areaList)
                areaView.setDefault(areaIndex)
                currentCityBean.county = areaList[areaIndex].charCode
                currentCityBean.cityName = areaList[areaIndex].districtCnName
            }
        }

        override fun onError(exception: Throwable) {
            exception.handleThrowable(context)
            stateView.showError()
        }
    }
    private var provinceIndex = 0
    private var cityIndex = 0
    private var areaIndex = 0
    private var list: List<ChildNodeBean> = ArrayList()
    private var cityList: List<ChildNodeBean> = ArrayList()
    private val currentCityBean = CurrentCityBean()
    var onConfirmClick: ((CurrentCityBean) -> Unit)? = null
    init {
        val contentView = layoutInflater.inflate(R.layout.dialog_city_picker, null)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        setContentView(contentView)
//        window.setBackgroundDrawable(ColorDrawable(0))
        val params = window.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.gravity = Gravity.BOTTOM
        window.attributes = params

        provinceView.setOnSelectListener(this)
        cityView.setOnSelectListener(this)
        areaView.setOnSelectListener(this)
        cancelTv.setOnClickListener(this)
        confirmTv.setOnClickListener(this)

        EBagApi.cityData(request)
        stateView.setOnRetryClickListener { EBagApi.cityData(request) }
    }

    override fun endSelect(view: View, id: Int, text: String, charCode: String) {
        when(view.id){
            R.id.provinceView ->{
                cityList = list[id].childNode
                cityView.setData(list[id].childNode)
                cityView.setDefault(0)

                currentCityBean.province = charCode
                currentCityBean.provinceName = text
                currentCityBean.city = cityList[0].charCode
                currentCityBean.cityName = cityList[0].districtCnName

                val areaList = cityList[0].childNode
                setArea(areaList)

                provinceIndex = id
                cityIndex = 0
            }
            R.id.cityView ->{

                currentCityBean.city = charCode
                currentCityBean.cityName = text

                val areaList = cityList[id].childNode
                setArea(areaList)

                cityIndex = id
            }
            R.id.areaView ->{
                currentCityBean.county = charCode
                currentCityBean.countyName = text
            }
        }
    }

    private fun setArea(areaList: List<ChildNodeBean>?){
        areaIndex = 0
        if (areaList != null && !areaList.isEmpty()) {
            areaView.setData(areaList)
            areaView.setDefault(0)
            areaView.visibility = View.VISIBLE

            currentCityBean.county = areaList[0].charCode
            currentCityBean.countyName = areaList[0].districtCnName
        }else{
            areaView.visibility = View.INVISIBLE

            currentCityBean.county = null
            currentCityBean.countyName = null
        }
    }

    override fun selecting(id: Int, text: String?) {

    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.cancelTv ->{
                dismiss()
            }
            R.id.confirmTv ->{
                onConfirmClick?.invoke(currentCityBean)
                SPUtils.put(context, Constants.PROVINCE_INDEX, provinceIndex)
                SPUtils.put(context, Constants.CITY_INDEX, cityIndex)
                SPUtils.put(context, Constants.AREA_INDEX, areaIndex)
                dismiss()
            }
        }
    }
}