package ebag.hd.widget

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.hd.R
import ebag.hd.bean.ChildNodeBean
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
            provinceView.setData(list)
            cityView.setData(list[0].childNode)
            provinceView.setDefault(0)
            cityView.setDefault(0)
        }

        override fun onError(exception: Throwable) {
            exception.handleThrowable(context)
            stateView.showError()
        }
    }
    private var list: List<ChildNodeBean> = ArrayList()
    private var cityList: List<ChildNodeBean> = ArrayList()
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

        EBagApi.cityData(request)
        stateView.setOnRetryClickListener { EBagApi.cityData(request) }
    }

    override fun endSelect(view: View, id: Int, text: String, abbreviation: String) {
        when(view.id){
            R.id.provinceView ->{
                cityList = list[id].childNode
                val areaList = cityList[0].childNode
                cityView.setData(list[id].childNode)
                cityView.setDefault(0)
                if (areaList != null && !areaList.isEmpty()) {
                    areaView.setData(areaList)
                    areaView.setDefault(0)
                    areaView.visibility = View.VISIBLE
                }else{
                    areaView.visibility = View.INVISIBLE
                }
            }
            R.id.cityView ->{
                val areaList = cityList[id].childNode
                if (areaList != null && !areaList.isEmpty()) {
                    areaView.setData(areaList)
                    areaView.setDefault(0)
                    areaView.visibility = View.VISIBLE
                }else{
                    areaView.visibility = View.INVISIBLE
                }
            }
            R.id.areaView ->{

            }
        }
    }

    override fun selecting(id: Int, text: String?) {

    }

    override fun onClick(v: View?) {

    }
}